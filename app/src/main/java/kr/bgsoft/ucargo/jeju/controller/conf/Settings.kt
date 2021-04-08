package kr.bgsoft.ucargo.jeju.controller.conf

import android.content.Context
import android.util.Log
import kr.bgsoft.ucargo.jeju.cview.App
import kr.bgsoft.ucargo.jeju.cview.DefaultActivity
import kr.bgsoft.ucargo.jeju.data.model.*
import kr.bgsoft.ucargo.jeju.data.sqlite.Infos
import kr.bgsoft.ucargo.jeju.utils.Etc
import org.json.JSONArray
import org.json.JSONObject

object Settings {
    var arrPermission: ArrayList<DefaultActivity.PEMTYPE> = ArrayList<DefaultActivity.PEMTYPE>()

    fun setAppSetting(context: Context, setting: AppSetting) {
        val jsonData = JSONObject()
        jsonData.put("isAlam", setting.isAlam)
        jsonData.put("isAlam2", setting.isAlam2)
        jsonData.put("isAutoLogin", setting.isAutoLogin)
        jsonData.put("isProgressShow", setting.isProgressShow)
        jsonData.put("isPush", setting.isPush)
        jsonData.put("isRefresh", setting.isRefresh)
        jsonData.put("isSensor", setting.isSensor)
        jsonData.put("isTextSet", setting.isTextSet)
        jsonData.put("refreshTime", setting.refreshTime)
        jsonData.put("textSize", setting.textSize)
        jsonData.put("themeType", setting.themeType)
        jsonData.put("cardDevice", setting.cardDevice)
        jsonData.put("isTimeView", setting.isTimeView)
        jsonData.put("isSound", setting.isSound)
        jsonData.put("useAlarm", setting.useAlarm)

        Etc.svSend(context, Config.VAL_SSCONFIG, jsonData.toString())
        val Info = Infos(context, Config.DB_Infos, null, Config.DB_Infos_ver)
        Info.insert(Config.VAL_SSCONFIG, jsonData.toString())
    }
    fun getAppSetting(context: Context) : AppSetting {
        var data    = Etc.svGet(context, Config.VAL_SSCONFIG)
        val setting = AppSetting()

        App.Log.d("settings", "data :: $data")

        if(data.isNullOrBlank()) {
            val Info = Infos(context, Config.DB_Infos, null, Config.DB_Infos_ver)
            data = Info.select(Config.VAL_SSCONFIG)
        }

        if(!data.isNullOrBlank()) {
            try {
                val jsondata = JSONObject(data)
                if (!jsondata.isNull("isAlam")) {           setting.isAlam = jsondata.getBoolean("isAlam") }
                if (!jsondata.isNull("isAlam2")) {          setting.isAlam2 = jsondata.getBoolean("isAlam2") }
                if (!jsondata.isNull("isAutoLogin")) {      setting.isAutoLogin = jsondata.getBoolean("isAutoLogin") }
                if (!jsondata.isNull("isProgressShow")) {   setting.isProgressShow = jsondata.getBoolean("isProgressShow")}
                if (!jsondata.isNull("isPush")) {           setting.isPush = jsondata.getBoolean("isPush") }
                if (!jsondata.isNull("isRefresh")) {        setting.isRefresh = jsondata.getBoolean("isRefresh") }
                if (!jsondata.isNull("isSensor")) {         setting.isSensor = jsondata.getBoolean("isSensor") }
                if (!jsondata.isNull("isTextSet")) {        setting.isTextSet = jsondata.getBoolean("isTextSet") }
                if (!jsondata.isNull("refreshTime")) {      setting.refreshTime = jsondata.getInt("refreshTime") }
                if (!jsondata.isNull("textSize")) {         setting.textSize = jsondata.getInt("textSize") }
                if (!jsondata.isNull("themeType")) {        setting.themeType = jsondata.getInt("themeType") }
                if (!jsondata.isNull("cardDevice")) {       setting.cardDevice = jsondata.getString("cardDevice") }
                if (!jsondata.isNull("isTimeView")) {       setting.isTimeView = jsondata.getBoolean("isTimeView") }
                if (!jsondata.isNull("isSound")) {          setting.isSound = jsondata.getBoolean("isSound") }
                if (!jsondata.isNull("useAlarm")) {         setting.useAlarm = jsondata.getInt("useAlarm") }

            } catch (e: Exception) {
                Log.d("nbs천재", "errror :: " + e.message)
            }
        }

        return setting
    }
    fun setID(context: Context, value: String) {
        Etc.svSend(context, Config.VAL_SSID, value)
        val Info = Infos(context, Config.DB_Infos, null, Config.DB_Infos_ver)
        Info.insert(Config.VAL_SSID, value)
    }

