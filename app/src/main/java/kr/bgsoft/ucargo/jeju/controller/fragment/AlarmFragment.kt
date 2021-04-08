package kr.bgsoft.ucargo.jeju.controller.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.SubActivity
import kr.bgsoft.ucargo.jeju.controller.conf.Settings
import kr.bgsoft.ucargo.jeju.controller.dialog.AddressDialog
import kr.bgsoft.ucargo.jeju.controller.dialog.CargoSettingDialog
import kr.bgsoft.ucargo.jeju.controller.dialog.DatePickerDialog
import kr.bgsoft.ucargo.jeju.cview.ArrowButton
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment
import kr.bgsoft.ucargo.jeju.data.model.AppSetting
import kr.bgsoft.ucargo.jeju.data.model.CargoAlert
import kr.bgsoft.ucargo.jeju.data.model.Code
import kr.bgsoft.ucargo.jeju.utils.Etc
import org.json.JSONObject
import java.util.*

class AlarmFragment: DefaultFragment(), View.OnClickListener{

    var nactivity: SubActivity? = null
    var nloaddate: TextView? = null
    var nloadhour: TextView? = null
    var nloadarea: ArrowButton? = null
    var ndownarea: ArrowButton? = null
    var ncarsize: ArrowButton? = null
    var ncartype: ArrowButton? = null
    var npaytext: EditText? = null
    var npushuse: CheckBox? = null
    var ndowntype1: RadioButton? = null
    var ndowntype2: RadioButton? = null
    var ndowntype3: RadioButton? = null
    var nstatus1: RadioButton? = null
    var nstatus2: RadioButton? = null
    var nstatus3: RadioButton? = null
    var nstatus4: RadioButton? = null
    var nstatus5: RadioButton? = null
    var nstatus6: RadioButton? = null
    var nscroll: ScrollView? = null
    var nsend: Button? = null
    var ndelete: Button? = null
    var nblockbox: RelativeLayout? = null
    var npushuse2: CheckBox? = null
    var nnotitle: TextView? = null

    val arrayData = ArrayList<CargoAlert>()
    var arrayLoadarea = ArrayList<Code>()
    var arrayDownarea = ArrayList<Code>()
    var nCarsize = ArrayList<Code>()
    var nCartype = ArrayList<Code>()

    var nappsettings = AppSetting()

