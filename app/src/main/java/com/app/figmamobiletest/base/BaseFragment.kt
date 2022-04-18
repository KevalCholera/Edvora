package com.embie.droid.fragments.common

import android.app.Activity
import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.app.figmamobiletest.base.AppController
import com.app.figmamobiletest.design.activity.DashboardActivity
import com.app.figmamobiletest.utils.PrefUtils
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

abstract class BaseFragment : Fragment() {

    val TAG = javaClass.simpleName
    protected lateinit var objSharedPref: PrefUtils
    protected lateinit var appcontroller: AppController
    final lateinit var mContext: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dashboard = DashboardActivity()
        val fragmentActivity = FragmentActivity()
        val requ = requireActivity()

        mContext =
            if (!dashboard.isFinishing)
                activity as DashboardActivity
            else if (!fragmentActivity.isFinishing)
                activity as FragmentActivity
            else
                requ

        objSharedPref = PrefUtils(mContext)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun progressBarTouchable(touchable: Boolean) {
        if (touchable)
            mContext.window.clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        else
            mContext.window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
    }

    fun getBooleanFromArgument(key: String, default: Boolean): Boolean {

        return if (arguments != null)
            requireArguments().getBoolean(key, default)
        else default

    }

    fun getBooleanFromArgument(key: String): Boolean {

        return if (arguments != null)
            requireArguments().getBoolean(key, false)
        else false
    }

    fun getStringFromArgument(key: String): String {

        return if (arguments != null && requireArguments().getString(key) != null)
            requireArguments().getString(key).toString()
        else ""

    }

}