package com.example.login.debug

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.login.LoginActivity
import com.example.login.databinding.LoginActivityDebugBinding

class DebugLoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityDebugBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityDebugBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnClick.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}