package com.fonda.intermidatestoryapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.fonda.intermidatestoryapp.ui.main.MainActivity
import com.fonda.intermidatestoryapp.R
import com.fonda.intermidatestoryapp.api.ApiConfig
import com.fonda.intermidatestoryapp.databinding.ActivityLoginBinding
import com.fonda.intermidatestoryapp.model.UserLoginResponse
import com.fonda.intermidatestoryapp.ui.register.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(),View.OnClickListener {

    companion object {
        val SHARED_PREFERENCES = "shared_preferences"
        val CHECKBOX = "checkbox"
        val NAME = "name"
        val USER_ID = "user_id"
        val TOKEN = "token"
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var isSaveLoginInfo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE)
        isSaveLoginInfo = sharedPreferences.getBoolean(CHECKBOX,false)
        saveloginInfo(isSaveLoginInfo)

        binding.loginButton.setOnClickListener(this)
        binding.signupButton.setOnClickListener(this)

        setupView()
        playAnimation()
        supportActionBar?.hide()
    }

    private fun playAnimation() {

        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(500)
        val cb = ObjectAnimator.ofFloat(binding.cbRemember, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA,1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(
                title,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                cb,
                login,
                register

            )
            startDelay = 500
        }.start()
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
        supportActionBar?.hide()    }

    private fun saveloginInfo(boolean: Boolean) {
        if(boolean){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun login() {
        showLoading(true)
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val client = ApiConfig.getApiService().getLoginUser(email, password)
        client.enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(
                call: Call<UserLoginResponse>,
                response: Response<UserLoginResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.loginResult?.apply {
                        validateLogin(userId, name, token)
                    }
                    Intent(this@LoginActivity, MainActivity::class.java).also { startActivity(it) }
                    showLoading(false)
                    clearEditText()
                    finish()
                }
                else{
                    showLoading(false)
                    Toast.makeText(
                        this@LoginActivity,
                        "Data yang dimasukan tidak valid",
                        Toast.LENGTH_SHORT
                    ).show()                }
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(
                    this@LoginActivity,
                    "Data yang dimasukan tidak valid",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun clearEditText() {
        binding.emailEditText.text!!.clear()
        binding.passwordEditText.text!!.clear()
    }


    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.loginButton -> {
                login()
            }
            R.id.signupButton ->{
                val mainIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(mainIntent)
            }
        }
    }

    private fun validateLogin(userId: String, name: String, token: String){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(NAME,name)
        editor.putString(USER_ID,userId)
        editor.putString(TOKEN,token)
        editor.putBoolean(CHECKBOX,binding.cbRemember.isChecked)
        editor.apply()
    }
}