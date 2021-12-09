package com.example.fragmentvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fragmentvm.databinding.MainActivityBinding
import com.example.fragmentvm.ui.main.MainFragment
import com.example.fragmentvm.ui.second.SecondFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

        binding.btnFirst.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

        binding.btnSecond.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SecondFragment.newInstance())
                .commitNow()
        }
    }
}