package com.example.application_anonyme.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.application_anonyme.R
import com.example.application_anonyme.databinding.ActivityLoginBinding
import com.example.application_anonyme.ui.main.MainActivity
import com.example.application_anonyme.utils.Constants
import com.example.application_anonyme.utils.PreferenceManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        if (preferenceManager.isLoggedIn()) {
            navigateToMain()
            return
        }

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.loginButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val pseudo = binding.pseudoEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (validateInputs(name, pseudo, password)) {
                viewModel.login(pseudo, password)
            }
        }

        binding.registerText.setOnClickListener {
            Toast.makeText(this, "Fonction d'inscription à venir 💫", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(name: String, pseudo: String, password: String): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            binding.nameInputLayout.error = getString(R.string.error_name_empty)
            isValid = false
        } else binding.nameInputLayout.error = null

        if (pseudo.isEmpty()) {
            binding.pseudoInputLayout.error = getString(R.string.error_pseudo_empty)
            isValid = false
        } else if (pseudo.length < Constants.MIN_PSEUDO_LENGTH) {
            binding.pseudoInputLayout.error = getString(R.string.error_pseudo_short)
            isValid = false
        } else {
            binding.pseudoInputLayout.error = null
        }

        if (password.isEmpty()) {
            binding.passwordInputLayout.error = getString(R.string.error_password_empty)
            isValid = false
        } else if (password.length < 6) {
            binding.passwordInputLayout.error = getString(R.string.error_password_short)
            isValid = false
        } else {
            binding.passwordInputLayout.error = null
        }

        return isValid
    }

    private fun observeViewModel() {
        viewModel.loginState.observe(this) { result ->
            when (result) {
                is LoginState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.loginButton.isEnabled = false
                }
                is LoginState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    preferenceManager.saveUser(result.user, result.token)
                    Toast.makeText(this, Constants.SUCCESS_LOGIN, Toast.LENGTH_SHORT).show()
                    navigateToMain()
                }
                is LoginState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.loginButton.isEnabled = true
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
