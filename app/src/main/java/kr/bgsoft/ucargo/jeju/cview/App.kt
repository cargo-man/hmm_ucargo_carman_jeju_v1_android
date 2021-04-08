package kr.bgsoft.ucargo.jeju.cview

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import com.bumptech.glide.Glide
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.controller.conf.Config
import kr.bgsoft.ucargo.jeju.controller.http.DefaultHttp
import kr.bgsoft.ucargo.jeju.utils.BGLog

open class App: Application() {
    var TAG = javaClass.name

    companion object {
        val Log = BGLog(Config.SET_LOG)
        val Http = DefaultHttp()
    }

    override fun onCreate() {
        super.onCreate()
        Http.setQue(this)

        //firebase topic add
//        FirebaseMessaging.getInstance().subscribeToTopic(resources.getString(R.string.fcm_topic))

        //Notification oreo version setting
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            val channelMessage = NotificationChannel(resources.getString(R.string.fcm_topic), resources.getString(
                R.string.app_name), NotificationManager.IMPORTANCE_HIGH)
            channelMessage.description = resources.getString(R.string.splash_text2)
            channelMessage.enableLights(true)
            channelMessage.lightColor = Color.GREEN
            channelMessage.enableVibration(true)
            channelMessage.vibrationPattern = longArrayOf(100, 200, 100, 200)
            channelMessage.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            notificationManager.createNotificationChannel(channelMessage)
        }
    }

    fun logd(value: String) {
        Log.d(TAG, value)
    }

    fun loge(value: String) {
        Log.e(TAG, value)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Glide.get(this).trimMemory(level)
    }

}