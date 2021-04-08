package kr.bgsoft.ucargo.jeju.controller.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.SubActivity
import kr.bgsoft.ucargo.jeju.controller.adapter.HHSubPagerAdapter
import kr.bgsoft.ucargo.jeju.controller.conf.Config
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment
import kr.bgsoft.ucargo.jeju.cview.SwipeViewPager

class HHMenuFragment: DefaultFragment(), View.OnClickListener {

    var hh_inbox: SwipeViewPager?   = null
    var hh_subbox1: LinearLayout?   = null
    var hh_subbox2: LinearLayout?   = null
    var sub                         = 0
    var type: SubActivity.TYPE?     = null
    var hh_submenu1: RadioButton?   = null
    var hh_submenu2: RadioButton?   = null
    var hh_submenu3: RadioButton?   = null
    var hh_submenu4: RadioButton?   = null
    var hh_submenu5: RadioButton?   = null
    var hh_submenu6: RadioButton?   = null
    var hh_menu1: RadioButton?      = null
    var hh_menu2: RadioButton?      = null
    var hh_menu3: RadioButton?      = null
    var hh_menu4: RadioButton?      = null
    var hh_menu5: RadioButton?      = null
    var view_title: TextView?       = null
    var isFinish                    = true

    companion object {
        var nhandler: Handler?      = null
        val HHNEXT                  = 1
        val HHPREV                  = 2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_hhmenu, container, false)

        val exType      = activity?.intent?.getSerializableExtra(Config.EXTRA_TYPE) as SubActivity.TYPE

        type = exType
        val view_back   = layout.findViewById<ImageButton>(R.id.header_back)

        view_title      = layout.findViewById(R.id.header_title)
        hh_inbox        = layout.findViewById(R.id.hh_inbox)
        hh_menu1        = layout.findViewById(R.id.hh_menu1)
        hh_menu2        = layout.findViewById(R.id.hh_menu2)
        hh_menu3        = layout.findViewById(R.id.hh_menu3)
        hh_menu4        = layout.findViewById(R.id.hh_menu4)
        hh_menu5        = layout.findViewById(R.id.hh_menu5)
        hh_submenu1     = layout.findViewById(R.id.hh_submenu1)
        hh_submenu2     = layout.findViewById(R.id.hh_submenu2)
        hh_submenu3     = layout.findViewById(R.id.hh_submenu3)
        hh_submenu4     = layout.findViewById(R.id.hh_submenu4)
        hh_submenu5     = layout.findViewById(R.id.hh_submenu5)
        hh_submenu6     = layout.findViewById(R.id.hh_submenu6)

        hh_subbox1      = layout.findViewById(R.id.hh_submenu_box1)
        hh_subbox2      = layout.findViewById(R.id.hh_submenu_box2)

        hh_submenu1?.setOnClickListener(this)
        hh_submenu2?.setOnClickListener(this)
        hh_submenu3?.setOnClickListener(this)
        hh_submenu4?.setOnClickListener(this)
        hh_submenu5?.setOnClickListener(this)
        hh_submenu6?.setOnClickListener(this)

        hh_menu1?.setOnClickListener(this)
        hh_menu2?.setOnClickListener(this)
        hh_menu3?.setOnClickListener(this)
        hh_menu4?.setOnClickListener(this)
        hh_menu5?.setOnClickListener(this)
        view_back.setOnClickListener(this)

        val adapter = HHSubPagerAdapter(activity!!.supportFragmentManager, activity!!, exType)
        hh_inbox?.adapter = adapter

