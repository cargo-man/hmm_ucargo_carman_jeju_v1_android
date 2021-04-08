package kr.bgsoft.ucargo.jeju.data.model

data class PriceSend(
    var type: String        = ""
    , var inCwnum: String   = ""
    , var inId: String      = ""
    , var inName: String    = ""
    , var outCwnum: String  = ""
    , var outId: String     = ""
    , var outName: String   = ""
    , var price: Int        = 0
    , var pass: String      = ""
    , var maketime: Int     = 0
    , var status: String    = ""
    , var stype: String     = ""
    , var sfcmid: String    = ""
)