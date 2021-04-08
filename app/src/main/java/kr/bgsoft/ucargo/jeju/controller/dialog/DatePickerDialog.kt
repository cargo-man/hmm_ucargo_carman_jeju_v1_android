package kr.bgsoft.ucargo.jeju.controller.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.RelativeLayout
import android.widget.TextView
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.cview.DefaultDialog
import kr.bgsoft.ucargo.jeju.utils.Etc
import java.util.*

class DatePickerDialog (context: Context, type: TYPE, title: String, date: Date?, min: Date?, max: Date?, callback: Callback) : DefaultDialog(context), View.OnClickListener, NumberPicker.OnValueChangeListener {

    val TP                          = type
    val CB                          = callback
    val title                       = title
    val nowdate                     = date ?: Date()
    val mindate                     = min ?: Etc.addYear(Date(), -5)
    val maxdate                     = max ?: Etc.addYear(Date(), 5)

    var view_year: NumberPicker?    = null
    var view_month: NumberPicker?   = null
    var view_day: NumberPicker?     = null
    var view_hour: NumberPicker?    = null

    interface Callback {
        fun onYes(date: Date)
        fun onNo()
    }

    enum class TYPE {
        DATE, MONTH, HOUR
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.dialog_datehour)

        val view_header = findViewById<RelativeLayout>(R.id.alert_header)
        val view_title  = findViewById<TextView>(R.id.alert_title)
        val view_yes    = findViewById<Button>(R.id.alert_yes)
        val view_no     = findViewById<Button>(R.id.alert_no)
        view_year       = findViewById(R.id.picker_year)
        view_month      = findViewById(R.id.picker_month)
        view_day        = findViewById(R.id.picker_day)
        view_hour       = findViewById(R.id.picker_hour)

        if(!title.isNullOrBlank()) {
            view_header.visibility = View.VISIBLE
            view_title.text = title
        } else {
            view_header.visibility = View.GONE
        }

        view_year?.wrapSelectorWheel = false
        view_month?.wrapSelectorWheel = false
        view_day?.wrapSelectorWheel = false

        view_year?.setOnValueChangedListener(this)
        view_month?.setOnValueChangedListener(this)
        view_day?.setOnValueChangedListener(this)
        view_hour?.setOnValueChangedListener(this)

        view_yes.setOnClickListener(this)
        view_no.setOnClickListener(this)

        view_year?.minValue = Etc.getYear(mindate)
        view_year?.maxValue = Etc.getYear(maxdate)

        onValueChange(view_year, 1, Etc.getYear(nowdate))
        onValueChange(view_month, 1, Etc.getMonth(nowdate))
        onValueChange(view_day, 1, Etc.getDay(nowdate))

        view_year?.value = Etc.getYear(nowdate)
        view_month?.value = Etc.getMonth(nowdate) + 1

        when(TP) {
            TYPE.DATE -> {
                view_day?.visibility    = View.VISIBLE
                view_day?.value         = Etc.getDay(nowdate)
                view_hour?.visibility   = View.GONE
            }
            TYPE.MONTH -> {
                view_day?.visibility    = View.GONE
                view_hour?.visibility   = View.GONE
            }
            TYPE.HOUR -> {
                view_day?.visibility    = View.VISIBLE
                view_hour?.visibility   = View.VISIBLE
                view_day?.value         = Etc.getDay(nowdate)
                view_hour?.value        = Etc.getHour(nowdate)

            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.alert_yes -> {
                val year    = view_year?.value.toString()
                val month   = view_month?.value.toString()
                var day     = "1"
                var hour    = "0"

                if(TP == TYPE.DATE) {
                    day = view_day?.value.toString()
                }
                if(TP == TYPE.HOUR) {
                    day = view_day?.value.toString()
                    hour = view_hour?.value.toString()
                }

                val date = Etc.IntToDate(year.toInt(), (month.toInt() - 1), day.toInt(), hour.toInt(),0, 0)
                CB.onYes(date)
                dismiss()
            }

            R.id.alert_no -> {
                CB.onNo()
                dismiss()
            }
        }
    }
    override fun onValueChange(v: NumberPicker?, o: Int, n: Int) {
        //logd("month : " + view_month?.value + " / day : " + view_day?.minValue)
        when(v?.id) {
            R.id.picker_year -> {
                if(Etc.getYear(mindate) == n) {
                    view_month?.minValue = Etc.getMonth(mindate) + 1
                    view_month?.maxValue = 12
                } else if(Etc.getYear(maxdate) == n) {
                    view_month?.minValue = 1
                    view_month?.maxValue = Etc.getMonth(maxdate) + 1
                } else {
                    view_month?.minValue = 1
                    view_month?.maxValue = 12
                }
            }
            R.id.picker_month -> {
                if((Etc.getMonth(mindate) + 1) == n && view_year?.value == Etc.getYear(mindate)) {
                    val maxday = Etc.IntToDate(view_year?.value.toString().toInt(), view_month?.value.toString().toInt(), 0, 0,0, 0)
                    view_day?.minValue = Etc.getDay(mindate)
                    view_day?.maxValue = Etc.getDay(maxday)
                } else if((Etc.getMonth(maxdate) + 1) == n && view_year?.value == Etc.getYear(maxdate)) {
                    view_day?.minValue = 1
                    view_day?.maxValue = Etc.getDay(maxdate)
                } else {
                    val minday = Etc.IntToDate(view_year?.value.toString().toInt(), view_month?.value.toString().toInt() - 1, 1, 0,0, 0)
                    val maxday = Etc.IntToDate(view_year?.value.toString().toInt(), view_month?.value.toString().toInt(), 0, 0,0, 0)

                    view_day?.minValue = Etc.getDay(minday)
                    view_day?.maxValue = Etc.getDay(maxday)
                }
            }
            R.id.picker_day -> {
                view_hour?.minValue     = 0
                view_hour?.maxValue     = 23
            }
        }
    }
}