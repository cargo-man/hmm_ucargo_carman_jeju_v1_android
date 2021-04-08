package kr.bgsoft.ucargo.jeju.controller.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.controller.conf.Settings
import kr.bgsoft.ucargo.jeju.data.model.Cargo
import kr.bgsoft.ucargo.jeju.utils.Etc
import java.util.*
import kotlin.math.ceil

class CargoListAdapter (context: Context): BaseAdapter() {

    val CTX         = context
    var arrayData   = ArrayList<Cargo>()
    var appsetting  = Settings.getAppSetting(context)
    var isNight     = Etc.getNight(appsetting)
    var nSortType   = ""

    class Holder {
        var hBox: LinearLayout?     = null
        var hStart: TextView?       = null
        var hEnd: TextView?         = null
        var hStartDong: TextView?   = null
        var hEndDong: TextView?     = null
        var hUpStatus: TextView?    = null
        var hKm: TextView?          = null
        var hDownStatus: TextView?  = null
        var hStartTime: TextView?   = null
        var hEndTime: TextView?     = null
        var hCarSize: TextView?     = null
        var hEtc: TextView?         = null
        var hPayType: TextView?     = null
        var hPay: TextView?         = null
        var hLine: ImageView?       = null
        var hCaricon: ImageView?    = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var holder: Holder? = null
        var layout  = convertView

        val mydata  = getItem(position)

        if(layout == null) {
            holder = Holder()
            val inflater = CTX.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            layout = inflater.inflate(R.layout.adapter_list3, null) as View

            holder.hBox         = layout.findViewById(R.id.adapter_list_box)
            holder.hStart       = layout.findViewById(R.id.adapter_list_start)
            holder.hStartDong   = layout.findViewById(R.id.adapter_list_startdong)
            holder.hEnd         = layout.findViewById(R.id.adapter_list_end)
            holder.hEndDong     = layout.findViewById(R.id.adapter_list_enddong)
            holder.hUpStatus    = layout.findViewById(R.id.adapter_list_upstatus)
            holder.hKm          = layout.findViewById(R.id.adapter_list_km)
            holder.hDownStatus  = layout.findViewById(R.id.adapter_list_downstatus)
            holder.hStartTime   = layout.findViewById(R.id.adapter_list_starttime)
            holder.hEndTime     = layout.findViewById(R.id.adapter_list_endtime)
            holder.hCarSize     = layout.findViewById(R.id.adapter_list_carsizetype)
            holder.hEtc         = layout.findViewById(R.id.adapter_list_etc)
            holder.hPayType     = layout.findViewById(R.id.adapter_list_paytype)
            holder.hPay         = layout.findViewById(R.id.adapter_list_pay)
            holder.hLine        = layout.findViewById(R.id.adapter_list2_line)
            holder.hCaricon     = layout.findViewById(R.id.adapter_list_route)

            layout.tag = holder

        } else {
            holder = layout.tag as Holder
        }

        val arrayStart  = mydata.sLoadLoc.split(" ")
        var startDong   = ""
        var startCity   = ""

        val arrayEnd  = mydata.sDownLoc.split(" ")
        var endDong     = ""
        var endCity     = ""

        when(arrayStart.count()) {
            1 -> {
                startCity = arrayStart[0].take(2)
                endDong = arrayStart[0].takeLast(arrayStart[0].length - 2)
            }
            else -> {
                for (i in 0 until arrayStart.count()) {
                    if(!arrayStart[i].isNullOrBlank()) {
                        if (i in 1..2) {
                            if (startDong.isNotBlank()) {
                                startDong += " "
                            }
                            startDong += arrayStart[i]
                        } else if (i == 0) {
                            startCity = arrayStart[i]
                        }
                    }
                }
            }
        }

