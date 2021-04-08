package kr.bgsoft.ucargo.jeju.controller.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kr.bgsoft.ucargo.jeju.App
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.SubActivity
import kr.bgsoft.ucargo.jeju.UCargoActivity
import kr.bgsoft.ucargo.jeju.controller.adapter.BankAdapter
import kr.bgsoft.ucargo.jeju.controller.conf.Settings
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment
import kr.bgsoft.ucargo.jeju.cview.IconButton
import kr.bgsoft.ucargo.jeju.data.model.MyBankInfo
import kr.bgsoft.ucargo.jeju.utils.Etc
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class MyBankFragment : DefaultFragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{

    var nAccount                    = JSONObject()
    var nactivity: SubActivity?     = null
    var npoint: TextView?           = null
    var nbankjoin: Button?          = null
    var nbankno: TextView?          = null
    var nstart: TextView?           = null
    var nend: TextView?             = null
    var nstartdate: Date?           = null
    var nenddate: Date?             = null
    var nstatus1: RadioButton?      = null
    var nstatus2: RadioButton?      = null
    var nstatus3: RadioButton?      = null
    var nswipe: SwipeRefreshLayout? = null
    var nemptybox: RelativeLayout?  = null
    var nlist: ListView?            = null
    var nadapter: BankAdapter?      = null
    var nbanknobox: LinearLayout?   = null
    var nbankinfo: TextView?        = null
    var set_usepay                  = 0
    var nusepoint                   = 0
    var ntousercut                  = 0
    var set_mybankno                = App.userInfo?.vAccount
    var set_mybankname              = App.userInfo?.sBankName
    var set_mybankusername          = App.userInfo?.name
    var ntype                       = ""
    var nlastordernum               = ""
    var lastitemVisibleFlag         = false
    var listResultEmpty             = false

    companion object {
        var isReload = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_mybank, container, false)

        nactivity = activity as SubActivity

        nAccount             = Settings.getAccount(activity!!)
        set_usepay           = nAccount.getInt("nInputFee")
        nusepoint            = nAccount.getInt("nWithdrawFee")
        ntousercut           = nAccount.getInt("nMinTransferPoint")

        val view_title      = layout.findViewById<TextView>(R.id.header_title)
        val view_back       = layout.findViewById<ImageButton>(R.id.header_back)
        val view_usepay     = layout.findViewById<TextView>(R.id.mybank_usepay)
        val view_bankout    = layout.findViewById<IconButton>(R.id.mybank_outpay)
        val view_touser     = layout.findViewById<IconButton>(R.id.mybank_touser)
        val view_qrgo       = layout.findViewById<IconButton>(R.id.mybank_qrgo)

        nstatus1            = layout.findViewById<RadioButton>(R.id.mybank_status1)
        nstatus2            = layout.findViewById<RadioButton>(R.id.mybank_status2)
        nstatus3            = layout.findViewById<RadioButton>(R.id.mybank_status3)
        npoint              = layout.findViewById(R.id.mybank_point)
        nbankno             = layout.findViewById(R.id.mybank_bankno)
        nbankjoin           = layout.findViewById(R.id.mybank_bankjoin)
        nstart              = layout.findViewById(R.id.mybank_startdate)
        nend                = layout.findViewById(R.id.mybank_enddate)
        nswipe              = layout.findViewById(R.id.board2_swipe)
        nlist               = layout.findViewById(R.id.board2_list)
        nemptybox           = layout.findViewById(R.id.board2_empty)
        nbanknobox          = layout.findViewById(R.id.mybank_banknobox)
        nbankinfo           = layout.findViewById(R.id.mybank_bankinfo)

        view_title.text = resources.getString(R.string.title_bank)
        view_back.setOnClickListener(this)
        nbankjoin?.setOnClickListener(this)
        view_bankout.setOnClickListener(this)
        view_touser.setOnClickListener(this)
        view_qrgo.setOnClickListener(this)
        nstart?.setOnClickListener(this)
        nend?.setOnClickListener(this)
        nstatus1?.setOnClickListener(this)
        nstatus2?.setOnClickListener(this)
        nstatus3?.setOnClickListener(this)

        view_usepay.text    = resources.getString(R.string.mybank_info2).replace("{usepay}", set_usepay.toString())
        npoint?.text        = App.userInfo?.point?.toInt()?.let { Etc.setComma(it) }

        if(set_mybankno.isNullOrBlank()) {
            nbanknobox?.visibility   = View.GONE
            nbankjoin?.visibility    = View.VISIBLE
        } else {
            val banknumber = set_mybankno.toString().substring(0, 4) + "-" + set_mybankno.toString().substring(4, 8) + "-" + set_mybankno.toString().substring(8, set_mybankno!!.length)

            nbankno?.text           = banknumber

            nbankinfo?.text         =  if(!set_mybankname.isNullOrBlank() && !set_mybankusername.isNullOrBlank()) {
                set_mybankname.toString().replace(resources.getString(R.string.mybank_bankinfo), "") + resources.getString(R.string.mybank_bankinfo) + " / " + set_mybankusername.toString()
            } else if(!set_mybankname.isNullOrBlank()) {
                set_mybankname.toString().replace(resources.getString(R.string.mybank_bankinfo), "") + resources.getString(R.string.mybank_bankinfo)
            } else if(set_mybankusername.isNullOrBlank()) {
                set_mybankusername.toString()
            } else {
                resources.getString(R.string.mybank_banknoinfo)
            }

            nbanknobox?.visibility  = View.VISIBLE
            nbankjoin?.visibility   = View.GONE
        }

        nenddate        = Date()
        nstartdate      = Etc.addMonth(nenddate!!, -11)
        nstartdate      = Etc.IntToDate(Etc.getYear(nstartdate!!), Etc.getMonth(nstartdate!!), 1, 0, 0, 0)

        nstart?.text    = Etc.convertDateToString(nstartdate, "yyyy.MM.dd")
        nend?.text      = Etc.convertDateToString(nenddate, "yyyy.MM.dd")

        nadapter        = BankAdapter(nactivity!!)

        nlist?.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    loadList()
                }
            }

            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                lastitemVisibleFlag = totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount
            }
        })

        nlist?.adapter  = nadapter

        nswipe?.setOnRefreshListener(this)
        nswipe?.setColorSchemeResources(
                R.color.colorListPay1,
                R.color.colorListPay2,
                R.color.colorListPay3,
                R.color.colorListPay4)

        listResultEmpty     = false
        nlastordernum       = ""

        val cwnum = Settings.getID(activity!!)
        if(!cwnum.isNullOrBlank()) {
            /*MemberHttp().getUserPoint(cwnum!!, Http, object : DefaultHttp.Callback {
                override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {

                }

                override fun onSuccess(json: Any) {
                    val data = json as JSONObject
                    App.userInfo!!.point    = data.getString("point")
                    npoint?.text            = App.userInfo?.point?.toInt()?.let { Etc.setComma(it) }
                }
            })*/
        }
        setBankNum()

        loadList()

        return layout
    }

    fun setBankNum() {
        if(set_mybankno.isNullOrBlank()) {
            nbanknobox?.visibility   = View.GONE
            nbankjoin?.visibility    = View.VISIBLE
        } else {
            val banknumber = set_mybankno.toString().substring(0, 4) + "-" + set_mybankno.toString().substring(4, 8) + "-" + set_mybankno.toString().substring(8, set_mybankno!!.length)

            nbankno?.text           = banknumber

            nbankinfo?.text         =  if(!set_mybankname.isNullOrBlank() && !set_mybankusername.isNullOrBlank()) {
                set_mybankname.toString().replace(resources.getString(R.string.mybank_bankinfo), "") + resources.getString(R.string.mybank_bankinfo) + " / " + set_mybankusername.toString()
            } else if(!set_mybankname.isNullOrBlank()) {
                set_mybankname.toString().replace(resources.getString(R.string.mybank_bankinfo), "") + resources.getString(R.string.mybank_bankinfo)
            } else if(set_mybankusername.isNullOrBlank()) {
                set_mybankusername.toString()
            } else {
                resources.getString(R.string.mybank_banknoinfo)
            }

            nbanknobox?.visibility  = View.VISIBLE
            nbankjoin?.visibility   = View.GONE
        }
    }

    fun loadList() {
        val cwnum = Settings.getID(activity!!)

        if(cwnum != null) {
            if (!listResultEmpty) {
                val start = Etc.convertDateToString(nstartdate, "yyyy-MM-dd")
                val end = Etc.convertDateToString(nenddate, "yyyy-MM-dd")

                /*MemberHttp().getPointList(cwnum, start, end, ntype, 10, nlastordernum, Http, object : DefaultHttp.Callback {
                    override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                        logd("error : " + message)
                        if(code != DefaultHttp.HttpCode.ERROR_700 && code != DefaultHttp.HttpCode.ERROR_701 && code != DefaultHttp.HttpCode.ERROR_702) {
                            try {
                                toasts(resources.getString(R.string.toast_error_server))
                            } catch (e: Exception) {

                            }
                        }
                    }

                    override fun onSuccess(json: Any) {
                        val data = json as JSONArray

                        logd("length() : " + data.length())

                        if(nlastordernum.isNullOrBlank()) {
                            nadapter?.clear()
                        }

                        if (data.length() > 0) {
                            for (i in 0..(data.length() - 1)) {
                                val field = data.getJSONObject(i)

                                try {
                                    val sNotes      = field.getString("sNotes")
                                    val sRegdate    = field.getString("sRegdate")
                                    val sLoadLoc    = field.getString("sLoadLoc")
                                    val sDownLoc    = field.getString("sDownLoc")
                                    val nInPoint    = field.getInt("nInPoint")
                                    val nOutPoint   = field.getInt("nOutPoint")
                                    val nNowPoint   = field.getInt("nNowPoint")
                                    val nIdx        = field.getInt("nIdx")
                                    val nStats      = field.getInt("nStats")

                                    nadapter?.addData(nIdx, sNotes, nStats, sRegdate, nInPoint, nOutPoint, nNowPoint, sLoadLoc, sDownLoc)

                                    nlastordernum = nIdx.toString()
                                } catch (e: Exception) {
                                    logd(e.toString())
                                }
                            }

                            nadapter?.dataChange()
                            nemptybox?.visibility = View.GONE

                        } else {
                            if(nlastordernum.isNullOrBlank()) {
                                nadapter?.clear()
                                nadapter?.dataChange()
                                nemptybox?.visibility = View.VISIBLE
                            } else {
                                listResultEmpty = true
                            }
                        }

                        nswipe?.isRefreshing = false
                    }
                })*/
            }
        }
    }

    override fun onResume() {
        super.onResume()
        logd("onResume ")
        npoint?.text = App.userInfo?.point?.toInt()?.let { Etc.setComma(it) }
        if(isReload) {
            isReload            = false
            listResultEmpty     = false
            nlastordernum       = ""
            ntype               = "0"

            loadList()
            logd("onResume reload start")
        }
    }

    override fun onRefresh() {
        listResultEmpty         = false
        nlastordernum           = ""
        loadList()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.header_back -> {
                nactivity?.finish()
            }
            R.id.mybank_bankjoin -> {
                val cwnum = Settings.getID(activity!!)
                if(!cwnum.isNullOrBlank()) {
                    /*MemberHttp().setVAccount(cwnum!!, Http, object : DefaultHttp.Callback {
                        override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                            logd("error : " + message)
                            if(code != DefaultHttp.HttpCode.ERROR_700 && code != DefaultHttp.HttpCode.ERROR_701 && code != DefaultHttp.HttpCode.ERROR_702) {
                                try {
                                    toasts(resources.getString(R.string.toast_error_server))
                                } catch (e: Exception) {

                                }
                            }
                        }

                        override fun onSuccess(json: Any) {
                            val data = json as JSONObject
                            App.userInfo?.vAccount   = data.getString("vAccount")
                            set_mybankno             = App.userInfo?.vAccount
                            set_mybankname           = "신한은행"
                            set_mybankusername       = App.userInfo?.name

                            setBankNum()
                        }
                    })*/
                }
            }
            R.id.mybank_outpay -> {

                val pay = App.userInfo?.point.toString().toInt() - nusepoint
                if(pay > 0) {
                    val cwnum = Settings.getID(activity!!)
                    if (!cwnum.isNullOrBlank()) {
                        /*MemberHttp().getPointInfo(cwnum!!, Http, object : DefaultHttp.Callback {
                            override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                                logd("error : " + message)
                                if (code != DefaultHttp.HttpCode.ERROR_700 && code != DefaultHttp.HttpCode.ERROR_701 && code != DefaultHttp.HttpCode.ERROR_702) {
                                    try {
                                        toasts(resources.getString(R.string.toast_error_server))
                                    } catch (e: Exception) {

                                    }
                                }
                            }

                            override fun onSuccess(json: Any) {
                                val data = json as JSONObject
                                val mybankinfo = MyBankInfo()
                                mybankinfo.sBankAccountCertCode = data.getString("sBankAccountCertCode")
                                mybankinfo.sVirtualAccount = data.getString("sVirtualAccount")
                                mybankinfo.sBankAccount = data.getString("sBankAccount")
                                mybankinfo.sBankAccountCertMsg = data.getString("sBankAccountCertMsg")
                                mybankinfo.sBankUserName = data.getString("sBankUserName")
                                mybankinfo.sBankName = data.getString("sBankName")
                                mybankinfo.nUserPoint = data.getInt("nUserPoint")

                                App.userInfo?.point = mybankinfo.nUserPoint.toString()
                                UCargoActivity.showMyBankout(mybankinfo)
                            }
                        })*/
                    }
                } else {
                    toasts(resources.getString(R.string.toast_mybank_underpoint1))
                }
            }
            R.id.mybank_touser -> {

                val pay = App.userInfo?.point.toString().toInt() - ntousercut
                if(pay > 0) {
                    UCargoActivity.showMyBankToUser()
                } else {
                    toasts(resources.getString(R.string.toast_mybank_underpoint2).replace("{paycut}", Etc.setComma(ntousercut)!!))
                }

                //toasts("준비중입니다.")
            }
            R.id.mybank_qrgo -> {
                UCargoActivity.showQRCode()
                isReload = true
            }
            R.id.mybank_startdate -> {
                val dialog = DatePickerDialog(nactivity!!, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    var date    = Etc.IntToDate(year, month, dayOfMonth, 0,0,0)
                    val cutdate = Etc.addYear(nenddate!!, -1)

                    if(cutdate > date) {
                        toasts(resources.getString(R.string.toast_mylist_cutdate))
                    } else {
                        nstartdate = date
                        (v as TextView).text = Etc.convertDateToString(date, "yyyy.MM.dd")
                        listResultEmpty         = false
                        nlastordernum           = ""
                        loadList()
                    }
                }, Etc.getYear(nstartdate!!), Etc.getMonth(nstartdate!!), Etc.getDay(nstartdate!!))

                dialog.show()
            }
            R.id.mybank_enddate -> {
                val dialog = DatePickerDialog(nactivity!!, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    var date    = Etc.IntToDate(year, month, dayOfMonth, 0,0,0)
                    val cutdate = Etc.addYear(nstartdate!!, 1)

                    if(cutdate < date) {
                        toasts(resources.getString(R.string.toast_mylist_cutdate))
                    } else {
                        nenddate = date
                        (v as TextView).text = Etc.convertDateToString(date, "yyyy.MM.dd")
                        listResultEmpty         = false
                        nlastordernum           = ""
                        loadList()
                    }
                }, Etc.getYear(nenddate!!), Etc.getMonth(nenddate!!), Etc.getDay(nenddate!!))

                dialog.show()
            }
            R.id.mybank_status1 -> {
                listResultEmpty         = false
                nlastordernum           = ""
                ntype = "0"

                loadList()
            }
            R.id.mybank_status2 -> {
                listResultEmpty         = false
                nlastordernum           = ""
                ntype = "1"

                loadList()
            }
            R.id.mybank_status3 -> {
                listResultEmpty         = false
                nlastordernum           = ""
                ntype = "2"

                loadList()
            }
        }
    }
}