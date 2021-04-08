package kr.bgsoft.ucargo.jeju.controller.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kr.bgsoft.ucargo.jeju.App
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.SubActivity
import kr.bgsoft.ucargo.jeju.UCargoService
import kr.bgsoft.ucargo.jeju.controller.conf.Config
import kr.bgsoft.ucargo.jeju.controller.conf.Settings
import kr.bgsoft.ucargo.jeju.controller.dialog.AlertDialog
import kr.bgsoft.ucargo.jeju.controller.dialog.WebDialog
import kr.bgsoft.ucargo.jeju.controller.http.DefaultHttp
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment
import kr.bgsoft.ucargo.jeju.data.model.AppSetting
import kr.bgsoft.ucargo.jeju.data.model.Cargo
import kr.bgsoft.ucargo.jeju.data.model.Location
import kr.bgsoft.ucargo.jeju.data.sqlite.IsRead
import kr.bgsoft.ucargo.jeju.utils.Etc
import org.json.JSONObject
import java.util.ArrayList
import kotlin.math.ceil

class ListViewFragment : DefaultFragment(), View.OnClickListener{

    var nactivity: SubActivity? = null
    var nmydata: Cargo? = null
    var npoint = 0
    var isSend = false
    var ndisttext: TextView? = null
    var appsetting = AppSetting()
    var isNight = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        appsetting = Settings.getAppSetting(activity!!)
        isNight = Etc.getNight(appsetting)

        isNight = false

        val layout = if (isNight) {
            inflater.inflate(R.layout.fragment_listview_n, container, false)
        } else {
            inflater.inflate(R.layout.fragment_listview2, container, false)
        }

        nactivity = activity as SubActivity

        nmydata = nactivity?.intent?.getSerializableExtra(Config.EXTRA_ORDER) as Cargo
        npoint  = if(App.userInfo.point.isNullOrBlank()) { 0 } else { App.userInfo.point.toInt() }

        if(nmydata == null) {
            try {
                toasts(resources.getString(R.string.toast_error_server))
            } catch (e: Exception) {

            }
            nactivity?.finish()
        }

        val view_title      = layout.findViewById<TextView>(R.id.header_title)
        val view_back       = layout.findViewById<ImageButton>(R.id.header_back)
        val view_point      = layout.findViewById<TextView>(R.id.listview_point)
        val view_warningbox = layout.findViewById<LinearLayout>(R.id.listview_warningbox)
        val view_warning    = layout.findViewById<ImageView>(R.id.listview_warning)
        val view_start      = layout.findViewById<TextView>(R.id.listview_start)
        val view_startdong  = layout.findViewById<TextView>(R.id.listview_startdong)
        val view_end        = layout.findViewById<TextView>(R.id.listview_end)
        val view_enddong    = layout.findViewById<TextView>(R.id.listview_enddong)
        val view_km         = layout.findViewById<TextView>(R.id.listview_km)
        val view_loadday    = layout.findViewById<TextView>(R.id.listview_loadday)
        val view_downday    = layout.findViewById<TextView>(R.id.listview_downday)
        val view_info       = layout.findViewById<TextView>(R.id.listview_info)
        val view_carsize2   = layout.findViewById<TextView>(R.id.listview_carsize2)
        val view_carsize    = layout.findViewById<TextView>(R.id.listview_carsize)
        val view_etc        = layout.findViewById<TextView>(R.id.listview_etc)
        val view_loadtype   = layout.findViewById<TextView>(R.id.listview_loadtype)
        val view_downtype   = layout.findViewById<TextView>(R.id.listview_downtype)
        val view_pay        = layout.findViewById<TextView>(R.id.listview_pay)
        val view_fee        = layout.findViewById<TextView>(R.id.listview_fee)
        val view_cutpay     = layout.findViewById<TextView>(R.id.listview_cutpay)
        val view_close      = layout.findViewById<Button>(R.id.listview_close)
        val view_send       = layout.findViewById<Button>(R.id.listview_send)
        val view_addpay     = layout.findViewById<TextView>(R.id.listview_addpay)
        val view_addpay2    = layout.findViewById<TextView>(R.id.listview_addpay2)
        val view_paytype    = layout.findViewById<TextView>(R.id.listview_paytype)
        val view_gotobank   = layout.findViewById<TextView>(R.id.listview_gotobank)
        val view_distbox    = layout.findViewById<LinearLayout>(R.id.listview_startdist_box)
        val view_regdate    = layout.findViewById<TextView>(R.id.listview_regdate)
        val view_title1     = layout.findViewById<TextView>(R.id.listview_title1)
        val view_title2     = layout.findViewById<TextView>(R.id.listview_title2)
        val view_title4     = layout.findViewById<TextView>(R.id.listview_title4)
        val view_title5     = layout.findViewById<TextView>(R.id.listview_title5)
        val view_title6     = layout.findViewById<TextView>(R.id.listview_title6)
        val view_startdist_text = layout.findViewById<TextView>(R.id.listview_startdist_text)
        val view_subtitle1  = layout.findViewById<TextView>(R.id.listview_subtitle1)
        val view_subtitle2  = layout.findViewById<TextView>(R.id.listview_subtitle2)
        val view_subtitle3  = layout.findViewById<TextView>(R.id.listview_subtitle3)
        val view_subtitle4  = layout.findViewById<TextView>(R.id.listview_subtitle4)
        val view_subtitle5  = layout.findViewById<TextView>(R.id.listview_subtitle5)
        val view_subtitle6  = layout.findViewById<TextView>(R.id.listview_subtitle6)

