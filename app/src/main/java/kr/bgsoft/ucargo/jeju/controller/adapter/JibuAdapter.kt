package kr.bgsoft.ucargo.jeju.controller.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.UCargoActivity
import kr.bgsoft.ucargo.jeju.data.model.JibuInfo
import kr.bgsoft.ucargo.jeju.data.model.Location

class JibuAdapter(context: Context) : BaseAdapter() {
    val CTX         = context
    var arrayData   = ArrayList<JibuInfo>()

    class Holder {
        var hName: TextView? = null
        var hAddress: TextView? = null
        var hTel1: TextView? = null
        var hFax: TextView? = null
        var hTelgo: TextView? = null
        var hMapgo: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: Holder? = null
        var layout = convertView

        val mydata = arrayData.get(position)

        if(layout == null) {
            holder = Holder()
            val inflater = CTX.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            layout = inflater.inflate(R.layout.adapter_jibu, null) as View

            holder.hName    = layout.findViewById(R.id.adpater_jibu_name)
            holder.hAddress = layout.findViewById(R.id.adpater_jibu_address)
            holder.hTel1    = layout.findViewById(R.id.adpater_jibu_tel1)
            holder.hFax     = layout.findViewById(R.id.adpater_jibu_fax)
            holder.hTelgo   = layout.findViewById(R.id.adpater_jibu_telgo)
            holder.hMapgo   = layout.findViewById(R.id.adpater_jibu_mapgo)

            layout.tag = holder

        } else {
            holder = layout.tag as Holder
        }

        holder.hName?.text      = (mydata.name + "(${mydata.area})").replace(" ", "\u00A0")
        holder.hAddress?.text   = mydata.address.replace(" ", "\u00A0")
        holder.hTel1?.text      = mydata.tel1.replace(" ", "\u00A0") + if(mydata.tel2.isNullOrBlank()) { "" } else { " / " + mydata.tel2 }
        holder.hFax?.text       = mydata.fax.replace(" ", "\u00A0")

        holder.hMapgo?.setOnClickListener {
            UCargoActivity.showMapDialog(mydata.name, Location(mydata.lat, mydata.lng, mydata.name))
        }
        holder.hTelgo?.setOnClickListener {
            UCargoActivity.showCall(mydata.tel1.replace("-", ""))
        }

        return layout
    }

    override fun getItem(position: Int): JibuInfo {
        return arrayData.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arrayData.size
    }

    fun addData(data: JibuInfo) {
        arrayData.add(data)
    }

    fun delete(position: Int) {
        arrayData.removeAt(position)
        dataChange()
    }

    fun clear() {
        arrayData = ArrayList<JibuInfo>()
    }

    fun dataChange() {
        notifyDataSetChanged()
    }

    fun setSort() {
        arrayData.sortBy { it.name }
    }

}