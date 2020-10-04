package com.tenisme.lockscreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var onBtn: Button
    private lateinit var offBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onBtn = findViewById(R.id.onBtn)
        offBtn = findViewById(R.id.offBtn)

        onBtn.setOnClickListener {
            val intent = Intent(applicationContext, ScreenService::class.java)
            startService(intent)
            Log.d("LockScreen", "Start LockScreen")
        }
        offBtn.setOnClickListener {
            val intent = Intent(applicationContext, ScreenService::class.java)
            stopService(intent)
            Log.d("LockScreen", "Stop LockScreen")
        }
    }
}