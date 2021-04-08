package kr.bgsoft.ucargo.jeju.data.model

import kr.bgsoft.ucargo.jeju.utils.Etc
import java.util.*

data class CargoAlert (
        var isSet: Boolean      = true
        ,var loadArea: String   = ""
        ,var loadCode: String   = ""
        ,var downArea: String   = ""
        ,var downCode: String   = ""
        ,var carType: String    = ""
        ,var carSize: String    = ""
        ,var mixUp: String      = ""
        ,var cutPay: Int        = 1
        ,var cutDay: String     = ""
        ,var startTime: String  = ""
        ,var endTime: String    = ""
        ,var setWeek: String    = ""
        ,var idx : String       = ""
        ,var downType: String   = ""
        ,var loadDate: String   = Etc.convertDateToString(Date(), "yyyy-MM-dd HH")
        ,var isAllSet: Boolean  = true
)