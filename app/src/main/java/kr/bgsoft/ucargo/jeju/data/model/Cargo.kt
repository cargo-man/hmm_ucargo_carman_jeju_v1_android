package kr.bgsoft.ucargo.jeju.data.model

import java.io.Serializable

data class Cargo(
        var sCarType: String = ""
        , var sPayment: String = ""
        , var nPay: Int = 0
        , var nGoodbad: Int = 0
        , var nFee: Int = 0
        , var sGoodsWeight: String = ""
        , var nDistance: String = ""
        , var sDownLoc: String = ""
        , var sLoadDay: String = ""
        , var sETC: String = ""
        , var nOrderNum: String = ""
        , var sLoadLoc: String = ""
        , var sDownMethod: String = ""
        , var sGoodsInfo: String = ""
        , var sLoadtype: String = ""
        , var sLoadMethod: String = ""
        , var sDownDay: String = ""
        , var sCarton: String = ""
        , var sGoodState: String = ""
        , var sRegDate: String = ""
        , var sEditDate: String = ""
        , var sOwnerNum: String = ""
        , var sLoadCode: String = ""
        , var sDownCode: String = ""
        , var sLoadAdd: String = ""
        , var sDownAdd: String = ""
        , var sDownCodeName: String = ""
        , var bCheck: Boolean = false
        , var sLoadLat: Double = 0.0
        , var sLoadLng: Double = 0.0
        , var sDownLat: Double = 0.0
        , var sDownLng: Double = 0.0
        , var nSortNum: Int = 0
        , var dDistance: Int = 0
): Serializable
