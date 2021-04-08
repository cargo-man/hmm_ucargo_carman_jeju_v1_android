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

class DaySortDialog (context: Context?, loadtime: Int, downtime: Int, callback: Callback): DefaultDialog(context), View.OnClickListener{

    var nloadtime = loadtime
    var ndowntime = downtime
    var nload = Code("", "")
    var ndown = Code("", "")
    val ncallback = callback
    var nadapter: CargoSettingAdapter? = null
    var ngridlist: GridView? = null
    var nadapter2: CargoSettingAdapter? = null
    var ngridlist2: GridView? = null

    interface Callback {
        fun onYes(loadtime: Code, downtime: Code)
        fun onNo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.dialog_daysort)


        logd("loadtime :: " + nloadtime + " / downtime :: " + ndowntime)

        val view_ok = findViewById<Button>(R.id.daysort_ok)
        val view_cancle = findViewById<Button>(R.id.daysort_cancle)
        ngridlist = findViewById(R.id.daysort_gridview1)
        ngridlist2 = findViewById(R.id.daysort_gridview2)

        view_ok.setOnClickListener(this)
        view_cancle.setOnClickListener(this)

nadapter = CargoSettingAdapter(thisContext!!)
        val arrayType1 = thisContext!!.resources.getStringArray(R.array.daysort_type1)
        val arrayType1Code = thisContext!!.resources.getStringArray(R.array.daysort_type1_code)

        for (i in 0..(arrayType1.size - 1)) {
            if (nloadtime.toString() == arrayType1Code[i]) {
                nload.code = arrayType1Code[i].toString()
                nload.value = arrayType1[i]
            }

            nadapter?.addData(
                arrayType1[i],
                arrayType1Code[i].toString(),
                (nloadtime.toString() == arrayType1Code[i])
            )
        }

        nadapter2 = CargoSettingAdapter(thisContext!!)
        val arrayType2 = thisContext!!.resources.getStringArray(R.array.daysort_type2)
        val arrayType2Code = thisContext!!.resources.getStringArray(R.array.daysort_type2_code)

        for (i in 0..(arrayType2.size - 1)) {
            if (ndowntime.toString() == arrayType2Code[i]) {
                ndown.code = arrayType2Code[i].toString()
                ndown.value = arrayType2[i]
            }

            nadapter2?.addData(
                arrayType2[i],
                arrayType2Code[i].toString(),
                (ndowntime.toString() == arrayType2Code[i])
            )
        }
        ngridlist?.adapter  = nadapter
        ngridlist2?.adapter = nadapter2

        ngridlist?.onItemClickListener = AdapterView.OnItemClickListener {
                parent, view, position, id ->
            run {
                val data = nadapter?.getItem(position)

                nadapter?.getCheckClear()
                data?.isSelect = true
                nload.code  = data?.Code.toString()
                nload.value = data?.Name.toString()

                nadapter?.dataChange()
            }
        }
        ngridlist2?.onItemClickListener = AdapterView.OnItemClickListener {
                parent, view, position, id ->
            run {
                val data = nadapter2?.getItem(position)

                nadapter2?.getCheckClear()
                data?.isSelect = true
                ndown.code  = data?.Code.toString()
                ndown.value = data?.Name.toString()

                nadapter2?.dataChange()
            }
        }

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.daysort_ok -> {
                ncallback?.onYes(nload, ndown)
                dismiss()
            }
            R.id.daysort_cancle -> {
                ncallback?.onNo()
                dismiss()
            }
        }
    }
}