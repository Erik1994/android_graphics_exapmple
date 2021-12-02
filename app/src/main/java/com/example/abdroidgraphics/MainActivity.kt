package com.example.abdroidgraphics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.example.abdroidgraphics.databinding.ActivityMainBinding
import com.example.abdroidgraphics.ui.custom.BrashView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var navController: NavController? = null
    private var binding: ActivityMainBinding? = null
    private var appBarConfiguration: AppBarConfiguration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        navController = this.findNavController(R.id.nav_host_fragment)
        setUpActionBar()
    }

    private fun setUpActionBar() {
        navController?.let {
            binding?.bottomToolbar?.setupWithNavController(it)
            appBarConfiguration = AppBarConfiguration(
                setOf(R.id.blendModesFragment, R.id.drawTextFragment, R.id.drawingFragment)
            )
            setupActionBarWithNavController(it, appBarConfiguration!!)
            NavigationUI.setupActionBarWithNavController(this, it)
        }
    }

    override fun onSupportNavigateUp(): Boolean = navController?.let { nav ->
        appBarConfiguration.takeIf { it != null }?.let {
            nav.navigateUp(it)
        } ?: nav.navigateUp()
    } ?: false
}