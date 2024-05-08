package com.example.router

import android.util.Log
import com.example.common.base.BaseApplication
import com.example.common.originrouter.RecordPathManager
import com.example.common.utils.Cons
//import com.example.order.OrderActivity

class RouterApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        Log.e(Cons.TAG, "onCreate:RouterApplication ")
//        RecordPathManager.joinGroup("order","/order/OrderActivity",OrderActivity::class.java)

    }
}