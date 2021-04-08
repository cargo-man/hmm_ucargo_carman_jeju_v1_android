package kr.bgsoft.ucargo.jeju.data.model

import java.io.Serializable

data class MyCargo(
        var sFeeSendMsg: String = ""
        , var sCarType: String = ""
        , var sPayment: String = ""
        , var nPay: Int = 0
        , var nGoodbad: Int = 0
        , var nFee: Int = 0
        , var sGoodsWeight: String = ""
        , var nDistance: String = ""
        , var sFeeSend: String = ""
        , var sOwnerName: String = ""
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
        , var sOwnerNum: String = ""
        , var sCarton: String = ""
        , var sGoodState: String = ""
        , var sRegdate: String = ""
        , var bListSend: Boolean = false
        , var sEditdate: String = ""
        , var sRealPhone: String = ""
        , var bUpDown: Boolean = false
        , var sLoadLat: Double = 0.0
        , var sLoadLng: Double = 0.0
        , var sDownLat: Double = 0.0
        , var sDownLng: Double = 0.0
        , var bGameng: Boolean = false
) : Serializable