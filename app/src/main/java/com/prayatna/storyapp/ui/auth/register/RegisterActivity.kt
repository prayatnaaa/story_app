package com.prayatna.storyapp.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.prayatna.storyapp.databinding.ActivityRegisterBinding
import com.prayatna.storyapp.helper.Result
import com.prayatna.storyapp.ui.AuthViewModelFactory
import com.prayatna.storyapp.ui.auth.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> {
        AuthViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupResult()

    }

    private fun setupResult() {
        viewModel.register.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Error -> {
                    showLoading(false)
                    showToast(result.error)
                }

                is Result.Success -> {
                    showLoading(false)
                    showToast(result.data.message.toString())
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }

    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener { register() }
        binding.btnHasAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun register() {
        val email = binding.emailInput.emailEditText.text.toString()
        val password = binding.passwordInput.passwordEditText.text.toString()
        val name = binding.nameInput.nameEditText.text.toString()
        viewModel.register(name, email, password)
    }
}