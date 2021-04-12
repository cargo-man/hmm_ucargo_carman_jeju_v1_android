package kr.bgsoft.ucargo.jeju.controller.fragment

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kr.bgsoft.ucargo.jeju.App
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.controller.conf.Config
import kr.bgsoft.ucargo.jeju.controller.conf.Settings
import kr.bgsoft.ucargo.jeju.controller.dialog.AlertDialog
import kr.bgsoft.ucargo.jeju.controller.http.AppHttp
import kr.bgsoft.ucargo.jeju.controller.http.DefaultHttp
import kr.bgsoft.ucargo.jeju.controller.http.MemberHttp
import kr.bgsoft.ucargo.jeju.cview.DefaultActivity
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment
import kr.bgsoft.ucargo.jeju.data.model.AppSetting
import kr.bgsoft.ucargo.jeju.data.model.UserInfo
import kr.bgsoft.ucargo.jeju.utils.AES256
import org.json.JSONObject

class SplashFragment : DefaultFragment(){

    val set_percent = 25

    var set_check = 0
    var nanimation: AnimationDrawable? = null
    var nappuuid = ""
    var nnumber = ""

    var splash_loading_location: ImageView? = null
    var isStart = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_splash, container, false)

        splash_loading_location = layout.findViewById(R.id.splash_loading_location)

        nanimation = splash_loading_location!!.background as AnimationDrawable

        return layout
    }

    fun checkApp() {
        //1. permission check
        var isPCheck = true
        val permission = thisActivity?.checkPermission()

        logd("1. start permission check == start")

        if (permission != null) {
            for (check in permission) {
                if (!check) {
                    isPCheck = false
                    break
                }
            }
        }

        if (!isPCheck) {
            logd("1. start permission check :: error")
            thisActivity?.startPermission(DefaultActivity.PEMTYPE.ALL)
        } else {
            val appver = thisActivity?.thisDeviceInfo?.getAppVersionName()
            nnumber = thisActivity?.thisDeviceInfo?.getNumber().toString()
            val appsetting = Settings.getAppSetting(activity!!)

            //nnumber          = "01092669963"

            logd("1. start permission check :: ok")
            set_check++
            startProgress()

            //2. version check
            AppHttp().getVersion(Http, object : DefaultHttp.Callback {
                override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                    logd(message)
                    try {
                        toasts(resources.getString(R.string.toast_error_server))
                        logd("tmddbs :: $message")
                    } catch (e: Exception) {

                    }
                }

                override fun onSuccess(json: Any) {
                    val vdata = json as JSONObject
                    val version: String = vdata.getString("versionName")
                    var vurl: String = vdata.getString("url")
                    var vmsg: String = vdata.getString("msg")
                    var vsetting: String = vdata.getString("setNum")
                    var setcheck: Boolean = false

                    val ndata = Settings.getVersion(activity!!)
                    if (ndata.isNull("setNum") || ndata.getString("setNum") != vsetting) {
                        setcheck = true
                    }

                    var appVern = 0
                    var appVero = 0
                    try {
                        vdata.put("setcode", ndata.getJSONObject("setcode"))
                        Settings.setVersion(activity!!, vdata.toString())

                        val arrVern = version.split(".")
                        val arrVero = appver.toString().split(".")

                        appVern =
                                (arrVern[0].toInt() * 1000000) + (if (arrVern.size < 2 || arrVern[1] == "0") {
                                    0
                                } else {
                                    arrVern[1].toInt() * 1000
                                }) + if (arrVern.size < 3) {
                                    0
                                } else {
                                    arrVern[2].toInt()
                                }
                        appVero =
                                (arrVero[0].toInt() * 1000000) + (if (arrVero.size < 2 || arrVero[1] == "0") {
                                    0
                                } else {
                                    arrVero[1].toInt() * 1000
                                }) + if (arrVero.size < 3) {
                                    0
                                } else {
                                    arrVero[2].toInt()
                                }

                    } catch (e: Exception) {
                        setcheck = true
                    }

                    logd("gv : $version ($appVern) > $appver ($appVero)")

                    if (appVern > appVero) {
                        if (vurl.isNullOrEmpty()) {
                            vmsg = resources.getString(R.string.dialog_update)
                            vurl =
                                    "https://play.google.com/store/apps/details?id=" + activity!!.packageName
                        }

                        val dialog = AlertDialog(
                                activity,
                                AlertDialog.TYPE.UPDATE,
                                resources.getString(R.string.dialog_update_title),
                                vmsg,
                                object : AlertDialog.Callback {
                                    override fun onYes() {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(vurl))
                                        startActivity(intent)
                                        activity?.finish()
                                    }

                                    override fun onNo() {
                                        activity?.finish()
                                    }
                                })
                        dialog.show()
                    } else {
                        getLogin(nnumber, appsetting)
                        if (setcheck) {
                            AppHttp().getCode(Http, object : DefaultHttp.Callback {
                                override fun onError(
                                        code: DefaultHttp.HttpCode,
                                        message: String,
                                        hcode: String
                                ) {
                                    logd(message)
                                    try {
                                        toasts(resources.getString(R.string.toast_error_server))
                                    } catch (e: Exception) {

                                    }
                                }

                                override fun onSuccess(json: Any) {
                                    val sdata = json as JSONObject

                                    vdata.put("setcode", sdata)
                                    Settings.setVersion(activity!!, vdata.toString())

                                    set_check++
                                    logd("2. version check ok")
                                    startProgress()
                                }
                            })
                        } else {
                            set_check++
                            logd("2. version check ok")
                            startProgress()
                        }
                    }
                }
            })

        }

    }

    fun getLogin(nnumber: String, appsetting: AppSetting) {
        nappuuid = thisActivity?.thisDeviceInfo?.getDevicesUUID().toString()
        logd(" // appuuid :: $nappuuid")

        //3. userinfo check
        var number = nnumber
        logd("number :: " + number)

        if (!appsetting.isAutoLogin) {
            thisActivity?.setResult(Config.RTNINT_LOGIN)
            thisActivity?.finish()
        } else {
//            val cwnum = Settings.getID(activity)
            val cwnum = ""

            if (cwnum.isNullOrBlank()) {

                if (number.isNullOrBlank()) {
                    number = ""
                }

                val hid = if (!number.isNullOrBlank()) {
                    AES256.encode(number.toString()).toString()
                } else {
                    ""
                }
                val huuid = AES256.encode(nappuuid).toString()

                MemberHttp().getLogin(hid, huuid, Http, object : DefaultHttp.Callback {
                    override fun onError(
                            code: DefaultHttp.HttpCode,
                            message: String,
                            hcode: String
                    ) {
                        if (code == DefaultHttp.HttpCode.CODE_ERROR || code == DefaultHttp.HttpCode.ERROR_201 || code == DefaultHttp.HttpCode.ERROR_700) {
                            thisActivity?.setResult(Config.RTNINT_LOGIN)
                            thisActivity?.finish()
                        } else {
                            logd(message)
                            try {
                                toasts(resources.getString(R.string.toast_error_server))
                            } catch (e: Exception) {

                            }
                        }
                    }

                    override fun onSuccess(json: Any) {
                        logd("3. userinfo check ok")
                        val data = json as JSONObject
                        val smsuser = data.getString("cwnum")


                   /* if(data.getBoolean("smscheck")) {
                        Settings.setID(activity!!, smsuser)
                        set_check++
                        logd("여기 1")
                        getUserInfo(smsuser)
                    } else { //sms 인증필요
                        val smschecktext = AES256.decode(data.getString("smschecktext").toString()).toString()
                        logd("여기 2")
                        thisActivity?.setResult(Config.RTNINT_SMSCHECK)
                        thisActivity?.intent?.putExtra(Config.EXTRA_DATA, smschecktext)
                        thisActivity?.finish()
                    }*/


                        Settings.setID(activity!!, smsuser)
                        set_check++

                        getUserInfo(smsuser)

                    }
                })
            } else {
                Settings.setID(activity!!, cwnum)
                set_check++

                getUserInfo(cwnum)
            }
        }
    }

    fun getUserInfo(cwnum: String) {
        MemberHttp().getUserInfo(cwnum, Http, object : DefaultHttp.Callback {
            override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                logd(message)
                thisActivity?.setResult(Config.RTNINT_LOGIN)
                thisActivity?.finish()
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

                logd("4. userinfo get check ok")

                set_check++
                startProgress()
            }
        })
    }

    fun startProgress() {
        var percent: Int = set_percent * set_check

        if (percent > 100) {
            percent = 100
        }

        logd("percent : $percent")

        if (percent == 100) {
            Handler().postDelayed({
                thisActivity?.setResult(Config.RTNINT_SUCESS)
                thisActivity?.finish()
            }, 1000)
        }
    }

    override fun onResume() {
        if (!isStart) {
            isStart = true
            if (Config.SET_TEST) {
                App.host = "http://59.13.192.199:8080/Y1TEST/"
                /*Handler().postDelayed({
                    checkApp()
                }, 100)*/
                checkApp()
            } else {
                /*AppHttp().getHost(Http, object : DefaultHttp.Callback {
                    override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                        Handler().postDelayed({
                            try {
                                logd("tmddbs :: $message")
                                toasts(resources.getString(R.string.toast_error_server))
                            } catch (e: Exception) {

                            }
                        }, 1000)
                    }

                    override fun onSuccess(json: Any) {
                        val data = json as JSONObject
                        App.host = AES256.decode(data.getString("UP")).toString()
                        logd(App.host)
                        Handler().postDelayed({
                            checkApp()
                        }, 1000)
                    }
                })*/

               // App.host = "http://192.168.0.83:8090/Y1/"
                    App.host = "http://59.13.192.199:8080/J1/"
                checkApp()
            }
        }

        if (nanimation != null) {
            nanimation?.start()
        }
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (nanimation != null) {
            nanimation?.stop()
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            var isPCheck = true
            val permission = thisActivity?.checkPermission()

            if (permission != null) {
                for (check in permission) {
                    if (!check) {
                        isPCheck = false
                        break
                    }
                }
            }

            if (!isPCheck) {
                val dialog = AlertDialog(
                        activity,
                        AlertDialog.TYPE.YESNO,
                        resources.getString(R.string.dialog_permission_title),
                        resources.getString(R.string.dialog_permission_check),
                        object : AlertDialog.Callback {
                            override fun onYes() {
                                thisActivity?.startPermission(DefaultActivity.PEMTYPE.ALL)
                            }

                            override fun onNo() {
                                thisActivity?.finish()
                            }
                        })
                dialog.show()
            } else {
                checkApp()
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}