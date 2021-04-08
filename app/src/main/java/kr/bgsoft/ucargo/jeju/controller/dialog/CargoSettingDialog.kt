package kr.bgsoft.ucargo.jeju.controller.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.GridView
import android.widget.TextView
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.controller.adapter.CargoSettingAdapter
import kr.bgsoft.ucargo.jeju.cview.DefaultDialog
import kr.bgsoft.ucargo.jeju.data.model.Code
import org.json.JSONObject

class CargoSettingDialog (context: Context?, type: TYPE?, area1: String, area2: String, area3: String, areatext: String, callback: Callback?) : DefaultDialog(context), View.OnClickListener {
    val TP                              = type
    val CB                              = callback
    var isFinish                        = true
    var nadapter: CargoSettingAdapter?  = null
    var nsettext: EditText?             = null
    var naddresstext: TextView?         = null
    var naddressadd: EditText?          = null
    var ngridlist: GridView?            = null

    var narea1: String                  = area1
    var narea2: String                  = area2
    var narea3: String                  = area3
    var nareatext: String               = areatext
    var nsetprint: String               = ""

    var nstep                           = 0
    var nselectaddress                  = ArrayList<Code>()
    var isChange                        = true
    var isbuttonclick                   = false


    interface Callback {
        fun onYes(data: JSONObject)
        fun onNo()
    }

    enum class TYPE {
        LOAD, DOWN, CARTYPE, CARSIZE, CARTYPES, CARSIZES, MIXUP, MYLISTTYPE, PAYMETHOD, LOADADDR, AREASELECT, DOWNADDR, LOADADDRADD, DOWNADDRADD, STATUS, CENTERAREA, CENTERTYPE, OILCARSIZE, OILEXPRESS, LISTSORT, CARDDEVICE, AREA, ADDRADD, BANK, BIZTYPE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.dialog_cargosetting)

    }

    override fun onClick(v: View?) {

    }
}