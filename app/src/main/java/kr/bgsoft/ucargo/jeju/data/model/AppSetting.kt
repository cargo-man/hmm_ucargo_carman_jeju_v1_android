package kr.bgsoft.ucargo.jeju.data.model

data class AppSetting(
        var isAutoLogin: Boolean = true
        , var isAlam: Boolean = true
        , var isPush: Boolean = true
        , var isRefresh: Boolean = false
        , var refreshTime: Int = 5
        , var isSensor: Boolean = false
        , var themeType: Int = 1
        , var isTextSet: Boolean = false
        , var textSize: Int = 1
        , var isProgressShow: Boolean = true
        , var cardDevice: String = ""
        , var isAlam2: Boolean = true
        , var isTimeView: Boolean = true
        , var isSound: Boolean = true
        , var useAlarm: Int = 1
)