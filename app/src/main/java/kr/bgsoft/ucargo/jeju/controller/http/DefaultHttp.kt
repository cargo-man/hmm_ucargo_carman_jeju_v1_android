package kr.bgsoft.ucargo.jeju.controller.http

import android.content.Context
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.navercorp.volleyextensions.volleyer.Volleyer
import com.navercorp.volleyextensions.volleyer.factory.DefaultRequestQueueFactory
import kr.bgsoft.ucargo.jeju.controller.conf.Config
import kr.bgsoft.ucargo.jeju.data.model.HttpInput
import kr.bgsoft.ucargo.jeju.utils.AES256
import kr.bgsoft.ucargo.jeju.utils.BGLog
import org.json.JSONObject

open class DefaultHttp() {
    var TAG                             = javaClass.name
    val Log                             = BGLog(Config.SET_LOG)
    var thisRequestQue: RequestQueue?   = null
    var thisContext: Context?           = null

    interface Callback {
        fun onError(code: HttpCode, message: String, hcode: String)
        fun onSuccess(json: Any)
    }

    enum class HttpCode {
        SUCCESS, ERROR, JSON_ERROR, CODE_ERROR, PARAM_ERROR, ERROR_700, ERROR_701, ERROR_702, ERROR_201
    }

    fun logd(value: String) {
        Log.d(TAG, value)
    }

    fun setQue(context: Context) {
        val requestQueue = DefaultRequestQueueFactory.create(context)
        requestQueue.start()
        thisRequestQue = requestQueue
        thisContext = context
    }

    fun requestJSON(url: String, data: JSONObject, callback: Callback) {
        requestJSON(url, data, true, callback)
    }

    fun requestJSON(url: String, data: JSONObject, isData: Boolean, callback: Callback) {

        val errorlistener = Response.ErrorListener { error ->
            logd("url : " + url + " / error : " + error.toString())
            try {
                try {
                    callback.onError(HttpCode.ERROR, "server error", "")
                } catch (e: Exception) {
                    logd(e.toString())
                }
            } catch (e: Exception) {
                logd(e.toString())
            }
        }

        val listener = Response.Listener<String> { response ->
            logd("url : " + url + " / response : " + response.toString())
            try {
                val json    = JSONObject(response)
                val code    = json.getString("code")
                val message = json.getString("msg")

                if(code == "100") {
                    val data = if (isData && !json.isNull("data")) {
                        json.get("data")
                    } else {
                        ""
                    }
                    val datainfo = JSONObject(data as String)
                    if(datainfo.getInt("success") > 0) {
                        callback.onSuccess(data)
                    }
                } else if(code == "200") {
                    val data = if (isData && !json.isNull("data")) {
                        json.get("data")
                    } else {
                        ""
                    }
                    callback.onSuccess(data)
                } else if(code == "201") {
                    callback.onError(HttpCode.ERROR_201, message, code)
                } else if(code == "701") {
                    callback.onError(HttpCode.ERROR_701, message, code)
                } else if(code == "702") {
                    callback.onError(HttpCode.ERROR_702, message, code)
                } else if(code == "700") { //server toast
                    toastl(message)
                    callback.onError(HttpCode.ERROR_700, message, code)
                } else {
                    callback.onError(HttpCode.CODE_ERROR, "server error [$code] : $message", code)
                }
            } catch (e : Exception) {
                callback.onError(HttpCode.JSON_ERROR, "json convert error " + e.toString(), "")
            }
        }

        data.put("hash", AES256.getHash())

        logd("url : $url / data : $data")

        Volleyer.volleyer(thisRequestQue).post(url)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .withBody(data.toString())
                .withErrorListener(errorlistener)
                .withListener(listener)
                .execute()
    }

    fun requestGet(url: String, callback: Callback) {
        requestGet(url, true, callback)
    }

