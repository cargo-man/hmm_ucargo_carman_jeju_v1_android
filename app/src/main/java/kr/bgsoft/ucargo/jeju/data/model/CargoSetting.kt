package kr.bgsoft.ucargo.jeju.data.model

data class CargoSetting(
        var Name: String
        , var Code: String
        , var isSelect: Boolean
        , var isCenter: Boolean = true
)