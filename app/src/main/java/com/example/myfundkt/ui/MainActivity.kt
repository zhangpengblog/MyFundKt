package com.example.myfundkt.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.myfundkt.R
import com.example.myfundkt.databinding.ActivityMainBinding


private const val TAG = "MainActivity"


class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var binding: ActivityMainBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        supportActionBar?.hide()

    }


}