package com.example.login

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.annotation.Router
import com.example.login.databinding.LoginActivityHomeBinding
import com.example.router_api.RouterManager

@Router(path = "/login/LoginActivity")
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityHomeBinding
    private val TAG = "login>>>"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.e(TAG, "onCreate:LoginActivity ")

        binding.loginBtnApp.setOnClickListener {
            RouterManager.build("/app/MainActivity").navigation(this)
        }

        binding.loginBtnOrder.setOnClickListener {
            RouterManager.build("/order/OrderActivity").navigation(this)
        }
    }
}