package com.example.sobright.auth.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.sobright.MainActivity
import com.example.sobright.R
import com.example.sobright.auth.data.AuthViewModel
import com.example.sobright.auth.data.ViewModelFactory
import com.example.sobright.auth.login.LoginActivity
import com.example.sobright.auth.register.RegisterActivity
import com.example.sobright.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    private val viewModel: AuthViewModel by viewModels {
        ViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel.getLoginState()

        viewModel.loginState.observe(this) {
            if (it) {
                Toast.makeText(this@StartActivity, "Logged in", Toast.LENGTH_LONG).show()
                val intent = Intent(this@StartActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else Toast.makeText(this@StartActivity, "No Auth Data", Toast.LENGTH_LONG).show()
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }


        binding.btnRegister.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}