package kr.bgsoft.ucargo.jeju.controller.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.controller.adapter.CargoSettingAdapter
import kr.bgsoft.ucargo.jeju.cview.DefaultDialog
import kr.bgsoft.ucargo.jeju.data.model.Code

class EtcSettingDialog(context: Context?, mixup: String, sort: String, callback: Callback?) : DefaultDialog(context), View.OnClickListener {

    var nmixup = mixup
    var nsort = sort
    var nmixupcode = Code("", "")
    var nsortcode = Code("", "")
    val ncallback = callback
    var nadapter: CargoSettingAdapter? = null
    var ngridlist: GridView? = null
    var nadapter2: CargoSettingAdapter? = null
    var ngridlist2: GridView? = null

    interface Callback {
        fun onYes(mixup: Code, sort: Code)
        fun onNo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.dialog_etcsetting)

        logd("nmixup :: " + nmixup + " / nsort :: " + nsort)

        val view_ok         = findViewById<Button>(R.id.etcsetting_ok)
        val view_cancle     = findViewById<Button>(R.id.etcsetting_cancle)
        ngridlist           = findViewById(R.id.etcsetting_gridview1)
        ngridlist2          = findViewById(R.id.etcsetting_gridview2)

        view_ok.setOnClickListener(this)
        view_cancle.setOnClickListener(this)

        nadapter            = CargoSettingAdapter(thisContext!!)
        val arrayType1      = thisContext!!.resources.getStringArray(R.array.list_uptype)
        val arrayType1Code  = thisContext!!.resources.getStringArray(R.array.list_uptype_code)

        for(i in 0..(arrayType1.size - 1)) {
            if(nmixup == arrayType1Code[i]) {
                nmixupcode.code  = arrayType1Code[i].toString()
                nmixupcode.value = arrayType1[i]
            }

            nadapter?.addData(arrayType1[i], arrayType1Code[i].toString(), (nmixup == arrayType1Code[i]))
        }

        nadapter2           = CargoSettingAdapter(thisContext!!)
        val arrayType2      = thisContext!!.resources.getStringArray(R.array.list_sort)
        val arrayType2Code  = thisContext!!.resources.getStringArray(R.array.list_sort_code)

        for(i in 0..(arrayType2.size - 1)) {
            if(nsort == arrayType2Code[i]) {
                nsortcode.code  = arrayType2Code[i].toString()
                nsortcode.value = arrayType2[i]
            }

            nadapter2?.addData(arrayType2[i], arrayType2Code[i].toString(), (nsort == arrayType2Code[i]))
        }

        val checkcount = if(nadapter2 != null) { nadapter2!!.getCheckCount()} else { 0 }

        if(checkcount <= 0) {
            nadapter2!!.getItem(0).isSelect = true
        }

        ngridlist?.adapter  = nadapter
        ngridlist2?.adapter = nadapter2

        ngridlist?.onItemClickListener = AdapterView.OnItemClickListener {
            parent, view, position, id ->
            run {
                val data = nadapter?.getItem(position)

                nadapter?.getCheckClear()
                data?.isSelect = true
                nmixupcode.code  = data?.Code.toString()
                nmixupcode.value = data?.Name.toString()

                nadapter?.dataChange()
            }
        }
        ngridlist2?.onItemClickListener = AdapterView.OnItemClickListener {
            parent, view, position, id ->
            run {
                val data = nadapter2?.getItem(position)

                nadapter2?.getCheckClear()
                data?.isSelect = true
                nsortcode.code  = data?.Code.toString()
                nsortcode.value = data?.Name.toString()

                nadapter2?.dataChange()
            }
        }

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.etcsetting_ok -> {
                ncallback?.onYes(nmixupcode, nsortcode)
                dismiss()
            }
            R.id.etcsetting_cancle -> {
                ncallback?.onNo()
                dismiss()
            }
        }
    }
}