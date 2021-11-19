package com.example.abdroidgraphics.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.abdroidgraphics.navigation.NavigationCommand
import com.example.abdroidgraphics.ui.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFragment(layoutId: Int): Fragment(layoutId) {
    protected abstract val viewModel: BaseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNavigation()
    }

    private fun observeNavigation() {
        lifecycleScope.launch {
            viewModel.navigationFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                    when(it) {
                        NavigationCommand.Back -> findNavController().navigateUp()
                        is NavigationCommand.To -> findNavController().navigate(it.direction)
                        is NavigationCommand.None -> Unit
                    }
                }
        }
    }
}