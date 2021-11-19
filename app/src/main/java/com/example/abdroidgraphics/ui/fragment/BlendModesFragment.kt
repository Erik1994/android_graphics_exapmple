package com.example.abdroidgraphics.ui.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abdroidgraphics.R
import com.example.abdroidgraphics.databinding.FragmentBlendModesBinding
import com.example.abdroidgraphics.ui.adapters.BlendModesRecyclerViewAdapter
import com.example.abdroidgraphics.ui.viewmodel.BlendModeViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

const val MIMETYPE_IMAGE = "image/*"

enum class ImagePosition {
    FIRST, SECOND, NONE
}

class BlendModesFragment : BaseFragment(R.layout.fragment_blend_modes) {
    override val viewModel: BlendModeViewModel by viewModels()
    private lateinit var binding: FragmentBlendModesBinding
    private var blendAdapter = BlendModesRecyclerViewAdapter()
    private var blendModeMap: HashMap<String, PorterDuff.Mode?>? = null
    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val deniedPermissions = arrayListOf<String>()
            permissions.entries.forEach {
                if (!it.value) {
                    deniedPermissions.add(it.key)
                }
                deniedPermissions.size.takeIf { size -> size > 0 }?.let {
                    showDialog()
                }
            }
        }

    private val resultContract = object : ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                type = MIMETYPE_IMAGE
            }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            val isSuccess = resultCode == Activity.RESULT_OK
            if (isSuccess) {
                return intent?.data
            }
            return null
        }
    }

    private val getContract: ActivityResultLauncher<Any?> =
        registerForActivityResult(resultContract) { uri ->
            uri.takeIf { it != null && activity != null }?.let {
                viewModel.createBitmapFromUri(it, requireActivity())
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentBlendModesBinding.inflate(layoutInflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        blendModeMap = viewModel.getBlendModeMap(requireContext())
        observeData()
        isPermissionsGranted().takeIf { !it }?.let {
            requestPermission()
        }
        setClickListeners()
        initRv()
    }

    private fun observeData() {
        this.lifecycleScope.launch {
            viewModel.bitmapFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest { bitmap ->
                    bitmap?.let {
                        binding.imageView.apply {
                            if (viewModel.imagePosition == ImagePosition.NONE) {
                                when (firstBitmap) {
                                    null -> firstBitmap = it
                                    else -> {
                                        binding.imageSwitcher.visibility = View.VISIBLE
                                        viewModel.imagePosition = ImagePosition.SECOND
                                        secondBitmap = it
                                    }
                                }
                            } else {
                                when (viewModel.imagePosition) {
                                    ImagePosition.FIRST -> firstBitmap = it
                                    ImagePosition.SECOND -> secondBitmap = it
                                    ImagePosition.NONE -> Unit
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun initRv() {
        binding.recycler.apply {
            adapter = blendAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            setHasFixedSize(true)
        }.also {
            val result = blendModeMap?.keys?.toMutableList()
            blendAdapter.submitList(result)
        }
    }

    private fun setClickListeners() {
        binding.apply {
            floatingActionButton.setOnClickListener {
                if (isPermissionsGranted()) {
                    getContract.launch(null)
                } else {
                    requestPermission()
                }
            }

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    val alpha = 255 * progress / 100
                    imageView.setAlpha(alpha)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

                override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
            })

            imageSwitcher.setOnCheckedChangeListener { _, isChecked ->
                viewModel.imagePosition = if (isChecked) ImagePosition.FIRST else ImagePosition.SECOND
            }

            blendAdapter.clicksFlow().debounce(300).onEach {
                imageView.setPorterDuffMode(blendModeMap?.get(it))
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            right.setOnClickListener {
                imageView.rotateImageRight()
            }

            left.setOnClickListener {
                imageView.rotateImageLeft()
            }
        }
    }

    private fun requestPermission() =
        requestPermissions.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))

    private fun isPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission required")
            .setMessage("Please grant all permissions to use this app")
            .setNegativeButton("Cancel") { dialog: DialogInterface, _ ->
                dialog.dismiss()
                activity?.finish()
            }
            .setPositiveButton("Ok") { dialog: DialogInterface, _ ->
                dialog.dismiss()
                requestPermission()

            }.show()
    }
}