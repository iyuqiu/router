package com.example.order

import android.os.Bundle
import android.util.Log
import com.example.annotation.Router
import com.example.common.base.BaseActivity
import com.example.order.databinding.OrderActivityHomeBinding

@Router(path = "/order/OrderActivity")
class OrderActivity :BaseActivity() {

    private lateinit var binding: OrderActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OrderActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.e(TAG, "onCreate: OrderActivity")
    }
}