        ndisttext           = layout.findViewById(R.id.listview_startdist_text)


        view_title.text = resources.getString(R.string.title_listview)
        view_back.setOnClickListener(this)
        view_point.text = Etc.setComma(npoint)

        if(nmydata!!.nGoodbad > 0) {
            for(i in 1..(nmydata!!.nGoodbad)) {
                val viewadd = ImageView(nactivity)
                viewadd.layoutParams = view_warning.layoutParams
                viewadd.setImageDrawable(view_warning.drawable)
                view_warningbox.addView(viewadd)
            }
        } else {
            val viewadd = ImageView(nactivity)
            val param =  view_warning.layoutParams
            param.width = ViewGroup.LayoutParams.WRAP_CONTENT
            viewadd.layoutParams = param
            viewadd.setImageResource(R.drawable.ico_nowarnning)
            view_warningbox.addView(viewadd)
        }

        view_warningbox.removeView(view_warning)

        val arrayStart  = nmydata!!.sLoadLoc.split(" ")
        var startDong   = ""
        var startCity   = ""

        val arrayEnd  = nmydata!!.sDownLoc.split(" ")
        var endDong   = ""
        var endCity   = ""

        when(arrayStart.count()) {
            1 -> {
                startCity = arrayStart[0].take(2)
                startDong = arrayStart[0].takeLast(arrayStart[0].length - 2)
            }
            2 -> {
                for (i in 0..(arrayStart.count() - 1)) {
                    if (i in 1..(arrayStart.count() - 1)) {
                        if(!arrayStart[i].isNullOrBlank()) {
                            if (startDong.isNotBlank()) {
                                startDong += " "
                            }
                            startDong += arrayStart[i]
                        }
                    } else {
                        if(!arrayStart[i].isNullOrBlank()) {
                            if (startCity.isNotBlank()) {
                                startCity += " "
                            }
                            startCity += arrayStart[i]
                        }
                    }
                }
            }
            else ->{
                for (i in 0..(arrayStart.count() - 1)) {
                    if(i in 2..(arrayStart.count() - 1)) {
                        if(!arrayStart[i].isNullOrBlank()) {
                            if (startDong.isNotBlank()) {
                                startDong += " "
                            }
                            startDong += arrayStart[i]
                        }
                    } else {
                        if(!arrayStart[i].isNullOrBlank()) {
                            if (startCity.isNotBlank()) {
                                startCity += " "
                            }
                            startCity += arrayStart[i]
                        }
                    }
                }
            }
        }

