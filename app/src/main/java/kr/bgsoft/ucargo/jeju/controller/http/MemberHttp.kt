package kr.bgsoft.ucargo.jeju.controller.http

import kr.bgsoft.ucargo.jeju.App
import org.json.JSONObject

class MemberHttp {

    val HTTP_LOGIN_HP           = App.host + "get_login_hp"
    val HTTP_LOGIN_USER         = App.host + "get_login_user"
    val HTTP_USERINFO           = App.host + "get_user_info"
    val HTTP_USERINFO_DS        = App.host + "get_search_member_ds"
    val HTTP_CHECK_ID           = App.host + "get_duplication_check"
    val HTTP_FINDIDPW           = App.host + "get_search_idpw"
    val HTTP_GETPOINT           = App.host + "get_user_point"
    val HTTP_SETEDIT            = App.host + "set_user_info"
    val HTTP_FCMID              = App.host + "set_fcmid"

    val HTTP_SETVACCOUNT        = App.host + "set_vaccount_create"
    val HTTP_GETPOINTLIST       = App.host + "get_mypoint_list"
    val HTTP_SETPOINTOUT        = App.host + "set_mypoint_withdraw"
    val HTTP_GETPOINTINFO       = App.host + "get_mypoint_info"
    val HTTP_GETUSERSEARCH      = App.host + "get_user_search"
    val HTTP_SETTOPOINT         = App.host + "set_mypoint_transfer"
    val HTTP_SETJOIN            = App.host + "set_user_register_sample"
    //    val HTTP_SETJOIN            = App.host + "set_user_register"
    val HTTP_GETJOIN_CHECK      = App.host + "get_member_sample" //간편가입 확인
    val HTTP_SETADDJOIN         = App.host + "set_user_register_addinfo" //회원 간편가입

    val HTTP_SETGPS             = App.host + "set_mygps_update"
    val HTTP_PASSCHECK          = App.host + "get_cwpass_check"
    val HTTP_SETPASS            = App.host + "set_password_update"
    val HTTP_GETCPOINT          = App.host + "get_event_count"
    val HTTP_SETADDBANK         = App.host + "set_bank_register"

    fun getLogin(hp: String, aid: String, http : DefaultHttp, callback: DefaultHttp.Callback) {7

        val json = JSONObject()
        json.put("sHP", hp)
        json.put("aid", aid)

        http.requestJSON(HTTP_LOGIN_HP, json, callback)

    }

    fun getLogin(id: String, pass: String, aid: String, hp: String, http : DefaultHttp, callback: DefaultHttp.Callback) {

        val json = JSONObject()
        json.put("sID", id)
        json.put("sPW", pass)
        json.put("aid", aid)
        json.put("sHP", hp)

        http.requestJSON(HTTP_LOGIN_USER, json, callback)

    }

    fun getUserInfo(cwnum : String, http : DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()
        json.put("nCwnum", cwnum)

        http.requestJSON(HTTP_USERINFO, json, callback)
    }

    fun getUserInfoDS(hp : String, carnum: String, http : DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()
        json.put("sHP", hp)
        json.put("sCarNum", carnum)

        http.requestJSON(HTTP_USERINFO_DS, json, callback)
    }

    fun getCheckID(id: String, http : DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()
        json.put("sValue", id)
        json.put("sType", "userid")

        http.requestJSON(HTTP_CHECK_ID, json, callback)
    }

    fun getUserPoint(cwnum: String, http : DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()
        json.put("nCwnum", cwnum)

        http.requestJSON(HTTP_GETPOINT, json, callback)
    }

    fun getUserCPoint(cwnum: String, http : DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()
        json.put("nCwnum", cwnum)

        http.requestJSON(HTTP_GETCPOINT, json, callback)
    }


    fun setVAccount(cwnum: String, http : DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()
        json.put("nCwnum", cwnum)

        http.requestJSON(HTTP_SETVACCOUNT, json, callback)
    }

    fun getPointList(cwnum: String, start: String, end: String, type: String, num: Int, idx: String, http : DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()
        json.put("nCwnum", cwnum)
        json.put("sDateStart", start)
        json.put("sDateEnd", end)
        json.put("nMode", type)
        json.put("nNum", num.toString())
        json.put("nIdx", idx)

        http.requestJSON(HTTP_GETPOINTLIST, json, callback)
    }

    fun setPointOut(cwnum: String, point: Int, password: String, http : DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()
        json.put("nCwnum", cwnum)
        json.put("nMoney", point.toString())
        json.put("sUserPw", password)

        http.requestJSON(HTTP_SETPOINTOUT, json, false, callback)
    }

    fun getPointInfo(cwnum: String, http : DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()
        json.put("nCwnum", cwnum)

        http.requestJSON(HTTP_GETPOINTINFO, json, callback)
    }

    fun getUserSearch(cwnum: String, value: String, http : DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()
        json.put("nCwnum", cwnum)
        json.put("sSearchValue", value)

        http.requestJSON(HTTP_GETUSERSEARCH, json, callback)
    }

    fun setToPoint(cwnum: String, tocwnum: String, point: Int, password: String, usertype: String, http : DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()
        json.put("nCwnum", cwnum)
        json.put("nCwnumTo", tocwnum)
        json.put("nMoneyTo", point.toString())
        json.put("sUserPw", password)
        json.put("sUserTypeTo", usertype)

        http.requestJSON(HTTP_SETTOPOINT, json, callback)
    }