        when(arrayEnd.count()) {
            1 -> {
                endCity = arrayEnd[0].take(2)
                endDong = arrayEnd[0].takeLast(arrayEnd[0].length - 2)
            }
            else -> {
                for (i in 0 until arrayEnd.count()) {
                    if(!arrayEnd[i].isNullOrBlank()) {
                        if (i in 1..2) {
                            if (endDong.isNotBlank()) {
                                endDong += " "
                            }
                            endDong += arrayEnd[i]
                        } else if (i == 0) {
                            endCity = arrayEnd[i]
                        }
                    }
                }
            }
        }

        //App.Log.d("천재", "start "+ startDong +" :: " + Etc.length(startDong) + " / end "+ endDong +" :: " + Etc.length(endDong))

        var cutstring = 15
        when(appsetting.textSize) {
            2 -> {
                cutstring = 12
            }
            3 -> {
                cutstring = 18
            }
        }

        if(Etc.length(startDong) > cutstring) {
            startDong = Etc.substring(startDong, cutstring)
        }
        if(Etc.length(endDong) > cutstring) {
            endDong = Etc.substring(endDong, cutstring)
        }

        holder.hStart?.text     = startCity.trim()
        holder.hStartDong?.text = " " + startDong.trim()
        holder.hEnd?.text       = endCity.trim()
        holder.hEndDong?.text   = " " + endDong.trim()

        /**
         * theme settings
         *
         * */

        if(appsetting.themeType == 1) {
            appsetting.isTextSet = !isNight
        }

        if(isNight) {
            holder.hLine?.setImageResource(R.color.colorUser21)
            holder.hCaricon?.setImageResource(R.drawable.ico_route_d)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.hBox?.setBackgroundColor(CTX.resources.getColor(R.color.colorUser22, null))
                holder.hStart?.setTextColor(CTX.resources.getColor(R.color.colorUser44, null))
                holder.hStartDong?.setTextColor(CTX.resources.getColor(R.color.colorUser44, null))
                holder.hEnd?.setTextColor(CTX.resources.getColor(R.color.colorUser44, null))
                holder.hEndDong?.setTextColor(CTX.resources.getColor(R.color.colorUser44, null))
                holder.hStartTime?.setTextColor(CTX.resources.getColor(R.color.colorUser5, null))
                holder.hEndTime?.setTextColor(CTX.resources.getColor(R.color.colorUser5, null))
                holder.hPay?.setTextColor(CTX.resources.getColor(R.color.colorListStatus3n, null))
                holder.hCarSize?.setTextColor(CTX.resources.getColor(R.color.colorUser5, null))
                holder.hEtc?.setTextColor(CTX.resources.getColor(R.color.colorUser5, null))
                holder.hKm?.setTextColor(CTX.resources.getColor(R.color.colorUser5, null))
            } else {
                holder.hBox?.setBackgroundColor(CTX.resources.getColor(R.color.colorUser22))
                holder.hStart?.setTextColor(CTX.resources.getColor(R.color.colorUser44))
                holder.hStartDong?.setTextColor(CTX.resources.getColor(R.color.colorUser44))
                holder.hEnd?.setTextColor(CTX.resources.getColor(R.color.colorUser44))
                holder.hEndDong?.setTextColor(CTX.resources.getColor(R.color.colorUser44))
                holder.hStartTime?.setTextColor(CTX.resources.getColor(R.color.colorUser5))
                holder.hEndTime?.setTextColor(CTX.resources.getColor(R.color.colorUser5))
                holder.hPay?.setTextColor(CTX.resources.getColor(R.color.colorListStatus3n))
                holder.hCarSize?.setTextColor(CTX.resources.getColor(R.color.colorUser5))
                holder.hEtc?.setTextColor(CTX.resources.getColor(R.color.colorUser5))
                holder.hKm?.setTextColor(CTX.resources.getColor(R.color.colorUser5))
            }

