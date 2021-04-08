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

class BoardKAdapter(context: Context) : BaseAdapter()  {

    val CTX         = context
    var arrayData   = ArrayList<Board>()

    class Holder {
        var hTitle: TextView? = null
        var hDate: TextView? = null
        var hType: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: Holder? = null
        var layout = convertView

        val mydata = arrayData.get(position)

        if(layout == null) {
            holder = Holder()
            val inflater = CTX.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            layout = inflater.inflate(R.layout.adapter_boardk, null) as View

            holder.hTitle   = layout.findViewById(R.id.adpater_boardk_title)
            holder.hDate    = layout.findViewById(R.id.adpater_boardk_date)
            holder.hType    = layout.findViewById(R.id.adapter_boardk_type)

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
                holder.hDate?.setTextColor(CTX.resources.getColor(R.color.colorUser11, null))
            } else {
                holder.hTitle?.setTextColor(CTX.resources.getColor(R.color.colorUser6))
                holder.hDate?.setTextColor(CTX.resources.getColor(R.color.colorUser11))
            }
        }

        when(mydata.ttype) {
            CTX.resources.getString(R.string.boardk_type1) -> {
                holder.hType?.text = CTX.resources.getString(R.string.boardk_type1)
                holder.hType?.setBackgroundResource(R.drawable.rbox_type2_2)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.hType?.setTextColor(CTX.resources.getColor(R.color.colorListStatus2, null))
                } else {
                    holder.hType?.setTextColor(CTX.resources.getColor(R.color.colorListStatus2))
                }
            }
            CTX.resources.getString(R.string.boardk_type2) -> {
                holder.hType?.text = CTX.resources.getString(R.string.boardk_type2)
                holder.hType?.setBackgroundResource(R.drawable.rbox_type2_4)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.hType?.setTextColor(CTX.resources.getColor(R.color.colorListMixup, null))
                } else {
                    holder.hType?.setTextColor(CTX.resources.getColor(R.color.colorListMixup))
                }
            }
            CTX.resources.getString(R.string.boardk_type3) -> {
                holder.hType?.text = CTX.resources.getString(R.string.boardk_type3)
                holder.hType?.setBackgroundResource(R.drawable.rbox_type2_1)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.hType?.setTextColor(CTX.resources.getColor(R.color.colorListStatus1, null))
                } else {
                    holder.hType?.setTextColor(CTX.resources.getColor(R.color.colorListStatus1))
                }
            }
            CTX.resources.getString(R.string.boardk_type4) -> {
                holder.hType?.text = CTX.resources.getString(R.string.boardk_type4)
                holder.hType?.setBackgroundResource(R.drawable.rbox_type2_2)
                holder.hDate?.visibility = View.GONE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.hType?.setTextColor(CTX.resources.getColor(R.color.colorListStatus2, null))
                } else {
                    holder.hType?.setTextColor(CTX.resources.getColor(R.color.colorListStatus2))
                }
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

    fun addData(idx: Int, title: String, date: String, read: Boolean, type: Int, ttype: String) {
        arrayData.add(Board(idx, title, "", date, read, type, ttype))
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