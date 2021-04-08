package kr.bgsoft.ucargo.jeju.cview

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import kr.bgsoft.ucargo.jeju.controller.conf.Config
import kr.bgsoft.ucargo.jeju.controller.http.DefaultHttp
import kr.bgsoft.ucargo.jeju.utils.BGLog
import kr.bgsoft.ucargo.jeju.utils.Etc

open class DefaultFragment : Fragment() {
    val TAG = javaClass.name
    val Log = BGLog(Config.SET_LOG)
    val Http = DefaultHttp()
    var isDend = false

    var thisActivity: DefaultActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        thisActivity = activity as DefaultActivity
        thisActivity?.let { Http.setQue(it) }
    }
    fun logd(value: String) {
        Log.d(TAG, value)
    }

    fun loge(value: String) {
        Log.e(TAG, value)
    }

    fun hideKeyboard() {
        if(thisActivity != null) {
            Etc.hideKeyboard(thisActivity!!)
        }
    }

    override fun onResume() {
        super.onResume()
        isDend = false
    }

    override fun onPause() {
        super.onPause()
        isDend = true
    }

    fun toasts(value: String) {
        try {
            Toast.makeText(context, value, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            logd(e.toString())
        }
    }

    fun toastl(value: String) {
        try {
            Toast.makeText(context, value, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            logd(e.toString())
        }
    }

    override fun onStop() {
        super.onStop()
        isDend = true
    }
}