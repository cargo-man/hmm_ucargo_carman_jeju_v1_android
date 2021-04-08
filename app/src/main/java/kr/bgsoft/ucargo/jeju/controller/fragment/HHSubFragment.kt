package kr.bgsoft.ucargo.jeju.controller.fragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.UCargoActivity
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment
import kr.bgsoft.ucargo.jeju.data.model.Location

class HHSubFragment: DefaultFragment(), View.OnClickListener  {

    var sub = SUB.PAGE1_1

    enum class SUB {
        PAGE1_1, PAGE1_2, PAGE1_3, PAGE2_1, PAGE2_2/*, PAGE2_3*/, PAGE3_0, PAGE3_1, PAGE3_2, PAGE3_3, PAGE3_4, PAGE3_5, PAGE3_6, PAGE4_1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var layout: View? = null
        when(sub) {
            SUB.PAGE1_2 -> {
                layout = inflater!!.inflate(R.layout.fragment_page1_2, container, false)
            }
            SUB.PAGE1_3 -> {
                layout = inflater!!.inflate(R.layout.fragment_page1_3, container, false)

                val page1_3_gotel   = layout.findViewById<TextView>(R.id.page1_3_gotel)
                val page1_3_gomap   = layout.findViewById<ImageView>(R.id.page1_3_gomap)

                page1_3_gotel.setOnClickListener(this)
                page1_3_gomap.setOnClickListener(this)
            }
            SUB.PAGE2_1 -> {
                layout = inflater!!.inflate(R.layout.fragment_page2_1, container, false)
            }
            SUB.PAGE2_2 -> {
                layout = inflater!!.inflate(R.layout.fragment_page2_2, container, false)
            }
            /*SUB.PAGE2_3 -> {
                layout = inflater!!.inflate(R.layout.fragment_page2_3, container, false)
            }*/
            SUB.PAGE3_0 -> {
                layout = inflater!!.inflate(R.layout.fragment_page3_0, container, false)

                val menu1 = layout.findViewById<Button>(R.id.page3_menu1go)
                val menu2 = layout.findViewById<Button>(R.id.page3_menu2go)
                val menu3 = layout.findViewById<Button>(R.id.page3_menu3go)
                val menu4 = layout.findViewById<Button>(R.id.page3_menu4go)
                val menu5 = layout.findViewById<Button>(R.id.page3_menu5go)
                val menu6 = layout.findViewById<Button>(R.id.page3_menu6go)

                menu1.setOnClickListener(this)
                menu2.setOnClickListener(this)
                menu3.setOnClickListener(this)
                menu4.setOnClickListener(this)
                menu5.setOnClickListener(this)
                menu6.setOnClickListener(this)
            }
            SUB.PAGE3_1 -> {
                layout = inflater!!.inflate(R.layout.fragment_page3_1, container, false)
            }
            SUB.PAGE3_2 -> {
                layout = inflater!!.inflate(R.layout.fragment_page3_2, container, false)
            }
            SUB.PAGE3_3 -> {
                layout = inflater!!.inflate(R.layout.fragment_page3_3, container, false)
            }
            SUB.PAGE3_4 -> {
                layout = inflater!!.inflate(R.layout.fragment_page3_4, container, false)
            }
            SUB.PAGE3_5 -> {
                layout = inflater!!.inflate(R.layout.fragment_page3_5, container, false)
            }
            SUB.PAGE3_6 -> {
                layout = inflater!!.inflate(R.layout.fragment_page3_6, container, false)
            }
            SUB.PAGE4_1 -> {
                layout = inflater!!.inflate(R.layout.fragment_page4_1, container, false)
            }
            else -> {
                layout = inflater!!.inflate(R.layout.fragment_page1_1, container, false)
                val page1_1_info1 = layout.findViewById<TextView>(R.id.page1_1_info1)
                val page1_1_info2 = layout.findViewById<TextView>(R.id.page1_1_info2)

                /*var text1 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(resources.getString(R.string.page1_1_info1), Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(resources.getString(R.string.page1_1_info1))
                }
                var text2 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(resources.getString(R.string.page1_1_info2), Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(resources.getString(R.string.page1_1_info2))
                }

                page1_1_info1.text = text1
                page1_1_info2.text = text2*/
            }
        }
        return layout
    }
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.page1_3_gotel -> {
                val tel = (v as TextView).text.toString().replace("-", "")
                UCargoActivity
            }
            R.id.page1_3_gomap -> {
                val lat = 37.302779
                val lng = 127.004309
                UCargoActivity.showMapDialog(resources.getString(R.string.page1_3_title), Location(lat, lng, resources.getString(R.string.page1_3_title)))
            }
            R.id.page3_menu1go -> {
                HHMenuFragment.nhandler?.sendEmptyMessage(1)
            }
            R.id.page3_menu2go -> {
                HHMenuFragment.nhandler?.sendEmptyMessage(2)
            }
            R.id.page3_menu3go -> {
                HHMenuFragment.nhandler?.sendEmptyMessage(3)
            }
            R.id.page3_menu4go -> {
                HHMenuFragment.nhandler?.sendEmptyMessage(4)
            }
            R.id.page3_menu5go -> {
                HHMenuFragment.nhandler?.sendEmptyMessage(5)
            }
            R.id.page3_menu6go -> {
                HHMenuFragment.nhandler?.sendEmptyMessage(6)
            }
        }
    }
}