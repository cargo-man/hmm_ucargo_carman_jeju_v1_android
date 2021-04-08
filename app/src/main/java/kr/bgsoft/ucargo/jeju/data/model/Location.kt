package kr.bgsoft.ucargo.jeju.data.model

import java.io.Serializable

data class Location(
        var lat: Double = 0.0
        , var lng: Double = 0.0
        , var title: String = ""
        , var address: String = ""
        , var country: String = ""
        , var city: String = ""
        , var addr1: String = ""
        , var addr2: String = ""
) : Serializable