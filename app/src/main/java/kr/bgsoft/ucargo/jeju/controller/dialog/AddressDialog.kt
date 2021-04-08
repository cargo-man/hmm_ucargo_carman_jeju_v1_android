package kr.bgsoft.ucargo.jeju.controller.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.controller.adapter.CargoSettingAdapter
import kr.bgsoft.ucargo.jeju.cview.DefaultDialog
import kr.bgsoft.ucargo.jeju.data.model.Code
import org.json.JSONArray
import org.json.JSONObject

class AddressDialog (context: Context?, type: TYPE?, title: String, hcount: Int, arrData: ArrayList<Code>, callback: Callback?) : DefaultDialog(context), View.OnClickListener {

    val ntype                           = type
    val ntitle                          = title
    val nhcount                         = hcount
    val ncallback                       = callback
    var nadapter: CargoSettingAdapter?  = null
    var ngridlist: GridView?            = null
    var nview_sido: TextView?           = null
    var nview_gugun: TextView?          = null
    var arrAddress                      = ArrayList<Code>()
    val arrNowData                      = arrData
    var nstep                           = 0
    var isFirst                         = true
    var nkktype                         = ""
    var isSelect                        = false

    interface Callback {
        fun onYes(data: JSONObject)
        fun onNo()
    }

    enum class TYPE {
        ALERTSELECT, SELECT, FULL
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.dialog_address)

        val view_title      = findViewById<TextView>(R.id.address_title)
        val view_ok         = findViewById<Button>(R.id.address_ok)
        val view_cancle     = findViewById<Button>(R.id.address_cancle)
        nview_sido          = findViewById(R.id.address_sido)
        nview_gugun         = findViewById(R.id.address_gugun)
        ngridlist           = findViewById(R.id.address_gridview)

        view_ok.setOnClickListener(this)
        view_cancle.setOnClickListener(this)

        view_title.text         = ntitle
        ngridlist?.numColumns   = nhcount
        nadapter                = CargoSettingAdapter(thisContext!!)

        ngridlist?.adapter      = nadapter

