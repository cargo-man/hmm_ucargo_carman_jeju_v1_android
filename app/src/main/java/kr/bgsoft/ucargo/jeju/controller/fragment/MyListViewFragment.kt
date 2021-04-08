package kr.bgsoft.ucargo.jeju.controller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.viewpager.widget.ViewPager
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.SubActivity
import kr.bgsoft.ucargo.jeju.controller.adapter.MylistViewPagerAdapter
import kr.bgsoft.ucargo.jeju.controller.conf.Config
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment
import kr.bgsoft.ucargo.jeju.data.model.MyCargo
import kr.bgsoft.ucargo.jeju.data.model.Owner
import org.json.JSONArray

class MyListViewFragment : DefaultFragment(), View.OnClickListener {

    var nactivity: SubActivity? = null
    var npager: ViewPager? = null
    var npageradapter: MylistViewPagerAdapter? = null
    var nupdown: Button? = null
    var nstep1: LinearLayout? = null
    var ntype = 0
    var isCancle = false
    var isUpdown = 0
    var setstate = JSONArray()

    companion object {
        var owner = Owner()
        var mydata: MyCargo? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_mylistview, container, false)

        nactivity   = activity as SubActivity
        owner       = Owner("", "", "", "", "", "", "", "", "", "", "")

        mydata      = nactivity?.intent?.getSerializableExtra(Config.EXTRA_ORDER) as MyCargo

        val view_title      = layout.findViewById<TextView>(R.id.header_title)
        val view_back       = layout.findViewById<ImageButton>(R.id.header_back)
        val view_call       = layout.findViewById<Button>(R.id.mylistview_call)
        val view_cancle     = layout.findViewById<Button>(R.id.mylistview_cancle)
        val view_close      = layout.findViewById<Button>(R.id.mylistview_close)
        val view_send       = layout.findViewById<Button>(R.id.mylistview_send)
        val view_status1    = layout.findViewById<RadioButton>(R.id.mylistview_status1)
        val view_status2    = layout.findViewById<RadioButton>(R.id.mylistview_status2)
        val view_status3    = layout.findViewById<RadioButton>(R.id.mylistview_status3)
        val view_step2      = layout.findViewById<LinearLayout>(R.id.mylistview_btnstep2)
        val view_split1     = layout.findViewById<ImageView>(R.id.mylistview_split1)
        val view_split2     = layout.findViewById<ImageView>(R.id.mylistview_split2)
        val view_statusbox  = layout.findViewById<RadioGroup>(R.id.mylistview_statusbox)

        nstep1              = layout.findViewById(R.id.mylistview_btnstep1)
        nupdown             = layout.findViewById(R.id.mylistview_updown)

        return layout
    }

    override fun onClick(v: View?) {

    }
}