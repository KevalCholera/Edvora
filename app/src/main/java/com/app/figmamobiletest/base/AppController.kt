package com.app.figmamobiletest.base

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.app.figmamobiletest.utils.PrefUtils

class AppController : Application() {
    lateinit var objSharedPref: PrefUtils
    private val TAG = "AppController"

    override fun onCreate() {
        super.onCreate()
        objSharedPref = PrefUtils(this@AppController)
        instance = this
    }

    companion object {
        lateinit var instance: AppController

        fun getContext(): Context {
            return instance
        }
    }

    fun getAppContext(): AppController {
        return instance
    }

    fun isNetworkAvailable(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}


