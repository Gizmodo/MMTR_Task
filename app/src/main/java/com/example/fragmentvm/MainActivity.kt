package com.example.fragmentvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fragmentvm.databinding.MainActivityBinding
import com.example.fragmentvm.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit()
        }
        setSupportActionBar(binding.toolbar)
    }
}