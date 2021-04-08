package kr.bgsoft.ucargo.jeju.cview

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import kr.bgsoft.ucargo.jeju.controller.conf.Config
import kr.bgsoft.ucargo.jeju.controller.http.DefaultHttp
import kr.bgsoft.ucargo.jeju.utils.BGLog

open class DefaultDialog(context: Context?) : Dialog(context!!, android.R.style.Theme_Translucent_NoTitleBar)  {

    val TAG         = javaClass.name
    val thisContext = context
    val resource    = thisContext?.resources
    val Log         = BGLog(Config.SET_LOG)
    val Http        = DefaultHttp()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        //thisContext?.let { Http.setQue(it) }
    }

    fun toasts(value: String) {
        try {
            Toast.makeText(thisContext, value, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            logd(e.toString())
        }
    }

    fun toastl(value: String) {
        try {
            Toast.makeText(thisContext, value, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            logd(e.toString())
        }
    }


    fun logd(value : String) {
        Log.d(TAG, value)
    }
    fun loge(value : String) {
        Log.e(TAG, value)
    }
}