        when(arrayEnd.count()) {
            1 -> {
                endCity = arrayEnd[0].take(2)
                endDong = arrayEnd[0].takeLast(arrayEnd[0].length - 2)
            }
            2 -> {
                for (i in 0..(arrayEnd.count() - 1)) {
                    if(i in 1..(arrayEnd.count() - 1)) {
                        if(!arrayEnd[i].isNullOrBlank()) {
                            if (endDong.isNotBlank()) {
                                endDong += " "
                            }
                            endDong += arrayEnd[i]
                        }
                    } else {
                        if(!arrayEnd[i].isNullOrBlank()) {
                            if (endCity.isNotBlank()) {
                                endCity += " "
                            }
                            endCity += arrayEnd[i]
                        }
                    }
                }
            }
            else ->{
                for (i in 0..(arrayEnd.count() - 1)) {
                    if(i in 2..(arrayEnd.count() - 1)) {
                        if(!arrayEnd[i].isNullOrBlank()) {
                            if (endDong.isNotBlank()) {
                                endDong += " "
                            }
                            endDong += arrayEnd[i]
                        }
                    } else {
                        if(!arrayEnd[i].isNullOrBlank()) {
                            if (endCity.isNotBlank()) {
                                endCity += " "
                            }
                            endCity += arrayEnd[i]
                        }
                    }
                }
            }
        }

        var cutstring = 30
        when(appsetting.textSize) {
            2 -> {
                cutstring = 24
            }
            3 -> {
                cutstring = 36
            }
        }

        if(Etc.length(startDong) > cutstring) {
            startDong = Etc.substring(startDong, cutstring)
        }
        if(Etc.length(endDong) > cutstring) {
            endDong = Etc.substring(endDong, cutstring)
        }

        view_start.text = startCity
        view_startdong.text = startDong

        view_end.text = endCity
        view_enddong.text = endDong

        if(startDong.isNullOrBlank()) {
            view_startdong.visibility = View.GONE
        }
        if(endDong.isNullOrBlank()) {
            view_enddong.visibility = View.GONE
        }

        view_loadday.text   = nmydata!!.sLoadDay.replace("-", ".")
        view_downday.text   = nmydata!!.sDownDay.replace("-", ".")

        view_km.text = if(nmydata!!.nDistance.isNullOrBlank() || nmydata!!.nDistance == "0") {
            "-"
        } else {
            if(nmydata!!.nDistance.toDouble() > 1000) {
                (nmydata!!.nDistance.toDouble() / 1000).toInt().toString() + "km"
            } else {
                nmydata!!.nDistance.toDouble().toInt().toString() + "km"
            }
        }

        val mixuptext = if(resources.getString(R.string.list_uptype1) == nmydata?.sLoadtype && nmydata!!.sGoodsInfo.indexOf(resources.getString(R.string.list_uptype1)) == -1) {
            resources.getString(R.string.list_uptype1) + " - "
        } else {
            ""
        }

        view_carsize2.text  = nmydata!!.sCarton + " | " + nmydata!!.sCarType
        view_info.text      = mixuptext + nmydata!!.sGoodsInfo
        view_etc.text       = if(nmydata!!.sETC.isNullOrBlank()) {
            "-"
        } else {
            nmydata!!.sETC
        }

        view_carsize.text       = if(nmydata!!.sGoodsWeight.isNullOrBlank()) {
            "-"
        } else {
            nmydata!!.sGoodsWeight
        }

        view_loadtype.text  = nmydata!!.sLoadMethod
        view_downtype.text  = nmydata!!.sDownMethod

        //총운임
        if((nmydata!!.nPay + nmydata!!.nFee) > 0) {
            view_pay.text = Etc.setComma((nmydata!!.nPay + nmydata!!.nFee))
        } else {
            view_pay.text = resources.getString(R.string.list_nopay)
            view_addpay2.visibility = View.GONE
        }
        view_fee.text       = Etc.setComma(nmydata!!.nFee)

        //기사운임
        if(nmydata!!.nPay > 0) {
            view_cutpay.text    = Etc.setComma(nmydata!!.nPay)
        } else {
            view_cutpay.text = resources.getString(R.string.list_nopay)
            view_addpay.visibility = View.GONE
        }

        var resid       = 0
        var stringid    = 0
        var colorid     = 0

        colorid = android.R.color.white