    fun getID(context: Context) : String {
        var data    = Etc.svGet(context, Config.VAL_SSID)
        if(data.isNullOrBlank()) {
            val Info = Infos(context, Config.DB_Infos, null, Config.DB_Infos_ver)
            data = Info.select(Config.VAL_SSID)
        }

        return data.toString()
    }
    fun setQRSend(context: Context, psend: PriceSend) {
        val jsonData = JSONObject()

        jsonData.put("type", psend.type)
        jsonData.put("incwnum", psend.inCwnum)
        jsonData.put("inid", psend.inId)
        jsonData.put("inname", psend.inName)
        jsonData.put("outcwnum", psend.outCwnum)
        jsonData.put("outid", psend.outId)
        jsonData.put("outname", psend.outName)
        jsonData.put("pass", psend.pass)
        jsonData.put("price", psend.price)
        jsonData.put("maketime", psend.maketime)
        jsonData.put("status", psend.status)

        Etc.svSend(context, Config.VAL_QRSEND, jsonData.toString())
        val Info = Infos(context, Config.DB_Infos, null, Config.DB_Infos_ver)
        Info.insert(Config.VAL_QRSEND, jsonData.toString())
    }

    fun getQRSend(context: Context) : PriceSend {
        var data    = Etc.svGet(context, Config.VAL_QRSEND)
        if(data.isNullOrBlank()) {
            val Info = Infos(context, Config.DB_Infos, null, Config.DB_Infos_ver)
            data = Info.select(Config.VAL_QRSEND)
        }

        val psend = PriceSend()

        if(!data.isNullOrBlank()) {
            try {
                val jsondata = JSONObject(data)
                if (!jsondata.isNull("type")) {             psend.type = jsondata.getString("type") }
                if (!jsondata.isNull("incwnum")) {          psend.inCwnum = jsondata.getString("incwnum") }
                if (!jsondata.isNull("inid")) {             psend.inId = jsondata.getString("inid")}
                if (!jsondata.isNull("inname")) {           psend.inName = jsondata.getString("inname") }
                if (!jsondata.isNull("outcwnum")) {         psend.outCwnum = jsondata.getString("outcwnum") }
                if (!jsondata.isNull("outid")) {            psend.outId = jsondata.getString("outid")}
                if (!jsondata.isNull("outname")) {          psend.outName = jsondata.getString("outname") }
                if (!jsondata.isNull("pass")) {             psend.pass = jsondata.getString("pass") }
                if (!jsondata.isNull("price")) {            psend.price = jsondata.getInt("price") }
                if (!jsondata.isNull("maketime")) {         psend.maketime = jsondata.getInt("maketime") }
                if (!jsondata.isNull("status")) {           psend.status = jsondata.getString("status") }
            } catch (e: Exception) {
                Log.d("nbs천재", "errror :: " + e.message)
            }
        }

        return psend
    }

    fun setFCMID(context: Context, value: String) {
        Etc.svSend(context, Config.VAL_FCMID, value)
        val Info = Infos(context, Config.DB_Infos, null, Config.DB_Infos_ver)
        Info.insert(Config.VAL_FCMID, value)
    }

    fun getFCMID(context: Context) : String {
        var data    = Etc.svGet(context, Config.VAL_FCMID)
        if(data.isNullOrBlank()) {
            val Info = Infos(context, Config.DB_Infos, null, Config.DB_Infos_ver)
            data = Info.select(Config.VAL_FCMID)
        }

        return data.toString()
    }

    fun setNiceID(context: Context, value: JSONObject) {
        Etc.svSend(context, Config.VAL_NICEID, value.toString())
        val Info = Infos(context, Config.DB_Infos, null, Config.DB_Infos_ver)
        Info.insert(Config.VAL_NICEID, value.toString())
    }

