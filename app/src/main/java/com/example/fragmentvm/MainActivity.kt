package com.example.fragmentvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fragmentvm.databinding.MainActivityBinding
import com.example.fragmentvm.repository.DataStoreRepositoryImpl
import com.example.fragmentvm.ui.LoginFragment
import com.example.fragmentvm.ui.MainFragment
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    init {
        App.instance().appGraph.embed(this)
    }

    private lateinit var binding: MainActivityBinding

    @Inject
    lateinit var ds: DataStoreRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val fragmentToRun = when (runBlocking { ds.getBool("flagReg") }) {
                true -> MainFragment.instance()
                else -> LoginFragment.instance()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragmentToRun)
                .commit()
        }
    }
}