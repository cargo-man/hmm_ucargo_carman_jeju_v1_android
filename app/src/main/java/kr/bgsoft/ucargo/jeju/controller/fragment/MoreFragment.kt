package kr.bgsoft.ucargo.jeju.controller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import kr.bgsoft.ucargo.jeju.App
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.SubActivity
import kr.bgsoft.ucargo.jeju.UCargoActivity
import kr.bgsoft.ucargo.jeju.controller.conf.Settings
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment
import org.json.JSONObject

class MoreFragment : DefaultFragment(), View.OnClickListener {

    var nAccount = JSONObject()
    var nmaxcut = 0
    var nusepoint = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_more, container, false)

        val view_title = layout.findViewById<TextView>(R.id.header_title)
        val view_back = layout.findViewById<ImageButton>(R.id.header_back)
        val more_menu1go = layout.findViewById<Button>(R.id.more_menu1go)
        val more_menu2go = layout.findViewById<Button>(R.id.more_menu2go)
        val more_menu3go = layout.findViewById<Button>(R.id.more_menu3go)
        val more_menu4go = layout.findViewById<Button>(R.id.more_menu4go)
        val more_menu5go = layout.findViewById<Button>(R.id.more_menu5go)
        val more_menu6go = layout.findViewById<Button>(R.id.more_menu6go)
        val more_menu7go = layout.findViewById<Button>(R.id.more_menu7go)
        val more_menu8go = layout.findViewById<Button>(R.id.more_menu8go)
        val more_menu9go = layout.findViewById<Button>(R.id.more_menu9go)

        view_back.setOnClickListener(this)
        more_menu1go.setOnClickListener(this)
        more_menu2go.setOnClickListener(this)
        more_menu3go.setOnClickListener(this)
        more_menu4go.setOnClickListener(this)
        more_menu5go.setOnClickListener(this)
        more_menu6go.setOnClickListener(this)
        more_menu7go.setOnClickListener(this)
        more_menu8go.setOnClickListener(this)
        more_menu9go.setOnClickListener(this)

        view_title.text = resources.getString(R.string.title_more)

        return layout
    }

    override fun onClick(v: View?) {
        val info = Settings.getInfo(activity!!)

        when (v?.id) {
            R.id.header_back -> {
                activity?.finish()
            }
            R.id.more_menu1go -> {
                /*if (info.bzNum.isNullOrBlank() || info.bzNum == "_") {
                    UCargoActivity.showAddjoin()
                } else {
                    UCargoActivity.showMyAdd()
                }*/
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.more_menu2go -> {
                /*if (info.bzNum.isNullOrBlank() || info.bzNum == "_") {
                    UCargoActivity.showAddjoin()
                } else {
                    UCargoActivity.showMyAddList()
                }*/
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.more_menu3go -> {
                /*if (info.bzNum.isNullOrBlank() || info.bzNum == "_") {
                    UCargoActivity.showAddjoin()
                } else {
                    UCargoActivity.showMyList()
                }*/
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.more_menu4go -> {
                /*if (info.bzNum.isNullOrBlank() || info.bzNum == "_") {
                    UCargoActivity.showAddjoin()
                } else {
                    UCargoActivity.showAlarm()
                }*/
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.more_menu5go -> {
                //UCargoActivity.showMyBank()
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.more_menu6go -> {
                /*nAccount = Settings.getAccount(activity!!)
                nmaxcut = nAccount.getInt("nMaxWithdraw")
                nusepoint = nAccount.getInt("nWithdrawFee")

                val pay = App.userInfo?.point.toString().toInt() - nusepoint
                if (pay > 0) {
                    val cwnum = Settings.getID(activity!!)
                    if (!cwnum.isNullOrBlank()) {
                        *//*MemberHttp().getPointInfo(cwnum!!, Http, object : DefaultHttp.Callback {
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
                        })*//*
                    }
                } else {
                    toasts(resources.getString(R.string.toast_mybank_underpoint1))
                }*/
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.more_menu7go -> {
                UCargoActivity.showBoard(SubActivity.TYPE.NOTICE)
            }
            R.id.more_menu8go -> {
                UCargoActivity.showCall()
            }
            R.id.more_menu9go -> {
                UCargoActivity.goShop()
            }
        }
    }
}