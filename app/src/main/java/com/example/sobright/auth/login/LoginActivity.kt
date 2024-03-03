package com.example.sobright.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.sobright.MainActivity
import com.example.sobright.auth.data.AuthState
import com.example.sobright.auth.data.AuthViewModel
import com.example.sobright.auth.data.ViewModelFactory
import com.example.sobright.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels {
        ViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel.authState.observe(this){

            when(it){
                AuthState.Idle -> {
                    binding.progressBar.visibility = View.GONE
                }
                AuthState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                AuthState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (email.isEmpty()||!isValidEmail(email)){
                Toast.makeText(this, "Email Should Not Be Empty and Valid", Toast.LENGTH_SHORT).show()
            }else if (password.isEmpty()){
                Toast.makeText(this, "Password Should Not Be Empty", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.handleSignIn(email, password)
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}