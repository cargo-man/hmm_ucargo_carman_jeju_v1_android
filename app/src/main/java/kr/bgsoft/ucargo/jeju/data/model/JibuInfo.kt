package kr.bgsoft.ucargo.jeju.data.model

import java.io.Serializable

data class JibuInfo(
        var lat: Double = 0.0
        , var lng: Double = 0.0
        , var name: String = ""
        , var address: String = ""
        , var area: String = ""
        , var tel1: String = ""
        , var tel2: String = ""
        , var fax: String = ""
) : Serializable