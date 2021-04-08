package kr.bgsoft.ucargo.jeju.controller.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.data.model.Board

class BoardAdapter(context: Context): BaseAdapter() {
    val CTX         = context
    var arrayData   = ArrayList<Board>()

    class Holder {
        var hTitle: TextView? = null
        var hDate: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: Holder? = null
        var layout = convertView

        val mydata = arrayData.get(position)

        if(layout == null) {
            holder = Holder()
            val inflater = CTX.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            layout = inflater.inflate(R.layout.adapter_board, null) as View

            holder.hTitle   = layout.findViewById(R.id.adpater_board_title)
            holder.hDate    = layout.findViewById(R.id.adpater_board_date)

            layout.tag = holder

        } else {
            holder = layout.tag as Holder
        }

        holder.hTitle?.text = mydata.title
        holder.hDate?.text = mydata.date.take(10).replace("-", ".")

        if(mydata.read) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.hTitle?.setTextColor(CTX.resources.getColor(R.color.colorUser4, null))
                holder.hDate?.setTextColor(CTX.resources.getColor(R.color.colorUser4, null))
            } else {
                holder.hTitle?.setTextColor(CTX.resources.getColor(R.color.colorUser4))
                holder.hDate?.setTextColor(CTX.resources.getColor(R.color.colorUser4))
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.hTitle?.setTextColor(CTX.resources.getColor(R.color.colorUser6, null))
                holder.hDate?.setTextColor(CTX.resources.getColor(R.color.colorUser6, null))
            } else {
                holder.hTitle?.setTextColor(CTX.resources.getColor(R.color.colorUser6))
                holder.hDate?.setTextColor(CTX.resources.getColor(R.color.colorUser6))
            }
        }

        return layout
    }

    override fun getItem(position: Int): Board {
        return arrayData.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arrayData.size
    }
    fun addData(idx: Int, title: String, date: String, read: Boolean, type: Int) {
        arrayData.add(Board(idx, title, "", date, read, type))
    }

    fun delete(position: Int) {
        arrayData.removeAt(position)
        dataChange()
    }

    fun clear() {
        arrayData = ArrayList<Board>()
    }

    fun dataChange() {
        notifyDataSetChanged()
    }
}