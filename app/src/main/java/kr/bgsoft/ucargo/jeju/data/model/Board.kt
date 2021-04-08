package kr.bgsoft.ucargo.jeju.data.model

import java.io.Serializable

data class Board (
        var idx: Int            = 0
        , var title: String     = ""
        , var contents: String  = ""
        , var date: String      = ""
        , var read: Boolean     = false
        , var type: Int         = 0
        , var ttype: String     = ""
        , var check: Boolean   = false
): Serializable