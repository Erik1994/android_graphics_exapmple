package com.example.abdroidgraphics.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.abdroidgraphics.R
import com.example.abdroidgraphics.databinding.FragmentDrawTextBinding
import com.example.abdroidgraphics.ui.viewmodel.DrawFragmentViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.reflect.InvocationTargetException


class DrawTextFragment : BaseFragment(R.layout.fragment_draw_text) {
    private lateinit var binding: FragmentDrawTextBinding
    override val viewModel: DrawFragmentViewModel by viewModels()
    private val args: DrawTextFragmentArgs by navArgs()
    private var inputText = ""

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
            binding = FragmentDrawTextBinding.inflate(layoutInflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        try {
            inputText = args.inputText ?: ""
            binding.imageView.drawingText = inputText
        } catch (e: InvocationTargetException) {
            //nothing to do
        }
        setClickListeners()
        observeData()
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.bitmapFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                    it?.let { bitmap ->
                        binding.imageView.bitmapBitmap = bitmap
                    }
                }
        }
    }

    private fun setClickListeners() {
        binding.floatingActionButton.setOnClickListener {
            getContract.launch(null)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.draw_text_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.drawTextMenu -> {
                viewModel.navigate(DrawTextFragmentDirections.actionDrawTextFragmentToAddTextFragment(binding.imageView.drawingText))
                return true
            }
        }
        return false
    }
}