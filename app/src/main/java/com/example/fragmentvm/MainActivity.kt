package com.example.fragmentvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.fragmentvm.databinding.MainActivityBinding
import com.example.fragmentvm.repository.data.DataStoreRepository
import com.example.fragmentvm.ui.LoginFragmentDirections
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    init {
        App.instance().appGraph.embed(this)
    }

    private lateinit var binding: MainActivityBinding

    @Inject
    lateinit var ds: DataStoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        if (savedInstanceState == null) {
            when (runBlocking { ds.getBool("flagReg") }) {
                true -> {
                    navHostFragment.findNavController()
                        .navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
                }
                else -> Timber.i("NoAction")
            }
        }
    }
}