    var nstatus = 1
    var ncutton = ""
    var isFirst = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_cargoalert, container, false)

        nactivity = activity as SubActivity
        nactivity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val view_title = layout.findViewById<TextView>(R.id.header_title)
        val view_back = layout.findViewById<ImageButton>(R.id.header_back)
        val view_add1 = layout.findViewById<TextView>(R.id.cargoalert_add1)
        val view_add2 = layout.findViewById<TextView>(R.id.cargoalert_add2)

        view_title.text = resources.getString(R.string.title_cargoalert)
        view_back.setOnClickListener(this)

        nloaddate = layout.findViewById(R.id.cargoalert_loaddate)
        nloadhour = layout.findViewById(R.id.cargoalert_loadhour)
        nloadarea = layout.findViewById(R.id.cargoalert_loadarea)
        ndownarea = layout.findViewById(R.id.cargoalert_downarea)
        ncarsize = layout.findViewById(R.id.cargoalert_carsize)
        ncartype = layout.findViewById(R.id.cargoalert_cartype)
        npaytext = layout.findViewById(R.id.cargoalert_cutpay)
        npushuse = layout.findViewById(R.id.cargoalert_onoff)
        npushuse2 = layout.findViewById(R.id.cargoalert_onoff2)
        ndowntype1 = layout.findViewById(R.id.cargoalert_type1)
        ndowntype2 = layout.findViewById(R.id.cargoalert_type2)
        ndowntype3 = layout.findViewById(R.id.cargoalert_type3)
        nstatus1 = layout.findViewById(R.id.cargoalert_status1)
        nstatus2 = layout.findViewById(R.id.cargoalert_status2)
        nstatus3 = layout.findViewById(R.id.cargoalert_status3)
        nstatus4 = layout.findViewById(R.id.cargoalert_status4)
        nstatus5 = layout.findViewById(R.id.cargoalert_status5)
        nstatus6 = layout.findViewById(R.id.cargoalert_status6)
        nscroll = layout.findViewById(R.id.cargoalert_scroll)
        nsend = layout.findViewById(R.id.cargoalert_save)
        ndelete = layout.findViewById(R.id.cargoalert_reset)
        nblockbox = layout.findViewById(R.id.cargoalert_blockbox)
        nnotitle = layout.findViewById(R.id.cargoalert_notitle)


        ncarsize?.setOnClickListener(this)
        ncartype?.setOnClickListener(this)
        npushuse?.setOnClickListener(this)
        ndowntype1?.setOnClickListener(this)
        ndowntype2?.setOnClickListener(this)
        ndowntype3?.setOnClickListener(this)
        nloaddate?.setOnClickListener(this)
        nloadhour?.setOnClickListener(this)
        nloadarea?.setOnClickListener(this)
        ndownarea?.setOnClickListener(this)
        nstatus1?.setOnClickListener(this)
        nstatus2?.setOnClickListener(this)
        nstatus3?.setOnClickListener(this)
        nstatus4?.setOnClickListener(this)
        nstatus5?.setOnClickListener(this)
        nstatus6?.setOnClickListener(this)
        npaytext?.setOnClickListener(this)
        nsend?.setOnClickListener(this)
        ndelete?.setOnClickListener(this)
        npushuse2?.setOnClickListener(this)

        npaytext?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length > 6) {
                    toasts(resources.getString(R.string.toast_cargoalert_paycuterror))
                    npaytext?.setText(s.toString().take(6))
                    Etc.hideKeyboard(activity!!)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        /**
         * reset data
         * */
        arrayData.add(CargoAlert())
        arrayData.add(CargoAlert())
        arrayData.add(CargoAlert())
        arrayData.add(CargoAlert())
        arrayData.add(CargoAlert())
        arrayData.add(CargoAlert())
        arrayData.add(CargoAlert())

        nappsettings = Settings.getAppSetting(activity!!)

        if (nappsettings?.useAlarm != null) {
            nstatus = nappsettings.useAlarm
            nstatus1?.isChecked = nstatus == 1
            nstatus2?.isChecked = nstatus == 2
            nstatus3?.isChecked = nstatus == 3
            nstatus4?.isChecked = nstatus == 4
            nstatus5?.isChecked = nstatus == 5
            nstatus6?.isChecked = nstatus == 6
        }

        loadPush()

        nblockbox?.visibility = View.VISIBLE

        isFirst = true

        npaytext?.setOnKeyListener { v, keyCode, event ->
            //Enter key Action
            if (event?.getAction() === KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                //Enter키눌렀을떄 처리
                Etc.hideKeyboard(activity!!)
                true
            } else {
                false
            }
        }

        return layout
    }

    fun loadPush() {
        var cwnum = Settings.getID(activity!!)

        if (!cwnum.isNullOrBlank()) {
            /*CargoHttp().getPushInfo(cwnum!!, nstatus, Http, object : DefaultHttp.Callback {
                override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                    logd("error: " + message)
                    toasts(resources.getString(R.string.toast_error_server))
                }

                override fun onSuccess(json: Any) {
                    val data = json as JSONObject

                    arrayData[nstatus].idx = data.getString("nIdx")
                    arrayData[nstatus].loadArea = data.getString("sLoadcity")
                    arrayData[nstatus].loadCode = data.getString("sLoadcity2")
                    arrayData[nstatus].downArea = data.getString("sDowncity")
                    arrayData[nstatus].downCode = data.getString("sDowncity2")
                    arrayData[nstatus].carSize = data.getString("sCarton").replace("a", ",")
                    arrayData[nstatus].carType = data.getString("sCartype").replace("a", ",")
                    arrayData[nstatus].cutPay = data.getInt("nPay")
                    arrayData[nstatus].loadDate =
                            if (!data.getString("sLoadDatetime").isNullOrBlank()) {
                                data.getString("sLoadDatetime").take(13)
                            } else {
                                Etc.convertDateToString(
                                        Date(), "yyyy-MM-dd HH"
                                )
                            }
                    arrayData[nstatus].isSet = data.getString("sPushUse") == "Y"
                    arrayData[nstatus].isAllSet = data.getString("sUsedPush") == "Y"
                    arrayData[nstatus].downType = data.getString("sArrival")

                    ncutton = data.getString("sUsedCarton")

                    if (isFirst) {
                        if (arrayData[nstatus].isAllSet) {
                            nblockbox?.visibility = View.GONE
                        } else {
                            nblockbox?.visibility = View.VISIBLE
                        }
                        npushuse?.isChecked = arrayData[nstatus].isAllSet

                        isFirst = false
                    }

                    arrayLoadarea = ArrayList<Code>()
                    arrayDownarea = ArrayList<Code>()
                    nCarsize = ArrayList<Code>()
                    nCartype = ArrayList<Code>()

                    val array = arrayData[nstatus].loadArea.split(",")
                    val code = arrayData[nstatus].loadCode.split("|")
                    for (i in 0..(array.count() - 1)) {
                        if (!array[i].isNullOrBlank() && !code[i].isNullOrBlank()) {
                            arrayLoadarea.add(Code(code[i].trim(), array[i].trim()))
                        }
                    }
                    val array2 = arrayData[nstatus].downArea.split(",")
                    val code2 = arrayData[nstatus].downCode.split("|")
                    for (i in 0..(array2.count() - 1)) {
                        if (!array2[i].isNullOrBlank() && !code2[i].isNullOrBlank()) {
                            arrayDownarea.add(Code(code2[i].trim(), array2[i].trim()))
                        }
                    }
                    val array3 = arrayData[nstatus].carSize.split("a")
                    for (i in 0..(array3.count() - 1)) {
                        if (!array3[i].isNullOrBlank()) {
                            nCarsize.add(
                                    Code(
                                            Settings.getCodeKey(activity!!, "ton", array3[i]),
                                            array3[i]
                                    )
                            )
                        }
                    }
                    val array4 = arrayData[nstatus].carType.split("a")
                    for (i in 0..(array4.count() - 1)) {
                        if (!array4[i].isNullOrBlank()) {
                            nCartype.add(
                                    Code(
                                            Settings.getCodeKey(activity!!, "carType", array4[i]),
                                            array4[i]
                                    )
                            )
                        }
                    }

                    changeView()
                }
            })*/
        }
    }

    fun changeView() {
        logd("changeView :: start")
        val loaddate = Etc.convertStringToDate(arrayData[nstatus].loadDate, "yyyy-MM-dd HH")
        val loadarea = if (arrayData[nstatus].loadArea.takeLast(1) == ",") {
            arrayData[nstatus].loadArea.take(arrayData[nstatus].loadArea.length - 1)
        } else {
            arrayData[nstatus].loadArea
        }
        val downarea = if (arrayData[nstatus].downArea.takeLast(1) == ",") {
            arrayData[nstatus].downArea.take(arrayData[nstatus].downArea.length - 1)
        } else {
            arrayData[nstatus].downArea
        }

        nloadarea?.setText(loadarea)
        ndownarea?.setText(downarea)
        ncarsize?.setText(arrayData[nstatus].carSize)
        ncartype?.setText(arrayData[nstatus].carType)

        val pay = arrayData[nstatus].cutPay / 1000
        npaytext?.setText(
                if (pay <= 0) {
                    ""
                } else {
                    pay.toString()
                }
        )
        nloaddate?.text = Etc.convertDateToString(loaddate, "yyyy.MM.dd")
        nloadhour?.text = Etc.convertDateToString(loaddate, "HH")

        when (arrayData[nstatus].downType) {
            resources.getString(R.string.cargoalert_type2) -> {
                ndowntype2?.isChecked = true
            }
            resources.getString(R.string.cargoalert_type3) -> {
                ndowntype3?.isChecked = true
            }
            else -> {
                ndowntype1?.isChecked = true
            }
        }

        npushuse2?.isChecked = arrayData[nstatus].isSet

        nnotitle?.text = nstatus.toString() + resources.getString(R.string.cargoalert_notitle)
    }

    fun gotoSend() {
        var isError = false
        var sMessage = ""

        if (!isError && nloadarea?.getText().isNullOrBlank()) {
            sMessage = resources.getString(R.string.toast_cargoalert_loadareaerror)
            isError = true
        }
        if (!isError && ndownarea?.getText().isNullOrBlank()) {
            sMessage = resources.getString(R.string.toast_cargoalert_downareaerror)
            isError = true
        }
        if (!isError && ncarsize?.getText().isNullOrBlank()) {
            sMessage = resources.getString(R.string.toast_cargoalert_carsizeerror)
            isError = true
        }
        if (!isError && ncartype?.getText().isNullOrBlank()) {
            sMessage = resources.getString(R.string.toast_cargoalert_cartypeerror)
            isError = true
        }
        if (!isError && npaytext?.text.isNullOrBlank()) {
            sMessage = resources.getString(R.string.toast_cargoalert_payerror)
            isError = true
        } else if (npaytext?.text.toString().length > 6) {
            toasts(resources.getString(R.string.toast_cargoalert_paycuterror))
        }

        checkcut()

        if (isError) {
            toasts(sMessage)
        } else {
            val cwnum = Settings.getID(activity!!)
            if (!cwnum.isNullOrBlank()) {
                var loadarea = ""
                var loadcode = ""
                var downarea = ""
                var downcode = ""

                for (i in 0..(arrayLoadarea.size - 1)) {
                    loadarea += if (loadarea.isNullOrBlank()) {
                        arrayLoadarea[i].value
                    } else {
                        "," + arrayLoadarea[i].value
                    }
                    loadcode += if (loadcode.isNullOrBlank()) {
                        arrayLoadarea[i].code
                    } else {
                        "|" + arrayLoadarea[i].code
                    }
                }
                for (i in 0..(arrayDownarea.size - 1)) {
                    downarea += if (downarea.isNullOrBlank()) {
                        arrayDownarea[i].value
                    } else {
                        "," + arrayDownarea[i].value
                    }
                    downcode += if (downcode.isNullOrBlank()) {
                        arrayDownarea[i].code
                    } else {
                        "|" + arrayDownarea[i].code
                    }
                }

                val carsize = ncarsize?.getText()?.replace(" ", "")?.replace(",", "a")
                val cartype = ncartype?.getText()?.replace(" ", "")?.replace(",", "a")

                logd(loadarea + " == " + loadcode + " & " + downarea + " == " + downcode)

                var arrayCarsize = carsize!!.split("a")
                var arrayCartype = cartype!!.split("a")

                var carsizecode = ""
                for (i in 0..(arrayCarsize.size - 1)) {
                    val code = Settings.getCodeKey(activity!!, "ton", arrayCarsize[i].trim())
                    if (code != "") {
                        carsizecode += if (carsizecode.isNullOrBlank()) {
                            ""
                        } else {
                            "|"
                        } + code
                    }
                }

                var cartypecode = ""
                for (i in 0..(arrayCartype.size - 1)) {
                    val code = Settings.getCodeKey(activity!!, "carType", arrayCartype[i].trim())
                    if (code != "") {
                        cartypecode += if (cartypecode.isNullOrBlank()) {
                            ""
                        } else {
                            "|"
                        } + code
                    }
                }

                var mixup = "독차|혼적"
                var loadmode = "당상|낼상|예약"
                val pay = if (npaytext?.text.isNullOrBlank() || npaytext?.text.toString() == "0") {
                    0
                } else {
                    npaytext?.text.toString().replace(",", "").toInt() * 1000
                }
                val idx = if (!arrayData[nstatus].idx.isNullOrBlank()) {
                    arrayData[nstatus].idx
                } else {
                    ""
                }
                val use = if (npushuse2!!.isChecked) {
                    "Y"
                } else {
                    "N"
                }
                val week = "1|2|3|4|5|6|7"
                var time1 = "00:00"
                var time2 = "23:59"
                val loadtime = arrayData[nstatus].loadDate + ":00:00"
                val downtype = arrayData[nstatus].downType

                /*CargoHttp().setPushInfo(
                        cwnum!!,
                        loadarea,
                        loadcode,
                        downarea,
                        downcode,
                        carsize,
                        carsizecode,
                        cartype,
                        cartypecode,
                        mixup,
                        pay.toString(),
                        loadmode,
                        time1,
                        time2,
                        week,
                        idx,
                        use,
                        loadtime,
                        nstatus,
                        downtype,
                        Http,
                        object : DefaultHttp.Callback {
                            override fun onError(
                                    code: DefaultHttp.HttpCode,
                                    message: String,
                                    hcode: String
                            ) {
                                if (DefaultHttp.HttpCode.ERROR_700 != code && DefaultHttp.HttpCode.ERROR_701 != code && DefaultHttp.HttpCode.ERROR_702 != code) {
                                    toasts(resources.getString(R.string.toast_error_server))
                                }
                            }

                            override fun onSuccess(json: Any) {

                                toasts(resources.getString(R.string.toast_cargoalert_send))
                                loadPush()
                            }
                        })
*/
            }
        }
    }

    fun checkcut() {
        var addString = ""
        val mydata = arrayData[nstatus]

        val carsize = mydata.carSize.split(",")
        val carcutsize = ncutton.split("|")

        if (mydata.carSize == resources.getString(R.string.list_carsize0)) {
            for (j in 0..(carcutsize.size - 1)) {
                addString += if (addString != "") {
                    "," + Settings.getCodeValue(activity!!, "ton", carcutsize[j])
                } else {
                    Settings.getCodeValue(activity!!, "ton", carcutsize[j])
                }

                logd(carcutsize[j])
            }
        } else {
            if (carsize.size > 0) {
                for (i in 0..(carsize.size - 1)) {
                    for (j in 0..(carcutsize.size - 1)) {
                        logd(
                                carsize[i] + "==" + Settings.getCodeValue(
                                        activity!!,
                                        "ton",
                                        carcutsize[j]
                                )
                        )
                        if (carsize[i] == Settings.getCodeValue(activity!!, "ton", carcutsize[j])) {
                            addString += if (addString != "") {
                                "," + Settings.getCodeValue(activity!!, "ton", carcutsize[j])
                            } else {
                                Settings.getCodeValue(activity!!, "ton", carcutsize[j])
                            }
                            break
                        }
                    }
                }
            }
        }

        mydata.carSize = addString
        ncarsize?.setText(addString)

        if (mydata.carType == resources.getString(R.string.list_cartype0)) {
            addString = ""

            val setcartype = Settings.getCodeList(activity!!, "carType")
            for (i in 0..(setcartype.size - 1)) {
                if (setcartype[i].value != resources.getString(R.string.list_cartype6)) {
                    addString += if (addString.isNullOrBlank()) {
                        setcartype[i].value
                    } else {
                        "," + setcartype[i].value
                    }
                }
            }
            addString += if (addString.isNullOrBlank()) {
                resources.getString(R.string.list_uptype1)
            } else {
                "," + resources.getString(R.string.list_uptype1)
            }

            mydata.carType = addString
            ncartype?.setText(addString)
        }
    }

    fun changeStatus() {
        val use = if (npushuse!!.isChecked) {
            "Y"
        } else {
            "N"
        }

        val cwnum = Settings.getID(activity!!)
        if (!cwnum.isNullOrBlank()) {
            /*CargoHttp().setPushInfoOnoff(cwnum!!, "", use, Http, object : DefaultHttp.Callback {
                override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                    if (DefaultHttp.HttpCode.ERROR_700 != code && DefaultHttp.HttpCode.ERROR_701 != code && DefaultHttp.HttpCode.ERROR_702 != code) {
                        toasts(resources.getString(R.string.toast_error_server))
                    }
                }

                override fun onSuccess(json: Any) {
                    if (npushuse!!.isChecked) {
                        nblockbox?.visibility = View.GONE
                    } else {
                        nblockbox?.visibility = View.VISIBLE
                    }

                    npushuse2!!.isChecked = npushuse!!.isChecked
                }
            })*/
        }
    }

    fun changeStatus(idx: String) {
        if (!idx.isNullOrBlank()) {
            val use = if (npushuse2!!.isChecked) {
                "Y"
            } else {
                "N"
            }

            val cwnum = Settings.getID(activity!!)
            if (!cwnum.isNullOrBlank()) {
                /*CargoHttp().setPushInfoOnoff(
                        cwnum!!,
                        idx,
                        use,
                        Http,
                        object : DefaultHttp.Callback {
                            override fun onError(
                                    code: DefaultHttp.HttpCode,
                                    message: String,
                                    hcode: String
                            ) {
                                if (DefaultHttp.HttpCode.ERROR_700 != code && DefaultHttp.HttpCode.ERROR_701 != code && DefaultHttp.HttpCode.ERROR_702 != code) {
                                    toasts(resources.getString(R.string.toast_error_server))
                                }
                            }

                            override fun onSuccess(json: Any) {
                                isFirst = true
                                loadPush()
                            }
                        })*/
            }
        }
    }

    fun gotoDelete() {
        val idx = if (!arrayData[nstatus].idx.isNullOrBlank()) {
            arrayData[nstatus].idx
        } else {
            ""
        }
        val cwnum = Settings.getID(activity!!)

        if (!idx.isNullOrBlank()) {
            if (!cwnum.isNullOrBlank()) {
                /*CargoHttp().setPushInfoDelete(cwnum!!, idx, Http, object : DefaultHttp.Callback {
                    override fun onError(
                            code: DefaultHttp.HttpCode,
                            message: String,
                            hcode: String
                    ) {
                        if (DefaultHttp.HttpCode.ERROR_700 != code && DefaultHttp.HttpCode.ERROR_701 != code && DefaultHttp.HttpCode.ERROR_702 != code) {
                            toasts(resources.getString(R.string.toast_error_server))
                        }
                    }

                    override fun onSuccess(json: Any) {
                        toasts(resources.getString(R.string.toast_cargoalert_delete))
                        loadPush()
                    }
                })*/
            }
        } else {
            toasts(resources.getString(R.string.toast_cargoalert_delete_error))
        }

        Etc.hideKeyboard(activity!!)
    }

    override fun onClick(v: View?) {
        if (v?.id != R.id.cargoalert_cutpay) {
            Etc.hideKeyboard(activity!!)
            npaytext?.clearFocus()
        }

        when (v?.id) {
            R.id.header_back -> {
                activity?.finish()
            }
            R.id.cargoalert_onoff -> {
                changeStatus()
            }
            R.id.cargoalert_onoff2 -> {
                changeStatus(arrayData[nstatus].idx)
            }
            R.id.cargoalert_status1 -> {
                nstatus = 1
                nappsettings?.useAlarm = nstatus
                Settings.setAppSetting(activity!!, nappsettings)
                loadPush()
            }
            R.id.cargoalert_status2 -> {
                nstatus = 2
                nappsettings?.useAlarm = nstatus
                Settings.setAppSetting(activity!!, nappsettings)
                loadPush()
            }
            R.id.cargoalert_status3 -> {
                nstatus = 3
                nappsettings?.useAlarm = nstatus
                Settings.setAppSetting(activity!!, nappsettings)
                loadPush()
            }
            R.id.cargoalert_status4 -> {
                nstatus = 4
                nappsettings?.useAlarm = nstatus
                Settings.setAppSetting(activity!!, nappsettings)
                loadPush()
            }
            R.id.cargoalert_status5 -> {
                nstatus = 5
                nappsettings?.useAlarm = nstatus
                Settings.setAppSetting(activity!!, nappsettings)
                loadPush()
            }
            R.id.cargoalert_status6 -> {
                nstatus = 6
                nappsettings?.useAlarm = nstatus
                Settings.setAppSetting(activity!!, nappsettings)
                loadPush()
            }
            R.id.cargoalert_cutpay -> {
                nscroll?.fullScroll(View.FOCUS_DOWN)
            }
            R.id.cargoalert_type1 -> {
                arrayData[nstatus].downType = "all"
            }
            R.id.cargoalert_type2 -> {
                arrayData[nstatus].downType = resources.getString(R.string.cargoalert_type2)
            }
            R.id.cargoalert_type3 -> {
                arrayData[nstatus].downType = resources.getString(R.string.cargoalert_type3)
            }
            R.id.cargoalert_carsize -> {
                val dialog = CargoSettingDialog(nactivity, CargoSettingDialog.TYPE.CARSIZE, "", "", "", arrayData[nstatus].carSize, object : CargoSettingDialog.Callback {
                    override fun onNo() {

                    }

                    override fun onYes(data: JSONObject) {
                        try {
                            arrayData[nstatus].carSize = data.getString("printtitle").replace(", ", ",")
                            (v as ArrowButton).setText(data.getString("printtitle"))
                        } catch (e: Exception) {

                        }
                    }
                })

                dialog.show()
            }
            R.id.cargoalert_cartype -> {
                val dialog = CargoSettingDialog(nactivity, CargoSettingDialog.TYPE.CARTYPE, "cargoalert", "", "", arrayData[nstatus].carType, object : CargoSettingDialog.Callback {
                    override fun onNo() {

                    }

                    override fun onYes(data: JSONObject) {
                        try {
                            arrayData[nstatus].carType = data.getString("printtitle").replace(", ", ",")
                            (v as ArrowButton).setText(data.getString("printtitle"))
                        } catch (e: Exception) {

                        }
                    }
                })

                dialog.show()
            }
            R.id.cargoalert_loaddate, R.id.cargoalert_loadhour -> {
                val loaddate = Etc.convertStringToDate(arrayData[nstatus].loadDate, "yyyy-MM-dd HH")

                logd(loaddate.toString())

                val now         = Date()
                val maxdate     = Etc.addYear(now, 2)
                val mindate     = if(now.time < loaddate!!.time) { now } else { loaddate }

                val dialog = DatePickerDialog(activity!!, DatePickerDialog.TYPE.HOUR, resources.getString(R.string.cargoalert_title10), loaddate, mindate, maxdate, object : DatePickerDialog.Callback {
                    override fun onNo() {

                    }

                    override fun onYes(date: Date) {
                        arrayData[nstatus].loadDate = Etc.convertDateToString(date, "yyyy-MM-dd HH")
                        nloaddate?.text     = Etc.convertDateToString(date, "yyyy.MM.dd")
                        nloadhour?.text     = Etc.convertDateToString(date, "HH")
                    }
                })
                dialog.show()
            }
            R.id.cargoalert_loadarea -> {
                val dialog = AddressDialog(activity, AddressDialog.TYPE.ALERTSELECT, resources.getString(R.string.cargoalert_title1), 4, arrayLoadarea, object: AddressDialog.Callback {
                    override fun onNo() {

                    }

                    override fun onYes(data: JSONObject) {
                        logd(data.toString())
                        try {
                            val code = data.getString("code").split(",")
                            val text = data.getString("text").split(",")

                            arrayLoadarea = ArrayList<Code>()

                            for(i in 0..(code.size - 1)) {
                                arrayLoadarea.add(Code(code[i], text[i]))
                            }
                            arrayData[nstatus].loadArea = data.getString("text")
                            nloadarea?.setText(arrayData[nstatus].loadArea)
                        } catch (e: Exception) {

                        }
                    }
                })
                dialog.show()
            }
            R.id.cargoalert_downarea -> {
                val dialog = AddressDialog(activity, AddressDialog.TYPE.ALERTSELECT, resources.getString(R.string.cargoalert_title2), 4, arrayDownarea, object: AddressDialog.Callback {
                    override fun onNo() {

                    }

                    override fun onYes(data: JSONObject) {
                        logd(data.toString())
                        try {
                            val code = data.getString("code").split(",")
                            val text = data.getString("text").split(",")

                            arrayDownarea = ArrayList<Code>()

                            for(i in 0..(code.size - 1)) {
                                arrayDownarea.add(Code(code[i], text[i]))
                            }

                            arrayData[nstatus].downArea = data.getString("text")
                            ndownarea?.setText(arrayData[nstatus].downArea)
                        } catch (e: Exception) {

                        }
                    }
                })
                dialog.show()
            }
            R.id.cargoalert_save -> {
                gotoSend()
            }
            R.id.cargoalert_reset -> {
                gotoDelete()
            }
        }
    }
}