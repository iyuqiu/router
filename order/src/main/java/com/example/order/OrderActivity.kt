package com.example.order

import android.os.Bundle
import android.util.Log
import com.example.annotation.Router
import com.example.common.base.BaseActivity
import com.example.order.databinding.OrderActivityHomeBinding
import com.example.router_api.RouterManager

@Router(path = "/order/OrderActivity")
class OrderActivity :BaseActivity() {
    private  val TAG = "order>>>"
    private lateinit var binding: OrderActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OrderActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.e(TAG, "onCreate: OrderActivity")
        binding.orderBtnApp.setOnClickListener {
            RouterManager.build("/app/MainActivity").navigation(this)
        }
        binding.orderBtnLogin.setOnClickListener {
            RouterManager.build("/login/LoginActivity").navigation(this)
        }
    }
}