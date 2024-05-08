package com.example.router

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.annotation.Parameter
import com.example.annotation.Router
import com.example.common.base.BaseActivity
import com.example.common.originrouter.RecordPathManager
import com.example.router.databinding.ActivityMainBinding

@Router(path = "/app/MainActivity")
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private  val TAG_APP = "MainActivity>>>"

    @Parameter
    var name:String = ""

    @Parameter
    var age:Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.e(TAG_APP, "onCreate: MainActivity")
        binding.btnRouter.setOnClickListener {
            clazzMethod()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG_APP, "onResume: ", )
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG_APP, "onPause: ", )
    }


    private fun mapMethod(){
        val clazz = RecordPathManager.getTargetClass("order", "/order/OrderActivity")
        clazz?.let {
            val intent = Intent(this,clazz)
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
        } catch (exception:ClassNotFoundException){
            Log.e(TAG_APP, "clazzMethod: $exception", )
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG_APP, "onDestroy: ", )
    }
}