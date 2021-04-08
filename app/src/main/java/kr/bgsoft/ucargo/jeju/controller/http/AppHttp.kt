package kr.bgsoft.ucargo.jeju.controller.http

import kr.bgsoft.ucargo.jeju.App
import kr.bgsoft.ucargo.jeju.controller.conf.Config
import org.json.JSONObject

class AppHttp {

    val HTTP_VERSOIN    = App.host + "get_app_ver"
    val HTTP_CODE       = App.host + "get_midcode_config"
    val HTTP_NOTICE     = App.host + "get_notice_list"
    val HTTP_JIBU       = App.host + "get_notice_jibu"
    val HTTP_HH         = App.host + "get_notice_ds"
    val HTTP_NEWS       = App.host + "get_news_list"
    val HTTP_FAQ        = App.host + "get_faq_list"
    val HTTP_NOTICEVIEW = App.host + "get_notice_detail"
    val HTTP_JIBUVIEW   = App.host + "get_notice_jibu_detail"
    val HTTP_HHVIEW     = App.host + "get_notice_ds_detail"
    val HTTP_NEWSVIEW   = App.host + "get_news_detail"
    val HTTP_FAQVIEW    = App.host + "get_faq_detail"

    fun getVersion(http : DefaultHttp, callback: DefaultHttp.Callback) {

        val json = JSONObject()
        json.put("code", "yongdal_car")

        http.requestJSON(HTTP_VERSOIN, json, callback)

    }

    fun getCode(http : DefaultHttp, callback: DefaultHttp.Callback) {

        val json = JSONObject()
        json.put("code", "yongdal_car")

        http.requestJSON(HTTP_CODE, json, callback)

    }

    fun getNoticeBoard(idx: Int, count: Int, http : DefaultHttp, callback: DefaultHttp.Callback) {

        val jIdx = if(idx > 0) { idx.toString() } else { "" }
        val jCount = count.toString()

        val json = JSONObject()
        json.put("nType", "1")
        json.put("nIdx", jIdx)
        json.put("nNum", jCount)

        http.requestJSON(HTTP_NOTICE, json, callback)
    }

    fun getJibuBoard(cwnum: String, idx: Int, count: Int, http : DefaultHttp, callback: DefaultHttp.Callback) {

        val jIdx = if(idx > 0) { idx.toString() } else { "" }
        val jCount = count.toString()

        val json = JSONObject()
        json.put("nCwnum", cwnum)
        json.put("nIdx", jIdx)
        json.put("nNum", jCount)

        http.requestJSON(HTTP_JIBU, json, callback)
    }

    fun getHHNoticeBoard(cwnum: String, idx: Int, count: Int, http : DefaultHttp, callback: DefaultHttp.Callback) {

        val jIdx = if(idx > 0) { idx.toString() } else { "" }
        val jCount = count.toString()

        val json = JSONObject()
        json.put("nCwnum", cwnum)
        json.put("nIdx", jIdx)
        json.put("nNum", jCount)

        http.requestJSON(HTTP_HH, json, callback)
    }

    fun getNewsBoard(cwnum: String, idx: Int, count: Int, http : DefaultHttp, callback: DefaultHttp.Callback) {

        val jIdx = if(idx > 0) { idx.toString() } else { "" }
        val jCount = count.toString()

        val json = JSONObject()
        json.put("nCwnum", cwnum)
        json.put("nIdx", jIdx)
        json.put("nNum", jCount)

        http.requestJSON(HTTP_NEWS, json, callback)
    }

    fun getFAQBoard(cwnum: String, idx: Int, count: Int, http : DefaultHttp, callback: DefaultHttp.Callback) {

        val jIdx = if(idx > 0) { idx.toString() } else { "" }
        val jCount = count.toString()

        val json = JSONObject()
        json.put("nCwnum", cwnum)
        json.put("nIdx", jIdx)
        json.put("nNum", jCount)

        http.requestJSON(HTTP_FAQ, json, callback)
    }

    fun getBoardView(type: Int, idx: Int, http : DefaultHttp, callback: DefaultHttp.Callback) {

        val jIdx = if(idx > 0) { idx.toString() } else { "" }
        val url = when(type) {
            Config.BOARD_JIBU -> { HTTP_JIBUVIEW }
            Config.BOARD_HNOTICE -> { HTTP_HHVIEW }
            Config.BOARD_NEWS -> { HTTP_NEWSVIEW }
            Config.BOARD_FAQ -> { HTTP_FAQVIEW }
            else ->{ HTTP_NOTICEVIEW }
        }

        val json = JSONObject()
        json.put("nIdx", jIdx)

        http.requestJSON(url, json, callback)
    }

}