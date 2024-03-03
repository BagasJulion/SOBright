package com.example.sobright.auth.register


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.sobright.auth.data.AuthState
import com.example.sobright.auth.data.AuthViewModel
import com.example.sobright.auth.data.ViewModelFactory
import com.example.sobright.auth.login.LoginActivity
import com.example.sobright.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels {
        ViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupAction()

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

                    Toast.makeText(this, "Registered", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                else -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }


    private fun setupAction() {
        binding.btnRegister.setOnClickListener {
            val name = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (name.isEmpty()) {
                showToast("Name is Empty, Please enter your name.")
            } else if (email.isEmpty()) {
                showToast("Email is Empty, Please enter your email.")
            } else if (password.isEmpty()) {
                showToast("Password is Empty, Please enter your password.")
            } else {
                viewModel.handleSignUp(email,password,name)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
