package com.example.router_api

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.collection.LruCache
import com.example.annotation.RouterBean
import java.lang.IllegalArgumentException

object RouterManager {

    private lateinit var group: String
    private lateinit var path: String
    private const val TAG = "RouterManager"

    private var groupLurCache: LruCache<String, RouterLoadGroup> = LruCache(100)
    private var pathLurCache: LruCache<String, RouterLoadPath> = LruCache(100)

    private const val FILE_GROUP_NAME = "Router$\$Group$$"
    private const val FILE_PATH_NAME = "Router$\$Path$$"

    fun build(path: String): BundleManager {
        if (TextUtils.isEmpty(path) || !path.startsWith("/")) {
            throw IllegalArgumentException("path不对，正确写法：如：/app/HomeActivity")
        }

        if (path.lastIndexOf("/") == 0) {
            throw IllegalArgumentException("path不对，正确写法：如：/app/HomeActivity")
        }
        val finalGroup = path.substring(1, path.indexOf("/", 1))
        if (TextUtils.isEmpty(finalGroup)) {
            throw IllegalArgumentException("path不对，正确写法：如：/app/HomeActivity")
        }
        this.path = path
        this.group = finalGroup
        return BundleManager()
    }

    fun navigation(context: Context) {
        val groupClassName = "com.xiaoma.apt.$FILE_GROUP_NAME$group"
        Log.e(TAG, "navigation: $groupClassName")
        try {
            var loadGroup = groupLurCache.get(group)
            if (null == loadGroup) {
                val clazz = Class.forName(groupClassName)
                loadGroup = clazz.getDeclaredConstructor().newInstance() as RouterLoadGroup
                groupLurCache.put(group, loadGroup)
            }
            if (loadGroup.loadGroup().isEmpty()) {
                throw RuntimeException("路由表Group是空的")
            }

            var loadPath = pathLurCache.get(path)
            if (null == loadPath) {
                val clazz = loadGroup.loadGroup()[group]
                loadPath = clazz?.getDeclaredConstructor()?.newInstance() as RouterLoadPath
                pathLurCache.put(path, loadPath)
            }

            if (loadPath.loadPath().isEmpty()) {
                throw RuntimeException("路由表Path是空的")
            }

            val routerBean = loadPath.loadPath()[path]
            if (routerBean != null) {
                if (routerBean.type == RouterBean.Type.ACTIVITY) {
                    val intent = Intent(context, routerBean.clazz)
                    context.startActivity(intent)
                }
            }

        } catch (exception: Exception) {
            Log.e(TAG, "navigation-exception: ${exception.cause}")
        }
    }
}

class BundleManager {
    fun navigation(context: Context) {
        RouterManager.navigation(context)
    }
}