        hh_inbox?.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                when(position) {
                    1 -> {
                        hh_submenu1?.isChecked = false
                        hh_submenu2?.isChecked = true
                        hh_submenu3?.isChecked = false
                        hh_submenu4?.isChecked = false
                        hh_submenu5?.isChecked = false
                        hh_submenu6?.isChecked = false
                    }
                    2 -> {
                        hh_submenu1?.isChecked = false
                        hh_submenu2?.isChecked = false
                        hh_submenu3?.isChecked = true
                        hh_submenu4?.isChecked = false
                        hh_submenu5?.isChecked = false
                        hh_submenu6?.isChecked = false
                    }
                    3 -> {
                        hh_submenu1?.isChecked = false
                        hh_submenu2?.isChecked = false
                        hh_submenu3?.isChecked = false
                        hh_submenu4?.isChecked = true
                        hh_submenu5?.isChecked = false
                        hh_submenu6?.isChecked = false
                    }
                    4 -> {
                        hh_submenu1?.isChecked = false
                        hh_submenu2?.isChecked = false
                        hh_submenu3?.isChecked = false
                        hh_submenu4?.isChecked = false
                        hh_submenu5?.isChecked = true
                        hh_submenu6?.isChecked = false
                    }
                    5 -> {
                        hh_submenu1?.isChecked = false
                        hh_submenu2?.isChecked = false
                        hh_submenu3?.isChecked = false
                        hh_submenu4?.isChecked = false
                        hh_submenu5?.isChecked = false
                        hh_submenu6?.isChecked = true
                    }
                    else -> {
                        hh_submenu1?.isChecked = true
                        hh_submenu2?.isChecked = false
                        hh_submenu3?.isChecked = false
                        hh_submenu4?.isChecked = false
                        hh_submenu5?.isChecked = false
                        hh_submenu6?.isChecked = false
                    }
                }
            }

        })

        isFinish                = true
        changeView()

        nhandler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)

                val code                = msg.what
                hh_inbox?.currentItem   = code
            }
        }

        if(exType == SubActivity.TYPE.HH5) {
            val exBType = activity!!.intent.getIntExtra(Config.EXTRA_DATA, 1)
            if(exBType != null) {
                hh_inbox?.currentItem = exBType - 1
            }
        }

        return layout
    }

    fun changeView() {
        hh_inbox?.scroll        = true
        hh_inbox?.swipe         = true
        hh_subbox1?.visibility  = View.VISIBLE
        hh_subbox2?.visibility  = View.VISIBLE
        hh_submenu3?.visibility = View.VISIBLE
        hh_subbox1?.weightSum   = 3f


        when(type) {
            SubActivity.TYPE.HH1 -> {
                hh_subbox2?.visibility = View.GONE
                hh_submenu1?.text = resources.getString(R.string.hh_menu1_sub1)
                hh_submenu2?.text = resources.getString(R.string.hh_menu1_sub2)
                hh_submenu3?.text = resources.getString(R.string.hh_menu1_sub3)
                hh_menu1?.isChecked = true

                view_title?.text = resources.getString(R.string.hh_menu1)
            }
            SubActivity.TYPE.HH2 -> {
                hh_submenu3?.visibility = View.GONE
                hh_subbox2?.visibility  = View.GONE
                hh_subbox1?.weightSum = 2f

                hh_submenu1?.text = resources.getString(R.string.hh_menu2_sub1)
                hh_submenu2?.text = resources.getString(R.string.hh_menu2_sub2)
                //hh_submenu3?.text = resources.getString(R.string.hh_menu2_sub3)
                hh_menu2?.isChecked = true

                view_title?.text = resources.getString(R.string.hh_menu2)
            }
            SubActivity.TYPE.HH3 -> {
                hh_subbox1?.visibility = View.GONE
                hh_subbox2?.visibility = View.GONE

                hh_submenu1?.text = resources.getString(R.string.hh_menu3_sub1)
                hh_submenu2?.text = resources.getString(R.string.hh_menu3_sub2)
                hh_submenu3?.text = resources.getString(R.string.hh_menu3_sub3)
                hh_submenu4?.text = resources.getString(R.string.hh_menu3_sub4)
                hh_submenu5?.text = resources.getString(R.string.hh_menu3_sub5)
                hh_submenu6?.text = resources.getString(R.string.hh_menu3_sub6)
                hh_menu3?.isChecked = true

                hh_inbox?.scroll    = false
                hh_inbox?.swipe     = false

                view_title?.text = resources.getString(R.string.hh_menu3)
            }
            SubActivity.TYPE.HH4 -> {
                hh_subbox1?.weightSum = 2f
                hh_submenu3?.visibility = View.GONE
                hh_subbox2?.visibility  = View.GONE

                hh_submenu1?.text = resources.getString(R.string.hh_menu4_sub1)
                hh_submenu2?.text = resources.getString(R.string.hh_menu4_sub2)
                hh_menu4?.isChecked = true

                view_title?.text = resources.getString(R.string.hh_menu4)
            }
            SubActivity.TYPE.HH5 -> {
                hh_subbox1?.weightSum = 2f
                hh_submenu3?.visibility = View.GONE
                hh_subbox2?.visibility  = View.GONE

                hh_submenu1?.text = resources.getString(R.string.hh_menu5_sub1)
                hh_submenu2?.text = resources.getString(R.string.hh_menu5_sub2)
                hh_menu5?.isChecked = true

                view_title?.text = resources.getString(R.string.hh_menu5)
            }
        }

        hh_submenu1?.isChecked = true
        hh_submenu2?.isChecked = false
        hh_submenu3?.isChecked = false
        hh_submenu4?.isChecked = false
        hh_submenu5?.isChecked = false
        hh_submenu6?.isChecked = false
    }

    fun onFinish() {
        if(type == SubActivity.TYPE.HH3) {
            val item = if (hh_inbox?.currentItem != null) {
                hh_inbox?.currentItem.toString().toInt()
            } else {
                0
            }

            logd("item : " + item)
            hh_inbox?.currentItem = 0
            isFinish = item <= 0
        } else {
            isFinish = true
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.header_back -> {
                activity?.finish()
            }
            R.id.hh_menu1 -> {
                type = SubActivity.TYPE.HH1
                val adapter = HHSubPagerAdapter(activity!!.supportFragmentManager, activity!!, SubActivity.TYPE.HH1)
                hh_inbox?.adapter = adapter
                sub = 0
                hh_inbox?.currentItem = sub
                changeView()
            }
            R.id.hh_menu2 -> {
                type = SubActivity.TYPE.HH2
                val adapter = HHSubPagerAdapter(activity!!.supportFragmentManager, activity!!, SubActivity.TYPE.HH2)
                hh_inbox?.adapter = adapter
                sub = 0
                hh_inbox?.currentItem = sub
                changeView()
            }
            R.id.hh_menu3 -> {
                type = SubActivity.TYPE.HH3
                val adapter = HHSubPagerAdapter(activity!!.supportFragmentManager, activity!!, SubActivity.TYPE.HH3)
                hh_inbox?.adapter = adapter
                sub = 0
                hh_inbox?.currentItem = sub
                changeView()
            }
            R.id.hh_menu4 -> {
                type = SubActivity.TYPE.HH4
                val adapter = HHSubPagerAdapter(activity!!.supportFragmentManager, activity!!, SubActivity.TYPE.HH4)
                hh_inbox?.adapter = adapter
                sub = 0
                hh_inbox?.currentItem = sub
                changeView()
            }
            R.id.hh_menu5 -> {
                type = SubActivity.TYPE.HH5
                val adapter = HHSubPagerAdapter(activity!!.supportFragmentManager, activity!!, SubActivity.TYPE.HH5)
                hh_inbox?.adapter = adapter
                sub = 0
                hh_inbox?.currentItem = sub
                changeView()
            }
            R.id.hh_submenu1 -> {
                sub = 0
                hh_inbox?.currentItem = sub
                hh_submenu1?.isChecked = true
                hh_submenu2?.isChecked = false
                hh_submenu3?.isChecked = false
                hh_submenu4?.isChecked = false
                hh_submenu5?.isChecked = false
                hh_submenu6?.isChecked = false
            }
            R.id.hh_submenu2 -> {
                sub = 1
                hh_inbox?.currentItem = sub
                hh_submenu1?.isChecked = false
                hh_submenu2?.isChecked = true
                hh_submenu3?.isChecked = false
                hh_submenu4?.isChecked = false
                hh_submenu5?.isChecked = false
                hh_submenu6?.isChecked = false
            }
            R.id.hh_submenu3 -> {
                sub = 2
                hh_inbox?.currentItem = sub
                hh_submenu1?.isChecked = false
                hh_submenu2?.isChecked = false
                hh_submenu3?.isChecked = true
                hh_submenu4?.isChecked = false
                hh_submenu5?.isChecked = false
                hh_submenu6?.isChecked = false
            }
            R.id.hh_submenu4 -> {
                sub = 3
                hh_inbox?.currentItem = sub
                hh_submenu1?.isChecked = false
                hh_submenu2?.isChecked = false
                hh_submenu3?.isChecked = false
                hh_submenu4?.isChecked = true
                hh_submenu5?.isChecked = false
                hh_submenu6?.isChecked = false
            }
            R.id.hh_submenu5 -> {
                sub = 4
                hh_inbox?.currentItem = sub
                hh_submenu1?.isChecked = false
                hh_submenu2?.isChecked = false
                hh_submenu3?.isChecked = false
                hh_submenu4?.isChecked = false
                hh_submenu5?.isChecked = true
                hh_submenu6?.isChecked = false
            }
            R.id.hh_submenu6 -> {
                sub = 5
                hh_inbox?.currentItem = sub
                hh_submenu1?.isChecked = false
                hh_submenu2?.isChecked = false
                hh_submenu3?.isChecked = false
                hh_submenu4?.isChecked = false
                hh_submenu5?.isChecked = false
                hh_submenu6?.isChecked = true
            }
        }
    }
}