        when (nmydata!!.sPayment) {
            resources.getString(R.string.list_pay1) -> {
                resid = R.drawable.box_type3_25
                stringid = R.string.list_pay1
            }
            resources.getString(R.string.list_pay2) -> {
                resid = R.drawable.box_type3_24
                stringid = R.string.list_pay2
            }
            resources.getString(R.string.list_pay3) -> {
                resid = R.drawable.box_type3_25
                stringid = R.string.list_pay3
            }
            resources.getString(R.string.list_pay4) -> {
                resid = R.drawable.box_type3_24
                stringid = R.string.list_pay4
            }
            resources.getString(R.string.list_pay5) -> {
                resid = R.drawable.box_type3_24
                stringid = R.string.list_pay5
            }
            resources.getString(R.string.list_pay6) -> {
                resid = R.drawable.box_type3_24
                stringid = R.string.list_pay6
            }
            resources.getString(R.string.list_pay7) -> {
                resid = R.drawable.box_type3_24
                stringid = R.string.list_pay7
                view_paytype.textSize = 13f
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view_start.setTextColor(resources.getColor(R.color.colorUser46, null))
            view_end.setTextColor(resources.getColor(R.color.colorUser47, null))
            view_startdong.setTextColor(resources.getColor(R.color.colorUser46, null))
            view_enddong.setTextColor(resources.getColor(R.color.colorUser47, null))
        } else {
            view_start.setTextColor(resources.getColor(R.color.colorUser46))
            view_end.setTextColor(resources.getColor(R.color.colorUser47))
            view_startdong.setTextColor(resources.getColor(R.color.colorUser46))
            view_enddong.setTextColor(resources.getColor(R.color.colorUser47))
        }
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view_paytype.setTextColor(resources.getColor(colorid, null))
        } else {
            view_paytype.setTextColor(resources.getColor(colorid))
        }
        view_paytype.text = resources.getString(stringid)
        view_paytype.setBackgroundResource(resid)

        view_close.setOnClickListener(this)
        view_send.setOnClickListener(this)
        view_gotobank.setOnClickListener(this)
        view_distbox.setOnClickListener(this)

        val cwnum = Settings.getID(activity!!)
        if(!cwnum.isNullOrBlank()) {
            /*MemberHttp().getUserPoint(cwnum!!, Http, object : DefaultHttp.Callback {
                override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {

                }

                override fun onSuccess(json: Any) {
                    val data = json as JSONObject
                    App.userInfo.point    = data.getString("point")
                    view_point.text       = Etc.setComma(App.userInfo.point.toInt())
                }
            })*/
        }

        view_regdate.text               = nmydata!!.sRegDate.substring(11, 16)

        view_startdong.isSelected = true
        view_enddong.isSelected = true

