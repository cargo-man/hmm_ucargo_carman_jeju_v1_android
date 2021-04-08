package kr.bgsoft.ucargo.jeju.data.model

data class Notice(
        var intSeq : Int = 0
        , var intType : Int = 0
        , var txtMid : String = ""
        , var txtTitle : String = ""
        , var txtInfo : String = ""
        , var txtData : String = ""
        , var txtReg : Int = 0
        , var intUse : Boolean = false
)
