package com.example.myfundkt.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myfundkt.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        lifecycleScope.launch {
            delay(3000L)
            jump()
        }
        binding.skip.setOnClickListener {
            jump()
        }

    }

    override fun onStop() {
        super.onStop()
        supportActionBar?.show()
        this.finish()
    }

    private fun jump() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}