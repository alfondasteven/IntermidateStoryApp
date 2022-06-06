package com.fonda.intermidatestoryapp.ui.splash

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import com.fonda.intermidatestoryapp.R
import com.fonda.intermidatestoryapp.ui.login.LoginActivity
import com.fonda.intermidatestoryapp.ui.main.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        setupView()

        preferences = getSharedPreferences(LoginActivity.SHARED_PREFERENCES, MODE_PRIVATE)
        val token = preferences.getString(LoginActivity.TOKEN, "").toString()

        val time = 2000
        Handler(Looper.getMainLooper()).postDelayed({
            if (token == ""){
                Intent(this, LoginActivity::class.java).also { startActivity(it) }
                finish()
            }else{
                Intent(this, MainActivity::class.java).also { startActivity(it) }
                finish()
            }
        }, time.toLong())
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}