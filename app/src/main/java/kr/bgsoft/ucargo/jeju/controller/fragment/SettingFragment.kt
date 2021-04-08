package kr.bgsoft.ucargo.jeju.controller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.SubActivity
import kr.bgsoft.ucargo.jeju.UCargoActivity
import kr.bgsoft.ucargo.jeju.controller.conf.Settings
import kr.bgsoft.ucargo.jeju.controller.dialog.AlertDialog
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment
import kr.bgsoft.ucargo.jeju.data.model.AppSetting

class SettingFragment: DefaultFragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    var nactivity: SubActivity? = null
    var appsetting = AppSetting()

    var view_textsize1: RadioButton?    = null
    var view_textsize2: RadioButton?    = null
    var view_textsize3: RadioButton?    = null
    var view_alerttext: CheckBox?       = null
    var view_colorbox: RelativeLayout?    = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_setting, container, false)

        nactivity = activity as SubActivity

        val view_title          = layout.findViewById<TextView>(R.id.header_title)
        val view_back           = layout.findViewById<ImageButton>(R.id.header_back)
        val view_checklogin     = layout.findViewById<CheckBox>(R.id.setting_checklogin)
        val view_checkalarm     = layout.findViewById<CheckBox>(R.id.setting_checkalarm)
        val view_checkpush      = layout.findViewById<CheckBox>(R.id.setting_checkpush)
        val view_checkpush2     = layout.findViewById<CheckBox>(R.id.setting_checkpush2)
        val view_checkrefresh   = layout.findViewById<CheckBox>(R.id.setting_refresh)
        val view_radiotime1     = layout.findViewById<RadioButton>(R.id.setting_time1)
        val view_radiotime2     = layout.findViewById<RadioButton>(R.id.setting_time2)
        val view_radiotime3     = layout.findViewById<RadioButton>(R.id.setting_time3)
        val view_radiotime4     = layout.findViewById<RadioButton>(R.id.setting_time4)
        val view_radiotime5     = layout.findViewById<RadioButton>(R.id.setting_time5)
        val view_sensor         = layout.findViewById<CheckBox>(R.id.setting_sensor)
        val view_theme1         = layout.findViewById<RadioButton>(R.id.setting_theme1)
        val view_theme2         = layout.findViewById<RadioButton>(R.id.setting_theme2)
        val view_theme3         = layout.findViewById<RadioButton>(R.id.setting_theme3)
        val view_progressshow   = layout.findViewById<CheckBox>(R.id.setting_prgressshow)
        val view_sounduse       = layout.findViewById<CheckBox>(R.id.setting_sounduse)
        val view_listtime       = layout.findViewById<CheckBox>(R.id.setting_listtime)
        view_alerttext          = layout.findViewById(R.id.setting_alerttext)
        view_textsize1          = layout.findViewById(R.id.setting_textsize1)
        view_textsize2          = layout.findViewById(R.id.setting_textsize2)
        view_textsize3          = layout.findViewById(R.id.setting_textsize3)
        view_colorbox           = layout.findViewById(R.id.setting_colorbox)

        view_title.text = resources.getString(R.string.title_setting)
        view_back.setOnClickListener(this)

        appsetting = Settings.getAppSetting(activity!!)

        view_checklogin.isChecked = appsetting.isAutoLogin
        appsetting.isAlam = false
        view_checkalarm.isChecked = appsetting.isAlam
        view_checkpush.isChecked = appsetting.isPush
        view_checkpush2.isChecked = appsetting.isAlam2
        appsetting.isRefresh = false
        view_checkrefresh.isChecked = appsetting.isRefresh
        appsetting.refreshTime = 0
        view_radiotime1.isChecked = appsetting.refreshTime == 1
        view_radiotime2.isChecked = appsetting.refreshTime == 2
        view_radiotime3.isChecked = appsetting.refreshTime == 3
        view_radiotime4.isChecked = appsetting.refreshTime == 4
        view_radiotime5.isChecked = appsetting.refreshTime == 5
        appsetting.isSensor = false
        view_sensor.isChecked = appsetting.isSensor
        appsetting.themeType = 0
        view_theme1.isChecked = appsetting.themeType == 1
        view_theme2.isChecked = appsetting.themeType == 2
        view_theme3.isChecked = appsetting.themeType == 3
        view_alerttext?.isChecked = appsetting.isTextSet
        view_textsize1?.isChecked = appsetting.textSize == 1
        view_textsize2?.isChecked = appsetting.textSize == 2
        view_textsize3?.isChecked = appsetting.textSize == 3
        appsetting.isProgressShow = false
        view_progressshow.isChecked = appsetting.isProgressShow
        appsetting.isTimeView = false
        view_listtime.isChecked = appsetting.isTimeView
        view_sounduse.isChecked = appsetting.isSound

        if(appsetting.themeType == 1) {
            view_colorbox?.visibility = View.VISIBLE
        } else {
            //view_colorbox?.visibility = View.GONE
            view_colorbox?.visibility = View.VISIBLE
        }

        view_checklogin.setOnClickListener(this)
        view_checkalarm.setOnClickListener(this)
        view_checkpush.setOnClickListener(this)
        view_checkpush2.setOnClickListener(this)
        view_checkrefresh.setOnClickListener(this)
        view_radiotime1.setOnClickListener(this)
        view_radiotime2.setOnClickListener(this)
        view_radiotime3.setOnClickListener(this)
        view_radiotime4.setOnClickListener(this)
        view_radiotime5.setOnClickListener(this)
        view_sensor.setOnClickListener(this)
        view_theme1.setOnClickListener(this)
        view_theme2.setOnClickListener(this)
        view_theme3.setOnClickListener(this)
        view_alerttext?.setOnClickListener(this)
        view_textsize1?.setOnClickListener(this)
        view_textsize2?.setOnClickListener(this)
        view_textsize3?.setOnClickListener(this)
        view_progressshow.setOnClickListener(this)
        view_listtime.setOnClickListener(this)
        view_sounduse.setOnClickListener(this)

        view_checklogin.setOnCheckedChangeListener(this)
        view_checkalarm.setOnCheckedChangeListener(this)
        view_checkpush.setOnCheckedChangeListener(this)
        view_checkpush2.setOnCheckedChangeListener(this)
        view_checkrefresh.setOnCheckedChangeListener(this)
        view_sensor.setOnCheckedChangeListener(this)
        view_alerttext?.setOnCheckedChangeListener(this)
        view_progressshow.setOnCheckedChangeListener(this)

        return layout
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when(buttonView?.id) {
            R.id.setting_checklogin -> {
                appsetting.isAutoLogin = isChecked
            }
            R.id.setting_checkalarm -> {
                //appsetting.isAlam = isChecked
                appsetting.isAlam = !isChecked
            }
            R.id.setting_checkpush -> {
                appsetting.isPush = isChecked
            }
            R.id.setting_checkpush2 -> {
                appsetting.isAlam2 = isChecked
            }
            R.id.setting_refresh -> {
                //appsetting.isRefresh = isChecked
                appsetting.isRefresh = !isChecked
            }
            R.id.setting_sensor -> {
                //appsetting.isSensor = isChecked
                appsetting.isSensor = !isChecked
            }
            R.id.setting_alerttext -> {
                appsetting.isTextSet = isChecked
            }
            R.id.setting_prgressshow -> {
                //appsetting.isProgressShow = isChecked
                appsetting.isProgressShow = !isChecked
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.header_back -> {
                nactivity?.finish()
            }
            R.id.setting_checklogin -> {
                appsetting.isAutoLogin = (v as CheckBox).isChecked
            }
            R.id.setting_checkalarm -> {
                appsetting.isAlam = (v as CheckBox).isChecked
            }
            R.id.setting_checkpush -> {
                appsetting.isPush = (v as CheckBox).isChecked
            }
            R.id.setting_checkpush2 -> {
                appsetting.isAlam2 = (v as CheckBox).isChecked
            }
            R.id.setting_refresh -> {
                appsetting.isRefresh = (v as CheckBox).isChecked
            }
            R.id.setting_time1 -> {
                appsetting.refreshTime = 1
            }
            R.id.setting_time2 -> {
                appsetting.refreshTime = 2
            }
            R.id.setting_time3 -> {
                appsetting.refreshTime = 3
            }
            R.id.setting_time4 -> {
                appsetting.refreshTime = 4
            }
            R.id.setting_time5 -> {
                appsetting.refreshTime = 5
            }
            R.id.setting_sensor -> {
                appsetting.isSensor = (v as CheckBox).isChecked
            }
            R.id.setting_listtime -> {
                appsetting.isTimeView = (v as CheckBox).isChecked
            }
            R.id.setting_theme1 -> {
                appsetting.themeType = 1
                view_colorbox?.visibility = View.VISIBLE
            }
            R.id.setting_theme2 -> {
                appsetting.themeType = 2
                if((v as RadioButton).isChecked) {
                    view_alerttext?.isChecked   = true
                    appsetting.isTextSet        = true
                    //view_colorbox?.visibility = View.GONE
                    view_colorbox?.visibility = View.VISIBLE
                }
            }
            R.id.setting_theme3 -> {
                appsetting.themeType = 3
                if((v as RadioButton).isChecked) {
                    view_alerttext?.isChecked   = false
                    appsetting.isTextSet        = false
                   // view_colorbox?.visibility = View.GONE
                    view_colorbox?.visibility = View.VISIBLE
                }
            }
            R.id.setting_alerttext -> {
                appsetting.isTextSet = (v as CheckBox).isChecked
            }
            R.id.setting_textsize1 -> {
                if(appsetting.textSize != 1) {
                    //(activity as DefaultActivity).onAttachedToWindow()
                    showRestart(1)
                }
            }
            R.id.setting_textsize2 -> {
                if(appsetting.textSize != 2) {
                    //(activity as DefaultActivity).onAttachedToWindow()
                    showRestart(2)
                }
            }
            R.id.setting_textsize3 -> {
                if(appsetting.textSize != 3) {
                    //(activity as DefaultActivity).onAttachedToWindow()
                    showRestart(3)
                }
            }
            R.id.setting_prgressshow -> {
                appsetting.isProgressShow = (v as CheckBox).isChecked
            }

            R.id.setting_sounduse -> {
                appsetting.isSound = (v as CheckBox).isChecked
            }
        }

        Settings.setAppSetting(activity!!, appsetting)
    }

    fun showRestart(set: Int) {
        val dialog = AlertDialog(activity, AlertDialog.TYPE.YESNO, resources.getString(R.string.dialog_setting_size_title), resources.getString(R.string.dialog_setting_size_info), object: AlertDialog.Callback{
            override fun onNo() {
                view_textsize1?.isChecked = appsetting.textSize == 1
                view_textsize2?.isChecked = appsetting.textSize == 2
                view_textsize3?.isChecked = appsetting.textSize == 3
            }

            override fun onYes() {
                appsetting.textSize = set
                Settings.setAppSetting(activity!!, appsetting)
                UCargoActivity.appRestart()
            }
        })
        dialog.show()
    }

}