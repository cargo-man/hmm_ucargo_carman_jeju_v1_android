package kr.bgsoft.ucargo.jeju.cview

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import kr.bgsoft.ucargo.jeju.controller.conf.Config
import kr.bgsoft.ucargo.jeju.utils.BGLog

open class DefaultService: Service() {
    var TAG     = javaClass.name
    val Log     = BGLog(Config.SET_LOG)
    val Http    = App.Http
    var isDend  = false

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
        isDend = false
    }

    override fun onDestroy() {
        super.onDestroy()
        isDend = true
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun logd(value: String) {
        Log.d(TAG, value)
    }

    fun loge(value: String) {
        Log.e(TAG, value)
    }

    fun toasts(value: String) {
        try {
            Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            logd(e.toString())
        }
    }

    fun toastl(value: String) {
        try {
            Toast.makeText(this, value, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            logd(e.toString())
        }
    }
}