        return layout
    }

    override fun onResume() {
        super.onResume()
        val location = kr.bgsoft.ucargo.jeju.device.Location(nactivity!!)

        if(location.isLocation() && UCargoService.nlat > 0 && UCargoService.nlng > 0) {
            getKm(false)
        }
    }

    fun goSend(title: String, contents: String) {
        var type: AlertDialog.TYPE = AlertDialog.TYPE.YESNO

        if(nmydata!!.sPayment == resources.getString(R.string.list_pay7_code) || nmydata!!.sPayment == resources.getString(R.string.list_pay7)) {
            type = AlertDialog.TYPE.YESNOH
        }

        val dialog = AlertDialog(nactivity, type, title, contents, object : AlertDialog.Callback {
            override fun onNo() {

            }

            override fun onYes() {
                val cwnum = Settings.getID(activity!!)
                if (!cwnum.isNullOrBlank()) {
                    isSend = true

                    /*CargoHttp().setSend(cwnum!!, nmydata!!.nOrderNum, resources.getString(R.string.scode_type2), Http, object : DefaultHttp.Callback {
                        override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                            logd("error : " + message)
                            if (code != DefaultHttp.HttpCode.ERROR_700 && code != DefaultHttp.HttpCode.ERROR_701 && code != DefaultHttp.HttpCode.ERROR_702) {
                                try {
                                    toasts(resources.getString(R.string.toast_error_server))
                                } catch (e: Exception) {

                                }
                            }
                            isSend = false
                        }

                        override fun onSuccess(json: Any) {
                            logd("suceess")
                            toastl(resources.getString(R.string.toast_listview_sendok))

                            UCargoActivity.showMyList(nmydata!!.nOrderNum)
                            UCargoActivity.nactivity_list?.finish()
                            nactivity?.finish()
                            isSend = false

                            UCargoActivity.resetPoint()
                        }
                    })*/
                }
            }
        })

        dialog.show()
    }

    fun getKm(isShow : Boolean) {
        val location = kr.bgsoft.ucargo.jeju.device.Location(nactivity!!)
        if(!location.isLocation()) {
            val dialog = AlertDialog(nactivity, AlertDialog.TYPE.LOCATION, resources.getString(R.string.dialog_location_title), resources.getString(R.string.dialog_location_info), object : AlertDialog.Callback {
                override fun onNo() {
                }

                override fun onYes() {
                    startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            })
            dialog.show()
        } else {
            ndisttext?.text = resources.getString(R.string.listview_location_wait)

            if (UCargoService.nlat > 0 && UCargoService.nlng > 0) {
                val array = ArrayList<Location>()

                array.add(Location(UCargoService.nlat, UCargoService.nlng))
                array.add(Location(nmydata!!.sLoadLat, nmydata!!.sLoadLng))

                /*AppHttp().getDistancetime(array, 0, Http, object : DefaultHttp.Callback {
                    override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                        try {
                            if(isShow) {
                                toasts(resources.getString(R.string.toast_error_server))
                            }
                            ndisttext?.text = resources.getString(R.string.listview_startdist_help)
                        } catch (e: Exception) {

                        }
                    }

                    override fun onSuccess(json: Any) {
                        val data = json as JSONObject

                        if(data.getInt("nTotalDistance") > 0) {
                            val totalkm = ceil(data.getInt("nTotalDistance").toDouble() / 1000).toInt()
                            val totaltime = data.getInt("nTotalTime")
                            ndisttext?.text = resources.getString(R.string.listview_startdist_text).replace("{km}", Etc.setComma(totalkm).toString()).replace("{time}", Etc.setSectoTime(totaltime, resources.getString(R.string.location_format1)))
                        } else {
                            if(isShow) {
                                toasts(resources.getString(R.string.toast_location_tryerror))
                            }
                            ndisttext?.text = resources.getString(R.string.listview_startdist_help)
                        }
                    }
                })*/
            } else {
                if(isShow) {
                    toasts(resources.getString(R.string.toast_location_tryerror))
                }
                ndisttext?.text = resources.getString(R.string.listview_startdist_help)
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.header_back, R.id.listview_close -> {
                nactivity?.finish()
            }
            R.id.listview_gotobank -> {
                //UCargoActivity.showMyBank()
            }
            R.id.listview_send -> {
                if(isSend) {
                    toasts(resources.getString(R.string.toast_listview_issend))
                } else {
//                    if (npoint < nmydata!!.nFee) {
//                        toastl(resources.getString(R.string.toast_listview_nopay))
//                    } else {
                    var title           = resources.getString(R.string.dialog_setok_title)
                    var contents        = resources.getString(R.string.dialog_setok_info)
                    var isDialogShow    = true

                    if(nmydata!!.sPayment == resources.getString(R.string.list_pay7_code) || nmydata!!.sPayment == resources.getString(R.string.list_pay7)) {
                        title           = resources.getString(R.string.dialog_cardsetok_title)
                        contents        = resources.getString(R.string.dialog_cardsetok_info)

                        val read = IsRead(activity, Config.DB_Reads, null, Config.DB_Reads_ver)
                        val isCut = read.select(Config.READ_CARDRULE)

                        if(!isCut) {
                            val dialog = WebDialog(nactivity, WebDialog.TYPE.JOIN, nactivity!!.resources.getString(R.string.dialog_rule_title4), "", nactivity!!.resources.getString(R.string.url_rule4).replace("{host}", App.host), object : WebDialog.Callback{
                                override fun onYes() {
                                    read.insert(Config.READ_CARDRULE, 1)
                                    goSend(title, contents)
                                }

                                override fun onNo() {

                                }
                            })
                            dialog.show()
                            isDialogShow = false
                        }
                    }

                    if(isDialogShow) {
                        goSend(title, contents)
                    }
//                    }
                }
            }
            R.id.listview_startdist_box -> {
                logd("listview_startdist_box click")
                getKm(true)
            }
        }
    }
}