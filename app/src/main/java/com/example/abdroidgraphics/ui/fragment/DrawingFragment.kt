package com.example.abdroidgraphics.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.abdroidgraphics.R
import com.example.abdroidgraphics.databinding.FragmentBlendModesBinding
import com.example.abdroidgraphics.databinding.FragmentDrawTextBinding
import com.example.abdroidgraphics.databinding.FragmentDrawingBinding
import com.example.abdroidgraphics.ui.viewmodel.BaseViewModel
import com.example.abdroidgraphics.ui.viewmodel.DrawFragmentViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class DrawingFragment : BaseFragment(R.layout.fragment_drawing) {
    private lateinit var binding: FragmentDrawingBinding
    override val viewModel: DrawFragmentViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(!this::binding.isInitialized) {
            binding = FragmentDrawingBinding.inflate(layoutInflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
        observeData()
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.bitmapFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                    it?.let { bitmap ->
                        binding.imageView.bitmap = bitmap
                    }
                }
        }
    }

    private fun setClickListeners() {
        binding.apply {
            floatingActionButton.setOnClickListener {
                getContract.launch(null)
            }
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

                override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
            })
        }
    }
}