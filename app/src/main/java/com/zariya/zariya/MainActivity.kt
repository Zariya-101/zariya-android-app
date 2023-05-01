package com.zariya.zariya

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.zariya.zariya.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpNavigation()

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false
    }

    private fun setUpNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        NavigationUI.setupWithNavController(
            binding.bottomNavigationView,
            navHostFragment.navController
        )

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    showBottomNav(true)
                    binding.fab.setImageResource(R.drawable.ic_add)
                }

                R.id.myServicesFragment -> {
                    showBottomNav(true)
                    binding.fab.setImageResource(R.drawable.ic_add)
                }

                R.id.merchandiseFragment -> {
                    showBottomNav(true)
                    binding.fab.setImageResource(R.drawable.ic_add)
                }

                R.id.profileFragment -> {
                    showBottomNav(true)
                    binding.fab.setImageResource(R.drawable.ic_question_mark)
                }

                else -> showBottomNav(false)
            }
        }
    }

    private fun showBottomNav(show: Boolean) {
        binding.bottomAppBar.isVisible = show
        binding.fab.isVisible = show
    }
}