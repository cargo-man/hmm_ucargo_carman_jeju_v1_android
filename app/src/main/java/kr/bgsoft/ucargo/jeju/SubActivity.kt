package kr.bgsoft.ucargo.jeju

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import kr.bgsoft.ucargo.jeju.controller.conf.Config
import kr.bgsoft.ucargo.jeju.controller.fragment.*
import kr.bgsoft.ucargo.jeju.cview.DefaultActivity
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment

class SubActivity : DefaultActivity(), SensorEventListener {

    var isFinish = true
    var nfragment: Fragment? = null
    var nsensormanager: SensorManager? = null
    var nsensor: Sensor? = null
    var isMidium = false

    enum class TYPE {
        SPLASH, LIST, MYLIST, MYLISTVIEW, SETTING, JOIN, BOARD, BOARDVIEW, USERINFO, MYPAGE, LISTVIEW, WEBVIEW, RULEPAY, LOGIN, MORE,
        MYADD, MYADDEDIT, MYADDLIST, MYADDVIEW, BANK, BANKOUT, BANKTOUSER, NHH, JIBU, NOTICE, ALARM, HH1, HH2, HH3, HH4, HH5,
        CARDMAIN, CARDSEND, CARDVIEW, PARTNER, PASSWORD, QRCODE, NEWS, FAQ, EVENT, ADDJOIN
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ucargo)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.colorPrimary, null)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color.colorPrimary)
        }

        setContentView(R.layout.activity_sub)

        val exType = intent.getSerializableExtra(Config.EXTRA_TYPE) as TYPE
        nfragment = getFragment(exType)

        if (nfragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.sub_contents, nfragment!!, "content").commit()
        }

        if (exType == TYPE.LIST) {
            try {
                nsensormanager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
                nsensor = nsensormanager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            } catch (e: Exception) {

            }
        }
    }

    fun getFragment(type: TYPE): Fragment? {
        var fragment: Fragment? = null
        isMidium = false

        when (type) {
            TYPE.SPLASH -> {
                 fragment = SplashFragment()
            }
            TYPE.SETTING -> {
                isMidium = true
                fragment = SettingFragment()
            }
            TYPE.LIST -> {
                isMidium = true
                fragment = ListFragment()
                UCargoActivity.nactivity_list = this
            }
            TYPE.LISTVIEW -> {
                isMidium = true
                // fragment = ListViewFragment()
            }
            TYPE.LOGIN -> {
                  fragment = LoginFragment()
            }

            TYPE.MYLIST -> {
                isMidium = true
                // fragment = MyListFragment()
            }
            TYPE.MYLISTVIEW -> {
                isMidium = true
                // fragment = MyListViewFragment()
            }
            TYPE.MORE -> {
                fragment = MoreFragment()
            }
            TYPE.MYADD -> {
                isMidium = true
                // fragment = MyAddFragment()
            }
            TYPE.MYADDLIST -> {
                isMidium = true
                // fragment = MyAddListFragment()
            }
            TYPE.MYADDVIEW -> {
                isMidium = true
                //   fragment = MyAddViewFragment()
            }
            TYPE.MYADDEDIT -> {
                isMidium = true
                //   fragment = MyAddEditFragment()
            }
            TYPE.BANK -> {
                isMidium = true
                fragment = MyBankFragment()
            }

            TYPE.BANKOUT -> {
                isMidium = true
                //    fragment = MyBankoutFragment()
            }
            TYPE.BANKTOUSER -> {
                isMidium = true
                //   fragment = MyBankToUserFragment()
            }
            TYPE.NOTICE, TYPE.JIBU, TYPE.NHH, TYPE.BOARD, TYPE.NEWS, TYPE.FAQ -> {
                fragment = BoardFragment()
            }

            TYPE.BOARDVIEW -> {
                fragment = BoardViewFragment()
            }
            TYPE.ALARM -> {
                isMidium = true
                fragment = AlarmFragment()
            }
            TYPE.MYPAGE -> {
                fragment = MypageFragment()
            }
            TYPE.HH1, TYPE.HH2, TYPE.HH3, TYPE.HH4, TYPE.HH5 -> {
                fragment = HHMenuFragment()
            }
            /*
                TYPE.CARDMAIN -> {
                    fragment = CardMainFragment()
                }
                TYPE.CARDSEND -> {
                    fragment = CardSendFragment()
                }
                TYPE.CARDVIEW -> {
                    fragment = CardViewFragment()
                } */
            TYPE.PARTNER -> {
                fragment = PartnerFragment()
            }
            TYPE.JOIN -> {
                //  fragment = JoinFragment()
            }
            TYPE.PASSWORD -> {
                //   fragment = PasswordFragment()
            }

            TYPE.RULEPAY -> {
                fragment = WebFragment()
            }
            TYPE.ADDJOIN -> {
                //fragment = AddjoinFragment()
            }
            TYPE.EVENT -> {
                fragment = WebFragment()
            }

        }


        return fragment
    }

    override fun finish() {
        val exType = intent.getSerializableExtra(Config.EXTRA_TYPE) as TYPE

        when (exType) {
            TYPE.HH1, TYPE.HH2, TYPE.HH3, TYPE.HH4, TYPE.HH5 -> {
                (nfragment as HHMenuFragment).onFinish()
                isFinish = (nfragment as HHMenuFragment).isFinish
            }
            TYPE.CARDSEND -> {
                //isFinish = (nfragment as CardSendFragment).CardFinish()
            }
            TYPE.CARDVIEW -> {
                // isFinish = (nfragment as CardViewFragment).CardFinish()
            }
        }

        if (isFinish) {
            super.finish()
        }

        super.finish()

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        val exType = intent.getSerializableExtra(Config.EXTRA_TYPE) as TYPE

        if (exType == TYPE.LIST) {
            (nfragment as ListFragment).onSensorChanged(event)
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val exType = intent.getSerializableExtra(Config.EXTRA_TYPE) as TYPE
        TAG = exType.toString()
        return super.onTouch(v, event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        nfragment?.onActivityResult(requestCode, resultCode, data)
    }
}