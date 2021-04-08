package kr.bgsoft.ucargo.jeju.data.model
import java.io.Serializable

data class CargoSearch (
        var condition1: String = ""
        ,var condition2: String = ""
        ,var condition3: String = ""
        ,var loadarea1: String = ""
        ,var loadarea2: String = ""
        ,var loadarea3: String = ""
        ,var loadareatext: String = ""
        ,var downarea1: String = ""
        ,var downarea2: String = ""
        ,var downarea3: String = ""
        ,var downareatext: String = ""
        ,var condition1print: String = ""
        ,var condition2print: String = ""
        ,var condition3print: String = ""
        ,var loadareaprint: String = ""
        ,var downareaprint: String = ""
        ,var sortup: String = ""
        ,var sortupprint: String = ""
        ,var ttstime: Int = 10
        ,var loadtime: Int = -1
        ,var downtime: Int = -1
) : Serializable