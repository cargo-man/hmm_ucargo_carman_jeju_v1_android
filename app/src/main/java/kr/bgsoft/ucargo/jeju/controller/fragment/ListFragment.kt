package kr.bgsoft.ucargo.jeju.controller.fragment

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.hardware.SensorEvent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.SubActivity
import kr.bgsoft.ucargo.jeju.UCargoActivity
import kr.bgsoft.ucargo.jeju.controller.adapter.CargoListAdapter
import kr.bgsoft.ucargo.jeju.controller.conf.Config
import kr.bgsoft.ucargo.jeju.controller.conf.Settings
import kr.bgsoft.ucargo.jeju.controller.dialog.CargoSettingDialog
import kr.bgsoft.ucargo.jeju.controller.dialog.DaySortDialog
import kr.bgsoft.ucargo.jeju.controller.dialog.EtcSettingDialog
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment
import kr.bgsoft.ucargo.jeju.data.model.*
import kr.bgsoft.ucargo.jeju.data.sqlite.IsRead
import kr.bgsoft.ucargo.jeju.utils.Etc
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.ceil

class ListFragment: DefaultFragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    var nactivity: SubActivity? = null
    var nswipe: SwipeRefreshLayout? = null
    var nempty: RelativeLayout? = null
    var nemptytext: TextView? = null
    var nadapter: CargoListAdapter? = null
    var nprogress: ProgressBar? = null
    var nloadtext: TextView? = null
    var ndowntext: TextView? = null
    var npointtext: TextView? = null
    var nmixup: Int = 0
    var nlist_view: ListView? = null
    var ncontrol: ImageButton? = null
    var isAuto = false
    var isLoading = false

    var appsetting = AppSetting()
    var isNight = false
    var nuserinfo = UserInfo()
    var nid = ""

    var nlasttime = 0L
    var nlastx = 0F
    var nlastz = 0F
    var nlasty = 0F
    var SHAKE_THRESHOLD = 900
    var duration = 30000

    var ani_progress: ObjectAnimator? = null
    var npos = 0
    var nlisttop = 0
    var isDuration = true
    var npage = 1
    var nlastordernum = ""
    var lastitemVisibleFlag = false
    var listResultEmpty = false
    var nHandler: Handler? = null
    lateinit var nRunnable: Runnable/*? = null*/
    var nRead: IsRead? = null

    companion object {
        var isReload = false
        var nSearch = CargoSearch("", "", "", "", "", "", "", "", "", "", "")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_list, container, false)

        nactivity = activity as SubActivity

        appsetting = Settings.getAppSetting(activity!!)
        isNight = Etc.getNight(appsetting)
        nuserinfo = Settings.getInfo(activity!!)
        nid = Settings.getID(activity!!)

        duration = when (appsetting.refreshTime) {
            1 -> {
                3000
            }
            2 -> {
                5000
            }
            3 -> {
                10000
            }
            4 -> {
                20000
            }
            else -> {
                30000
            }
        }

        val view_title = layout.findViewById<TextView>(R.id.header_title)
        val view_back = layout.findViewById<ImageButton>(R.id.header_back)
        val view_speak = layout.findViewById<Button>(R.id.list_speak)
        val view_reload = layout.findViewById<Button>(R.id.list_reload)
        val view_load = layout.findViewById<Button>(R.id.list_loadselect)
        val view_down = layout.findViewById<TextView>(R.id.list_downselect)
        val view_carsize = layout.findViewById<Button>(R.id.list_carsize)
        val view_cartype = layout.findViewById<Button>(R.id.list_cartype)
        val view_mixup = layout.findViewById<Button>(R.id.list_mixup)
        val view_etcsetting = layout.findViewById<Button>(R.id.list_etcsetting)

        //npointtext          = layout.findViewById<TextView>(R.id.list_point)
        nprogress = layout.findViewById(R.id.list_progress)
        nswipe = layout.findViewById(R.id.board2_swipe)
        nempty = layout.findViewById(R.id.board2_empty)
        nemptytext = layout.findViewById(R.id.board2_empty_text)
        ndowntext = layout.findViewById(R.id.list_downselect_text)
        nloadtext = layout.findViewById(R.id.list_loadselect_text)
        ncontrol = layout.findViewById(R.id.list_progress_control)
        nlist_view = layout.findViewById(R.id.board2_list)
        nadapter = CargoListAdapter(nactivity!!)

        nHandler = Handler()

        nRunnable = Runnable {

        }
        nswipe?.setOnRefreshListener(this)

        nswipe?.setColorSchemeResources(
                R.color.colorListPay1,
                R.color.colorListPay2,
                R.color.colorListPay3,
                R.color.colorListPay4
        )

        nlist_view?.adapter = nadapter

        nlist_view?.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    run {
                        val mydata = nadapter!!.getItem(position)
                        var isLoad = true
                        var isNicedata = false
                        val niceid = Settings.getNiceID(activity!!)

                        if (mydata.sPayment == resources.getString(R.string.list_pay7_code) || mydata.sPayment == resources.getString(
                                        R.string.list_pay7
                                )
                        ) {
                            if (!nuserinfo.bzNum.isNullOrBlank()) {
                                try {
                                    val checkNo = niceid.getString("checkNo")

                                    //팝업안내추가
                                    if (checkNo != nuserinfo.bzNum) {
                                        isNicedata = true
                                    }
                                } catch (e: Exception) {
                                    isNicedata = true
                                }
                            } else {
                                toasts(resources.getString(R.string.toast_list_nobiznum))
                                isLoad = false
                            }
                        }

                        if (isNicedata) {
                            /*NiceData().getUserInfo(
                                    nuserinfo.bzNum!!,
                                    Http,
                                    object : DefaultHttp.Callback {
                                        override fun onError(
                                                code: DefaultHttp.HttpCode,
                                                message: String,
                                                hcode: String
                                        ) {
                                            if (code == DefaultHttp.HttpCode.ERROR_201) {
                                                val dialog = AlertDialog(
                                                        nactivity,
                                                        AlertDialog.TYPE.CARDJOIN,
                                                        resources.getString(R.string.dialog_cardjoin_title),
                                                        resources.getString(R.string.dialog_cardjoin_info),
                                                        object : AlertDialog.Callback {
                                                            override fun onNo() {

                                                            }

                                                            override fun onYes() {
                                                                UCargoActivity.showWebView(
                                                                        resources.getString(R.string.url_cardjoin),
                                                                        resources.getString(R.string.title_cardjoinweb)
                                                                )
                                                            }
                                                        })

                                                dialog.show()
                                            } else {
                                                try {
                                                    toasts(resources.getString(R.string.toast_error_server))
                                                } catch (e: Exception) {

                                                }
                                            }
                                        }

                                        override fun onSuccess(json: Any) {
                                            val data = json as JSONObject
                                            data.put("checkNo", nuserinfo.bzNum)
                                            Settings.setNiceID(activity!!, data)
                                            showView(mydata)
                                        }
                                    })
*/
                            isLoad = false
                        }

                        if (isLoad) {
                            showView(mydata)
                        }
                    }
                }

        nlist_view?.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    npage++
                    loadList()
                }
            }

            override fun onScroll(
                    view: AbsListView,
                    firstVisibleItem: Int,
                    visibleItemCount: Int,
                    totalItemCount: Int
            ) {
                lastitemVisibleFlag =
                        totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount
            }
        })

        nSearch = Settings.getSearch(activity!!)

        nlist_view?.dividerHeight = 0


        logd("duration :: " + duration.toString())

        nempty?.visibility = View.GONE
        view_title.text = resources.getString(R.string.title_list)
        val point = nuserinfo.point
        //npointtext?.text = nactivity?.Etc?.setComma(point!!.toInt())

        view_back.setOnClickListener(this)
        view_speak.setOnClickListener(this)
        view_reload.setOnClickListener(this)
        view_load.setOnClickListener(this)
        view_down.setOnClickListener(this)
        view_carsize.setOnClickListener(this)
        view_cartype.setOnClickListener(this)
        view_mixup.setOnClickListener(this)
        view_etcsetting.setOnClickListener(this)
        ncontrol?.setOnClickListener(this)

        var setduration = duration
        isDuration = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val durationScale = android.provider.Settings.Global.getFloat(
                    context?.contentResolver,
                    android.provider.Settings.Global.ANIMATOR_DURATION_SCALE,
                    1.0f
            )
            if (durationScale != 0f) {
                setduration = (setduration * (1f / durationScale)).toInt()
            } else {
                isDuration = false
            }

            logd("durationScale :: $durationScale")
        }

        nprogress?.max = setduration
        nprogress?.progress = 0

        logd("nprogress?.max duration :: " + nprogress?.max.toString())

        ani_progress = ObjectAnimator.ofInt(nprogress, "progress", setduration)
        ani_progress?.duration = setduration.toLong()
        ani_progress?.interpolator = DecelerateInterpolator() as TimeInterpolator?
        ani_progress?.addUpdateListener { animation ->
            //logd(animation?.animatedValue.toString() + " == " + setduration)
            if (animation?.animatedValue == setduration) {
                //onRefresh()
            }
        }

        logd(
                if (appsetting.isRefresh) {
                    "true"
                } else {
                    "false"
                }
        )

        if (appsetting.isRefresh && isDuration) {
            isAuto = true
            ncontrol?.setImageResource(R.drawable.ico_pause)
        } else {
            isAuto = false
            ncontrol?.setImageResource(R.drawable.ico_play)
            onRefresh()

            if (!isDuration) {
                ncontrol?.visibility = View.GONE
            }
        }


        if (!nSearch.condition2print.isNullOrBlank()) {
            view_cartype.text = nSearch.condition2print
        }
        if (!nSearch.condition3print.isNullOrBlank()) {
            view_carsize.text = nSearch.condition3print
        }
        if (!nSearch.loadareaprint.isNullOrBlank()) {
            nloadtext?.text = nSearch.loadareaprint
        }
        if (!nSearch.downareaprint.isNullOrBlank()) {
            ndowntext?.text = nSearch.downareaprint
        }

        logd(
                "progressshow :: " + if (appsetting.isProgressShow) {
                    "true"
                } else {
                    "false"
                }
        )

        if (appsetting.isProgressShow || !isDuration) {
            nprogress?.visibility = View.GONE
        } else {
            nprogress?.visibility = View.VISIBLE
        }

        if (isNight) {
            nempty!!.setBackgroundResource(R.color.colorUser22)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                nemptytext!!.setTextColor(resources.getColor(R.color.colorUser23, null))
            } else {
                nemptytext!!.setTextColor(resources.getColor(R.color.colorUser23))
            }
            nlist_view!!.setBackgroundResource(R.color.colorUser22)
        }

        isReload = true

        val arrayType1 = resources.getStringArray(R.array.daysort_type1)
        val arrayType1Code = resources.getStringArray(R.array.daysort_type1_code)
        val arrayType2 = resources.getStringArray(R.array.daysort_type2)
        val arrayType2Code = resources.getStringArray(R.array.daysort_type2_code)

        var loadtime = ""
        for (i in 0..(arrayType1.size - 1)) {
            if (nSearch.loadtime.toString() == arrayType1Code[i]) {
                loadtime = arrayType1[i]
                break
            }
        }

        var downtime = ""
        for (i in 0..(arrayType2.size - 1)) {
            if (nSearch.downtime.toString() == arrayType2Code[i]) {
                downtime = arrayType2[i]
                break
            }
        }

        nRead = IsRead(context, Config.DB_Reads, null, Config.DB_Reads_ver)
        val isCut = nRead?.select(1003) as Boolean

        if (!isCut) {
            view_mixup.text = resources.getString(R.string.listsetting_loaddown)
        } else {
            view_mixup.text = loadtime + "/" + downtime
        }

        listResultEmpty = false
        nlastordernum = ""

        return layout
    }

    fun showView(mydata: Cargo) {

        if (mydata.sGoodsWeight.isNullOrBlank()) {
            nswipe?.isRefreshing = true

            /*CargoHttp().getView(mydata.nOrderNum, Http, object : DefaultHttp.Callback {
                override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                    try {
                        toasts(resources.getString(R.string.toast_error_server))
                    } catch (e: Exception) {

                    }
                    nswipe?.isRefreshing = false
                }

                override fun onSuccess(json: Any) {
                    val data = json as JSONObject
                    mydata.sCarType = data.getString("sCarType")
                    mydata.sPayment = data.getString("sPayment")
                    mydata.nPay = data.getInt("nPay")
                    mydata.nGoodbad = data.getInt("nGoodbad")
                    mydata.nFee = data.getInt("nFee")
                    mydata.sGoodsWeight = data.getString("sGoodsWeight")
                    mydata.nDistance = data.getString("nDistance")
                    mydata.dDistance = if(mydata.nDistance.isNullOrBlank() || mydata.nDistance == "0") {
                        0
                    } else {
                        if(mydata.nDistance.toDouble() > 1000) {
                            ceil(mydata.nDistance.toDouble() / 1000).toInt()
                        } else {
                            ceil(mydata.nDistance.toDouble()).toInt()
                        }
                    }

                    logd(mydata.nDistance + " ==> " + mydata.dDistance)
                    mydata.sDownLoc = data.getString("sDownLoc")
                    mydata.sLoadDay = data.getString("sLoadDay")
                    mydata.sETC = data.getString("sETC")
                    mydata.sLoadLoc = data.getString("sLoadLoc")
                    mydata.sDownMethod = data.getString("sDownMethod")
                    mydata.sGoodsInfo = data.getString("sGoodsInfo")
                    mydata.sLoadtype = data.getString("sLoadtype")
                    mydata.sLoadMethod = data.getString("sLoadMethod")
                    mydata.sDownDay = data.getString("sDownDay")
                    mydata.sCarton = data.getString("sCarton")
                    mydata.sGoodState = data.getString("sGoodState")
                    mydata.sLoadLat = if (data.getString("nLoadLat").isNullOrBlank()) {
                        0.0
                    } else {
                        data.getString("nLoadLat").toDouble()
                    }
                    mydata.sLoadLng = if (data.getString("nLoadLng").isNullOrBlank()) {
                        0.0
                    } else {
                        data.getString("nLoadLng").toDouble()
                    }
                    mydata.sDownLat = if (data.getString("nDownLat").isNullOrBlank()) {
                        0.0
                    } else {
                        data.getString("nDownLat").toDouble()
                    }
                    mydata.sDownLng = if (data.getString("nDownLng").isNullOrBlank()) {
                        0.0
                    } else {
                        data.getString("nDownLng").toDouble()
                    }
                    mydata.sRegDate = data.getString("sRegdate")

                    npos = nlist_view!!.firstVisiblePosition
                    val v = nlist_view!!.getChildAt(0)
                    nlisttop = if (v == null) 0 else v!!.getTop()

                    UCargoActivity.showListView(mydata)

                    nswipe?.isRefreshing = false
                }
            })*/
        } else {
            UCargoActivity.showListView(mydata)
        }
    }

    fun loadList() {
        val cwnum = nid

        if (cwnum != null && !isLoading) {
            nHandler?.removeCallbacks(nRunnable!!)
            nRunnable = Runnable {
                onRefresh()
            }

            if(npage >= 5) {
                listResultEmpty         = true
                nswipe?.isRefreshing    = false
            } else {
                if (!listResultEmpty) {
                    isLoading = true
                    nswipe?.isRefreshing = true

                    if (nSearch.condition1.isNullOrBlank()) {
                        nSearch = Settings.getSearch(activity!!)
                    }

                    var condition2 = nSearch.condition2

                    if(nSearch.condition2.indexOf(resources.getString(R.string.list_cartype12_code)) > -1) {
                        condition2 = condition2.replace(resources.getString(R.string.list_cartype12_code), resources.getString(R.string.list_cartype1_code))
                    }

                    /*CargoHttp().getList(cwnum, nSearch.condition1, condition2, nSearch.condition3, nSearch.loadarea1, nSearch.loadarea2, nSearch.loadarea3, nSearch.loadareatext,
                            nSearch.downarea1, nSearch.downarea2, nSearch.downarea3, nSearch.downareatext, nSearch.loadtime, nSearch.downtime, nSearch.sortup, npage, Http, object : DefaultHttp.Callback {
                        override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                            try {
                                try {
                                    toasts(resources.getString(R.string.toast_error_server))
                                } catch (e: Exception) {

                                }

                                nswipe?.isRefreshing = false
                                isLoading = false

                                if (isAuto) {
                                    nprogress?.progress = 0
                                    ani_progress?.start()
                                    nHandler?.postDelayed(nRunnable!!, duration.toLong())
                                }
                            } catch (e: Exception) {

                            }
                        }

                        override fun onSuccess(json: Any) {
                            val data = json as JSONArray

                            logd("length() : " + data.length())

                            if (data.length() > 0) {
                                if (npage == 1) {
                                    nadapter?.clear()
                                }

                                for (i in 0..(data.length() - 1)) {
                                    val field = data.getJSONObject(i)

                                    try {
                                        var nOrderNum = field.getString("nOrderNum")
                                        var nDistance = field.getString("nDistance")
                                        var sDownLoc = field.getString("sDownLoc")
                                        var sLoadDay = field.getString("sLoadDay")
                                        var sLoadTime = field.getString("sLoadTime")
                                        var sCarType = field.getString("sCarType")
                                        var sLoadLoc = field.getString("sLoadLoc")
                                        var sPayment = field.getString("sPayment")
                                        var nPay = field.getInt("nPay")
                                        var nFee = field.getInt("nFee")
                                        var sLoadType = field.getString("sLoadtype")
                                        var sCarton = field.getString("sCarton")
                                        var sGoodsInfo = field.getString("sGoodsInfo")
                                        var sDownDay = field.getString("sDownDay")

                                        if(nadapter != null) {
                                            if (!nadapter!!.checkOrdernum(nOrderNum)) {
                                                nadapter?.addData(nOrderNum, nDistance, sDownLoc, (sLoadDay + " " + sLoadTime + ":00"), sCarType, sLoadLoc, sPayment, nPay, nFee, sLoadType, sCarton, sGoodsInfo, sDownDay)
                                                nlastordernum = nOrderNum
                                            }
                                        }
                                    } catch (e: Exception) {
                                        logd(e.toString())
                                    }
                                }

                                nadapter?.dataChange()
                                if (npage == 1) {
                                    nlist_view?.setSelectionAfterHeaderView()
                                    nempty?.visibility = View.GONE
                                }
                            } else {
                                if (npage == 1) {
                                    nadapter?.clear()
                                    nempty?.visibility = View.VISIBLE
                                } else {
                                    listResultEmpty = true
                                    nswipe?.isRefreshing    = false
                                }
                            }

                            if (ani_progress!!.isRunning) {
                                ani_progress?.cancel()
                            }

                            if (isAuto) {
                                nprogress?.progress = 0
                                ani_progress?.start()
                                nHandler?.postDelayed(nRunnable!!, duration.toLong())
                            }

                            nswipe?.isRefreshing = false
                            isLoading = false
                        }
                    })*/
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        //nSearch = App.getSearch()

        //val point = App.userInfo?.point
        //npointtext?.text = nactivity?.Etc?.setComma(point!!.toInt())

        duration    = when(appsetting.refreshTime) {
            1 -> { 3000 }
            2 -> { 5000 }
            3 -> { 10000 }
            4 -> { 20000 }
            else -> { 30000 }
        }

        if(isReload) {
            isReload = false
            onRefresh()
        } else if(ani_progress != null && isAuto) {
            if (!ani_progress!!.isRunning && appsetting.isRefresh) {
                nprogress?.progress = 0
                ani_progress?.start()
                nRunnable = Runnable {
                    onRefresh()
                }
                nHandler?.postDelayed(nRunnable!!, duration.toLong())
            }
        }

        nlist_view?.setSelectionFromTop(npos, nlisttop)
    }

    override fun onPause() {
        Settings.setSearch(activity!!, nSearch)
        if(ani_progress != null) {
            ani_progress!!.cancel()
            nHandler?.removeCallbacks(nRunnable!!)
        }
        super.onPause()
    }

    override fun onDestroy() {
        if(ani_progress != null) {
            if(ani_progress!!.isRunning) {
                ani_progress!!.cancel()
                nHandler?.removeCallbacks(nRunnable!!)
            }
        }

        super.onDestroy()
    }


    fun onSensorChanged(event: SensorEvent?) {

        if(appsetting.isSensor && event != null) {
            val currenttime = System.currentTimeMillis()
            val gabtime     = currenttime - nlasttime

            if(gabtime > 200) {
                nlasttime = currenttime

                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val speed = Math.abs(x + y + z - nlastx - nlasty - nlastz) / gabtime * 10000

                //logd("onSensorChanged call $speed")

                if (speed > SHAKE_THRESHOLD) {
                    loadList()
                }

                nlastx = x
                nlasty = y
                nlastz = z
            }
        }
    }

    override fun onRefresh() {
        npage           = 1
        listResultEmpty = false
        nlastordernum   = ""
        loadList()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.header_back -> {
                nactivity?.finish()
            }
            R.id.list_reload -> {
                onRefresh()
            }
            R.id.list_loadselect -> {
                val dialog = CargoSettingDialog(nactivity, CargoSettingDialog.TYPE.LOAD, nSearch.loadarea1, nSearch.loadarea2, nSearch.loadarea3, nSearch.loadareatext, object : CargoSettingDialog.Callback{
                    override fun onNo() {
                        if(isAuto) {
                            ani_progress?.start()
                            nRunnable = Runnable {
                                onRefresh()
                            }
                            nHandler?.postDelayed(nRunnable!!, duration.toLong())
                        }
                    }

                    override fun onYes(data: JSONObject) {
                        logd(data.toString())
                        try {
                            nSearch.loadarea1 = data.getString("nloadarea1")
                            nSearch.loadarea2 = data.getString("nloadarea2")
                            nSearch.loadarea3 = data.getString("nloadarea3")
                            nSearch.loadareatext = data.getString("nloadareatext")
                            nSearch.loadareaprint = data.getString("printtitle")
                            nloadtext?.text = data.getString("printtitle")

                            Settings.setSearch(activity!!, nSearch)
                            loadList()
                        } catch (e: Exception) {

                        }
                    }
                })

                dialog.show()

                if(ani_progress != null) {
                    if(ani_progress!!.isRunning) {
                        ani_progress!!.cancel()
                        nHandler?.removeCallbacks(nRunnable!!)
                    }
                }

                nHandler?.removeCallbacks(nRunnable!!)
            }
            R.id.list_downselect -> {
                val dialog = CargoSettingDialog(nactivity, CargoSettingDialog.TYPE.DOWN, nSearch.downarea1, nSearch.downarea2, nSearch.downarea3, nSearch.downareatext, object : CargoSettingDialog.Callback{
                    override fun onNo() {
                        if(isAuto) {
                            ani_progress?.start()
                            nRunnable = Runnable {
                                onRefresh()
                            }
                            nHandler?.postDelayed(nRunnable!!, duration.toLong())
                        }
                    }

                    override fun onYes(data: JSONObject) {
                        logd(data.toString())
                        try {
                            nSearch.downarea1 = data.getString("ndownarea1")
                            nSearch.downarea2 = data.getString("ndownarea2")
                            nSearch.downarea3 = data.getString("ndownarea3")
                            nSearch.downareatext = data.getString("ndownareatext")
                            nSearch.downareaprint = data.getString("printtitle")
                            ndowntext?.text = data.getString("printtitle")

                            Settings.setSearch(activity!!, nSearch)
                            loadList()
                        } catch (e: Exception) {

                        }
                    }
                })

                dialog.show()

                if(ani_progress != null) {
                    if(ani_progress!!.isRunning) {
                        ani_progress!!.cancel()
                    }
                }

                nHandler?.removeCallbacks(nRunnable!!)
            }
            R.id.list_cartype -> {
                logd("car type : " + nSearch.condition2)
                val dialog = CargoSettingDialog(nactivity, CargoSettingDialog.TYPE.CARTYPE, "", "", "cut", nSearch.condition2, object : CargoSettingDialog.Callback{
                    override fun onNo() {
                        if(isAuto) {
                            ani_progress?.start()
                            nRunnable = Runnable {
                                onRefresh()
                            }
                            nHandler?.postDelayed(nRunnable!!, duration.toLong())
                        }
                    }

                    override fun onYes(data: JSONObject) {
                        logd(data.toString())
                        try {
                            nSearch.condition2 = data.getString("ncondition2")
                            nSearch.condition2print = data.getString("printnik")
                            (v as Button).text = data.getString("printnik")

                            Settings.setSearch(activity!!, nSearch)
                            loadList()
                        } catch (e: Exception) {

                        }
                    }
                })

                dialog.show()

                if(ani_progress != null) {
                    if(ani_progress!!.isRunning) {
                        ani_progress!!.cancel()
                    }
                }

                nHandler?.removeCallbacks(nRunnable!!)
            }
            R.id.list_carsize -> {
                logd("car size : " + nSearch.condition3)
                val dialog = CargoSettingDialog(nactivity, CargoSettingDialog.TYPE.CARSIZE, "", "", "cut", nSearch.condition3, object : CargoSettingDialog.Callback{
                    override fun onNo() {
                        if(isAuto) {
                            ani_progress?.start()
                            nRunnable = Runnable {
                                onRefresh()
                            }
                            nHandler?.postDelayed(nRunnable!!, duration.toLong())
                        }
                    }

                    override fun onYes(data: JSONObject) {
                        logd(data.toString())
                        try {
                            nSearch.condition3 = data.getString("ncondition3")
                            nSearch.condition3print = data.getString("printnik")
                            (v as Button).text = data.getString("printnik")

                            Settings.setSearch(activity!!, nSearch)
                            loadList()
                        } catch (e: Exception) {

                        }
                    }
                })

                dialog.show()

                if(ani_progress != null) {
                    if(ani_progress!!.isRunning) {
                        ani_progress!!.cancel()
                    }
                }

                nHandler?.removeCallbacks(nRunnable!!)
            }
            R.id.list_mixup -> {
                nRead?.insert(1003, 1)
                val dialog = DaySortDialog(nactivity, nSearch.loadtime, nSearch.downtime, object : DaySortDialog.Callback{
                    override fun onNo() {
                        if(isAuto) {
                            ani_progress?.start()
                            nRunnable = Runnable {
                                onRefresh()
                            }
                            nHandler?.postDelayed(nRunnable!!, duration.toLong())
                        }
                    }

                    override fun onYes(loadtime: Code, downtime: Code) {

                        logd("loadtime :: " + loadtime.toString() + " / downtime :: " + downtime.toString())
                        logd("loadtime :: " + loadtime.code.toInt() + " / downtime :: " + downtime.code.toInt())

                        nSearch.loadtime = loadtime.code.toInt()
                        nSearch.downtime = downtime.code.toInt()

                        (v as Button).text = loadtime.value + "/" + downtime.value
                        Settings.setSearch(activity!!, nSearch)
                        loadList()
                    }
                })

                dialog.show()

                if(ani_progress != null) {
                    if(ani_progress!!.isRunning) {
                        ani_progress!!.cancel()
                    }
                }

                nHandler?.removeCallbacks(nRunnable!!)
            }
            R.id.list_etcsetting -> {
                val dialog = EtcSettingDialog(nactivity, nSearch.condition1, nSearch.sortup,  object : EtcSettingDialog.Callback {
                    override fun onNo() {
                        if(isAuto) {
                            ani_progress?.start()
                            nRunnable = Runnable {
                                onRefresh()
                            }
                            nHandler?.postDelayed(nRunnable!!, duration.toLong())
                        }
                    }

                    override fun onYes(mixup: Code, sort: Code) {
                        nSearch.condition1  = mixup.code
                        nSearch.sortup      = sort.code

                        Settings.setSearch(activity!!, nSearch)
                        onRefresh()
                    }
                })

                dialog.show()

                if(ani_progress != null) {
                    if(ani_progress!!.isRunning) {
                        ani_progress!!.cancel()
                    }
                }

                nHandler?.removeCallbacks(nRunnable!!)
            }
            R.id.list_progress_control -> {
                if(ani_progress != null && appsetting.isRefresh) {
                    if(isAuto || ani_progress!!.isRunning) {
                        isAuto = false

                        ani_progress!!.cancel()
                        nprogress?.progress = 0

                        nHandler?.removeCallbacks(nRunnable!!)

                        ncontrol?.setImageResource(R.drawable.ico_play)
                    } else {
                        isAuto = true
                        ani_progress!!.start()

                        nRunnable = Runnable {
                            onRefresh()
                        }
                        nHandler?.postDelayed(nRunnable!!, duration.toLong())

                        ncontrol?.setImageResource(R.drawable.ico_pause)
                    }
                }
            }
        }
    }
}