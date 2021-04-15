package kr.bgsoft.ucargo.jeju.cview

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import kr.bgsoft.ucargo.jeju.controller.conf.Config
import kr.bgsoft.ucargo.jeju.controller.conf.Settings
import kr.bgsoft.ucargo.jeju.controller.http.DefaultHttp
import kr.bgsoft.ucargo.jeju.device.Info
import kr.bgsoft.ucargo.jeju.utils.BGLog

open class DefaultActivity : AppCompatActivity(), View.OnTouchListener {

    var TAG = javaClass.name
    val thisActivity = this
    val thisContext: Context = this
    val Log = BGLog(Config.SET_LOG)
    val Http = DefaultHttp()
    val thisDeviceInfo = Info(this)
    var nfba: FirebaseAnalytics? = null
    var arrPermission = Settings.arrPermission
    var nfcmid = ""
    var isDend = false

    enum class PEMTYPE {
        CALENDAR, CAMERA, CONTACTS, LOCATION, MIC, PHONE, SENSOR, SMS, STORAGE, ALL, CUSTOM
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        Http.setQue(this)

        logd("create Activity")

        nfba = FirebaseAnalytics.getInstance(this)

        val rootview = RelativeLayout(this)
        val param = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        rootview.layoutParams = param
        val root = window.decorView as ViewGroup
        root.addView(rootview)
        rootview.setOnTouchListener(this)

    }

    override fun onResume() {
        super.onResume()
        logd("resume Activity")
        isDend = false
    }

    override fun onPause() {
        super.onPause()
        isDend = true
    }

    override fun onRestart() {
        super.onRestart()
        logd("restart Activity")
    }

