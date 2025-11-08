package com.example.application_anonyme.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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

        // Vérifier si l'utilisateur est déjà connecté
        if (preferenceManager.isLoggedIn()) {
            navigateToMain()
            return
        }

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.loginButton.setOnClickListener {
            val pseudo = binding.pseudoEditText.text.toString().trim()

            if (validatePseudo(pseudo)) {
                viewModel.login(pseudo)
            }
        }
    }

    private fun validatePseudo(pseudo: String): Boolean {
        return when {
            pseudo.isEmpty() -> {
                binding.pseudoInputLayout.error = getString(com.example.application_anonyme.R.string.error_pseudo_empty)
                false
            }
            pseudo.length < Constants.MIN_PSEUDO_LENGTH -> {
                binding.pseudoInputLayout.error = getString(com.example.application_anonyme.R.string.error_pseudo_short)
                false
            }
            pseudo.length > Constants.MAX_PSEUDO_LENGTH -> {
                binding.pseudoInputLayout.error = getString(com.example.application_anonyme.R.string.error_pseudo_long)
                false
            }
            else -> {
                binding.pseudoInputLayout.error = null
                true
            }
        }
    }

    private fun observeViewModel() {
        viewModel.loginState.observe(this) { result ->
            when (result) {
                is LoginState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.loginButton.isEnabled = false
                    binding.pseudoEditText.isEnabled = false
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
                    binding.pseudoEditText.isEnabled = true
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}