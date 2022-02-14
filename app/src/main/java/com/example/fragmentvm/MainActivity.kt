package com.example.fragmentvm

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.fragmentvm.databinding.MainActivityBinding
import com.example.fragmentvm.ui.LoginFragmentDirections
import com.example.fragmentvm.viewmodel.MainActivityVM
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    init {
        App.instance().appGraph.embed(this)
    }

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model: MainActivityVM by viewModels()
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