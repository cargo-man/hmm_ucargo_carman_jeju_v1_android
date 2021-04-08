package kr.bgsoft.ucargo.jeju.controller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.RelativeLayout
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.controller.adapter.JibuAdapter
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment
import kr.bgsoft.ucargo.jeju.data.model.JibuInfo

class JibuListFragment: DefaultFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater!!.inflate(R.layout.fragment_jibulist, container, false)

        val view_list   = layout.findViewById<ListView>(R.id.board_list)
        val view_empty  = layout.findViewById<RelativeLayout>(R.id.board_empty)

        val adapter     = JibuAdapter(activity!!)

        val arrName     = resources.getStringArray(R.array.jibu_name)
        val arrArea     = resources.getStringArray(R.array.jibu_area)
        val arrAddress  = resources.getStringArray(R.array.jibu_address)
        val arrTel1     = resources.getStringArray(R.array.jibu_tel1)
        val arrTel2     = resources.getStringArray(R.array.jibu_tel2)
        val arrFax      = resources.getStringArray(R.array.jibu_fax)
        val arrLocation = resources.getStringArray(R.array.jibu_location)

        for(i in 0..(arrName.size - 1)) {
            val arrLocation = arrLocation[i].split("|")
            val jibu        = JibuInfo()
            jibu.name       = arrName[i]
            jibu.area       = arrArea[i]
            jibu.address    = arrAddress[i]
            jibu.tel1       = arrTel1[i]
            jibu.tel2       = arrTel2[i]
            jibu.fax        = arrFax[i]
            jibu.lat        = arrLocation[0].toDouble()
            jibu.lng        = arrLocation[1].toDouble()

            adapter.addData(jibu)
        }

        adapter.setSort()

        view_empty.visibility = View.GONE
        view_list.adapter = adapter

        return layout
    }

}