package com.example.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.annotation.Router
import com.example.login.databinding.LoginActivityHomeBinding

@Router(path = "/login/LoginActivity")
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}