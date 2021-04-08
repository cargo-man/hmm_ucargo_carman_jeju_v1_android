package kr.bgsoft.ucargo.jeju.data.model

data class BankInfo(
        var idx: Int
        , var sNotes: String
        , var nStats: Int
        , var sRegdate: String
        , var nInPoint: Int = 0
        , var nOutPoint: Int = 0
        , var nNowPoint: Int = 0
        , var sLoadLoc: String = ""
        , var sDownLoc: String = ""
)