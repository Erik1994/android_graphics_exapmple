package com.example.abdroidgraphics.navigation

import androidx.navigation.NavDirections

sealed class NavigationCommand {
    data class To (val direction: NavDirections): NavigationCommand()
    object Back: NavigationCommand()
    object None: NavigationCommand()
}