        ngridlist?.onItemClickListener = AdapterView.OnItemClickListener {
                parent, view, position, id ->
            run {
                val data = nadapter?.getItem(position)

                logd(nadapter?.getSelectCount().toString())

                if(nstep == 1 && nadapter?.getSelectCount()!! >= 5 && !data!!.isSelect) {
                    toasts(thisContext!!.resources.getString(R.string.toast_sido_maxerror))
                } else if(data != null) {
                    if(nstep > 1 && (data.Code == "all" || data.Code.indexOf("kktype") > -1) && !data.isSelect) {
                        nadapter?.getCheckClear()
                    }
                    if(nstep == 2 && data.Code != "all" && data.Code.indexOf("kktype") == -1) {
                        for(i in 0..(nadapter!!.arrayData.size - 1)) {
                            if(nadapter!!.arrayData[i].Code == "all" || nkktype == nadapter!!.arrayData[i].Code) {
                                nadapter!!.arrayData[i].isSelect = false
                            }
                        }
                    }

                    data.isSelect = !data.isSelect

                    if(data.Code.indexOf("kktype") > -1 && data.isSelect) {
                        nkktype = data.Code
                        for (i in 0..(nadapter?.arrayData!!.size - 1)) {
                            if (data.Code == "kktype1" && (nadapter?.arrayData!![i].Name == "가평" || nadapter?.arrayData!![i].Name == "구리" ||
                                        nadapter?.arrayData!![i].Name == "남양주" || nadapter?.arrayData!![i].Name == "양평" ||
                                        nadapter?.arrayData!![i].Name == "포천" || nadapter?.arrayData!![i].Name == "하남")) {
                                nadapter?.arrayData!![i].isSelect = true
                            }
                            if (data.Code == "kktype2" && (nadapter?.arrayData!![i].Name == "고양" || nadapter?.arrayData!![i].Name == "김포" ||
                                        nadapter?.arrayData!![i].Name == "동두천" || nadapter?.arrayData!![i].Name == "양주" ||
                                        nadapter?.arrayData!![i].Name == "연천" || nadapter?.arrayData!![i].Name == "의정부" ||
                                        nadapter?.arrayData!![i].Name == "파주")) {
                                nadapter?.arrayData!![i].isSelect = true
                            }
                            if (data.Code == "kktype3" && (nadapter?.arrayData!![i].Name == "광주" || nadapter?.arrayData!![i].Name == "성남" ||
                                        nadapter?.arrayData!![i].Name == "수원" || nadapter?.arrayData!![i].Name == "안성" ||
                                        nadapter?.arrayData!![i].Name == "양평" || nadapter?.arrayData!![i].Name == "여주" ||
                                        nadapter?.arrayData!![i].Name == "오산" || nadapter?.arrayData!![i].Name == "용인" ||
                                        nadapter?.arrayData!![i].Name == "이천" || nadapter?.arrayData!![i].Name == "하남")) {
                                nadapter?.arrayData!![i].isSelect = true
                            }
                            if (data.Code == "kktype4" && (nadapter?.arrayData!![i].Name == "과천" || nadapter?.arrayData!![i].Name == "광명" ||
                                        nadapter?.arrayData!![i].Name == "군포" || nadapter?.arrayData!![i].Name == "부천" ||
                                        nadapter?.arrayData!![i].Name == "수원" || nadapter?.arrayData!![i].Name == "안산" ||
                                        nadapter?.arrayData!![i].Name == "안양" || nadapter?.arrayData!![i].Name == "오산" ||
                                        nadapter?.arrayData!![i].Name == "의왕" || nadapter?.arrayData!![i].Name == "평택" ||
                                        nadapter?.arrayData!![i].Name == "화성")) {
                                nadapter?.arrayData!![i].isSelect = true
                            }

                            logd(data.Code + " >> " + nadapter?.arrayData!![i].Name + " :: " + nadapter?.arrayData!![i].isSelect.toString())
                        }
                    } else if(data.Code.indexOf("kktype") > -1 && !data.isSelect) {
                        nadapter?.getCheckClear()
                    }
                }

                var text = ""

                for(i in 0..(nadapter!!.arrayData.size - 1)) {
                    if(nadapter!!.arrayData[i].isSelect && nadapter!!.arrayData[i].Code.indexOf("kktype") == -1) {
                        if(nstep == 1) {
                            text = if(!text.isNullOrBlank()) {
                                text + "," + nadapter!!.arrayData[i].Name
                            } else {
                                nadapter!!.arrayData[i].Name
                            }
                        } else if(nstep == 2) {
                            text = if(!text.isNullOrBlank()) {
                                text + "," + arrAddress[0].value + " " + nadapter!!.arrayData[i].Name
                            } else {
                                arrAddress[0].value + " " + nadapter!!.arrayData[i].Name
                            }
                        }
                    }
                }

                if(nstep == 1) {
                    nview_sido?.text = text
                } else if(nstep == 2) {
                    nview_gugun?.text = text
                }

                nadapter?.dataChange()
            }
        }

        arrAddress.add(Code("", ""))
        arrAddress.add(Code("", ""))
        arrAddress.add(Code("", ""))

        if(arrNowData.size == 0) {
            arrNowData.add(Code("", ""))
        }

        var text = ""
        for(i in 0..(arrNowData.size - 1)) {
            text = if(text.isNullOrBlank()) {
                arrNowData[i].value
            } else {
                text + "," + arrNowData[i].value
            }
        }

        if(arrNowData[0].code.length == 1 && arrNowData.size == 1) {
            arrAddress[0].value = arrNowData[0].value.take(2)
            arrAddress[0].code  = arrNowData[0].code.take(1)

            nstep = 2
            loadAddress(nstep, arrAddress[0].code)
            nview_sido?.text    = arrAddress[0].value
            nview_gugun?.text   = text

        } else if(arrNowData[0].code.length > 1) {

            arrAddress[0].value = arrNowData[0].value.take(2)
            arrAddress[0].code  = arrNowData[0].code.take(1)

            nstep = 2
            loadAddress(nstep, arrAddress[0].code)
            nview_sido?.text    = arrAddress[0].value
            nview_gugun?.text   = text
        } else {
            nstep = 1
            loadAddress(nstep, "")
            nview_sido?.text    = text
        }

