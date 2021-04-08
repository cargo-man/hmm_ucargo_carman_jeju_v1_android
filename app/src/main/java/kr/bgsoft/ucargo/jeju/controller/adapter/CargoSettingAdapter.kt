package kr.bgsoft.ucargo.jeju.controller.adapter

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.data.model.CargoSetting
import kr.bgsoft.ucargo.jeju.utils.Etc

class CargoSettingAdapter(context: Context): BaseAdapter() {
    val CTX         = context
    var arrayData   = ArrayList<CargoSetting>()

    class Holder {
        var hName: TextView? = null
        var hSelect: ImageView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: Holder? = null
        var layout = convertView

        val mydata = arrayData.get(position)

        if(layout == null) {
            holder = Holder()
            val inflater = CTX.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            layout = inflater.inflate(R.layout.adapter_cargosetting, null) as View

            holder.hName    = layout.findViewById(R.id.adapter_cargosetting_text)
            holder.hSelect  = layout.findViewById(R.id.adapter_cargosetting_select)

            layout.tag = holder

        } else {
            holder = layout.tag as Holder
        }

        holder.hName?.text = mydata.Name

        if(mydata.isCenter) {
            holder.hName?.gravity = Gravity.CENTER
        } else {
            holder.hName?.gravity = Gravity.CENTER_VERTICAL

            val layoutParams = holder.hName?.layoutParams as RelativeLayout.LayoutParams
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0)
            val left = if(Etc.convertDpToPixel(20f, CTX) != null) { Etc.convertDpToPixel(30f, CTX)!!.toInt() } else { 0 }
            layoutParams.setMargins(left, 0, 0,0)
            holder.hName?.layoutParams = layoutParams

        }

        if(mydata.isSelect) {
            holder.hSelect?.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.hName?.setTextColor(CTX.resources.getColor(R.color.colorUser13, null))
            } else {
                holder.hName?.setTextColor(CTX.resources.getColor(R.color.colorUser13))
            }
        } else {
            holder.hSelect?.visibility = View.GONE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.hName?.setTextColor(CTX.resources.getColor(R.color.colorUser6, null))
            } else {
                holder.hName?.setTextColor(CTX.resources.getColor(R.color.colorUser6))
            }
        }

        return layout
    }

    override fun getItem(position: Int): CargoSetting {
        return arrayData.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arrayData.size
    }

    fun getSelectCount(): Int {
        var rtnvalue = 0

        for(i in 0..(arrayData.size - 1)) {
            if(arrayData[i].isSelect && arrayData[i].Code.indexOf("kktype") == -1) {
                rtnvalue++
            }
        }
        return rtnvalue
    }
    fun getCheckCode(): String {
        var rtnvalue = ""

        for (i in 0..(arrayData.size - 1)) {
            val mydata = getItem(i)

            if(mydata.isSelect) {
                if(rtnvalue.isNotBlank()) {
                    rtnvalue += "|"
                }
                rtnvalue += mydata.Code
            }
        }

        return rtnvalue
    }

    fun getCheckText(): String {
        var rtnvalue = ""

        for (i in 0..(arrayData.size - 1)) {
            val mydata = getItem(i)

            if(mydata.isSelect) {
                if(rtnvalue.isNotBlank()) {
                    rtnvalue += ", "
                }
                val name = mydata.Name.split("\n")

                rtnvalue += name[0]
            }
        }

        if(rtnvalue.isNullOrBlank()) {
            val mydata = getItem(0)
            rtnvalue = mydata.Name
        }

        return rtnvalue
    }
    fun addData(name: String, code: String, select: Boolean) {
        arrayData.add(CargoSetting(name, code, select))
    }

    fun addData(name: String, code: String, select: Boolean, iscenter: Boolean) {
        arrayData.add(CargoSetting(name, code, select, iscenter))
    }

    fun getCheckClear() {
        for (i in 0..(arrayData.size - 1)) {
            val mydata = getItem(i)
            mydata.isSelect = false
        }

        dataChange()
    }
    fun getCheckCount(): Int {
        var rtnvalue = 0

        for (i in 0..(arrayData.size - 1)) {
            val mydata = getItem(i)
            if(mydata.isSelect) {
                rtnvalue++
            }
        }

        return rtnvalue
    }

    fun delete(position: Int) {
        arrayData.removeAt(position)
        dataChange()
    }

    fun clear() {
        arrayData = ArrayList<CargoSetting>()
    }

    fun dataChange() {
        notifyDataSetChanged()
    }
}