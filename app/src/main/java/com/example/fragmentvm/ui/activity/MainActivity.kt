package com.example.fragmentvm.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fragmentvm.App
import com.example.fragmentvm.R
import com.example.fragmentvm.core.utils.findFragmentContainerNavController
import com.example.fragmentvm.databinding.MainActivityBinding
import com.example.fragmentvm.ui.fragments.LoginFragmentDirections
import com.example.fragmentvm.ui.fragments.MainFragmentDirections
import com.example.fragmentvm.ui.viewmodels.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    init {
        App.instance().appGraph.embed(this)
    }

    private lateinit var binding: MainActivityBinding

    private val navController: NavController by lazy {
        findFragmentContainerNavController(R.id.nav_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model: MainActivityViewModel by viewModels()
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navView: BottomNavigationView = binding.navView
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> hideBottomNav()
                R.id.apiFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }

        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favourites -> {
                    Timber.d(navController.currentDestination.toString())
                    if (navController.currentDestination?.id != R.id.navigation_favourites) {
                        navController.navigate(MainFragmentDirections.actionMainFragmentToFavouriteFragment())
                    }
                    true
                }
                R.id.main -> {
                    navController.currentDestination?.let { dest ->
                        navController.popBackStack(dest.id, true)
                    }
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            model.getIsAlreadyRegistered().observe(this) {
                when (it) {
                    true -> {
                        navHostFragment.findNavController()
                            .navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
                    }
                    else -> Timber.i("NoAction")
                }
            }
        }
    }

    private fun showBottomNav() {
        binding.navView.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.navView.visibility = View.GONE
    }
}
