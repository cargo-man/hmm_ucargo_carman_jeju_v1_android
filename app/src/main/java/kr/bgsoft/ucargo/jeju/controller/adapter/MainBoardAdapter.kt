package kr.bgsoft.ucargo.jeju.controller.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.controller.conf.Config
import kr.bgsoft.ucargo.jeju.data.model.Board
import kr.bgsoft.ucargo.jeju.utils.Etc

class MainBoardAdapter (context: Context) : BaseAdapter() {
    val CTX         = context
    var arrayData   = ArrayList<Board>()
    var setsize     = Etc.convertDpToPixel(45f, context)!!.toInt()

    class Holder {
        var hArrow: ImageView?  = null
        var hTitle: TextView?   = null
        var hType: TextView?    = null
        var hBox: LinearLayout? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: Holder? = null
        var layout = convertView

        val mydata = arrayData.get(position)

        if(layout == null) {
            holder = Holder()
            val inflater = CTX.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            layout = inflater.inflate(R.layout.adapter_mainboardlist, null) as View

            holder.hArrow   = layout.findViewById(R.id.adapter_mainboard_arrow)
            holder.hTitle   = layout.findViewById(R.id.adapter_mainboard_text)
            holder.hType    = layout.findViewById(R.id.adapter_mainboard_type)
            holder.hBox     = layout.findViewById(R.id.adapter_mainboard_box)

            layout.tag = holder

        } else {
            holder = layout.tag as Holder
        }

        holder.hTitle?.text         = mydata.title
        holder.hArrow?.visibility   = View.VISIBLE

        val param = holder.hBox?.layoutParams
        param?.height = setsize?.toInt()
        holder.hBox?.layoutParams = param

        if(mydata.type == Config.BOARD_HNOTICE && mydata.ttype == "mainlink") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.hTitle?.setTextColor(CTX.resources.getColor(R.color.colorListStatus1, null))
            } else {
                holder.hTitle?.setTextColor(CTX.resources.getColor(R.color.colorListStatus1))
            }
            holder.hArrow?.visibility   = View.GONE
        } else if(mydata.type == Config.BOARD_JIBU && mydata.ttype == "mainlink") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.hTitle?.setTextColor(CTX.resources.getColor(R.color.colorListStatus1, null))
            } else {
                holder.hTitle?.setTextColor(CTX.resources.getColor(R.color.colorListStatus1))
            }
            holder.hArrow?.visibility   = View.GONE

            val top = Etc.convertDpToPixel(10f, CTX)!!.toInt()
            val param = holder.hBox?.layoutParams as RelativeLayout.LayoutParams

            param.topMargin = top
            holder.hBox?.layoutParams = param
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.hTitle?.setTextColor(CTX.resources.getColor(R.color.colorUser4, null))
            } else {
                holder.hTitle?.setTextColor(CTX.resources.getColor(R.color.colorUser4))
            }

            val param = holder.hBox?.layoutParams as RelativeLayout.LayoutParams
            param.topMargin = 0
            holder.hBox?.layoutParams = param
        }

        if(mydata.title.isNullOrBlank()) {
            holder.hArrow?.visibility   = View.GONE
        }

        if(mydata.type == Config.BOARD_HNOTICE || mydata.type == Config.BOARD_JIBU) {
            holder.hType?.visibility = View.GONE
        } else {
            holder.hType?.visibility = View.VISIBLE
        }

        when(mydata.ttype) {
            CTX.resources.getString(R.string.boardk_type1) -> {
                holder.hType?.visibility = View.VISIBLE
                holder.hType?.text = CTX.resources.getString(R.string.boardk_type1)
                holder.hType?.setBackgroundResource(R.drawable.rbox_type2_2)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.hType?.setTextColor(CTX.resources.getColor(R.color.colorListStatus2, null))
                } else {
                    holder.hType?.setTextColor(CTX.resources.getColor(R.color.colorListStatus2))
                }
            }
            CTX.resources.getString(R.string.boardk_type2) -> {
                holder.hType?.visibility = View.VISIBLE
                holder.hType?.text = CTX.resources.getString(R.string.boardk_type2)
                holder.hType?.setBackgroundResource(R.drawable.rbox_type2_4)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.hType?.setTextColor(CTX.resources.getColor(R.color.colorListMixup, null))
                } else {
                    holder.hType?.setTextColor(CTX.resources.getColor(R.color.colorListMixup))
                }
            }
            CTX.resources.getString(R.string.boardk_type3) -> {
                holder.hType?.visibility = View.VISIBLE
                holder.hType?.text = CTX.resources.getString(R.string.boardk_type3)
                holder.hType?.setBackgroundResource(R.drawable.rbox_type2_1)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.hType?.setTextColor(CTX.resources.getColor(R.color.colorListStatus1, null))
                } else {
                    holder.hType?.setTextColor(CTX.resources.getColor(R.color.colorListStatus1))
                }
            }
            CTX.resources.getString(R.string.boardk_type4) -> {
                holder.hType?.visibility = View.VISIBLE
                holder.hType?.text = CTX.resources.getString(R.string.boardk_type4)
                holder.hType?.setBackgroundResource(R.drawable.rbox_type2_2)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.hType?.setTextColor(CTX.resources.getColor(R.color.colorListStatus2, null))
                } else {
                    holder.hType?.setTextColor(CTX.resources.getColor(R.color.colorListStatus2))
                }
            }
            else -> {
                holder.hType?.visibility = View.GONE
            }
        }

        return layout
    }

    override fun getItem(position: Int): Board {
        return arrayData[position]
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

    fun addData(board: Board) {
        arrayData.add(board)
    }

    fun delete(position: Int) {
        arrayData.removeAt(position)
        dataChange()
    }

    fun setHeight(px: Int) {
        setsize = px
    }

    fun clear() {
        arrayData = ArrayList<Board>()
    }

    fun dataChange() {
        notifyDataSetChanged()
    }
}