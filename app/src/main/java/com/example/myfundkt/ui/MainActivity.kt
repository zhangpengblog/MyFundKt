package com.example.myfundkt.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.myfundkt.R
import com.example.myfundkt.databinding.ActivityMainBinding
import com.example.myfundkt.db.DbRepository
import com.example.myfundkt.db.entity.FoudInfoEntity
import com.example.myfundkt.utils.ToastUtil
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*


private const val TAG = "MainActivity"


class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var binding: ActivityMainBinding
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)




        Log.d(TAG, "onCreate: $title")



    }



}