package com.example.common.base

import android.app.Application
import android.util.Log
import com.example.common.utils.Cons

open class BaseApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        Log.e(Cons.TAG, "onCreate:BaseApplication ")
    }
}