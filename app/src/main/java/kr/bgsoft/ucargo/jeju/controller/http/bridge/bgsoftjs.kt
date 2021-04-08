package kr.bgsoft.ucargo.jeju.controller.http.bridge

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.JavascriptInterface
import kr.bgsoft.ucargo.jeju.SubActivity
import kr.bgsoft.ucargo.jeju.UCargoActivity
import kr.bgsoft.ucargo.jeju.controller.conf.Settings
import kr.bgsoft.ucargo.jeju.utils.Etc

class bgsoftjs(context: Context?) {
    val CTX = context
    val nactivity = (CTX as SubActivity)

    @JavascriptInterface
    fun program(type: String, value: String, etc: String) {
        Log.d("nbs천재", "type : $type / value : $value / etc : $etc")
        when(type) {
            "webview" -> {
                when(value) {

                }
            }
            "board" -> {
                when(value) {
                    "jibu" -> {
                        //KTCActivity.showBoard(SubActivity.TYPE.JIBU)
                    }
                    "notice" -> {
                        //KTCActivity.showBoard(SubActivity.TYPE.NOTICE)
                    }
                }
            }
            "program" -> {
                when(value) {
                    "membersearch" -> {
                        //KTCActivity.showMemberSearch()
                    }
                    "mypage" -> {
                        val info = Settings.getInfo(CTX!!)
                        if(info.bzNum.isNullOrBlank() || info.bzNum == "_") {
                            UCargoActivity.showAddjoin()
                        } else {
                            UCargoActivity.showMypage()
                        }
                    }
                }
            }
            "app" -> {

            }
            else -> {

            }
        }
    }

    fun gotoAppLink(pkg: String) {
        var intent: Intent? = null

        if (Etc.isInstall(pkg, nactivity)) {
            val pm = nactivity?.packageManager
            intent = pm?.getLaunchIntentForPackage(pkg)
            intent?.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        } else {
            val uri = Uri.parse("market://details?id=" + pkg)
            intent = Intent(Intent.ACTION_VIEW, uri)
        }

        if(intent != null) {
            nactivity.startActivity(intent)
        }
    }

}