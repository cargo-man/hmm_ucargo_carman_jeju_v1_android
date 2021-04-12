package kr.bgsoft.ucargo.jeju.controller.fragment

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import kr.bgsoft.ucargo.jeju.App
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.SubActivity
import kr.bgsoft.ucargo.jeju.controller.conf.Config
import kr.bgsoft.ucargo.jeju.controller.conf.Settings
import kr.bgsoft.ucargo.jeju.controller.dialog.AlertDialog
import kr.bgsoft.ucargo.jeju.controller.http.DefaultHttp
import kr.bgsoft.ucargo.jeju.controller.http.MemberHttp
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment
import kr.bgsoft.ucargo.jeju.data.model.UserInfo
import kr.bgsoft.ucargo.jeju.utils.AES256
import org.json.JSONObject

class LoginFragment : DefaultFragment(), View.OnClickListener {

    var nactivity: SubActivity? = null
    var login_id: EditText? = null
    var login_pass: EditText? = null
    var nnumber = ""
    var login_send: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout: View? = null

        nnumber = if (thisActivity?.thisDeviceInfo?.getNumber().isNullOrBlank()) {
            ""
        } else {
            thisActivity?.thisDeviceInfo?.getNumber().toString()
        }
        //nnumber = "01092669963"

        nactivity = activity as SubActivity

        layout = inflater!!.inflate(R.layout.fragment_login, container, false)

        login_id = layout.findViewById(R.id.login_id)
        login_pass = layout.findViewById(R.id.login_pass)

        login_send = layout.findViewById(R.id.login_send)

        val login_findid = layout.findViewById<Button>(R.id.login_findid)
        val login_findpass = layout.findViewById<Button>(R.id.login_findpass)
        val login_join = layout.findViewById<Button>(R.id.login_join)

        login_id?.privateImeOptions = "defaultInputmode=english;"
        login_pass?.privateImeOptions = "defaultInputmode=english;"

        login_send?.setOnClickListener(this)
        login_findid.setOnClickListener(this)
        login_findpass.setOnClickListener(this)
        login_join.setOnClickListener(this)