    fun getNiceID(context: Context) : JSONObject {
        var data    = Etc.svGet(context, Config.VAL_NICEID)
        var json = JSONObject()

        if(data.isNullOrBlank()) {
            val Info = Infos(context, Config.DB_Infos, null, Config.DB_Infos_ver)
            data = Info.select(Config.VAL_NICEID)
        }

        try {
            json = JSONObject(data)
        } catch (e : Exception) {

        }

        return json
    }


    fun setVersion(context: Context, value: String) {
        Etc.svSend(context, Config.VAL_SSVER, value)
        val Info = Infos(context, Config.DB_Infos, null, Config.DB_Infos_ver)
        Info.insert(Config.VAL_SSVER, value)
    }

    fun getVersion(context: Context) : JSONObject {
        var data    = Etc.svGet(context, Config.VAL_SSVER)
        if(data.isNullOrBlank()) {
            val Info = Infos(context, Config.DB_Infos, null, Config.DB_Infos_ver)
            data = Info.select(Config.VAL_SSVER)
        }

        var json = JSONObject()

        try {
            json = JSONObject(data.toString())
        } catch (e : Exception) {

        }

        return json
    }

    fun setInfo(context: Context, info: UserInfo) {
        val jsonData = JSONObject()
        jsonData.put("catid", info.catid)
        jsonData.put("vAccount", info.vAccount)
        jsonData.put("bzNum", info.bzNum)
        jsonData.put("mShipJoin", info.mShipJoin)
        jsonData.put("lvType", info.lvType)
        jsonData.put("name", info.name)
        jsonData.put("lv00", info.lv00)
        jsonData.put("lv01", info.lv01)
        jsonData.put("id", info.id)
        jsonData.put("lvArea", info.lvArea)
        jsonData.put("point", info.point)
        jsonData.put("lv03", info.lv03)
        jsonData.put("sComZip", info.sComZip)
        jsonData.put("sCartonName", info.sCartonName)
        jsonData.put("sCartonCode", info.sCartonCode)
        jsonData.put("sCeoName", info.sCeoName)
        jsonData.put("sCarNum", info.sCarNum)
        jsonData.put("sUptap", info.sUptap)
        jsonData.put("sUpjang", info.sUpjang)
        jsonData.put("sComName", info.sComName)
        jsonData.put("sComAddr", info.sComAddr)
        jsonData.put("sEmail", info.sEmail)
        jsonData.put("sHP", info.sHP)
        jsonData.put("sGPSAddress", info.sGPSAddress)
        jsonData.put("sGPSTime", info.sGPSTime)
        jsonData.put("sTRSNum", info.sTRSNum)
        jsonData.put("sCartypeName", info.sCartypeName)
        jsonData.put("sCartypeCode", info.sCartypeCode)
        jsonData.put("sBankUserName", info.sBankUserName)
        jsonData.put("sBankName", info.sBankName)
        jsonData.put("sCmsBankCode", info.sCmsBankCode)
        jsonData.put("sCarName", info.sCarName)
        jsonData.put("sBzType", info.sBzType)


        Etc.svSend(context, Config.VAL_SSINFO, jsonData.toString())
        val Info = Infos(context, Config.DB_Infos, null, Config.DB_Infos_ver)
        Info.insert(Config.VAL_SSINFO, jsonData.toString())
    }

