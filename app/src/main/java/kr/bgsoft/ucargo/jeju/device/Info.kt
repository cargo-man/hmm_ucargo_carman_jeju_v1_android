package kr.bgsoft.ucargo.jeju.device

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import java.util.*

open class Info(context: Context) {

    val CTX = context

    @SuppressLint("MissingPermission")
    fun getNumber() : String? {
        var value   = ""
        var isCheck = true
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && CTX.checkSelfPermission(Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_DENIED) {
            isCheck = false
        }

        if(isCheck) {
            try {
                val telManager: TelephonyManager = CTX.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                value = telManager.line1Number
                value = value.replace("+82", "0")
            } catch (e: Exception) {

            }
        }
        return value
    }

    @SuppressLint("MissingPermission")
    fun getDeviceSerial() : String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Build.getSerial()
        } else {
            Build.SERIAL
        }
    }

    fun getAndroidid() : String? {
        return Settings.Secure.getString(CTX.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getModel() : String? {
        return Build.MODEL
    }

    fun getCarrier() : String? {
        var value   = ""
        var isCheck = true
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && CTX.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
            isCheck = false
        }

        if(isCheck) {
            try {
                val telManager: TelephonyManager = CTX.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                value = telManager.networkOperatorName
            } catch (e: Exception) {

            }
        }

        return value
    }

    fun getAppVersion(): Int {
        var rtnValue = 0
        var pInfo: PackageInfo? = null
        try {
            pInfo = CTX.packageManager.getPackageInfo(CTX.packageName, 0)
            rtnValue = pInfo!!.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return rtnValue
    }

    fun getAppVersionName(): String {
        var rtnValue = "1.0"
        var pInfo: PackageInfo? = null
        try {
            pInfo = CTX.packageManager.getPackageInfo(CTX.packageName, 0)
            rtnValue = pInfo!!.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return rtnValue
    }

    @SuppressLint("MissingPermission")
    fun isWifiConnect() : Boolean {
        var value = false

        val ncm = CTX.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val info = ncm.activeNetworkInfo as NetworkInfo
            if(info.type == ConnectivityManager.TYPE_WIFI && info.isConnectedOrConnecting) {
                value = true
            }
        } else {
            val info = ncm.getNetworkInfo(ConnectivityManager.TYPE_WIFI) as NetworkInfo
            value = (info.isAvailable && info.isConnected)
        }

        return value
    }

    @SuppressLint("MissingPermission")
    fun is3GConnect() : Boolean {
        var value = false

        val ncm = CTX.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val info = ncm.activeNetworkInfo as NetworkInfo
            if(info.type == ConnectivityManager.TYPE_MOBILE && info.isConnectedOrConnecting) {
                value = true
            }
        } else {
            val info = ncm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) as NetworkInfo
            value = (info.isAvailable && info.isConnected)
        }

        return value
    }

    @SuppressLint("MissingPermission")
    fun getDevicesUUID2(): String {
        val tm = CTX.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val tmDevice = "" + if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tm.imei
        } else {
            tm.deviceId
        }
        val tmSerial = "" + tm.simSerialNumber

        val androidId = "" + getAndroidid()
        val deviceUuid = UUID(androidId.hashCode().toLong(), tmDevice.hashCode().toLong() shl 32 or tmSerial.hashCode().toLong())
        return deviceUuid.toString()
    }

    @SuppressLint("MissingPermission")
    fun getDevicesUUID(): String {
        val androidId   = "" + getAndroidid()
        var deviceUuid  = ""

        if(androidId == "9774d56d682e549c") {
            val tm = CTX.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val tmDevice = "" + if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tm.imei
            } else {
                tm.deviceId
            }

            if(tmDevice.isNullOrBlank()) {
                deviceUuid = UUID.randomUUID().toString()
            } else {
                deviceUuid = UUID.nameUUIDFromBytes(tmDevice.toByteArray(Charsets.UTF_8)).toString()
            }
        } else {
            deviceUuid = UUID.nameUUIDFromBytes(androidId.toByteArray(Charsets.UTF_8)).toString()
        }

        return deviceUuid
    }

}