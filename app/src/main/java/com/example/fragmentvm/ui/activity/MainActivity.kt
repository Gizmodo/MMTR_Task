package com.example.fragmentvm.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fragmentvm.App
import com.example.fragmentvm.R
import com.example.fragmentvm.databinding.MainActivityBinding
import com.example.fragmentvm.ui.fragments.LoginFragmentDirections
import com.example.fragmentvm.ui.fragments.MainFragment
import com.example.fragmentvm.ui.fragments.MainFragmentDirections
import com.example.fragmentvm.ui.viewmodels.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    init {
        App.instance().appGraph.embed(this)
    }

    private lateinit var binding: MainActivityBinding
    private fun replaceFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model: MainActivityViewModel by viewModels()
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val nav = navHostFragment.findNavController()
        val navView: BottomNavigationView = binding.navView
        navView.setupWithNavController(nav)
        nav.addOnDestinationChangedListener { _, destination, _ ->
            Timber.d(destination.displayName)
            when (destination.id) {
                R.id.loginFragment -> hideBottomNav()
                R.id.apiFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }
        navView.setOnItemSelectedListener {
            if (it.itemId == R.id.favourites) {
//                replaceFragment(FavouriteFragment())
                val arg = Bundle().apply { putString("USERID", "SOME DATA") }
                val action = MainFragmentDirections.actionMainFragmentToFavouriteFragment()
                nav.navigate(action)
            }
            if (it.itemId == R.id.main) {
                replaceFragment(MainFragment())
            }
            true
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