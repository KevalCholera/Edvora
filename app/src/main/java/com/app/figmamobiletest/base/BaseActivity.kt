package com.app.figmamobiletest.base

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.app.figmamobiletest.service.RetrofitService
import com.app.figmamobiletest.utils.*
import java.util.*

open class BaseActivity : DataBindingActivity() {
    val TAG = "ET--" + javaClass.simpleName
    open lateinit var objSharedPref: PrefUtils

    protected lateinit var mActivity: Activity

    lateinit var retrofitService: RetrofitService

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBaseComponants(this)
        retrofitService = RetrofitService.getInstance()

    }

    fun initBaseComponants(activityBase: BaseActivity) {
        objSharedPref = PrefUtils(this@BaseActivity)
        mActivity = this@BaseActivity
        globalActivity = this@BaseActivity

    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var globalActivity: Activity
    }

    override fun attachBaseContext(newBase: Context) {
        val sPrefs = PrefUtils(newBase)
        // val sPrefs = newBase.getSharedPreferences("HaramainSharedPref", Context.MODE_PRIVATE)
        val languageCode = sPrefs.getString("key_lang")
        super.attachBaseContext(newBase)
        setApplicationLanguage("en")
//        ContextWrapper.wrap(newBase, locale)
    }

    private fun setApplicationLanguage(newLanguage: String?) {
        val activityRes = resources
        val activityConf = activityRes.configuration
        val newLocale = Locale(newLanguage)
        activityConf.setLocale(newLocale)

        activityRes.updateConfiguration(activityConf, activityRes.displayMetrics)
        val applicationRes: Resources = applicationContext.resources
        val applicationConf = applicationRes.configuration
        applicationConf.setLocale(newLocale)
        applicationRes.updateConfiguration(applicationConf, applicationRes.displayMetrics)
    }


    override fun onDestroy() {
        super.onDestroy()
        logD("objSharedPref", "onDestroy")
    }

    fun progressBarTouchable(touchable: Boolean) {
        if (touchable)
            window.clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        else
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
    }

    fun logI(tag: String, msg: String) {
        Log.i(TAG, "$tag: $msg")
    }

    fun logE(tag: String, msg: String) {
        Log.e(TAG, "$tag: $msg")
    }

    fun logD(tag: String, msg: String) {
        Log.d(TAG, "$tag: $msg")
    }

    fun logW(tag: String, msg: String) {
        Log.w(TAG, "$tag: $msg")
    }

}