        login_id?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                changeButton()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
        login_pass?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                changeButton()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })

        changeButton()

        return layout
    }

    fun changeButton() {
        if (!login_id?.text.isNullOrBlank() && !login_pass?.text.isNullOrBlank()) {
            login_send?.setBackgroundResource(R.drawable.cbutton_type1)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                login_send?.setTextColor(resources.getColor(android.R.color.white, null))
            } else {
                login_send?.setTextColor(resources.getColor(android.R.color.white))
            }
        } else {
            login_send?.setBackgroundResource(R.drawable.cbutton_type5)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                login_send?.setTextColor(resources.getColor(R.color.colorUser5, null))
            } else {
                login_send?.setTextColor(resources.getColor(R.color.colorUser5))
            }
        }
    }

    fun sendLogin() {
        var error = false
        var message = ""
        val aid = AES256.encode(thisActivity?.thisDeviceInfo?.getDevicesUUID().toString())

        hideKeyboard()

        if (login_id?.text.isNullOrBlank() && !error) {
            message = resources.getString(R.string.dialog_login_error_id)
            error = true
        }
        if (login_pass?.text.isNullOrBlank() && !error) {
            message = resources.getString(R.string.dialog_login_error_pass)
            error = true
        }

        if (error) {
            val dialog = AlertDialog(
                activity,
                AlertDialog.TYPE.OK,
                "",
                message,
                object : AlertDialog.Callback {
                    override fun onNo() {
                    }

                    override fun onYes() {
                    }
                })
            dialog.show()
        } else {
            val id = AES256.encode(login_id?.text.toString()).toString()
            val pw = AES256.encode(login_pass?.text.toString()).toString()
            val hp = AES256.encode(nnumber).toString()

            MemberHttp().getLogin(id, pw, aid.toString(), hp, Http, object : DefaultHttp.Callback {
                override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                    if (code == DefaultHttp.HttpCode.ERROR_201 || code == DefaultHttp.HttpCode.CODE_ERROR) {
                        val dialog = AlertDialog(
                            activity,
                            AlertDialog.TYPE.OK,
                            "",
                            message,
                            object : AlertDialog.Callback {
                                override fun onNo() {
                                }

                                override fun onYes() {

                                }
                            })
                        dialog.show()
                    } else if (code != DefaultHttp.HttpCode.ERROR_700) {
                        try {
                            toasts(resources.getString(R.string.toast_error_server))
                        } catch (e: Exception) {

                        }
                        logd(message)
                    }
                }

                override fun onSuccess(json: Any) {
                    val data = (json as JSONObject)
                    val smsuser = data.getString("cwnum")

                    Settings.setID(activity!!, smsuser)
                    getUserInfo(smsuser)
                }
            })
        }
    }

    fun getUserInfo(cwnum: String) {
        MemberHttp().getUserInfo(cwnum, Http, object : DefaultHttp.Callback {
            override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                logd(message)
                try {
                    toasts(resources.getString(R.string.toast_error_server))
                } catch (e: Exception) {

                }

                logd("4. userinfo get check error")
            }

            override fun onSuccess(json: Any) {
                val catid = (json as JSONObject).getString("catid")
                val vAccount = json.getString("vAccount")
                val bzNum = json.getString("bzNum")
                val mShipJoin = json.getString("mShipJoin")
                val lvType = json.getString("lvType")
                val name = AES256.decode(json.getString("name")).toString()
                val lv00 = json.getString("lv00")
                val lv01 = json.getString("lv01")
                val id = AES256.decode(json.getString("id")).toString()
                val lvArea = json.getString("lvArea")
                val point = json.getString("point")
                val lv03 = json.getString("sJibu")
                val sComZip = json.getString("sComZip")
                val sCartonName = json.getString("sCartonName")
                val sCartonCode = json.getString("sCartonCode")
                val sCeoName = json.getString("sCeoName")
                val sCarNum = json.getString("sCarNum")
                val sUptap = json.getString("sUptae")
                val sUpjang = json.getString("sUpjong")
                val sComName = json.getString("sComName")
                val sComAddr = json.getString("sAddr1") + "|" + json.getString("sAddr2")
                var sAddress = json.getString("sCarAddr1") + "|" + json.getString("sCarAddr2")
                val sEmail = ""
                val sHP = AES256.decode(json.getString("sHP")).toString()
                val sBankName = json.getString("sBankName")
                val sBankUserName = AES256.decode(json.getString("sBankUserName")).toString()
                var sCartypeCode = json.getString("sCartypeCode")
                var sCartypeName = json.getString("sCartypeName")
                var sCarName = json.getString("sCarName")
                var nTRSNum = json.getString("nTRSNum")
                var sBzType = json.getString("sBzType")
                var sCmsBankCode =
                    if (json.isNull("sCmsBankCode")) "" else json.getString("sCmsBankCode")
                var sCmsBankName =
                    if (json.isNull("sCmsBankName")) "" else json.getString("sCmsBankName")
                var sCmsBankUser =
                    if (json.isNull("sCmsBankUser")) "" else AES256.decode(json.getString("sCmsBankUser"))
                        .toString()
                var sBankAccount = json.getString("sBankAccount")

                App.userInfo = UserInfo(
                    catid,
                    vAccount,
                    bzNum,
                    mShipJoin,
                    lvType,
                    name,
                    lv00,
                    lv01,
                    id,
                    lvArea,
                    point,
                    lv03,
                    sComZip,
                    sCartonName,
                    sCartonCode,
                    sCeoName,
                    sCarNum,
                    sUptap,
                    sUpjang,
                    sComName,
                    sComAddr,
                    sEmail,
                    sHP,
                    "",
                    "",
                    nTRSNum,
                    sCartypeName,
                    sCartypeCode,
                    sBankUserName,
                    sBankName,
                    sCarName,
                    sCmsBankCode,
                    sBankAccount,
                    sCmsBankUser,
                    sCmsBankName,
                    "",
                    sAddress,
                    "",
                    sBzType
                )

                Settings.setInfo(activity!!, App.userInfo)

                logd("load info" + Settings.getInfo(activity!!).toString())

                Handler().postDelayed({
                    thisActivity?.setResult(Config.RTNINT_SUCESS)
                    thisActivity?.finish()
                }, 1000)

            }
        })
    }

    fun sendFindId() {
        var message = ""
        var iserror = false

        if (nnumber.isNullOrBlank()) {
            message = resources.getString(R.string.dialog_findid_nonumber)
            iserror = true
        } else {
            val aesnum = AES256.encode(nnumber).toString()

            MemberHttp().getFindID(aesnum, Http, object : DefaultHttp.Callback {
                override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                    if (code == DefaultHttp.HttpCode.ERROR_201 || code == DefaultHttp.HttpCode.CODE_ERROR) {
                        val dialog = AlertDialog(
                            activity,
                            AlertDialog.TYPE.OK,
                            resources.getString(R.string.dialog_findid_title),
                            message,
                            object : AlertDialog.Callback {
                                override fun onNo() {
                                }

                                override fun onYes() {

                                }
                            })
                        dialog.show()
                    } else if (code != DefaultHttp.HttpCode.ERROR_700) {
                        try {
                            toasts(resources.getString(R.string.toast_error_server))
                        } catch (e: Exception) {

                        }
                        logd(message)
                    }
                }

                override fun onSuccess(json: Any) {
                    logd(json.toString())
                    val data = json as JSONObject
                    val id =
                        if (data.isNull("sResult")) "" else AES256.decode(data.getString("sResult"))
                            .toString()

                    message = resources.getString(R.string.dialog_findid_info).replace("{id}", id)

                    val dialog = AlertDialog(
                        activity,
                        AlertDialog.TYPE.YESNO,
                        resources.getString(R.string.dialog_findid_title),
                        message,
                        object : AlertDialog.Callback {
                            override fun onNo() {
                            }

                            override fun onYes() {
                                login_id?.setText(id)
                            }
                        })
                    dialog.show()

                }
            })
        }

        if (!message.isNullOrBlank() && iserror) {
            val dialog = AlertDialog(
                activity,
                AlertDialog.TYPE.OK,
                resources.getString(R.string.dialog_findid_title),
                message,
                object : AlertDialog.Callback {
                    override fun onNo() {
                    }

                    override fun onYes() {
                    }
                })
            dialog.show()
        }
    }

    fun sendFindPass() {
        var message = ""
        var iserror = false

        if (nnumber.isNullOrBlank()) {
            message = resources.getString(R.string.dialog_findpass_nonumber)
            iserror = true
        } else if (login_id?.text.isNullOrBlank()) {
            message = resources.getString(R.string.dialog_login_error_passid)
            iserror = true
        } else {
            val aesnum = AES256.encode(nnumber).toString()
            val aesid = AES256.encode(login_id?.text.toString()).toString()

            MemberHttp().getFindPass(aesnum, aesid, Http, object : DefaultHttp.Callback {
                override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                    if (code == DefaultHttp.HttpCode.ERROR_201 || code == DefaultHttp.HttpCode.CODE_ERROR) {
                        val dialog = AlertDialog(
                            activity,
                            AlertDialog.TYPE.OK,
                            resources.getString(R.string.dialog_findpass_title),
                            message,
                            object : AlertDialog.Callback {
                                override fun onNo() {
                                }

                                override fun onYes() {

                                }
                            })
                        dialog.show()
                    } else if (code != DefaultHttp.HttpCode.ERROR_700) {
                        try {
                            toasts(resources.getString(R.string.toast_error_server))
                        } catch (e: Exception) {

                        }
                        logd(message)
                    }
                }

                override fun onSuccess(json: Any) {
                    val data = json as JSONObject
                    val pass =
                        if (data.isNull("sResult")) "" else AES256.decode(data.getString("sResult"))
                            .toString()

                    message =
                        resources.getString(R.string.dialog_findpass_info).replace("{pass}", pass)

                    val dialog = AlertDialog(
                        activity,
                        AlertDialog.TYPE.YESNO,
                        resources.getString(R.string.dialog_findpass_title),
                        message,
                        object : AlertDialog.Callback {
                            override fun onNo() {
                            }

                            override fun onYes() {
                                login_pass?.setText(pass)
                            }
                        })
                    dialog.show()
                }
            })

        }

        if (!message.isNullOrBlank() && iserror) {
            val dialog = AlertDialog(
                activity,
                AlertDialog.TYPE.OK,
                resources.getString(R.string.dialog_findpass_title),
                message,
                object : AlertDialog.Callback {
                    override fun onNo() {
                    }

                    override fun onYes() {
                    }
                })
            dialog.show()
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login_send -> {
                sendLogin()
            }
            R.id.login_findid -> {
                logd("find id click")
                sendFindId()
            }
            R.id.login_findpass -> {
                logd("find pass click")
                sendFindPass()
            }
            /*R.id.login_join ->{
                if(nnumber.isNullOrBlank()) {
                    val dialog = AlertDialog(activity, AlertDialog.TYPE.OK, resources.getString(R.string.dialog_join_title), resources.getString(R.string.dialog_join_nonumber), object : AlertDialog.Callback {
                        override fun onNo() {
                        }

                        override fun onYes() {
                        }
                    })
                    dialog.show()
                } else {
                    val aesnum = AES256.encode(nnumber).toString()

                    MemberHttp().getFindID(aesnum, Http, object : DefaultHttp.Callback {
                        override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                            if(code == DefaultHttp.HttpCode.ERROR_201 || code == DefaultHttp.HttpCode.CODE_ERROR) {
                                val dialog = LiteJoinDialog(activity, aesnum, object: LiteJoinDialog.Callback {
                                    override fun onNo() {
//                                        thisActivity?.setResult(Config.RTNINT_JOIN)
//                                        thisActivity?.finish()
                                    }

                                    override fun onYes() {

                                    }
                                })
                                dialog.show()
                            } else if(code != DefaultHttp.HttpCode.ERROR_700) {
                                try {
                                    toasts(resources.getString(R.string.toast_error_server))
                                } catch (e: Exception) {

                                }
                                logd(message)
                            }
                        }

                        override fun onSuccess(json: Any) {
                            logd(json.toString())
                            val data    = json as JSONObject
                            val id      = if(data.isNull("sResult")) "" else AES256.decode(data.getString("sResult")).toString()

                            val message = resources.getString(R.string.dialog_findid_info2).replace("{id}", id)

                            val dialog = AlertDialog(activity, AlertDialog.TYPE.YESNO, resources.getString(R.string.dialog_join_title), message, object : AlertDialog.Callback {
                                override fun onNo() {
                                }

                                override fun onYes() {
                                    login_id?.setText(id)
                                }
                            })
                            dialog.show()

                        }
                    })
                }
            }*/
        }
    }
}