        isFirst     = true
        isSelect    = false
    }

    fun loadAddress(step: Int, code: String) {
        /*AppHttp().getAddress(step, code, Http, object : DefaultHttp.Callback{
            override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                toasts(resource!!.getString(R.string.toast_error_server))
            }

            override fun onSuccess(json: Any) {
                val data = json as JSONArray

                if(data.length() > 0) {
                    nadapter?.clear()

                    if(code == "H") {
                        nadapter?.addData(resource!!.getString(R.string.address_kk_type1), "kktype1", false)
                        nadapter?.addData(resource!!.getString(R.string.address_kk_type2), "kktype2", false)
                        nadapter?.addData(resource!!.getString(R.string.address_kk_type3), "kktype3", false)
                        nadapter?.addData(resource!!.getString(R.string.address_kk_type4), "kktype4", false)
                    }
                    if(nstep > 1) {
                        nadapter?.addData(resource!!.getString(R.string.address_all), "all", false)
                    }

                    for(i in 0..(data.length() -1)) {
                        var address = data.getJSONObject(i)
                        var value   = ""
                        var code    = address.getString("sCode")
                        when(step) {
                            1 -> {
                                value = address.getString("sSido")
                            }
                            2 -> {
                                value += if(!address.getString("sGu").isNullOrBlank()) { address.getString("sGu") } else { "" }
                                value += if(!value.isNullOrBlank()) { " " } else { "" } + if(!address.getString("sGun").isNullOrBlank()) { address.getString("sGun") } else { "" }
                            }
                            3 -> {
                                value += if(!address.getString("sGun").isNullOrBlank()) { address.getString("sGun") } else { "" }
                                value += if(!value.isNullOrBlank()) { " " } else { "" } + if(!address.getString("sDong").isNullOrBlank()) { address.getString("sDong") } else { "" }
                            }
                        }

                        var isTrue = false

                        if(isFirst) {
                            logd("isFirst start")
                            for(j in 0..(arrNowData.size - 1)) {
                                logd(arrNowData[j].code + "==" +  code)
                                if(arrNowData[j].code == code) {
                                    isTrue = true
                                    break
                                }
                            }
                        } else {
                            if (arrAddress.size >= nstep) {
                                val savedata = arrAddress[nstep - 1]

                                if (savedata.code == code) {
                                    isTrue = true
                                }
                            }
                        }

                        nadapter?.addData(value.trim(), code.trim(), isTrue)
                    }

                    if(nstep > 1 && nadapter!!.getSelectCount() == 0 && arrNowData.size > 0) {
                        if(code == "H") {
                            nadapter!!.arrayData[4].isSelect = true
                        } else {
                            nadapter!!.arrayData[0].isSelect = true
                        }
                    }
                }

                nadapter?.dataChange()
                isFirst = false
            }
        })*/
    }

    override fun dismiss() {
        if(isSelect) {
            super.dismiss()
        } else if(nstep == 2) {
            nstep = 1
            loadAddress(nstep, "")
            nview_gugun?.text = ""
        } else {
            super.dismiss()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.address_ok -> {
                if(nadapter!!.getSelectCount() == 0) {
                    toasts(resource!!.getString(R.string.toast_addselect_nocount))
                } else {
                    if (nadapter!!.getSelectCount() == 1 && nstep == 1) {
                        nstep = 2

                        for(i in 0..(nadapter!!.arrayData.size - 1)) {
                            if(nadapter!!.arrayData[i].isSelect) {
                                arrAddress[0].code  = nadapter!!.arrayData[i].Code
                                arrAddress[0].value = nadapter!!.arrayData[i].Name
                                break
                            }
                        }

                        loadAddress(nstep, arrAddress[0].code)
                    } else {
                        val rtnJson = JSONObject()

                        var text = ""
                        var code = ""

                        for(i in 0..(nadapter!!.arrayData.size - 1)) {
                            if(nadapter!!.arrayData[i].isSelect && nadapter!!.arrayData[i].Code.indexOf("kktype") == -1) {
                                if(nstep == 1) {
                                    text = if(!text.isNullOrBlank()) {
                                        text + "," + nadapter!!.arrayData[i].Name
                                    } else {
                                        nadapter!!.arrayData[i].Name
                                    }
                                } else if(nstep == 2) {
                                    text = if(!text.isNullOrBlank()) {
                                        text + "," + arrAddress[0].value + " " + nadapter!!.arrayData[i].Name
                                    } else {
                                        arrAddress[0].value + " " + nadapter!!.arrayData[i].Name
                                    }
                                }

                                code = if(!code.isNullOrBlank()) {
                                    code + "," + nadapter!!.arrayData[i].Code
                                } else {
                                    nadapter!!.arrayData[i].Code
                                }
                            }
                        }

                        logd("code :: "+ code + " // " + arrAddress[0].code)

                        code = code.replace("all", arrAddress[0].code)

                        isSelect = true
                        rtnJson.put("code", code)
                        rtnJson.put("text", text)
                        ncallback?.onYes(rtnJson)
                        dismiss()
                    }
                }
            }
            R.id.address_cancle -> {
                if(nstep == 2) {
                    nstep = 1
                    loadAddress(nstep, "")
                    nview_gugun?.text = ""
                } else {
                    ncallback?.onNo()
                    dismiss()
                }
            }
        }
    }
}