    fun setJoin(jibu: String, name: String, id: String, hp: String, password: String, carnum: String, cartype: String, carsize: String, address1: String, address2: String,
                biztype: String, biznum: String, caraddress1: String, caraddress2: String, corpname: String, ceoname: String, uptae: String, upjong: String,
                cmsname: String, cmsbank: String, cmsbankcode: String, cmsnum: String, fcmid: String, memberidx: String, http : DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()
        json.put("sJoinType", "용달|경기")
        json.put("sJibu", jibu)
        json.put("sName", name)
        json.put("sUserid", id)
        json.put("sHP", hp)
        json.put("sUserpw", password)
        json.put("sCarNum", carnum)
        json.put("sCarType", cartype)
        json.put("sCarTon", carsize)
        json.put("sAddr1", address1)
        json.put("sAddr2", address2)
        json.put("sBzType", biztype)
        json.put("nBzNum", biznum)
        json.put("sBzComName", corpname)
        json.put("sBzOwnerName", ceoname)
        json.put("sCarAddr1", caraddress1)
        json.put("sCarAddr2", caraddress2)
        json.put("sUptae", uptae)
        json.put("sUpjong", upjong)
        json.put("sCmsName", cmsname)
        json.put("sCmsBank", cmsbank)
        json.put("sCmsBankCode", cmsbankcode)
        json.put("nCmsAccount", cmsnum)
        json.put("FCMID", fcmid)
        json.put("memberidx", memberidx)

        http.requestJSON(HTTP_SETJOIN, json, false, callback)
    }

    fun setEdit(cwnum: String, hp: String, carnum: String, carsize: String, cartype: String, address1: String, address2: String, biznum: String, corpname: String, ceoname: String, caraddress1: String, caraddress2: String,
                uptae: String, upjong: String, http: DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()

        json.put("nCwnum", cwnum)
        json.put("sHP", hp)
        json.put("sCarNum", carnum)
        json.put("sCarType", cartype)
        json.put("sCarTon", carsize)
        json.put("sAddr1", address1)
        json.put("sAddr2", address2)
        json.put("nBzNum", biznum)
        json.put("sBzComName", corpname)
        json.put("sBzOwnerName", ceoname)
        json.put("sCarAddr1", caraddress1)
        json.put("sCarAddr2", caraddress2)
        json.put("sUptae", uptae)
        json.put("sUpjong", upjong)

        http.requestJSON(HTTP_SETEDIT, json, false, callback)
    }

    fun setPass(cwnum: String, hp: String, pass: String, newpass: String, http: DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()

        json.put("nCwnum", cwnum)
        json.put("sHP", hp)
        json.put("sPW", pass)
        json.put("sPWNEW", newpass)

        http.requestJSON(HTTP_SETPASS, json, false, callback)
    }

    fun getFindID(hp: String, http : DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()
        json.put("sHP", hp)
        json.put("sType", "userid")
        json.put("sValue", "")

        http.requestJSON(HTTP_FINDIDPW, json, callback)
    }

    fun getFindPass(hp: String, id: String, http : DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()
        json.put("sHP", hp)
        json.put("sType", "userpw")
        json.put("sValue", id)

        http.requestJSON(HTTP_FINDIDPW, json, callback)
    }

    fun setMyGPS(cwnum: String, lat: Double, lng: Double, address: String, http : DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()
        json.put("nCwnum", cwnum)
        json.put("nLat", lat.toString())
        json.put("nLng", lng.toString())
        json.put("sAddr", address)

        http.requestJSON(HTTP_SETGPS, json, false, callback)
    }

    fun getPasscheck(cwnum: String, password: String, http : DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()
        json.put("nCwnum", cwnum)
        json.put("sPW", password)

        http.requestJSON(HTTP_PASSCHECK, json, callback)
    }

    fun setFCM(cwnum: String, fcmid: String, http : DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()
        json.put("nCwnum", cwnum)
        json.put("sFCMID", fcmid)

        http.requestJSON(HTTP_FCMID, json, false, callback)
    }

    fun getJoinCheck(hp: String, http : DefaultHttp, callback: DefaultHttp.Callback) {

        val json = JSONObject()
        json.put("sHP", hp)

        http.requestJSON(HTTP_GETJOIN_CHECK, json, callback)

    }

    fun setAddJoin(cwnum: String, biztype: String, biznum: String, caraddress1: String, caraddress2: String, corpname: String, ceoname: String, uptae: String, upjong: String,
                   cmsname: String, cmsbank: String, cmsbankcode: String, cmsnum: String, http : DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()
        json.put("nCwnum", cwnum)
        json.put("sBzType", biztype)
        json.put("nBzNum", biznum)
        json.put("sBzComName", corpname)
        json.put("sBzOwnerName", ceoname)
        json.put("sAddr1", caraddress1)
        json.put("sAddr2", caraddress2)
        json.put("sUptae", uptae)
        json.put("sUpjong", upjong)
        json.put("sCmsName", cmsname)
        json.put("sCmsBank", cmsbank)
        json.put("sCmsBankCode", cmsbankcode)
        json.put("nCmsAccount", cmsnum)

        http.requestJSON(HTTP_SETADDJOIN, json, false, callback)
    }

    fun setAddBank(cwnum: String, cmsname: String, cmsbank: String, cmsbankcode: String, cmsnum: String, http : DefaultHttp, callback: DefaultHttp.Callback) {
        val json = JSONObject()
        json.put("nCwnum", cwnum)
        json.put("sCmsName", cmsname)
        json.put("sCmsBank", cmsbank)
        json.put("sCmsBankCode", cmsbankcode)
        json.put("nCmsAccount", cmsnum)

        http.requestJSON(HTTP_SETADDBANK, json, false, callback)
    }


}