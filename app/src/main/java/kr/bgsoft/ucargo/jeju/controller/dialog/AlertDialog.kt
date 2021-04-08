package kr.bgsoft.ucargo.jeju.controller.dialog

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TableRow
import android.widget.TextView
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.cview.DefaultActivity
import kr.bgsoft.ucargo.jeju.cview.DefaultDialog

class AlertDialog(context: Context?, type: TYPE?, title: String, contents: String, callback: Callback?) : DefaultDialog(context), View.OnClickListener {
    val TP                              = type
    val CB                              = callback
    val title                           = title
    val info                            = contents
    var isFinish                        = true
    var nDefActivity: DefaultActivity?  = null


    interface Callback {
        fun onYes()
        fun onNo()
    }

    enum class TYPE {
        YESNO, LOCATION, OK, NOK, OKCANCLE, UPDATE, CANCLE, OKSTOP, ADDCANCLE, TOPOINT, CARDJOIN, YESNOH, NEWJOIN, GOMYPAGE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
setContentView(R.layout.dialog_alert)

        val view_header     = findViewById<RelativeLayout>(R.id.alert_header)
        val view_title      = findViewById<TextView>(R.id.alert_title)
        val view_contents   = findViewById<TextView>(R.id.alert_contents)
        val view_yes        = findViewById<Button>(R.id.alert_yes)
        val view_no         = findViewById<Button>(R.id.alert_no)

        view_contents.text = info
        view_title.text = title
        view_yes.setOnClickListener(this)
        view_no.setOnClickListener(this)

        if(title.isEmpty()) {
            view_header.visibility = View.GONE
        } else {
            view_header.visibility = View.VISIBLE
        }

        when(TP) {
            TYPE.YESNO -> {
                isFinish = false
            }
            TYPE.YESNOH -> {
                isFinish = false

                view_contents.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(info, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(info)
                }

                view_contents.gravity = Gravity.CENTER_VERTICAL
            }
            TYPE.LOCATION -> {
                isFinish = false
            }
            TYPE.OK -> {
                view_no.visibility = View.GONE
                view_yes.text = thisContext!!.resources.getString(R.string.alert_ok)
                view_yes.setBackgroundResource(R.drawable.cbutton_type6f)
                val params = TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 5f)
                view_yes.layoutParams = params
            }
            TYPE.NOK -> {
                isFinish = false
                view_no.visibility = View.GONE
                view_yes.text = thisContext!!.resources.getString(R.string.alert_ok)
                val params = TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 5f)
                view_yes.layoutParams = params
                view_yes.setBackgroundResource(R.drawable.cbutton_type6f)
            }
            TYPE.OKCANCLE -> {
                view_yes.text = thisContext!!.resources.getString(R.string.alert_ok)
                view_no.text = resource?.getString(R.string.alert_cancle)
            }
            TYPE.UPDATE -> {
                isFinish = false
                view_yes.text = thisContext!!.resources.getString(R.string.alert_update)
                view_no.text = resource?.getString(R.string.alert_cancle)
            }
            TYPE.CANCLE -> {
                isFinish = false
                view_yes.text = thisContext!!.resources.getString(R.string.alert_listcancle)
                view_no.text = resource?.getString(R.string.alert_close)
            }
            TYPE.OKSTOP -> {
                isFinish = false
                view_yes.text = thisContext!!.resources.getString(R.string.alert_stop)
                view_no.text = resource?.getString(R.string.alert_ok)
            }
            TYPE.ADDCANCLE -> {
                view_yes.text = thisContext!!.resources.getString(R.string.alert_call)
                view_no.text = resource?.getString(R.string.alert_close)
            }
            TYPE.TOPOINT -> {
                view_yes.text = thisContext!!.resources.getString(R.string.alert_banksend)
                view_no.text = resource?.getString(R.string.alert_cancle)

                view_contents.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(info, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(info)
                }
            }
            TYPE.NEWJOIN -> {
                view_yes.text = resource?.getString(R.string.alert_renew)
                view_no.text = resource?.getString(R.string.alert_callcenter)
            }
            TYPE.CARDJOIN -> {
                isFinish = false

                view_yes.text = thisContext!!.resources.getString(R.string.alert_join)
                view_no.text = resource?.getString(R.string.alert_close)
            }
            TYPE.GOMYPAGE -> {
                view_yes.text = thisContext!!.resources.getString(R.string.title_mypage)
                view_no.text = resource?.getString(R.string.alert_close)
            }
        }

    }

    override fun dismiss() {
        if(isFinish) {
            super.dismiss()
        }
    }

    override fun onClick(v: View?) {
        val id = v?.id

        when(id) {
            R.id.alert_yes -> {
                CB?.onYes()
                isFinish = true
                dismiss()
            }
            R.id.alert_no -> {
                CB?.onNo()
                isFinish = true
                dismiss()
            }
        }
    }
}