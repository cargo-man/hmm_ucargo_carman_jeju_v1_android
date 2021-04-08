package kr.bgsoft.ucargo.jeju.controller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.UCargoActivity
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment

class PartnerFragment: DefaultFragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_partner, container, false)

        val view_title      = layout.findViewById<TextView>(R.id.header_title)
        val view_back       = layout.findViewById<ImageButton>(R.id.header_back)
        val partner_menu1go = layout.findViewById<Button>(R.id.partner_menu1go)
        val partner_menu2go = layout.findViewById<Button>(R.id.partner_menu2go)
        val partner_menu3go = layout.findViewById<Button>(R.id.partner_menu3go)
        val partner_menu4go = layout.findViewById<Button>(R.id.partner_menu4go)
        val partner_menu5go = layout.findViewById<Button>(R.id.partner_menu5go)
        val partner_menu6go = layout.findViewById<Button>(R.id.partner_menu6go)
        val partner_menu7go = layout.findViewById<Button>(R.id.partner_menu7go)
        val partner_menu8go = layout.findViewById<Button>(R.id.partner_menu8go)
        val partner_menu9go = layout.findViewById<Button>(R.id.partner_menu9go)
        val partner_menu10go = layout.findViewById<Button>(R.id.partner_menu10go)

        view_back.setOnClickListener(this)
        partner_menu1go.setOnClickListener(this)
        partner_menu2go.setOnClickListener(this)
        partner_menu3go.setOnClickListener(this)
        partner_menu4go.setOnClickListener(this)
        partner_menu5go.setOnClickListener(this)
        partner_menu6go.setOnClickListener(this)
        partner_menu7go.setOnClickListener(this)
        partner_menu8go.setOnClickListener(this)
        partner_menu9go.setOnClickListener(this)
        partner_menu10go.setOnClickListener(this)

        view_title.text = resources.getString(R.string.title_partner)

        return layout
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.header_back -> {
                activity?.finish()
            }
            R.id.partner_menu1go -> {
                UCargoActivity.showBrowser(resources.getString(R.string.partner_url1))
            }
            R.id.partner_menu2go -> {
                UCargoActivity.showBrowser(resources.getString(R.string.partner_url2))
            }
            R.id.partner_menu3go -> {
                UCargoActivity.showBrowser(resources.getString(R.string.partner_url3))
            }
            R.id.partner_menu4go -> {
                UCargoActivity.showBrowser(resources.getString(R.string.partner_url4))
            }
            R.id.partner_menu5go -> {
                UCargoActivity.showBrowser(resources.getString(R.string.partner_url5))
            }
            R.id.partner_menu6go -> {
                UCargoActivity.showBrowser(resources.getString(R.string.partner_url6))
            }
            R.id.partner_menu7go -> {
                UCargoActivity.showBrowser(resources.getString(R.string.partner_url7))
            }
            R.id.partner_menu8go -> {
                UCargoActivity.showBrowser(resources.getString(R.string.partner_url8))
            }
            R.id.partner_menu9go -> {
                UCargoActivity.showBrowser(resources.getString(R.string.partner_url9))
            }
            R.id.partner_menu10go -> {
                UCargoActivity.showBrowser(resources.getString(R.string.partner_url10))
            }
        }
    }
}