package kr.bgsoft.ucargo.jeju.data.model

import java.io.File

data class HttpInput(
        var key: String         = ""
        , var value: String     = ""
        , var file: File?       = null
)