package kr.bgsoft.ucargo.jeju.controller.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.data.model.BankInfo
import kr.bgsoft.ucargo.jeju.utils.Etc

class BankAdapter (context: Context) : BaseAdapter() {
    val CTX         = context
    var arrayData   = ArrayList<BankInfo>()

    class Holder {
        var hStatus: TextView?  = null
        var hInfo: TextView?   = null
        var hDate: TextView?    = null
        var hPoint: TextView?   = null
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: Holder? = null
        var layout = convertView

        val mydata = arrayData.get(position)

        if(layout == null) {
            holder = Holder()
            val inflater = CTX.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            layout = inflater.inflate(R.layout.adapter_bank, null) as View

            holder.hStatus  = layout.findViewById(R.id.adapter_bank_status)
            holder.hInfo    = layout.findViewById(R.id.adapter_bank_info)
            holder.hDate    = layout.findViewById(R.id.adapter_bank_date)
            holder.hPoint   = layout.findViewById(R.id.adapter_bank_point)

            layout.tag = holder

        } else {
            holder = layout.tag as Holder
        }

        var info = mydata.sNotes

        if(mydata.nInPoint != 0) {
            holder.hStatus?.text = CTX.resources.getString(R.string.mybank_status2)
            holder.hPoint?.text = Etc.setComma(mydata.nInPoint) + " " + CTX.resources.getString(R.string.info_pointadd)

            if(!mydata.sLoadLoc.isNullOrBlank() || !mydata.sDownLoc.isNullOrBlank()) {
                info += "\n" + mydata.sLoadLoc + " ➝ " + mydata.sDownLoc
            }
        } else if(mydata.nOutPoint != 0) {
            holder.hStatus?.text = CTX.resources.getString(R.string.mybank_status1)
            holder.hPoint?.text = Etc.setComma(mydata.nOutPoint) + " " + CTX.resources.getString(R.string.info_pointadd)

            if(!mydata.sLoadLoc.isNullOrBlank() || !mydata.sDownLoc.isNullOrBlank()) {
                info += "\n" + mydata.sLoadLoc + " ➝ " + mydata.sDownLoc
            }
        } else {
            holder.hStatus?.text = CTX.resources.getString(R.string.mybank_status2)
            holder.hPoint?.text = "0 " + CTX.resources.getString(R.string.info_pointadd)

            if(!mydata.sLoadLoc.isNullOrBlank() || !mydata.sDownLoc.isNullOrBlank()) {
                info += "\n" + mydata.sLoadLoc + " ➝ " + mydata.sDownLoc
            }
        }

        holder.hInfo?.text = info
        holder.hDate?.text = mydata.sRegdate.take(16).replace("-", ".")

        return layout
    }

    override fun getItem(position: Int): BankInfo {
        return arrayData.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arrayData.size
    }

    fun addData(idx: Int, info: String, status: Int, date: String, inpoint: Int, outpoint: Int, nowpoint: Int, loadaddr: String = "", downaddr: String = "") {
        arrayData.add(BankInfo(idx, info, status, date, inpoint, outpoint, nowpoint, loadaddr, downaddr))
    }

    fun delete(position: Int) {
        arrayData.removeAt(position)
        dataChange()
    }

    fun clear() {
        arrayData = ArrayList<BankInfo>()
    }

    fun dataChange() {
        notifyDataSetChanged()
    }

}