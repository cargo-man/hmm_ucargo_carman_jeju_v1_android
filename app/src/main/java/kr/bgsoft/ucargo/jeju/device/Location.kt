package kr.bgsoft.ucargo.jeju.device

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import java.util.*

class Location(context: Context) : LocationListener {

    val thisContext                     = context
    val MIN_DISTANCE_UPDATES: Float     = 100f              //GPS 정보 업데이트 거리 10미터
    val MIN_TIME_UPDATES: Long          = 1000 * 60 * 10    //GPS 정보 업데이트 시간 1/1000

    var thisManager : LocationManager?  = null
    var isGPS                           = false
    var isNPS                           = false
    var lat: Double                     = 0.0
    var lng: Double                     = 0.0

    override fun onLocationChanged(location: Location) {
        lat = location!!.latitude
        lng = location!!.longitude
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }

    @SuppressLint("MissingPermission")
    fun setLocation() {
        if(ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        if(isNPS) {
            thisManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_UPDATES, MIN_DISTANCE_UPDATES, this)
            val location = thisManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if(location != null) {
                lat = location.latitude
                lng = location.longitude
            }
        }

        if(isGPS) {
            thisManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPDATES, MIN_DISTANCE_UPDATES, this)
            val location = thisManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if(location != null) {
                lat = location.latitude
                lng = location.longitude
            }
        }
    }

    fun isLocation() : Boolean {
        isGPS = thisManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        isNPS = thisManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        return isNPS || isGPS
    }

    fun getAddress(lat: Double, lng: Double) : String {
        var address = ""

        val geo = Geocoder(thisContext, Locale.getDefault())
        var list: List<Address>? = null

        try {
            list = geo.getFromLocation(lat, lng, 1)

            val addr = list.get(0)

            address = changeAddrtoString(addr)

        } catch (e : Exception) {

        }

        return address
    }

    fun searchAddress(value: String, max: Int) : List<Address>? {
        val geo = Geocoder(thisContext, Locale.getDefault())
        var list: List<Address>? = null

        Log.d("nbs천재", Locale.getDefault().toString())

        try {
            list = geo.getFromLocationName(value, max)
        } catch (e : Exception) {
            Log.d("nbs천재", e.toString())
        }

        return list
    }

    fun changeAddrtoString(addr: Address) : String {
        var address = ""

        if(addr.getAddressLine(0).isNullOrBlank()) {
            address = if (!addr.adminArea.isNullOrBlank()) {
                addr.adminArea
            } else {
                ""
            } + if (!addr.locality.isNullOrBlank() && addr.adminArea != addr.locality) {
                " " + addr.locality
            } else {
                ""
            } + if (!addr.thoroughfare.isNullOrBlank()) {
                " " + addr.thoroughfare
            } else {
                ""
            } + if (!addr.featureName.isNullOrBlank()) {
                " " + addr.featureName
            } else {
                ""
            }
        } else {
            address = addr.getAddressLine(0).replace(addr.countryName, "").trim()
        }

        return address
    }

    fun getLocation() : DoubleArray {
        setLocation()
        return doubleArrayOf(lat, lng)
    }

    init {
        thisManager = thisContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        isGPS = thisManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        isNPS = thisManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    fun stopLocation() {
        thisManager?.removeUpdates(this)
    }


}