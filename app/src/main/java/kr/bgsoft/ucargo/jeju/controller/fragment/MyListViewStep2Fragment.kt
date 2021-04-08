package kr.bgsoft.ucargo.jeju.controller.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.SubActivity
import kr.bgsoft.ucargo.jeju.controller.conf.Settings
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment
import kr.bgsoft.ucargo.jeju.data.model.Code
import kr.bgsoft.ucargo.jeju.data.model.MyCargo
import kr.bgsoft.ucargo.jeju.data.model.Owner
import kr.bgsoft.ucargo.jeju.utils.Etc
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class MyListViewStep2Fragment: DefaultFragment(), View.OnClickListener  {

    var nactivity: SubActivity?     = null
    var nmydata: MyCargo?           = null
    val setpaytype                  = JSONArray()
    var ncorpname: TextView?        = null
    var ncorpceo: TextView?         = null
    var ncorpnum: TextView?         = null
    var ncorptype: TextView?        = null
    var ncorptype2: TextView?       = null
    var ncorpemail: TextView?       = null
    var ncorpaddress: TextView?     = null
    var ncorptel: TextView?         = null
    var ncorpfax: TextView?         = null
    var ncorpgocall: ImageButton?   = null
    var ncorptax: Button?           = null
    var nregdate: TextView?         = null
    var ntelnumber                  = ""
    var nfranchisee                 = ""

    var isCancle: Boolean           = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_mylistview_step2, container, false)

        nactivity   = activity as SubActivity
        nmydata     = MyListViewFragment.mydata

        ncorpname               = layout.findViewById(R.id.mylistview_corpname)
        ncorpceo                = layout.findViewById(R.id.mylistview_corpceo)
        ncorpnum                = layout.findViewById(R.id.mylistview_corpnum)
        ncorptype               = layout.findViewById(R.id.mylistview_corptype)
        ncorptype2              = layout.findViewById(R.id.mylistview_corptype2)
        ncorpemail              = layout.findViewById(R.id.mylistview_corpemail)
        ncorpaddress            = layout.findViewById(R.id.mylistview_corpaddress)
        ncorptel                = layout.findViewById(R.id.mylistview_corptel)
        ncorpfax                = layout.findViewById(R.id.mylistview_corpfax)

        nregdate                = layout.findViewById(R.id.listview_regdate)
        val view_warningbox     = layout.findViewById<LinearLayout>(R.id.listview_warningbox)
        val view_warning        = layout.findViewById<ImageView>(R.id.listview_warning)

        Settings.getCode(activity!!,"payMethod")

        logd(setpaytype.toString())

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
        nregdate?.text = if(nmydata!!.sRegdate.take(10) == Etc.convertDateToString(Date()).take(10)) { nmydata!!.sRegdate.substring(11, 16) } else { nmydata!!.sRegdate.substring(5, 16).replace("-", ".") }

        ncorpgocall?.setOnClickListener(this)
        ncorptax?.setOnClickListener(this)

        //카드가 포함일경우 계산서 발행 불가
        val setmypay = Code("", "")
        for(i in 0..(setpaytype.length() - 1)) {
            val data = setpaytype.getJSONObject(i)
            if(data.getString("value") == nmydata?.sPayment) {
                setmypay.code   = data.getString("key")
                setmypay.value  = data.getString("value")
                break
            }
        }

        if(setmypay.code == "payMethod06" || setmypay.code == "payMethod07") {
            ncorptax?.visibility = View.GONE
        }

        if(MyListViewFragment.owner?.sOwnerName.isNullOrBlank()) {
            /*CargoHttp().getOwnerView(nmydata!!.sOwnerNum, Http, object : DefaultHttp.Callback {
                override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                    logd(message)
                    try {
                        toasts(resources.getString(R.string.toast_error_server))
                    } catch (e: Exception) {

                    }

                    nactivity?.finish()
                }

                override fun onSuccess(json: Any) {
                    val mydata = json as JSONObject

                    logd(json.toString())

                    if (mydata.isNull("sOwnerName")) {
                        try {
                            toasts(resources.getString(R.string.toast_error_server))
                        } catch (e: Exception) {

                        }
                        nactivity?.finish()
                    } else {
                        MyListViewFragment.owner?.sOwnerName = mydata.getString("sOwnerName")
                        MyListViewFragment.owner?.sOwnerTel = mydata.getString("sOwnerTel")
                        MyListViewFragment.owner?.sBzNum = mydata.getString("sBzNum")
                        MyListViewFragment.owner?.sOwnerFax = mydata.getString("sOwnerFax")
                        MyListViewFragment.owner?.sOwnerAddr = mydata.getString("sOwnerAddr")
                        MyListViewFragment.owner?.sOwnerEmail = mydata.getString("sOwnerEmail")
                        MyListViewFragment.owner?.sOwnerHP = mydata.getString("sOwnerHP")
                        MyListViewFragment.owner?.sUptap = mydata.getString("sUptap")
                        MyListViewFragment.owner?.sUpjang = mydata.getString("sUpjang")
                        MyListViewFragment.owner?.sOwnerLv = mydata.getString("sOwnerLv")
                        MyListViewFragment.owner?.sOwnerCompany = mydata.getString("sOwnerCompany")

                        changeView(MyListViewFragment?.owner!!)
                    }
                }
            })*/
        } else {
            changeView(MyListViewFragment?.owner!!)
        }

        nfranchisee = "차주"

        /*CargoHttp().getFranchiseeCheck(nmydata?.sOwnerNum!!, "차주", Http, object : DefaultHttp.Callback {
            override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                nfranchisee = "차주"
            }
            override fun onSuccess(json: Any) {
                nfranchisee = "기업"
            }
        })*/

        isCancle        = true
        val nowdate     = Etc.convertDateToString(Date(), "yyyy-MM-dd")
        val startdate   = MyListViewFragment.mydata!!.sLoadDay.take(10)

        if(nowdate > startdate) {
            val params = TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 10f)
            isCancle    = false
        }

        return layout
    }

    fun changeView(mydata: Owner) {

        ntelnumber = ""
        if(!mydata.sOwnerTel.isNullOrBlank()) {
            ntelnumber = mydata.sOwnerTel
        } else if(!mydata.sOwnerHP.isNullOrBlank()) {
            ntelnumber = mydata.sOwnerHP
        } else {
            ncorpgocall?.visibility = View.GONE
        }

        ncorpname?.text      = mydata.sOwnerCompany
        ncorpceo?.text       = mydata.sOwnerName
        ncorpnum?.text       = Etc.getCorpNum(mydata.sBzNum)
        ncorptype?.text      = mydata.sUptap
        ncorptype2?.text     = mydata.sUpjang
        ncorpemail?.text     = mydata.sOwnerEmail
        ncorpaddress?.text   = mydata.sOwnerAddr
        ncorptel?.text       = Etc.getTelNum(ntelnumber)
        ncorpfax?.text       = Etc.getTelNum(mydata.sOwnerFax)
    }

    fun gotoCall(activity: SubActivity) {
        if (!MyListViewFragment.owner?.sOwnerTel.isNullOrBlank()) {
            if(isCancle) {
                val cwnum = Settings.getID(activity)

                if (!cwnum.isNullOrBlank()) {
                    /*CargoHttp().getCall(cwnum!!, MyListViewFragment.mydata!!.nOrderNum, Http, object : DefaultHttp.Callback {
                        override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                            if (code != DefaultHttp.HttpCode.ERROR_700 && code != DefaultHttp.HttpCode.ERROR_701 && code != DefaultHttp.HttpCode.ERROR_702) {
                                try {
                                    toasts(resources.getString(R.string.toast_error_server))
                                } catch (e: Exception) {

                                }
                            }
                        }

                        override fun onSuccess(json: Any) {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ntelnumber))
                            activity?.startActivity(intent)
                            UCargoActivity.resetPoint()
                        }
                    })*/
                }
            } else {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ntelnumber))
                activity?.startActivity(intent)
            }
        }
    }

    override fun onClick(v: View?) {

    }
}