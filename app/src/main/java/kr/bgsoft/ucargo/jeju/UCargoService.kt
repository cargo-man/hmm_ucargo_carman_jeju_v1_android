package kr.bgsoft.ucargo.jeju

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import kr.bgsoft.ucargo.jeju.controller.conf.Settings
import kr.bgsoft.ucargo.jeju.cview.DefaultService
import kr.bgsoft.ucargo.jeju.device.Location2

class UCargoService: DefaultService() {
    var nhandler: Handler? = null
    val CUT_COUNT = 60
    val TIMER_TIME = 5 * 60 * 1000L
    var nlocation: Location2? = null
    var ntrycount = 0

    companion object {
        var nlat = 0.0
        var nlng = 0.0
        var isStart = false
        var naddress = ""
        val STATUS_LOCATION_EMPTY = 3
        val STATUS_LOCATION_GETTING = 4
        val STATUS_LOCATION_GET = 5
        val ERROR_CUTTIME = 1
        var nstatus = 0
        var cwnum = ""

        var gohandler: Handler? = null

        fun reload() {
            if(nstatus == STATUS_LOCATION_EMPTY) {
                gohandler?.sendEmptyMessage(STATUS_LOCATION_EMPTY)
            }
        }
    }
    interface Callback {
        fun onError(code: Int)
        fun onSucess(data: Any)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        nlocation = Location2(this)
        nhandler = Handler(Looper.getMainLooper())

        cwnum = Settings.getID(this)

        isStart = true
        setManage()
        logd("onCreate start ")

        gohandler = @SuppressLint("HandlerLeak")
        object : Handler() {//핸들러 객체를 만들고
        override fun handleMessage(msg: Message) {
            if (msg.what == STATUS_LOCATION_EMPTY) {
                setManage()
            }
        }

        }
    }

    override fun onDestroy() {
        stopSelf()
        isStart = false

        logd("onDestroy end ")
        super.onDestroy()
    }

    fun getLocation(callback: Callback?, isReset: Boolean = false) {
        //logd("getLocation")

        if (nlocation!!.isLocation()) {
            // logd("getLocation location on")

            if (isReset) {
                nlat = 0.0
                nlng = 0.0
                ntrycount = 0
                nstatus = STATUS_LOCATION_EMPTY
                logd("getLocation location reset")
            }

            if (nstatus == STATUS_LOCATION_EMPTY) {
                logd("getLocation location empty")
                nlocation?.setLocation()
                nstatus = STATUS_LOCATION_GETTING
            }

            if (nstatus == STATUS_LOCATION_GETTING) {
                //logd("getLocation location trycount : $ntrycount / $CUT_COUNT")

                if (CUT_COUNT < ntrycount) {
                    nstatus = STATUS_LOCATION_EMPTY
                    callback?.onError(ERROR_CUTTIME)
                } else {
                    val dlocation = nlocation!!.getLocation()
                    if (dlocation[0] == 0.0 && dlocation[1] == 0.0) {
                        nhandler?.postDelayed({
                            getLocation(callback)
                            ntrycount++
                        }, 500)
                    } else {
                        nlat = dlocation[0]
                        nlng = dlocation[1]
                        nstatus = STATUS_LOCATION_GET
                        callback?.onSucess(dlocation)
                    }
                }
            }
        }
    }

    fun setManage() {
        getLocation(object : Callback {
            override fun onError(code: Int) {

            }

            override fun onSucess(data: Any) {
                val loc = data as DoubleArray
                nlat = loc[0]
                nlng = loc[1]

                val address = nlocation?.getAddress(nlat, nlng)

                logd(loc[0].toString() + " .. " + loc[1].toString() + " :: " + address + " == " + naddress)

                if(!address.isNullOrBlank() && naddress != address.toString()) {
                    if (!cwnum.isNullOrBlank()) {
                        logd("start save")
                        Handler().postDelayed({
                            /*MemberHttp().setMyGPS(cwnum!!, nlat, nlng, address.toString(), Http, object : DefaultHttp.Callback {
                                override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                                    logd("error :: " + message)
                                }

                                override fun onSuccess(json: Any) {
                                    logd("saveok")
                                    naddress = address.toString()
                                }
                            })*/
                        }, 1000)
                        naddress = address.toString()
                    }
                }

            }
        }, true)

        nhandler?.postDelayed({
            setManage()
        }, TIMER_TIME)
    }
}