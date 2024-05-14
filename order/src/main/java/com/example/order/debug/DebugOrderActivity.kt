package com.example.order.debug

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.order.OrderActivity
import com.example.order.R
import com.example.order.databinding.OrderActivityDebugBinding

class DebugOrderActivity : AppCompatActivity() {

    private lateinit var binding: OrderActivityDebugBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OrderActivityDebugBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.o)
        binding.orderBtnOrder.setOnClickListener {
            startActivity(Intent(this, OrderActivity::class.java))
        }
//       R.layout.order_activity_debug

    }
}