    fun getInfo(context: Context) : UserInfo {
        var data    = Etc.svGet(context, Config.VAL_SSINFO)
        val info    = UserInfo()

        if(data.isNullOrBlank()) {
            val Info = Infos(context, Config.DB_Infos, null, Config.DB_Infos_ver)
            data = Info.select(Config.VAL_SSINFO)
        }

        if(!data.isNullOrBlank()) {
            try {
                val jsondata = JSONObject(data)
                if (!jsondata.isNull("catid")) {            info.catid = jsondata.getString("catid") }
                if (!jsondata.isNull("vAccount")) {         info.vAccount = jsondata.getString("vAccount") }
                if (!jsondata.isNull("bzNum")) {            info.bzNum = jsondata.getString("bzNum") }
                if (!jsondata.isNull("mShipJoin")) {        info.mShipJoin = jsondata.getString("mShipJoin") }
                if (!jsondata.isNull("lvType")) {           info.lvType = jsondata.getString("lvType") }
                if (!jsondata.isNull("name")) {             info.name = jsondata.getString("name") }
                if (!jsondata.isNull("lv00")) {             info.lv00 = jsondata.getString("lv00") }
                if (!jsondata.isNull("lv01")) {             info.lv01 = jsondata.getString("lv01") }
                if (!jsondata.isNull("id")) {               info.id = jsondata.getString("id") }
                if (!jsondata.isNull("lvArea")) {           info.lvArea = jsondata.getString("lvArea") }
                if (!jsondata.isNull("point")) {            info.point = jsondata.getString("point") }
                if (!jsondata.isNull("lv03")) {             info.lv03 = jsondata.getString("lv03") }
                if (!jsondata.isNull("sComZip")) {          info.sComZip = jsondata.getString("sComZip") }
                if (!jsondata.isNull("sCartonName")) {      info.sCartonName = jsondata.getString("sCartonName") }
                if (!jsondata.isNull("sCartonCode")) {      info.sCartonCode = jsondata.getString("sCartonCode") }
                if (!jsondata.isNull("sCeoName")) {         info.sCeoName = jsondata.getString("sCeoName") }
                if (!jsondata.isNull("sCarNum")) {          info.sCarNum = jsondata.getString("sCarNum") }
                if (!jsondata.isNull("sUptap")) {           info.sUptap = jsondata.getString("sUptap") }
                if (!jsondata.isNull("sUpjang")) {          info.sUpjang = jsondata.getString("sUpjang") }
                if (!jsondata.isNull("sComName")) {         info.sComName = jsondata.getString("sComName") }
                if (!jsondata.isNull("sComAddr")) {         info.sComAddr = jsondata.getString("sComAddr") }
                if (!jsondata.isNull("sEmail")) {           info.sEmail = jsondata.getString("sEmail") }
                if (!jsondata.isNull("sHP")) {              info.sHP = jsondata.getString("sHP") }
                if (!jsondata.isNull("sGPSAddress")) {      info.sGPSAddress = jsondata.getString("sGPSAddress") }
                if (!jsondata.isNull("sGPSTime")) {         info.sGPSTime = jsondata.getString("sGPSTime") }
                if (!jsondata.isNull("sTRSNum")) {          info.sTRSNum = jsondata.getString("sTRSNum") }
                if (!jsondata.isNull("sCartypeName")) {     info.sCartypeName = jsondata.getString("sCartypeName") }
                if (!jsondata.isNull("sCartypeCode")) {     info.sCartypeCode = jsondata.getString("sCartypeCode") }
                if (!jsondata.isNull("sBankUserName")) {    info.sBankUserName = jsondata.getString("sBankUserName") }
                if (!jsondata.isNull("sBankName")) {        info.sBankName = jsondata.getString("sBankName") }
                if (!jsondata.isNull("sCarName")) {         info.sCarName = jsondata.getString("sCarName") }
                if (!jsondata.isNull("sBzType")) {          info.sBzType = jsondata.getString("sBzType") }
                if (!jsondata.isNull("sCmsBankCode")) {     info.sCmsBankCode = jsondata.getString("sCmsBankCode") }

            } catch (e: Exception) {
                Log.d("nbs천재", "errror :: " + e.message)
            }
        }

        return info
    }

    fun setSearch(context: Context, search: CargoSearch) {
        val jsonData = JSONObject()
        jsonData.put("condition1", search.condition1)
        jsonData.put("condition2", search.condition2)
        jsonData.put("condition3", search.condition3)
        jsonData.put("loadarea1", search.loadarea1)
        jsonData.put("loadarea2", search.loadarea2)
        jsonData.put("loadarea3", search.loadarea3)
        jsonData.put("loadareatext", search.loadareatext)
        jsonData.put("downarea1", search.downarea1)
        jsonData.put("downarea2", search.downarea2)
        jsonData.put("downarea3", search.downarea3)
        jsonData.put("downareatext", search.downareatext)
        jsonData.put("condition1print", search.condition1print)
        jsonData.put("condition2print", search.condition2print)
        jsonData.put("condition3print", search.condition3print)
        jsonData.put("loadareaprint", search.loadareaprint)
        jsonData.put("downareaprint", search.downareaprint)
        jsonData.put("sortup", search.sortup)
        jsonData.put("sortupprint", search.sortupprint)
        jsonData.put("ttstime", search.ttstime.toString())
        jsonData.put("loadtime", search.loadtime.toString())
        jsonData.put("downtime", search.downtime.toString())

        Etc.svSend(context, Config.VAL_SEARCH, jsonData.toString())
        val Info = Infos(context, Config.DB_Infos, null, Config.DB_Infos_ver)
        Info.insert(Config.VAL_SEARCH, jsonData.toString())
    }

