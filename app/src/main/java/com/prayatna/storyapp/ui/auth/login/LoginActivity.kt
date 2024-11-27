package com.prayatna.storyapp.ui.auth.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.prayatna.storyapp.data.source.UserModel
import com.prayatna.storyapp.databinding.ActivityLoginBinding
import com.prayatna.storyapp.helper.Result
import com.prayatna.storyapp.ui.AuthViewModelFactory
import com.prayatna.storyapp.ui.auth.register.RegisterActivity
import com.prayatna.storyapp.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupView()
        setupResult()

    }

    private fun setupResult() {
        viewModel.login.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    val data = result.data.loginResult!!
                    val user = UserModel(
                        userId = data.userId,
                        userName = data.name,
                        token = data.token,
                        isLogin = true
                    )
                    viewModel.saveSession(user)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is Result.Error -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    @Suppress("DEPRECATION")
    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                FLAG_FULLSCREEN,
                FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            goLogin()
        }
        binding.btnHasNoAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun goLogin() {
        val email = binding.emailInput.emailEditText.text.toString()
        val password = binding.passwordInput.passwordEditText.text.toString()
        viewModel.login(email, password)
    }
}