    override fun finish() {
        super.finish()
        logd("finish Activity")
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

    fun getPermissions(type: PEMTYPE) : ArrayList<String> {
        var array = ArrayList<String>()

        when(type) {
            PEMTYPE.CALENDAR -> {
                if(hasPermission(Manifest.permission.READ_CALENDAR)) {
                    array.add(Manifest.permission.READ_CALENDAR)
                }
                if(hasPermission(Manifest.permission.WRITE_CALENDAR)) {
                    array.add(Manifest.permission.WRITE_CALENDAR)
                }
            }
            PEMTYPE.CAMERA -> {
                if(hasPermission(Manifest.permission.CAMERA)) {
                    array.add(Manifest.permission.CAMERA)
                }
            }
            PEMTYPE.CONTACTS -> {
                if(hasPermission(Manifest.permission.READ_CONTACTS)) {
                    array.add(Manifest.permission.READ_CONTACTS)
                }
                if(hasPermission(Manifest.permission.WRITE_CONTACTS)) {
                    array.add(Manifest.permission.WRITE_CONTACTS)
                }
                if(hasPermission(Manifest.permission.GET_ACCOUNTS)) {
                    array.add(Manifest.permission.GET_ACCOUNTS)
                }
            }
            /*PEMTYPE.LOCATION -> {
                if(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    array.add(Manifest.permission.ACCESS_FINE_LOCATION)
                }
                if(hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    array.add(Manifest.permission.ACCESS_COARSE_LOCATION)
                }
            }*/
            PEMTYPE.MIC -> {
                if(hasPermission(Manifest.permission.RECORD_AUDIO)) {
                    array.add(Manifest.permission.RECORD_AUDIO)
                }
            }
            PEMTYPE.PHONE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if(hasPermission(Manifest.permission.READ_PHONE_NUMBERS)) {
                        array.add(Manifest.permission.READ_PHONE_NUMBERS)
                    }
                    logd("tmddbs durl durldruldrul")
                } else {
                    if(hasPermission(Manifest.permission.READ_PHONE_STATE)) {
                        array.add(Manifest.permission.READ_PHONE_STATE)
                    }
                    logd("승윤 여기여기여기여기!! ")
                }
                if(hasPermission(Manifest.permission.CALL_PHONE)) {
                    array.add(Manifest.permission.CALL_PHONE)
                }
                if(hasPermission(Manifest.permission.READ_CALL_LOG)) {
                    array.add(Manifest.permission.READ_CALL_LOG)
                }
                if(hasPermission(Manifest.permission.WRITE_CALL_LOG)) {
                    array.add(Manifest.permission.WRITE_CALL_LOG)
                }
                if(hasPermission(Manifest.permission.ADD_VOICEMAIL)) {
                    array.add(Manifest.permission.ADD_VOICEMAIL)
                }
                if(hasPermission(Manifest.permission.USE_SIP)) {
                    array.add(Manifest.permission.USE_SIP)
                }
                if(hasPermission(Manifest.permission.PROCESS_OUTGOING_CALLS)) {
                    array.add(Manifest.permission.PROCESS_OUTGOING_CALLS)
                }
            }
            PEMTYPE.SENSOR -> {
                if(hasPermission(Manifest.permission.BODY_SENSORS)) {
                    array.add(Manifest.permission.BODY_SENSORS)
                }
            }
            PEMTYPE.SMS -> {
                if(hasPermission(Manifest.permission.SEND_SMS)) {
                    array.add(Manifest.permission.SEND_SMS)
                }
                if(hasPermission(Manifest.permission.RECEIVE_SMS)) {
                    array.add(Manifest.permission.RECEIVE_SMS)
                }
                if(hasPermission(Manifest.permission.READ_SMS)) {
                    array.add(Manifest.permission.READ_SMS)
                }
                if(hasPermission(Manifest.permission.BROADCAST_WAP_PUSH)) {
                    array.add(Manifest.permission.BROADCAST_WAP_PUSH)
                }
                if(hasPermission(Manifest.permission.RECEIVE_MMS)) {
                    array.add(Manifest.permission.RECEIVE_MMS)
                }
            }
            PEMTYPE.STORAGE -> {
                if(hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    array.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
                if(hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    array.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
            PEMTYPE.ALL -> {
                try {
                    val info = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
                    if (info.requestedPermissions != null) {
                        for (p in info.requestedPermissions) {
                            array.add(p)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            PEMTYPE.CUSTOM -> {
                for(p in arrPermission) {
                    val array2 = getPermissions(p)
                    for(q in array2) {
                        array.add(q)
                    }
                }
            }
        }

        return array
    }

    fun hasPermission(permission: String): Boolean {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            if (info.requestedPermissions != null) {
                for (p in info.requestedPermissions) {
                    if (p == permission) {
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    fun startPermission(type: PEMTYPE) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val array = getPermissions(type)

            if (array.size > 0) {
                val array2 = arrayOfNulls<String>(array.size)
                array.toArray(array2)

                ActivityCompat.requestPermissions(this, array2, type.ordinal)
            }
        }
    }

    fun setPermission(type: PEMTYPE) {
        var isPem = false

        for(p in arrPermission) {
            if(p == type) {
                isPem = true
                break
            }
        }

        if(!isPem) {
            arrPermission.add(type)
        }
    }

    fun delPermission(type: PEMTYPE) {
        arrPermission.remove(type)
    }

    fun checkPermission(type: PEMTYPE) : Boolean {

        var arrReturn = true

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val array = getPermissions(type)

            for (permission in array) {
                var check = ContextCompat.checkSelfPermission(this, permission)

                if (check == PackageManager.PERMISSION_DENIED) {
                    arrReturn = false
                    break
                }
            }
        }

        return arrReturn
    }

    fun checkPermission() : ArrayList<Boolean>? {

        var arrReturn   = ArrayList<Boolean>()
        var count       = 0

        if(arrPermission.size > 0) {
            for (type2 in arrPermission) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    arrReturn.add(checkPermission(type2))
                }
            }

            return arrReturn
        } else {
            return null
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        val fragments : List<Fragment> = supportFragmentManager.fragments

        var type: PEMTYPE? = null

        for (p in PEMTYPE.values()) {
            if(p.ordinal == requestCode) {
                type = p
                break
            }
        }

        if(fragments.isNotEmpty()) {
            for(flagment : Fragment in fragments) {
                flagment.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        if(MotionEvent.ACTION_DOWN == event?.action) {
            logd(TAG + " tounch down :: " + event?.x.toString() + " - " + event?.y.toString())
        }

        return false
    }
}