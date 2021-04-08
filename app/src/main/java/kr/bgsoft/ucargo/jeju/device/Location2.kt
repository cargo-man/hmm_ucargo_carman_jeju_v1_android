package kr.bgsoft.ucargo.jeju.device

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import kr.bgsoft.ucargo.jeju.cview.App
import java.util.*

class Location2(context: Context): GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    val thisContext                     = context
    val MIN_DISTANCE_UPDATES: Float     = 100f              //GPS 정보 업데이트 거리 10미터
    val MIN_TIME_UPDATES: Long          = 1000 * 60 * 10    //GPS 정보 업데이트 시간 1/1000
    val Log                             = App.Log
    val TAG                             = javaClass.name
    var nlng                            = 0.0
    var nlat                            = 0.0
    var isStart                         = false

    val mLocationRequest: LocationRequest?                  = null
    var mGoogleApiClient: GoogleApiClient?                  = null
    var fusedLocationClient: FusedLocationProviderClient?   = null


    val UPDATE_INTERVAL: Long = 15000   /* 15 secs */
    val FASTEST_INTERVAL: Long = 5000   /* 5 secs */

    init {
        logd("start Location2")

        mGoogleApiClient = GoogleApiClient.Builder(thisContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()

        isStart = false
    }

    fun start() {
        if (mGoogleApiClient != null && !isStart) {
            mGoogleApiClient?.connect()
            isStart = true
        }
    }

    fun stop() {
        if (mGoogleApiClient != null && isStart) {
            mGoogleApiClient?.disconnect()
            isStart = false
        }
    }

    fun getLocation() : DoubleArray {
        setLocation()
        return doubleArrayOf(nlat, nlng)
    }

    fun isLocation() : Boolean {
        val lm = thisContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        val isGPS = lm?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
        val isNPS = lm?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?: false

        return isNPS || isGPS
    }

    @SuppressLint("MissingPermission")
    fun setLocation() {
        start()
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(thisContext)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            location: Location? ->
            nlat = location?.latitude ?: 0.0
            nlng = location?.longitude ?: 0.0
            stop()

            // logd("last location :: lat = " + nlat.toString() + " / lng = " + nlng.toString())
        }
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

        android.util.Log.d("nbs천재", Locale.getDefault().toString())

        try {
            list = geo.getFromLocationName(value, max)
        } catch (e : Exception) {
            android.util.Log.d("nbs천재", e.toString())
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

    override fun onConnected(p0: Bundle?) {
        // logd("onConnected")
    }

    override fun onConnectionSuspended(p0: Int) {
        logd("onConnectionSuspended")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        logd("onConnectionFailed")
    }

    fun logd(value: String) {
        Log.d(TAG, value)
    }
}