    fun requestGet(url: String, isData: Boolean, callback: Callback) {
        val errorlistener = Response.ErrorListener {
            try {
                try {
                    callback.onError(HttpCode.ERROR, "server error", "")
                } catch (e: Exception) {
                    logd(e.toString())
                }
            } catch (e: Exception) {
                logd(e.toString())
            }
        }

        val listener = Response.Listener<String> { response ->
            logd("url : " + url + " / response : " + response.toString())
            try {
                val json    = JSONObject(response)
                val code    = json.getString("code")
                val message = json.getString("msg")

                if(code == "200") {
                    val data = if (isData && !json.isNull("data")) {
                        json.get("data")
                    } else {
                        ""
                    }
                    callback.onSuccess(data)
                } else if(code == "201") {
                    callback.onError(HttpCode.ERROR_201, message, code)
                } else if(code == "700") { //server toast
                    toastl(message)
                    callback.onError(HttpCode.ERROR_700, message, code)
                } else if(code == "701") {
                    callback.onError(HttpCode.ERROR_701, message, code)
                } else if(code == "702") {
                    callback.onError(HttpCode.ERROR_702, message, code)
                } else {
                    callback.onError(HttpCode.CODE_ERROR, "server error [$code] : $message", code)
                }
            } catch (e : Exception) {
                callback.onError(HttpCode.JSON_ERROR, "json convert error " + e.toString(), "")
            }
        }

        logd("url : $url")

        Volleyer.volleyer(thisRequestQue).get(url)
                .withErrorListener(errorlistener)
                .withListener(listener)
                .execute()
    }

    fun requestPost(url: String, input: HttpInput, isData: Boolean, callback: Callback) {

        val array   = ArrayList<HttpInput>()
        array.add(input)

        requestPost(url, array, isData, callback)
    }

    fun requestPost(url: String, input: ArrayList<HttpInput>, isData: Boolean, callback: Callback) {

        val http = Volleyer.volleyer(thisRequestQue)
                .post(url)

        val errorlistener = Response.ErrorListener { error ->
            callback.onError(HttpCode.ERROR, "server error $error", "")
        }

        val listener = Response.Listener<String> { response ->
            logd("url : " + url + " / response : " + response.toString())
            try {
                val json    = JSONObject(response)
                val code    = json.getString("code")
                val message = json.getString("msg")

                if(code == "200") {
                    val data = if (isData && !json.isNull("data")) {
                        json.get("data")
                    } else {
                        ""
                    }
                    callback.onSuccess(data)
                } else if(code == "201") {
                    callback.onError(HttpCode.ERROR_201, message, code)
                } else if(code == "700") { //server toast
                    toastl(message)
                    callback.onError(HttpCode.ERROR_700, message, code)
                } else if(code == "701") {
                    callback.onError(HttpCode.ERROR_701, message, code)
                } else if(code == "702") {
                    callback.onError(HttpCode.ERROR_702, message, code)
                } else {
                    callback.onError(HttpCode.CODE_ERROR, "server error [$code] : $message", code)
                }
            } catch (e : Exception) {
                callback.onError(HttpCode.JSON_ERROR, "json convert error " + e.toString(), "")
            }
        }

        logd("url : " + url + " / input : " + input.toString())

        for(i in 0..(input.size - 1)) {
            val data = input[i]

            if(data.file != null) {
                if(data.file!!.exists()) {
                    http.addFilePart(data.key, data.file)
                }
            } else {
                http.addStringPart(data.key, data.value)
            }
        }

        http.withErrorListener(errorlistener)
                .withListener(listener)
                .execute()
    }

    fun requestFCM(url: String, data: JSONObject, callback: Callback) {

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

    /*fun toasts(value: String) {
        val toast = Toast.makeText(thisContext, value, Toast.LENGTH_SHORT)
        val view = toast.view
        val text = view.findViewById<TextView>(android.R.id.message)
        view.setBackgroundResource(R.drawable.box_type1_5)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            text.setTextColor(thisContext!!.resources.getColor(android.R.color.white, null))
        } else {
            text.setTextColor(thisContext!!.resources.getColor(android.R.color.white))
        }
        toast.show()
    }

    fun toastl(value: String) {
        val toast = Toast.makeText(thisContext, value, Toast.LENGTH_SHORT)
        val view = toast.view
        val text = view.findViewById<TextView>(android.R.id.message)
        view.setBackgroundResource(R.drawable.box_type1_5)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            text.setTextColor(thisContext!!.resources.getColor(android.R.color.white, null))
        } else {
            text.setTextColor(thisContext!!.resources.getColor(android.R.color.white))
        }
        toast.show()
    }*/
}