package com.example.fragmentvm.screen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.fragmentvm.App
import com.example.fragmentvm.R
import com.example.fragmentvm.databinding.MainActivityBinding
import com.example.fragmentvm.screen.login.LoginFragmentDirections
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    init {
        App.instance().appGraph.embed(this)
    }

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model: MainActivityViewModel by viewModels()
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
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
}