package com.example.abdroidgraphics.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.abdroidgraphics.R
import com.example.abdroidgraphics.databinding.*
import com.example.abdroidgraphics.extension.getInputList
import com.example.abdroidgraphics.ui.viewmodel.AddTextFragmentViewModel


class AddTextFragment : BaseFragment(R.layout.fragment_add_text) {
    private lateinit var binding: FragmentAddTextBinding
    override val viewModel: AddTextFragmentViewModel by viewModels()
    private val args: AddTextFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentAddTextBinding.inflate(layoutInflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.textInputEditText.apply {
            requestFocus()
            val input = args.inputText ?: ""
            setText(input)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_text_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addTextMenu -> {
                val text = binding.textInputEditText.text.toString()
                viewModel.navigate(
                    AddTextFragmentDirections.actionAddTextFragmentToDrawTextFragment(
                        text
                    )
                )
                return true
            }
        }
        return false
    }
}