package com.tenisme.lockscreen

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast

class LockScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock_screen)

        Log.d("LockScreen", "짠")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            // WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED <= 기본 잠금화면보다 현재 Activity 를 상위에 위치시켜라.
            // WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD <= 안드로이드 기본 잠금화면을 없애라.
            this.window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }

        findViewById<TextView>(R.id.textView).setOnClickListener {
            Toast.makeText(this, "토스트 짠", Toast.LENGTH_SHORT).show()
        }
    }
}