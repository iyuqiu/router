package com.example.router

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.common.base.BaseApplication
import com.example.common.utils.Cons
class RouterApplication : BaseApplication() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        const val TOKEN = "9GSd4l98n2QRUwI3"
    }

    override fun onCreate() {
        super.onCreate()
        Log.e(Cons.TAG, "onCreate:RouterApplication ")
        context = applicationContext
//        RecordPathManager.joinGroup("order","/order/OrderActivity",OrderActivity::class.java)

    }
}