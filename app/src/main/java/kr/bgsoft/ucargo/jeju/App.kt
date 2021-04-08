package kr.bgsoft.ucargo.jeju

import android.content.Context
import com.bumptech.glide.Glide
import kr.bgsoft.ucargo.jeju.controller.conf.Config
import kr.bgsoft.ucargo.jeju.cview.App
import kr.bgsoft.ucargo.jeju.data.model.UserInfo
import kr.bgsoft.ucargo.jeju.utils.AES256

class App: App() {
    var CTX: Context? = null
    var fcmid = ""

    companion object {
        var host = ""
        var userInfo = UserInfo()
    }

    override fun onCreate() {
        super.onCreate()

        AES256.set(Config.SET_AES)
        AES256.set2(Config.SET_HASH)

    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Glide.get(this).trimMemory(level)
    }

    fun aesEncode(value: String): String? {
        return AES256.encode(value)
    }

    fun aesDecode(value: String): String? {
        return AES256.decode(value)
    }

}