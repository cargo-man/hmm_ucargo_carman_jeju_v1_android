package kr.bgsoft.ucargo.jeju

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.iid.FirebaseInstanceId
import kr.bgsoft.ucargo.jeju.controller.adapter.MainBoardAdapter
import kr.bgsoft.ucargo.jeju.controller.conf.Config
import kr.bgsoft.ucargo.jeju.controller.conf.Settings
import kr.bgsoft.ucargo.jeju.controller.dialog.AlertDialog
import kr.bgsoft.ucargo.jeju.controller.dialog.NoticeDialog
import kr.bgsoft.ucargo.jeju.controller.http.AppHttp
import kr.bgsoft.ucargo.jeju.controller.http.DefaultHttp
import kr.bgsoft.ucargo.jeju.controller.http.MemberHttp
import kr.bgsoft.ucargo.jeju.cview.DefaultActivity
import kr.bgsoft.ucargo.jeju.data.model.*
import kr.bgsoft.ucargo.jeju.data.sqlite.Infos
import kr.bgsoft.ucargo.jeju.data.sqlite.IsRead
import kr.bgsoft.ucargo.jeju.utils.Etc
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.system.exitProcess

@Suppress("DEPRECATION")
class UCargoActivity : DefaultActivity(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    var nRead: IsRead? = null
    var nNotice: kr.bgsoft.ucargo.jeju.data.sqlite.Notice? = null
    var noti_type = ""
    var noti_idx = 0
    var noti_data = Cargo()

    var nadapter: MainBoardAdapter? = null
    var nboardtype = 1
    var nlistheight = 0
    var nswipe: SwipeRefreshLayout? = null
    var nempty: RelativeLayout? = null
    var nemptytext: TextView? = null
    var noti1_count = 0
    var noti2_count = 0

    var isStop = false

    companion object {
        var isSplash = false
        var isFinish = false
        var isLogin = false
        var nactivity: DefaultActivity? = null
        var nactivity_list: Activity? = null
        var lservice: Intent? = null
        var isAlertDialog = false
        var nhandler: Handler? = null

        fun showSplash() {
            isSplash = true
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.SPLASH)
            nactivity?.startActivityForResult(intent, Config.RESULT_SPLASH)
        }
        fun showLogin() {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.LOGIN)
            nactivity?.startActivityForResult(intent, Config.RESULT_LOGIN)
        }

        fun showJoin() {
           /* val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.JOIN)
            nactivity?.startActivityForResult(intent, Config.RESULT_JOIN)*/
        }

        fun showCall() {
            val intent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:" + nactivity!!.resources.getString(R.string.app_tel))
            )
            nactivity?.startActivity(intent)
        }

        fun showCall(telnum: String) {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telnum))
            nactivity?.startActivity(intent)
        }

        fun showHHPage(type: SubActivity.TYPE) {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, type)
            nactivity?.startActivity(intent)
        }

        fun showMapDialog(title: String, location: kr.bgsoft.ucargo.jeju.data.model.Location) {
           /* val intent = Intent(nactivity, MapActivity::class.java)
            intent.putExtra(MapActivity.EXTRA_TYPE, MapActivity.TYPE.DIALOG)
            intent.putExtra(MapActivity.EXTRA_TITLE, title)
            intent.putExtra(MapActivity.EXTRA_LOC, location)
            nactivity?.startActivity(intent)*/
        }

        fun showBoard(type: SubActivity.TYPE) {
            showBoard(type, 1)
        }

        fun showBoard(type: SubActivity.TYPE, sub: Int) {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, type)
            intent.putExtra(Config.EXTRA_DATA, sub)
            nactivity?.startActivity(intent)
        }

        fun showBoardView(mydata: Board) {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.BOARDVIEW)
            intent.putExtra(Config.EXTRA_ORDER, mydata)
            nactivity?.startActivity(intent)
        }

        fun showPartner() {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.PARTNER)
            nactivity?.startActivity(intent)
        }

        fun showBrowser(url: String) {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            nactivity?.startActivity(intent)
        }

        fun showSetting() {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.SETTING)
            nactivity?.startActivity(intent)
        }

        fun showMypage() {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.MYPAGE)
            nactivity?.startActivity(intent)
        }

        fun showPassword() {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.PASSWORD)
            nactivity?.startActivity(intent)
        }

        fun showMyBank() {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.BANK)
            nactivity?.startActivityForResult(intent, Config.RESULT_BANK)
        }

        fun showEvent() {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.EVENT)
            nactivity?.startActivity(intent)
        }

        fun showAlarm() {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.ALARM)
            nactivity?.startActivity(intent)
        }

        fun showMyAddView(mydata: Cargo) {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.MYADDVIEW)
            intent.putExtra(Config.EXTRA_ORDER, mydata)
            nactivity?.startActivity(intent)
        }

        fun showMyAdd() {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.MYADD)
            nactivity?.startActivity(intent)
        }

        fun showMyAddCopy(data: Cargo) {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.MYADD)
            intent.putExtra(Config.EXTRA_ORDER, data)
            nactivity?.startActivity(intent)
        }

        fun showMyAddEdit(data: Cargo) {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.MYADDEDIT)
            intent.putExtra(Config.EXTRA_ORDER, data)
            nactivity?.startActivity(intent)
        }

        fun showMyAddList() {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.MYADDLIST)
            nactivity?.startActivity(intent)
        }

        fun showMyList() {
            showMyList("")
        }

        fun showMyList(ordernum: String) {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.MYLIST)
            intent.putExtra(Config.EXTRA_ORDER, ordernum)
            nactivity?.startActivity(intent)
        }

        fun showMyListView(mydata: MyCargo) {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.MYLISTVIEW)
            intent.putExtra(Config.EXTRA_ORDER, mydata)
            nactivity?.startActivity(intent)
        }

        fun showList() {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.LIST)
            nactivity?.startActivity(intent)
        }

        fun showListView(mydata: Cargo) {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.LISTVIEW)
            intent.putExtra(Config.EXTRA_ORDER, mydata)
            nactivity?.startActivity(intent)
        }

        fun showWebView(url: String, title: String) {
            nactivity?.logd("url : $url / title : $title")
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.WEBVIEW)
            intent.putExtra(Config.EXTRA_ORDER, url + "|" + title)
            nactivity?.startActivity(intent)
        }

        fun goShop() {
            var url = ""
            val array = Settings.getCodeList(nactivity!!, "gyeonggiConfig")
            nactivity?.logd(array.toString())
            try {
                if (array.size > 0) {
                    if (array[0].value.isNullOrBlank()) {
                        url = array[1].value
                    } else {
                        url = array[0].value
                    }
                } else {
                    url = "http://kgyd.itgoyo.com"
                }
            } catch (e: Exception) {
                url = "http://kgyd.itgoyo.com"
                nactivity?.logd("go shop exeption :: $e")
            }

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            nactivity?.startActivity(intent)
        }

        fun goShop2() {
            val url = "http://kgyd.itgoyo.com"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            nactivity?.startActivity(intent)
        }

        fun showMore() {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.MORE)
            nactivity?.startActivity(intent)
        }

        fun resetPoint() {
            if (nhandler != null) {
                nhandler?.sendEmptyMessage(Config.HDL_POINT)
            }
        }

        fun showMyBankout(info: MyBankInfo) {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.BANKOUT)
            intent.putExtra(Config.EXTRA_ORDER, info)
            nactivity?.startActivity(intent)
        }

        fun showMyBankToUser() {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.BANKTOUSER)
            nactivity?.startActivity(intent)
        }

        fun showQRCode() {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.QRCODE)
            nactivity?.startActivity(intent)
        }

        fun showAddjoin() {
            val intent = Intent(nactivity, SubActivity::class.java)
            intent.putExtra(Config.EXTRA_TYPE, SubActivity.TYPE.ADDJOIN)
            nactivity?.startActivity(intent)
        }


        fun appRestart() {
            val packageManager = nactivity?.packageManager
            val intent = packageManager?.getLaunchIntentForPackage(nactivity?.packageName!!)
            val componentName = intent!!.component
            val mainIntent = Intent.makeRestartActivityTask(componentName)
            nactivity?.startActivity(mainIntent)
            exitProcess(0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ucargo)

        val main_menu1go = findViewById<Button>(R.id.main_menu1go)
        val main_menu2go = findViewById<Button>(R.id.main_menu2go)
        val main_menu3go = findViewById<Button>(R.id.main_menu3go)
        val main_menu4go = findViewById<Button>(R.id.main_menu4go)
        val main_menu5go = findViewById<Button>(R.id.main_menu5go)
        val main_menu6go = findViewById<Button>(R.id.main_menu6go)
        val main_menu7go = findViewById<Button>(R.id.main_menu7go)
        val main_menu9go = findViewById<Button>(R.id.main_menu9go)
        val main_menu10go = findViewById<Button>(R.id.main_menu10go)
        val main_mypagego = findViewById<ImageButton>(R.id.main_mypagego)
        val main_settinggo = findViewById<ImageButton>(R.id.main_settinggo)
        val main_telgo = findViewById<Button>(R.id.main_telgo)
        val main_board1 = findViewById<Button>(R.id.main_board1)
        val main_board2 = findViewById<Button>(R.id.main_board2)
        val main_board3 = findViewById<Button>(R.id.main_board3)

        main_menu1go.setOnClickListener(this)
        main_menu2go.setOnClickListener(this)
        main_menu3go.setOnClickListener(this)
        main_menu4go.setOnClickListener(this)
        main_menu5go.setOnClickListener(this)
        main_menu6go.setOnClickListener(this)
        main_menu7go.setOnClickListener(this)
        main_menu9go.setOnClickListener(this)
        main_menu10go.setOnClickListener(this)
        main_mypagego.setOnClickListener(this)
        main_settinggo.setOnClickListener(this)
        main_telgo.setOnClickListener(this)
        main_board1.setOnClickListener(this)
        main_board2.setOnClickListener(this)
        main_board3.setOnClickListener(this)

        nswipe = findViewById(R.id.board2_swipe)
        nempty = findViewById(R.id.board2_empty)
        nemptytext = findViewById(R.id.board2_empty_text)

        nemptytext?.text = resources.getString(R.string.list_empty2)

        nswipe?.setOnRefreshListener(this)
        nswipe?.setColorSchemeResources(
                R.color.colorListPay1,
                R.color.colorListPay2,
                R.color.colorListPay3,
                R.color.colorListPay4
        )

        //permission set
        setPermission(PEMTYPE.CONTACTS)
        setPermission(PEMTYPE.LOCATION)
        //setPermission(PEMTYPE.MIC)
        setPermission(PEMTYPE.SENSOR)
        //setPermission(PEMTYPE.SMS)
        setPermission(PEMTYPE.STORAGE)
        setPermission(PEMTYPE.CAMERA)
        setPermission(PEMTYPE.PHONE)

        isFinish = false
        isLogin = false
        nactivity = this

        Http.setQue(this)

        if (!isSplash) {
            showSplash()
        }

        lservice = Intent(thisContext, UCargoService::class.java)
        thisContext.startService(lservice)

        logd("main activity :: " + Settings.getInfo(this).toString())

        nhandler = Handler { message ->
            when (message.what) {
                Config.HDL_POINT -> {
                    val cwnum = Settings.getID(thisContext)
                    getUserpoint(cwnum)
                }
            }

            true
        }

        nRead = IsRead(this, Config.DB_Reads, null, Config.DB_Reads_ver)

        if (!intent.getStringExtra(Config.EXTRA_TYPE).isNullOrBlank()) {
            noti_type = intent.getStringExtra(Config.EXTRA_TYPE)!!
            if (noti_type.toInt() == Config.FCM_ORDER) {
                noti_data = intent.getSerializableExtra(Config.EXTRA_DATA) as Cargo
            } else {
                noti_idx = intent.getIntExtra(Config.EXTRA_IDX, 0)
            }
        }

        nadapter = MainBoardAdapter(this)

        val board2_list = findViewById<ListView>(R.id.board2_list)
        board2_list.adapter = nadapter
        board2_list.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    run {
                        var mydata = nadapter!!.getItem(position)

                        if (mydata.ttype != "mainlink") {
                            if (!mydata.title.isNullOrBlank()) {
                                AppHttp().getBoardView(
                                        mydata.type,
                                        mydata.idx,
                                        Http,
                                        object : DefaultHttp.Callback {
                                            override fun onError(
                                                    code: DefaultHttp.HttpCode,
                                                    message: String,
                                                    hcode: String
                                            ) {
                                                toastl(resources.getString(R.string.toast_error_server))
                                            }

                                            override fun onSuccess(json: Any) {
                                                val data = json as JSONObject
                                                try {
                                                    mydata.contents = data.getString("wText")
                                                    mydata.check = true
                                                    UCargoActivity.showBoardView(mydata)
                                                } catch (e: Exception) {
                                                    logd("isRead : " + e.toString())
                                                    toastl(resources.getString(R.string.toast_error_server))
                                                }
                                            }
                                        })
                            }
                        } else {
                            logd(mydata.type.toString())
                            when (mydata.type) {
                                Config.BOARD_HNOTICE -> {
                                    if (noti1_count > 0) {
                                        showBoard(SubActivity.TYPE.HH5, 1)
                                    }
                                }
                                Config.BOARD_JIBU -> {
                                    if (noti2_count > 0) {
                                        showBoard(SubActivity.TYPE.HH5, 2)
                                    }
                                }
                            }
                        }
                    }
                }
    }

    fun setFCMID() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) { instanceIdResult ->
            val newToken = instanceIdResult.token
            val cwnum = Settings.getID(this)
            val oldtoken = Settings.getFCMID(this)

            nfcmid = newToken

            if (oldtoken != newToken && !cwnum.isNullOrBlank()) {
                /*MemberHttp().setFCM(cwnum, newToken, Http, object : DefaultHttp.Callback {
                    override fun onError(
                            code: DefaultHttp.HttpCode,
                            message: String,
                            hcode: String
                    ) {
                        logd("fcmid error : " + message)
                    }

                    override fun onSuccess(json: Any) {
                        logd("fcmid save ok")
                        Settings.setFCMID(thisContext, newToken)
                    }
                })*/
            }
        }
    }

    fun getUserpoint(cwnum: String) {
        MemberHttp().getUserPoint(cwnum!!, Http, object : DefaultHttp.Callback {
            override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                nactivity?.logd("getUserPoint error")
            }

            override fun onSuccess(json: Any) {
                val data = json as JSONObject
                App.userInfo!!.point = data.getString("point")
            }
        })
    }

    fun showNotification() {
        logd("start :: showNotification() ==> noti_type : " + noti_type + " // " + noti_idx + " // " + noti_data.toString())

        if (!noti_type.isNullOrBlank()) {
            var jsondata = JSONObject()

            var notidata = Notice()
            if (noti_idx > 0) {
                notidata = nNotice?.select(noti_idx) as Notice
                jsondata = if (notidata.txtData.isNullOrBlank() || notidata.txtData == "{}") {
                    JSONObject()
                } else {
                    JSONObject(notidata.txtData)
                }
            }

            when (noti_type.toInt()) {
                Config.FCM_ORDER -> {
                    if (!noti_data.nOrderNum.isNullOrBlank()) {
                        showMyList(noti_data.nOrderNum)
                    }
                }
                Config.FCM_NBOARD -> {
                    if (!notidata.intUse && notidata.intSeq > 0) {
                        val jidx = if (jsondata.isNull("idx")) {
                            0
                        } else {
                            jsondata.getString("idx").toInt()
                        }

                        AppHttp().getBoardView(0, jidx, Http, object : DefaultHttp.Callback {
                            override fun onError(
                                    code: DefaultHttp.HttpCode,
                                    message: String,
                                    hcode: String
                            ) {
                                toasts(resources.getString(R.string.toast_error_server))
                            }

                            override fun onSuccess(json: Any) {
                                val data = json as JSONObject
                                try {
                                    val bidx = data.getString("idx").toInt()
                                    val btitle = data.getString("wTitle")
                                    val bdate = data.getString("wRegdate")
                                    val bcontents = data.getString("wText")

                                    val mydata = Board(bidx, btitle, bcontents, bdate, false, 0)
                                    showBoardView(mydata)

                                    nRead?.insert(0, bidx)

                                } catch (e: Exception) {
                                    logd("isRead : " + e.toString())
                                    toastl(resources.getString(R.string.toast_error_server))
                                }
                            }
                        })

                        nNotice?.updateUse(notidata.intSeq)
                    }
                }
                Config.FCM_JBOARD -> {
                    if (!notidata.intUse && notidata.intSeq > 0) {
                        val jidx = if (jsondata.isNull("idx")) {
                            0
                        } else {
                            jsondata.getString("idx").toInt()
                        }

                        AppHttp().getBoardView(1, jidx, Http, object : DefaultHttp.Callback {
                            override fun onError(
                                    code: DefaultHttp.HttpCode,
                                    message: String,
                                    hcode: String
                            ) {
                                toasts(resources.getString(R.string.toast_error_server))
                            }

                            override fun onSuccess(json: Any) {
                                val data = json as JSONObject
                                try {
                                    val bidx = data.getString("idx").toInt()
                                    val btitle = data.getString("wTitle")
                                    val bdate = data.getString("wRegdate")
                                    val bcontents = data.getString("wText")
                                    val btype = data.getString("wTitleType")

                                    val mydata =
                                            Board(bidx, btitle, bcontents, bdate, false, 1, btype)
                                    showBoardView(mydata)

                                    nRead?.insert(1, bidx)

                                } catch (e: Exception) {
                                    logd("isRead : " + e.toString())
                                    toastl(resources.getString(R.string.toast_error_server))
                                }
                            }
                        })

                        nNotice?.updateUse(notidata.intSeq)
                    }
                }
                Config.FCM_HBOARD -> {
                    if (!notidata.intUse && notidata.intSeq > 0) {
                        val jidx = if (jsondata.isNull("idx")) {
                            0
                        } else {
                            jsondata.getString("idx").toInt()
                        }

                        AppHttp().getBoardView(2, jidx, Http, object : DefaultHttp.Callback {
                            override fun onError(
                                    code: DefaultHttp.HttpCode,
                                    message: String,
                                    hcode: String
                            ) {
                                toasts(resources.getString(R.string.toast_error_server))
                            }

                            override fun onSuccess(json: Any) {
                                val data = json as JSONObject
                                try {
                                    val bidx = data.getString("idx").toInt()
                                    val btitle = data.getString("wTitle")
                                    val bdate = data.getString("wRegdate")
                                    val bcontents = data.getString("wText")
                                    val btype = data.getString("wTitleType")

                                    val mydata =
                                            Board(bidx, btitle, bcontents, bdate, false, 2, btype)
                                    showBoardView(mydata)

                                    nRead?.insert(2, bidx)

                                } catch (e: Exception) {
                                    logd("isRead : " + e.toString())
                                    toastl(resources.getString(R.string.toast_error_server))
                                }
                            }
                        })

                        nNotice?.updateUse(notidata.intSeq)
                    }
                }
                Config.FCM_BANK -> {
                    if (!notidata.intUse && notidata.intSeq > 0) {
                        showMyBank()

                        nNotice?.updateUse(notidata.intSeq)
                    }
                }
                else -> {
                    if (!notidata.intUse && notidata.intSeq > 0) {
                        val dinfo = "[" + notidata.txtTitle + "]\n\n" + notidata.txtInfo

                        val dialog = AlertDialog(
                                this,
                                AlertDialog.TYPE.OK,
                                "",
                                dinfo,
                                object : AlertDialog.Callback {
                                    override fun onNo() {

                                    }

                                    override fun onYes() {

                                    }
                                })

                        dialog.show()

                        nNotice?.updateUse(notidata.intSeq)
                    }
                }
            }


        }

        noti_type = ""
        noti_idx = 0
        noti_data = Cargo()
    }

    fun startApp() {
        val cwnum = Settings.getID(this)
        if (!cwnum.isNullOrBlank()) {
            getUserpoint(cwnum)
        }
        Handler().postDelayed({
            getNotice()
        }, 300)
        //setFCMID()
        checkLocation()
        showPopup()
        showNotification()
        isLogin = true
    }

    fun getNotice() {
        val id = Settings.getID(this)
        val main_boardlist = findViewById<RelativeLayout>(R.id.main_boardlist)
        nlistheight = main_boardlist.height
        nadapter?.clear()

        when (nboardtype) {
            2 -> { //소식
                nadapter?.setHeight((nlistheight - Etc.convertDpToPixel(20f, this)!!.toInt()) / 5)

                AppHttp().getNewsBoard(id, 0, 5, Http, object : DefaultHttp.Callback {
                    override fun onError(
                            code: DefaultHttp.HttpCode,
                            message: String,
                            hcode: String
                    ) {
                        nactivity?.logd("getUserPoint error")
                        nswipe?.isRefreshing = false
                        nempty?.visibility = View.VISIBLE
                    }

                    override fun onSuccess(json: Any) {
                        val data = json as JSONArray

                        if (data.length() > 0) {
                            for (i in 0..(data.length() - 1)) {
                                val field = data.getJSONObject(i)
                                val data = Board()

                                data?.type = Config.BOARD_NEWS
                                data?.ttype = field.getString("titleType")
                                data?.idx = field.getString("idx").toInt()
                                data?.title = field.getString("title")
                                data?.date = field.getString("regdate")

                                nadapter?.addData(data)
                            }
                            nadapter?.dataChange()
                            nempty?.visibility = View.GONE
                        } else {
                            nempty?.visibility = View.VISIBLE
                        }

                        nswipe?.isRefreshing = false
                    }
                })
            }
            3 -> { //FAQ
                nadapter?.setHeight((nlistheight - Etc.convertDpToPixel(20f, this)!!.toInt()) / 5)

                AppHttp().getFAQBoard(id, 0, 5, Http, object : DefaultHttp.Callback {
                    override fun onError(
                            code: DefaultHttp.HttpCode,
                            message: String,
                            hcode: String
                    ) {
                        nactivity?.logd("getUserPoint error")
                        nswipe?.isRefreshing = false
                        nempty?.visibility = View.VISIBLE
                    }

                    override fun onSuccess(json: Any) {
                        val data = json as JSONArray

                        if (data.length() > 0) {
                            for (i in 0..(data.length() - 1)) {
                                val field = data.getJSONObject(i)
                                val data = Board()

                                data?.type = Config.BOARD_FAQ
                                data?.ttype = resources.getString(R.string.boardk_type4)
                                data?.idx = field.getString("idx").toInt()
                                data?.title = field.getString("title")
                                data?.date = field.getString("regdate")

                                nadapter?.addData(data)
                            }

                            nadapter?.dataChange()
                            nswipe?.isRefreshing = false
                            nempty?.visibility = View.GONE
                        } else {
                            nempty?.visibility = View.VISIBLE
                        }
                    }
                })
            }
            else -> { //공지사항
                nadapter?.setHeight((nlistheight - Etc.convertDpToPixel(30f, this)!!.toInt()) / 6)

                nadapter?.addData(
                        Board(
                                0,
                                resources.getString(R.string.hh_menu5_sub1),
                                "",
                                "",
                                false,
                                Config.BOARD_HNOTICE,
                                "mainlink"
                        )
                )
                nadapter?.addData(Board())
                nadapter?.addData(Board())
                nadapter?.addData(
                        Board(
                                0,
                                resources.getString(R.string.hh_menu5_sub2),
                                "",
                                "",
                                false,
                                Config.BOARD_JIBU,
                                "mainlink"
                        )
                )
                nadapter?.addData(Board())
                nadapter?.addData(Board())

                AppHttp().getHHNoticeBoard(id, 0, 2, Http, object : DefaultHttp.Callback {
                    override fun onError(
                            code: DefaultHttp.HttpCode,
                            message: String,
                            hcode: String
                    ) {
                        nactivity?.logd("getUserPoint error")
                        nswipe?.isRefreshing = false

                        if (noti1_count == 0 && noti2_count == 0) {
                            nempty?.visibility = View.VISIBLE
                        } else {
                            nempty?.visibility = View.GONE
                        }
                    }

                    override fun onSuccess(json: Any) {
                        val data = json as JSONArray

                        noti1_count = data.length()

                        if (data.length() > 0) {
                            for (i in 0..(data.length() - 1)) {
                                val field = data.getJSONObject(i)
                                val data = when (i) {
                                    1 -> {
                                        nadapter?.getItem(2)
                                    }
                                    else -> {
                                        nadapter?.getItem(1)
                                    }
                                }

                                data?.type = Config.BOARD_HNOTICE
                                data?.idx = field.getString("idx").toInt()
                                data?.title = field.getString("title")
                                data?.date = field.getString("regdate")
                            }

                            nadapter?.dataChange()

                            nswipe?.isRefreshing = false
                        }

                        if (noti1_count == 0 && noti2_count == 0) {
                            nempty?.visibility = View.VISIBLE
                        } else {
                            nempty?.visibility = View.GONE
                        }
                    }
                })

                AppHttp().getJibuBoard(id, 0, 2, Http, object : DefaultHttp.Callback {
                    override fun onError(
                            code: DefaultHttp.HttpCode,
                            message: String,
                            hcode: String
                    ) {
                        nactivity?.logd("getUserPoint error")
                        nswipe?.isRefreshing = false

                        if (noti1_count == 0 && noti2_count == 0) {
                            nempty?.visibility = View.VISIBLE
                        } else {
                            nempty?.visibility = View.GONE
                        }
                    }

                    override fun onSuccess(json: Any) {
                        val data = json as JSONArray

                        noti2_count = data.length()

                        if (data.length() > 0) {
                            for (i in 0..(data.length() - 1)) {
                                val field = data.getJSONObject(i)
                                val data = when (i) {
                                    1 -> {
                                        nadapter?.getItem(5)
                                    }
                                    else -> {
                                        nadapter?.getItem(4)
                                    }
                                }

                                data?.type = Config.BOARD_JIBU
                                data?.idx = field.getString("idx").toInt()
                                data?.title = field.getString("title")
                                data?.date = field.getString("regdate")
                            }

                            nadapter?.dataChange()

                            nswipe?.isRefreshing = false
                        }

                        if (noti1_count == 0 && noti2_count == 0) {
                            nempty?.visibility = View.VISIBLE
                        } else {
                            nempty?.visibility = View.GONE
                        }
                    }
                })
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        logd("start onNewIntent()")
        if (isLogin && intent != null) {
            logd(
                    "start intent renew :: " + if (!intent.getStringExtra(Config.EXTRA_TYPE)
                                    .isNullOrBlank()
                    ) {
                        intent.getStringExtra(Config.EXTRA_TYPE)
                    } else {
                        "nonono"
                    }
            )
            if (!intent.getStringExtra(Config.EXTRA_TYPE).isNullOrBlank()) {
                noti_type = intent.getStringExtra(Config.EXTRA_TYPE)!!
                if (noti_type.toInt() == Config.FCM_ORDER) {
                    noti_data = intent.getSerializableExtra(Config.EXTRA_DATA) as Cargo
                } else {
                    noti_idx = intent.getIntExtra(Config.EXTRA_IDX, 0)
                }

                logd("start intent renew :: " + noti_type + " // ")

                showNotification()
            }
        }
    }

    fun checkLocation() {
        logd(
                "call checkLocation :: " + if (isAlertDialog) {
                    "true"
                } else {
                    "false"
                }
        )
        val location = kr.bgsoft.ucargo.jeju.device.Location(this)
        if (!location.isLocation()) {
            if (!isAlertDialog) {
                isAlertDialog = true
                val dialog = AlertDialog(
                        this,
                        AlertDialog.TYPE.LOCATION,
                        resources.getString(R.string.dialog_location_title),
                        resources.getString(R.string.dialog_location_info),
                        object : AlertDialog.Callback {
                            override fun onNo() {
                            }

                            override fun onYes() {
                                startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                            }
                        })
                dialog.show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        var isPCheck = true
        val permission = checkPermission()

        if (permission != null && !isSplash) {
            for (check in permission) {
                if (!check) {
                    isPCheck = false
                    break
                }
            }
        }

        if (!isPCheck) {
            showSplash()
        }

        if (isStop) {
            startApp()
            isStop = false
        }
    }

    override fun finish() {

        if (isFinish) {
            super.finish()
        } else {
            val dialog = AlertDialog(
                    this,
                    AlertDialog.TYPE.YESNO,
                    resources.getString(R.string.dialog_finish_title),
                    resources.getString(R.string.dialog_finish_info),
                    object : AlertDialog.Callback {
                        override fun onNo() {

                        }

                        override fun onYes() {
                            isFinish = true
                            finish()
                            stopService(lservice)
                        }
                    })

            dialog.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            Config.RESULT_SPLASH -> {
                val data = intent?.getStringArrayExtra(Config.EXTRA_DATA)
                isSplash = false

                when (resultCode) {
                    Config.RTNINT_SUCESS -> {
                        logd("app start call")
                        startApp()
                        showPopup()
                    }
                    Config.RTNINT_LOGIN -> {
                        logd("login start call")
                        showLogin()
                    }
                    else -> {
                        isFinish = true
                        finish()
                    }
                }
            }
            Config.RESULT_SMS, Config.RESULT_LOGIN -> {
                when (resultCode) {
                    Config.RTNINT_SUCESS -> {
                        logd("app start call")
                        startApp()
                        showPopup()
                    }
                    Config.RTNINT_JOIN -> {
                        showJoin()
                    }
                    else -> {
                        isFinish = true
                        finish()
                    }
                }
            }
            Config.RESULT_JOIN -> {
                showLogin()
            }
            Config.RESULT_BANK -> {
                resetPoint()
            }
            else -> {
                isFinish = true
                finish()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRefresh() {
        getNotice()
    }

    fun showPopup() {
        val db = Infos(this, Config.DB_Infos, null, Config.DB_Infos_ver)
        val date = db.select("noticedaycut")
        logd("승윤 :: " + date)
        /*if (date.isNullOrBlank() || date.toString() < Date().time.toString()) {
            val ndata = Settings.getVersion(this)
            if (!ndata.isNull("noticepop") && !ndata.getString("noticepop")
                    .isNullOrBlank() && nnotiidx.isNullOrBlank()
            ) {
                val popurl = ndata.getString("noticepop")
                logd(popurl)
                val dialog = NoticeDialog(
                    this,
                    NoticeDialog.TYPE.WEB,
                    popurl,
                    true,
                    object : NoticeDialog.Callback {
                        override fun onNoShow() {
                            val adddate = Etc.addDay(Date(), 1)
                            App.Infos?.insert("noticedaycut", adddate.time.toString())
                        }

                        override fun onClose() {

                        }

                        override fun onClick() {

                        }
                    })
                dialog.show()
            }
        }*/
        if (date.isNullOrBlank() || date.toString() < Date().time.toString()) {
            val nData = Settings.getVersion(this)
            logd("tmddbs :: " + nData)
            if (!nData.isNull("noticepop") && !nData.getString("noticepop")
                            .isNullOrBlank() /*&& noti_idx.toString().isNullOrBlank()*/
            ) {
                val popurl = nData.getString("noticepop")
                val dialog = NoticeDialog(
                        this,
                        NoticeDialog.TYPE.WEB,
                        popurl,
                        true,
                        object : NoticeDialog.Callback {
                            override fun onClick() {

                            }

                            override fun onClose() {

                            }

                            override fun onNoShow() {
                                val addate = Etc.addDay(Date(), 1)
                                db.insert("noticedaycut", addate.time.toString())
                            }
                        })
                dialog.show()
            }
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.main_menu1go -> { //협회소개
                showHHPage(SubActivity.TYPE.HH1)
            }
            R.id.main_menu2go -> { //주요사업
                showHHPage(SubActivity.TYPE.HH2)
            }
            R.id.main_menu3go -> { //업무안내
                showHHPage(SubActivity.TYPE.HH3)
            }
            R.id.main_menu4go -> { //협회안내
                showHHPage(SubActivity.TYPE.HH4)
            }
            R.id.main_menu5go -> { //공지사항
                showHHPage(SubActivity.TYPE.HH5)
            }
            R.id.main_menu6go -> { //관련사이트
                showPartner()
            }
            R.id.main_menu7go -> { //일반화물정보
                /*val info = Settings.getInfo(this)
                if (info.bzNum.isNullOrBlank() || info.bzNum == "_") {
                  //  showAddjoin()
                } else {
                 showList()
                }*/

                toasts(resources.getString(R.string.no_ready))
            }
            R.id.main_menu9go -> { //이벤트
                goShop2()
            }
//            R.id.main_menu9go -> { //카드결제
//
//                val info = Settings.getInfo(this)
//                if(info.catid == "" || info.catid == "_" || info.bzNum == "_" || info.bzNum == "_") {
//                    val dialog = AlertDialog(this, AlertDialog.TYPE.OK, resources.getString(R.string.dialog_card_title), resources.getString(R.string.dialog_card_info), object: AlertDialog.Callback{
//                        override fun onNo() {
//
//                        }
//
//                        override fun onYes() {
//
//                        }
//                    })
//                    dialog.show()
//                } else {
//                    if(Etc.isInstall(Config.SMATRO_PKG, this)) {
//                        showCardMain()
//                    } else {
//                        toasts(resources.getString(R.string.toast_smartro_install))
//                        val uri = Uri.parse("market://details?id=" + Config.SMATRO_PKG)
//                        val intentNew = Intent(Intent.ACTION_VIEW, uri)
//                        startActivity(intentNew)
//                    }
//                }
//            }
            R.id.main_menu10go -> { //더보기
                showMore()
            }
            R.id.main_mypagego -> { //마이페이지
                showMypage()
            }
            R.id.main_settinggo -> { //설정
                showSetting()
            }
            R.id.main_board1 -> {
                nboardtype = 1
                getNotice()
            }
            R.id.main_board2 -> {
                nboardtype = 2
                getNotice()
            }
            R.id.main_board3 -> {
                nboardtype = 3
                getNotice()
            }
            R.id.main_telgo -> { //전화연결
                showCall()
            }
        }
    }
}