package com.example.fragmentvm

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.fragmentvm.ui.main.MainFragment
import com.example.fragmentvm.ui.second.SecondFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

        val btnFirst: Button = findViewById(R.id.btnFirst)
        val btnSecond: Button = findViewById(R.id.btnSecond)

        btnFirst.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

        btnSecond.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SecondFragment.newInstance())
                .commitNow()
        }
    }
}