    fun getSearch(context: Context) : CargoSearch {
        var data    = Etc.svGet(context, Config.VAL_SEARCH)
        val search  = CargoSearch()

        if(data.isNullOrBlank()) {
            val Info = Infos(context, Config.DB_Infos, null, Config.DB_Infos_ver)
            data = Info.select(Config.VAL_SEARCH)
        }

        if(!data.isNullOrBlank()) {
            try {
                val jsondata = JSONObject(data)
                if (!jsondata.isNull("condition1")) {       search.condition1 = jsondata.getString("condition1") }
                if (!jsondata.isNull("condition2")) {       search.condition2 = jsondata.getString("condition2") }
                if (!jsondata.isNull("condition3")) {       search.condition3 = jsondata.getString("condition3") }
                if (!jsondata.isNull("loadarea1")) {        search.loadarea1 = jsondata.getString("loadarea1") }
                if (!jsondata.isNull("loadarea2")) {        search.loadarea2 = jsondata.getString("loadarea2") }
                if (!jsondata.isNull("loadarea3")) {        search.loadarea3 = jsondata.getString("loadarea3") }
                if (!jsondata.isNull("loadareatext")) {     search.loadareatext = jsondata.getString("loadareatext") }
                if (!jsondata.isNull("downarea1")) {        search.downarea1 = jsondata.getString("downarea1") }
                if (!jsondata.isNull("downarea2")) {        search.downarea2 = jsondata.getString("downarea2") }
                if (!jsondata.isNull("downarea3")) {        search.downarea3 = jsondata.getString("downarea3") }
                if (!jsondata.isNull("downareatext")) {     search.downareatext = jsondata.getString("downareatext") }
                if (!jsondata.isNull("condition1print")) {  search.condition1print = jsondata.getString("condition1print") }
                if (!jsondata.isNull("condition2print")) {  search.condition2print = jsondata.getString("condition2print") }
                if (!jsondata.isNull("condition3print")) {  search.condition3print = jsondata.getString("condition3print") }
                if (!jsondata.isNull("loadareaprint")) {    search.loadareaprint = jsondata.getString("loadareaprint") }
                if (!jsondata.isNull("downareaprint")) {    search.downareaprint = jsondata.getString("downareaprint") }
                if (!jsondata.isNull("sortup")) {           search.sortup = jsondata.getString("sortup") }
                if (!jsondata.isNull("sortupprint")) {      search.sortupprint = jsondata.getString("sortupprint") }
                if (!jsondata.isNull("ttstime")) {          search.ttstime = jsondata.getString("ttstime").toInt() }
                if (!jsondata.isNull("loadtime")) {          search.loadtime = jsondata.getString("loadtime").toInt() }
                if (!jsondata.isNull("downtime")) {          search.downtime = jsondata.getString("downtime").toInt() }

            } catch (e: Exception) {
                Log.d("nbs천재", "errror :: " + e.message)
            }
        }

        return search
    }


    fun getCode(context: Context, type: String) : JSONArray {
        val json = getVersion(context).getJSONObject("setcode")
        return json.getJSONArray(type)
    }

    fun getAccount(context: Context) : JSONObject {
        val json = getVersion(context).getJSONObject("setcode")
        return json.getJSONObject("vAccountConfig")
    }

    fun getCodeList(context: Context, type: String) : ArrayList<Code> {
        val json    = getCode(context, type)
        val array   = ArrayList<Code>()

        for(i in 0..(json.length() - 1)) {
            val data = json.getJSONObject(i)
            array.add(Code(data.getString("key"), data.getString("value")))
        }

        return array
    }

    fun getCodeValue(context: Context, type: String, key: String) : String {
        var value = ""
        var array = getCodeList(context, type)
        for(i in 0..(array.size - 1)) {
            val data = array.get(i)
            if(data.code == key) {
                value = data.value
                break
            }
        }
        return value
    }

    fun getCodeKey(context: Context, type: String, value: String) : String {
        var code = ""
        var array = getCodeList(context, type)
        for(i in 0..(array.size - 1)) {
            val data = array.get(i)
            if(data.value == value) {
                code = data.code
                break
            }
        }
        return code
    }
}