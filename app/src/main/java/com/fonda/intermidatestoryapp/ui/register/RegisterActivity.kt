package com.fonda.intermidatestoryapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fonda.intermidatestoryapp.R
import com.fonda.intermidatestoryapp.api.ApiConfig
import com.fonda.intermidatestoryapp.databinding.ActivityRegisterBinding
import com.fonda.intermidatestoryapp.model.UserRegisterResponse
import com.fonda.intermidatestoryapp.ui.login.LoginActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        playAnimation()
        binding.signupButton.setOnClickListener(this)
        binding.tvLogin.setOnClickListener(this)
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val nameTextView = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.etName, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)



        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup,
                login
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
        supportActionBar?.hide()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.signupButton -> {
                if(validateCreateAccount()) {
                    requestCreateAccount()
                    clearEditText()

                } else {
                    clearEditText()
                }
            }
            R.id.tv_login ->{
                val mainIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(mainIntent)
                finish()
            }
        }
    }

    private fun clearEditText() {
        binding.etName.text.clear()
        binding.emailEditText.text!!.clear()
        binding.passwordEditText.text!!.clear()    }

    private fun requestCreateAccount() {
        val name = binding.etName.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        showLoading(true)
        val client = ApiConfig.getApiService().createAccount(name, email, password)
        client.enqueue(object : Callback<UserRegisterResponse>{
            override fun onResponse(
                call: Call<UserRegisterResponse>,
                response: Response<UserRegisterResponse>
            ) {
                if(response.isSuccessful){
                    showLoading(false)
                    Toast.makeText(this@RegisterActivity,"Register Success",Toast.LENGTH_SHORT).show()
                    val mainIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    showLoading(false)
                    startActivity(mainIntent)
                    finish()
                }else {
                   showLoading(false)
                    try {
                        val jObjError = JSONObject(response.errorBody()?.string())
                        Toast.makeText(this@RegisterActivity,jObjError.getString("message"),Toast.LENGTH_LONG).show()
                    }catch (e:Exception){
                        Toast.makeText(this@RegisterActivity,e.message,Toast.LENGTH_LONG).show()
                    }

                    }
            }
            override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {

                Log.e("error : ","${t.message}")
                Toast.makeText( this@RegisterActivity, "${t.message.toString()}", Toast.LENGTH_SHORT).show()

            }
        })

    }



    private fun validateCreateAccount(): Boolean {
        return if(binding.emailEditText.text!!.isNotEmpty() && binding.passwordEditText.text!!.isNotEmpty() && binding.etName.text.isNotEmpty()
            && android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.text.toString()).matches()
            && binding.passwordEditText.text.toString().length > 5){
            true
        }else {
            Toast.makeText(this,"Invalid Format",Toast.LENGTH_SHORT).show()
            false
        }

    }
    private fun showLoading(state : Boolean){
        if(state){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }
}