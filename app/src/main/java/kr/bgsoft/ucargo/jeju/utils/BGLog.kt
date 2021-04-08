package kr.bgsoft.ucargo.jeju.utils

import android.util.Log

open class BGLog(isUse: Boolean = false) {
    var TAG = ""
    var USE = isUse

    fun save(tag : String, value : String, type : String?) {
        if(USE) {
            when (type) {
                "e" -> Log.e(TAG, "[$tag] ==> $value")
                "w" -> Log.w(TAG, "[$tag] ==> $value")
                "i" -> Log.i(TAG, "[$tag] ==> $value")
                "v" -> Log.v(TAG, "[$tag] ==> $value")
                else -> Log.d(TAG, "[$tag] ==> $value")
            }
        }
    }

    fun e(tag : String, value : String) {
        save(tag, value, "e")
    }
    fun w(tag : String, value : String) {
        save(tag, value, "w")
    }
    fun i(tag : String, value : String) {
        save(tag, value, "i")
    }
    fun v(tag : String, value : String) {
        save(tag, value, "v")
    }
    fun d(tag : String, value : String) {
        save(tag, value, "d")
    }

    init {
        TAG = javaClass.simpleName
    }
}