            if(appsetting.isTextSet) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.hStart?.setTextColor(CTX.resources.getColor(R.color.colorUser13, null))
                    holder.hEnd?.setTextColor(CTX.resources.getColor(R.color.colorUser45, null))
                    holder.hStartDong?.setTextColor(CTX.resources.getColor(R.color.colorUser13, null))
                    holder.hEndDong?.setTextColor(CTX.resources.getColor(R.color.colorUser45, null))
                } else {
                    holder.hStart?.setTextColor(CTX.resources.getColor(R.color.colorUser13))
                    holder.hEnd?.setTextColor(CTX.resources.getColor(R.color.colorUser45))
                    holder.hStartDong?.setTextColor(CTX.resources.getColor(R.color.colorUser13))
                    holder.hEndDong?.setTextColor(CTX.resources.getColor(R.color.colorUser45))
                }
            }
        } else {
            if (appsetting.isTextSet) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.hStart?.setTextColor(CTX.resources.getColor(R.color.colorUser46, null))
                    holder.hEnd?.setTextColor(CTX.resources.getColor(R.color.colorUser47, null))
                    holder.hStartDong?.setTextColor(CTX.resources.getColor(R.color.colorUser46, null))
                    holder.hEndDong?.setTextColor(CTX.resources.getColor(R.color.colorUser47, null))
                } else {
                    holder.hStart?.setTextColor(CTX.resources.getColor(R.color.colorUser46))
                    holder.hEnd?.setTextColor(CTX.resources.getColor(R.color.colorUser47))
                    holder.hStartDong?.setTextColor(CTX.resources.getColor(R.color.colorUser46))
                    holder.hEndDong?.setTextColor(CTX.resources.getColor(R.color.colorUser47))
                }
            }
        }

        val start       = Etc.convertStringToDate(mydata.sLoadDay)
        val startdate   = Etc.convertDateToString(start, "yyyy-MM-dd")
        var starttime   = Etc.convertDateToString(start, "HH:mm")

        val end       = Etc.convertStringToDate(mydata.sDownDay.take(16), "yyyy-MM-dd HH:mm")
        val enddate   = Etc.convertDateToString(end, "yyyy-MM-dd")
        var endtime   = Etc.convertDateToString(end, "HH:mm")

        val now         = Date()
        val nowdate     = Etc.convertDateToString(now, "yyyy-MM-dd")
        val add1date    = Etc.addDayToString(now, 1, "yyyy-MM-dd")

        var resid       = 0
        var stringid    = 0
        var colorid     = 0

        if(isNight) {
            if (startdate == nowdate) {
                resid = R.drawable.box_type3_26
                stringid = R.string.list_status1
                colorid = android.R.color.white
            } else if (startdate == add1date) {
                resid = R.drawable.box_type3_27
                stringid = R.string.list_status2
                colorid = android.R.color.white
            } else {
                resid = R.drawable.box_type3_28
                stringid = R.string.list_status3
                colorid = android.R.color.white
                starttime = Etc.convertDateToString(start, "MM.dd/HH:mm")
            }
        } else {
            if (startdate == nowdate) {
                resid = R.drawable.box_type3_21
                stringid = R.string.list_status1
                colorid = android.R.color.white
            } else if (startdate == add1date) {
                resid = R.drawable.box_type3_22
                stringid = R.string.list_status2
                colorid = android.R.color.white
            } else {
                resid = R.drawable.box_type3_23
                stringid = R.string.list_status3
                colorid = android.R.color.white
                starttime = Etc.convertDateToString(start, "MM.dd/HH:mm")
            }
        }

        holder.hUpStatus?.text = CTX.resources.getString(stringid)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.hUpStatus?.setTextColor(CTX.resources.getColor(colorid, null))
        } else {
            holder.hUpStatus?.setTextColor(CTX.resources.getColor(colorid))
        }
        holder.hUpStatus?.setBackgroundResource(resid)


        holder.hKm?.text = if(mydata.nDistance.isNullOrBlank() || mydata.nDistance == "0") {
            "-"
        } else {
            if(mydata.nDistance.toDouble() > 1000) {
                ceil(mydata.nDistance.toDouble() / 1000).toInt().toString() + "km"
            } else {
                ceil(mydata.nDistance.toDouble()).toInt().toString() + "km"
            }
        }

        if(isNight) {
            if (enddate == nowdate) {
                resid = R.drawable.box_type3_21
                stringid = R.string.list_dstatus1
                colorid = android.R.color.white
            } else if (enddate == add1date) {
                resid = R.drawable.box_type3_22
                stringid = R.string.list_dstatus2
                colorid = android.R.color.white
            } else {
                resid = R.drawable.box_type3_23
                stringid = R.string.list_dstatus3
                colorid = android.R.color.white
                holder.hStartTime?.visibility = View.VISIBLE
                endtime = Etc.convertDateToString(end, "MM.dd/HH:mm")
            }
        } else {
            if (enddate == nowdate) {
                resid = R.drawable.box_type3_21
                stringid = R.string.list_dstatus1
                colorid = android.R.color.white
            } else if (enddate == add1date) {
                resid = R.drawable.box_type3_22
                stringid = R.string.list_dstatus2
                colorid = android.R.color.white
            } else {
                resid = R.drawable.box_type3_23
                stringid = R.string.list_dstatus3
                colorid = android.R.color.white
                holder.hStartTime?.visibility = View.VISIBLE
                endtime = Etc.convertDateToString(end, "MM.dd/HH:mm")
            }
        }

        holder.hDownStatus?.text = CTX.resources.getString(stringid)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.hDownStatus?.setTextColor(CTX.resources.getColor(colorid, null))
        } else {
            holder.hDownStatus?.setTextColor(CTX.resources.getColor(colorid))
        }
        holder.hDownStatus?.setBackgroundResource(resid)

        holder.hStartTime?.text = starttime
        holder.hEndTime?.text   = endtime

        if(appsetting.isTimeView || starttime.length > 5) {
            holder.hStartTime?.visibility   = View.VISIBLE
        } else {
            holder.hStartTime?.visibility   = View.GONE
        }
        if(appsetting.isTimeView || endtime.length > 5) {
            holder.hEndTime?.visibility     = View.VISIBLE
        } else {
            holder.hEndTime?.visibility     = View.GONE
        }

        val mixuptext = if(CTX.resources.getString(R.string.list_uptype1) == mydata.sLoadtype && mydata.sGoodsInfo.indexOf(CTX.resources.getString(
                        R.string.list_uptype1)) == -1) {
            CTX.resources.getString(R.string.list_uptype1) + " - "
        } else {
            ""
        }

        holder.hCarSize?.text = mydata.sCarton + " / " + mydata.sCarType.replace("/", "")

        if(isNight) {
            resid = R.drawable.box_type3_29
            colorid = R.color.colorUser43

            when (mydata.sPayment) {
                CTX.resources.getString(R.string.list_pay1) -> {
                    resid = R.drawable.box_type3_25
                    stringid = R.string.list_pay1
                    colorid = android.R.color.white
                }
                CTX.resources.getString(R.string.list_pay2) -> {
                    stringid = R.string.list_pay2
                }
                CTX.resources.getString(R.string.list_pay3) -> {
                    resid = R.drawable.box_type3_25
                    stringid = R.string.list_pay3
                    colorid = android.R.color.white
                }
                CTX.resources.getString(R.string.list_pay4) -> {
                    stringid = R.string.list_pay4
                }
                CTX.resources.getString(R.string.list_pay5) -> {
                    stringid = R.string.list_pay5
                }
                CTX.resources.getString(R.string.list_pay6) -> {
                    stringid = R.string.list_pay6
                }
                CTX.resources.getString(R.string.list_pay7) -> {
                    stringid = R.string.list_pay7
                    holder.hPayType?.textSize = 13f
                }
            }
        } else {
            colorid = android.R.color.white

            when (mydata.sPayment) {
                CTX.resources.getString(R.string.list_pay1) -> {
                    resid = R.drawable.box_type3_25
                    stringid = R.string.list_pay1
                }
                CTX.resources.getString(R.string.list_pay2) -> {
                    resid = R.drawable.box_type3_24
                    stringid = R.string.list_pay2
                }
                CTX.resources.getString(R.string.list_pay3) -> {
                    resid = R.drawable.box_type3_25
                    stringid = R.string.list_pay3
                }
                CTX.resources.getString(R.string.list_pay4) -> {
                    resid = R.drawable.box_type3_24
                    stringid = R.string.list_pay4
                }
                CTX.resources.getString(R.string.list_pay5) -> {
                    resid = R.drawable.box_type3_24
                    stringid = R.string.list_pay5
                }
                CTX.resources.getString(R.string.list_pay6) -> {
                    resid = R.drawable.box_type3_24
                    stringid = R.string.list_pay6
                }
                CTX.resources.getString(R.string.list_pay7) -> {
                    resid = R.drawable.box_type3_24
                    stringid = R.string.list_pay7
                    holder.hPayType?.textSize = 13f
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.hPayType?.setTextColor(CTX.resources.getColor(colorid, null))
        } else {
            holder.hPayType?.setTextColor(CTX.resources.getColor(colorid))
        }
        holder.hPayType?.text = CTX.resources.getString(stringid)
        holder.hPayType?.setBackgroundResource(resid)

        holder.hEtc?.text = if(mydata.sGoodsInfo.isNullOrBlank()) {
            mixuptext + ""
        } else {
            mixuptext + mydata.sGoodsInfo
        }

        holder.hPay?.text = if(mydata.nPay > 0) {
            Etc.setComma(mydata.nPay + mydata.nFee)
        } else {
            CTX.resources.getString(R.string.list_nopay)
        }


        holder.hBox?.visibility = View.VISIBLE
        holder.hLine?.visibility = View.VISIBLE

        /**
         * font change!!
         * */

        return layout
    }

    override fun getItem(position: Int): Cargo {
        return arrayData.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arrayData.size
    }

    fun addData(nOrderNum: String, nDistance: String, sDownLoc: String, sLoadDay: String, sCarType: String, sLoadLoc: String, sPayment: String, nPay: Int, nFee: Int, sLoadType: String, sCarton: String, sGoodsInfo: String, sDownDay : String) {
        val insert = Cargo(sCarType, sPayment, nPay, 0, nFee, "", nDistance, sDownLoc, sLoadDay, "", nOrderNum, sLoadLoc, "", sGoodsInfo, sLoadType, "", sDownDay, sCarton, "")

        insert.nSortNum = arrayData.size
        insert.dDistance = if(nDistance.isNullOrBlank() || nDistance == "0") {
            0
        } else {
            if(nDistance.toDouble() > 1000) {
                ceil(nDistance.toDouble() / 1000).toInt()
            } else {
                ceil(nDistance.toDouble()).toInt()
            }
        }
        arrayData.add(insert)
    }

    fun checkOrdernum(ordernum: String):Boolean {
        var rtnValue = false
        for(i in 0..(arrayData.size -1)) {
            if(arrayData[i].nOrderNum == ordernum) {
                rtnValue = true
                break
            }
        }
        return rtnValue
    }

    fun delete(position: Int) {
        arrayData.removeAt(position)
        dataChange()
    }

    fun clear() {
        arrayData   = ArrayList<Cargo>()
        isNight     = Etc.getNight(appsetting)
        dataChange()
    }

    fun dataChange() {
        notifyDataSetChanged()
    }

    fun selector(p: Cargo, type: Int): Any {
        return when(type) {
            1 -> {
                p.nSortNum
            }
            2 -> {
                p.nPay
            }
            3 -> {
                p.dDistance
            }
            else -> {
                p.sLoadDay
            }
        }
    }
}