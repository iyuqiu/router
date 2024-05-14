package com.example.router

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.annotation.Parameter
import com.example.annotation.Router
import com.example.common.base.BaseActivity
import com.example.common.originrouter.RecordPathManager
import com.example.router.databinding.ActivityMainBinding
import com.example.router_api.RouterManager

@Router(path = "/app/MainActivity")
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "app>>>"

    @Parameter
    var name: String = ""

    @Parameter
    var age: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.e(TAG, "onCreate: MainActivity-url")
        binding.btnRouter.setOnClickListener {
            binding.tvContent.text = "app首页-url=${BuildConfig.API_URL}"
            clazzMethod()
        }


        binding.btnLogin.setOnClickListener {
            RouterManager.build("/login/LoginActivity").navigation(this)
        }

        binding.btnOrder.setOnClickListener {
            RouterManager.build("/order/OrderActivity").navigation(this)
        }
    }

    private fun mapMethod() {
        val clazz = RecordPathManager.getTargetClass("order", "/order/OrderActivity")
        clazz?.let {
            val intent = Intent(this, clazz)
            startActivity(intent)
        }
    }

    private fun clazzMethod() {
        /**
         *  通过类加载交互方式完成module之间的跳转
         */
        try {
            val clazz = Class.forName("com.example.order.OrderActivity")
            val intent = Intent(this, clazz)
            startActivity(intent)
        } catch (exception: ClassNotFoundException) {
            Log.e(TAG, "clazzMethod: $exception")
        }

    }
}