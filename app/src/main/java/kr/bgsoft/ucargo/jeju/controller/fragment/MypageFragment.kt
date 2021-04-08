package kr.bgsoft.ucargo.jeju.controller.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kr.bgsoft.ucargo.jeju.App
import kr.bgsoft.ucargo.jeju.R
import kr.bgsoft.ucargo.jeju.SubActivity
import kr.bgsoft.ucargo.jeju.UCargoActivity
import kr.bgsoft.ucargo.jeju.controller.conf.Settings
import kr.bgsoft.ucargo.jeju.controller.dialog.AlertDialog
import kr.bgsoft.ucargo.jeju.controller.dialog.CargoSettingDialog
import kr.bgsoft.ucargo.jeju.cview.DefaultFragment
import kr.bgsoft.ucargo.jeju.data.model.UserInfo
import kr.bgsoft.ucargo.jeju.utils.AES256
import kr.bgsoft.ucargo.jeju.utils.Etc
import org.json.JSONObject

class MypageFragment : DefaultFragment(), View.OnClickListener {

    var nactivity: SubActivity? = null
    var mypage_id: TextView? = null
    var mypage_name: TextView? = null
    var mypage_point: TextView? = null
    var mypage_goPoint: Button? = null
    var mypage_carnum_arrow: ImageView? = null
    var mypage_carnum_show: Button? = null
    var mypage_carnum_box: LinearLayout? = null
    var mypage_carnum: TextView? = null
    var mypage_carnum_edit: EditText? = null
    var mypage_cartype: TextView? = null
    var mypage_cartype_edit: TextView? = null
    var mypage_carsize: TextView? = null
    var mypage_carsize_edit: TextView? = null
    var mypage_address: TextView? = null
    var mypage_address_edit1: TextView? = null
    var mypage_address_edit2: EditText? = null
    var mypage_address_editbox: LinearLayout? = null
    var mypage_carnum_editgo: Button? = null

    //    var mypage_carnum_cacle: Button?                = null
    var mypage_carnum_send: Button? = null
    var mypage_biznum_arrow: ImageView? = null
    var mypage_biznum_show: Button? = null
    var mypage_biznum_box: LinearLayout? = null
    var mypage_bizname: TextView? = null
    var mypage_bizname_edit: EditText? = null
    var mypage_bizceo: TextView? = null
    var mypage_bizceo_edit: EditText? = null
    var mypage_biznum: TextView? = null
    var mypage_biznum_edit: EditText? = null
    var mypage_bizuptae: TextView? = null
    var mypage_bizuptae_edit: EditText? = null
    var mypage_bizupjong: TextView? = null
    var mypage_bizupjong_edit: EditText? = null
    var mypage_bizemail: TextView? = null
    var mypage_bizemail_edit: EditText? = null
    var mypage_bizaddress: TextView? = null
    var mypage_bizaddress_edit1: TextView? = null
    var mypage_bizaddress_edit2: EditText? = null
    var mypage_bizaddress_editbox: LinearLayout? = null
    var mypage_biznum_editgo: Button? = null

    var mypage_biznum_send: Button? = null
    var mypage_pass_show: Button? = null
    var mypage_pass_arrow: ImageView? = null
    var mypage_title1: TextView? = null
    var mypage_title2: TextView? = null
    var mypage_title3: TextView? = null
    var mypage_title4: TextView? = null
    var mypage_title5: TextView? = null
    var mypage_title6: TextView? = null

    var mypage_box1: LinearLayout? = null
    var mypage_cpoint: TextView? = null
    var mypage_bank_arrow: ImageView? = null
    var mypage_bank_show: Button? = null
    var mypage_bank_box: LinearLayout? = null
    var mypage_bankname: TextView? = null
    var mypage_bankname_edit: TextView? = null
    var mypage_bankuser: TextView? = null
    var mypage_bankuser_edit: EditText? = null
    var mypage_banknum: TextView? = null
    var mypage_banknum_edit: EditText? = null
    var mypage_bank_editgo: Button? = null
    var mypage_bank_send: Button? = null
    var mypage_goevent: Button? = null

    var nbankcode = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_mypage, container, false)

        nactivity = activity as SubActivity

        val view_title = layout.findViewById<TextView>(R.id.header_title)
        val view_back = layout.findViewById<ImageButton>(R.id.header_back)
        mypage_id = layout.findViewById(R.id.mypage_id)
        mypage_name = layout.findViewById(R.id.mypage_name)
        mypage_point = layout.findViewById(R.id.mypage_point)
        mypage_goPoint = layout.findViewById(R.id.mypage_goPoint)
        mypage_carnum_arrow = layout.findViewById(R.id.mypage_carnum_arrow)
        mypage_carnum_show = layout.findViewById(R.id.mypage_carnum_show)
        mypage_carnum_box = layout.findViewById(R.id.mypage_carnum_box)
        mypage_carnum = layout.findViewById(R.id.mypage_carnum)
        mypage_carnum_edit = layout.findViewById(R.id.mypage_carnum_edit)
        mypage_cartype = layout.findViewById(R.id.mypage_cartype)
        mypage_cartype_edit = layout.findViewById(R.id.mypage_cartype_edit)
        mypage_carsize = layout.findViewById(R.id.mypage_carsize)
        mypage_carsize_edit = layout.findViewById(R.id.mypage_carsize_edit)
        mypage_address_editbox = layout.findViewById(R.id.mypage_address_editbox)
        mypage_address = layout.findViewById(R.id.mypage_address)
        mypage_address_edit1 = layout.findViewById(R.id.mypage_address_edit1)
        mypage_address_edit2 = layout.findViewById(R.id.mypage_address_edit2)
        mypage_carnum_editgo = layout.findViewById(R.id.mypage_carnum_editgo)
//        mypage_carnum_cacle         = layout.findViewById(R.id.mypage_carnum_cacle)
        mypage_carnum_send = layout.findViewById(R.id.mypage_carnum_send)
        mypage_biznum_arrow = layout.findViewById(R.id.mypage_biznum_arrow)
        mypage_biznum_show = layout.findViewById(R.id.mypage_biznum_show)
        mypage_biznum_box = layout.findViewById(R.id.mypage_biznum_box)
        mypage_bizname = layout.findViewById(R.id.mypage_bizname)
        mypage_bizname_edit = layout.findViewById(R.id.mypage_bizname_edit)
        mypage_bizceo = layout.findViewById(R.id.mypage_bizceo)
        mypage_bizceo_edit = layout.findViewById(R.id.mypage_bizceo_edit)
        mypage_biznum = layout.findViewById(R.id.mypage_biznum)
        mypage_biznum_edit = layout.findViewById(R.id.mypage_biznum_edit)
        mypage_bizuptae = layout.findViewById(R.id.mypage_bizuptae)
        mypage_bizuptae_edit = layout.findViewById(R.id.mypage_bizuptae_edit)
        mypage_bizupjong = layout.findViewById(R.id.mypage_bizupjong)
        mypage_bizupjong_edit = layout.findViewById(R.id.mypage_bizupjong_edit)
        mypage_bizaddress_editbox = layout.findViewById(R.id.mypage_bizaddress_editbox)
        mypage_bizaddress = layout.findViewById(R.id.mypage_bizaddress)
        mypage_bizaddress_edit1 = layout.findViewById(R.id.mypage_bizaddress_edit1)
        mypage_bizaddress_edit2 = layout.findViewById(R.id.mypage_bizaddress_edit2)
        mypage_biznum_editgo = layout.findViewById(R.id.mypage_biznum_editgo)
//        mypage_biznum_cacle         = layout.findViewById(R.id.mypage_biznum_cacle)
        mypage_biznum_send = layout.findViewById(R.id.mypage_biznum_send)
        mypage_pass_show = layout.findViewById(R.id.mypage_pass_show)
        mypage_pass_arrow = layout.findViewById(R.id.mypage_pass_arrow)

        mypage_title1 = layout.findViewById(R.id.mypage_title1)
        mypage_title2 = layout.findViewById(R.id.mypage_title2)
        mypage_title3 = layout.findViewById(R.id.mypage_title3)
        mypage_title4 = layout.findViewById(R.id.mypage_title4)
        mypage_title5 = layout.findViewById(R.id.mypage_title5)
        mypage_title6 = layout.findViewById(R.id.mypage_title6)

        mypage_box1 = layout.findViewById(R.id.mypage_box1)
        mypage_cpoint = layout.findViewById(R.id.mypage_cpoint)
        mypage_goevent = layout.findViewById(R.id.mypage_goEvent)

        mypage_bank_arrow = layout.findViewById(R.id.mypage_bank_arrow)
        mypage_bank_show = layout.findViewById(R.id.mypage_bank_show)
        mypage_bank_box = layout.findViewById(R.id.mypage_bank_box)
        mypage_bankname = layout.findViewById(R.id.mypage_bankname)
        mypage_bankname_edit = layout.findViewById(R.id.mypage_bankname_edit)
        mypage_bankuser = layout.findViewById(R.id.mypage_bankuser)
        mypage_bankuser_edit = layout.findViewById(R.id.mypage_bankuser_edit)
        mypage_banknum = layout.findViewById(R.id.mypage_banknum)
        mypage_banknum_edit = layout.findViewById(R.id.mypage_banknum_edit)
        mypage_bank_editgo = layout.findViewById(R.id.mypage_bank_editgo)
        mypage_bank_send = layout.findViewById(R.id.mypage_bank_send)

        val info = App.userInfo

        logd(info.toString())

        view_title.text = resources.getString(R.string.title_mypage)
        mypage_point?.text = if (info.point.isNullOrBlank()) {
            "0"
        } else {
            Etc.setComma(info.point.toInt())
        }
        mypage_name?.text = App.userInfo.name
        mypage_id?.text = "(" + App.userInfo.id + ")"

        nbankcode = info.sCmsBankCode

        view_back.setOnClickListener(this)
        mypage_goPoint?.setOnClickListener(this)
        mypage_carnum_show?.setOnClickListener(this)
//        mypage_carnum_cacle?.setOnClickListener(this)
        mypage_carnum_send?.setOnClickListener(this)
        mypage_carnum_editgo?.setOnClickListener(this)
        mypage_biznum_show?.setOnClickListener(this)
//        mypage_biznum_cacle?.setOnClickListener(this)
        mypage_biznum_send?.setOnClickListener(this)
        mypage_biznum_editgo?.setOnClickListener(this)
        mypage_pass_show?.setOnClickListener(this)
        mypage_address_edit1?.setOnClickListener(this)
        mypage_bizaddress_edit1?.setOnClickListener(this)
        mypage_cartype_edit?.setOnClickListener(this)
        mypage_carsize_edit?.setOnClickListener(this)

        mypage_bank_show?.setOnClickListener(this)
        mypage_bankname_edit?.setOnClickListener(this)
        mypage_bank_send?.setOnClickListener(this)
        mypage_bank_editgo?.setOnClickListener(this)
        mypage_goevent?.setOnClickListener(this)

        return layout
    }

    fun setInfoEdit2() {
        Etc.hideKeyboard(activity!!)

        var iserror = false
        var message = ""

        if (!iserror && mypage_bankname_edit?.text.isNullOrBlank()) {
            iserror = true
            message = resources.getString(R.string.dialog_mypage_error_bank)
        }
        if (!iserror && mypage_bankuser_edit?.text.isNullOrBlank()) {
            iserror = true
            message = resources.getString(R.string.dialog_mypage_error_bankuser)
        }
        if (!iserror && mypage_banknum_edit?.text.isNullOrBlank()) {
            iserror = true
            message = resources.getString(R.string.dialog_mypage_error_banknum)
        }
        if (iserror) {
            val dialog = AlertDialog(
                activity,
                AlertDialog.TYPE.OK,
                "",
                message,
                object : AlertDialog.Callback {
                    override fun onNo() {

                    }

                    override fun onYes() {

                    }
                })

            dialog.show()
        } else {
            val Info = App.userInfo

            val cwnum = Settings.getID(context!!)
            val cmsname = if (mypage_bankuser_edit?.text.toString().isNullOrBlank()) {
                ""
            } else {
                AES256.encode(mypage_bankuser_edit?.text.toString()).toString()
            }
            val cmsbank = if (mypage_bankname_edit?.text.toString().isNullOrBlank()) {
                ""
            } else {
                mypage_bankname_edit?.text.toString()
            }
            val cmsbankcode = nbankcode
            val cmsnum = if (mypage_banknum_edit?.text.toString().isNullOrBlank()) {
                ""
            } else {
                AES256.encode(mypage_banknum_edit?.text.toString()).toString()
            }

            /*MemberHttp().setAddBank(
                    cwnum,
                    cmsname,
                    cmsbank,
                    cmsbankcode,
                    cmsnum,
                    Http,
                    object : DefaultHttp.Callback {
                        override fun onError(
                                code: DefaultHttp.HttpCode,
                                message: String,
                                hcode: String
                        ) {
                            if (code != DefaultHttp.HttpCode.ERROR_700) {
                                try {
                                    toasts(resources.getString(R.string.toast_error_server))
                                } catch (e: Exception) {

                                }
                            }
                        }

                        override fun onSuccess(json: Any) {
                            Info.sCmsName = mypage_bankuser_edit?.text.toString()
                            Info.sCmsBank = mypage_bankname_edit?.text.toString()
                            Info.sCmsBankCode = nbankcode
                            Info.nCmsAccount = mypage_banknum_edit?.text.toString()

                            Settings.setInfo(activity!!, Info)

                            toasts(resources.getString(R.string.toast_mypage_editok))
                            changeView(Info, 3, true)
                            changeViewEdit(Info, 3, false)
                        }
                    })
            */
        }
    }

    fun setInfoEdit(type: Int) {

        Etc.hideKeyboard(activity!!)

        var iserror = false
        var message = ""
        val Info = App.userInfo

        var carnum = Info.sCarNum
        var cartype = Info.sCartypeName
        var carsize = Info.sCartonName
        var address = ArrayList<String>()
        address.add("")
        address.add("")
        if (!Info.sAddress.isNullOrBlank()) {
            val addr = Info.sAddress.split("|")
            address[0] = addr[0]
            address[1] = addr[1]
        }

        var corpname = Info.sComName
        var corpceo = Info.sCeoName
        var biznum = Info.bzNum
        var uptae = Info.sUptap
        var upjong = Info.sUpjang
        var bizaddress = ArrayList<String>()
        bizaddress.add("")
        bizaddress.add("")

        if (!Info.sComAddr.isNullOrBlank()) {
            val addr2 = Info.sComAddr.split("|")
            bizaddress[0] = addr2[0]
            bizaddress[1] = addr2[1]
        }

        if (type == 1) { //차량정보
            if (!iserror && mypage_carnum_edit?.text.isNullOrBlank()) {
                iserror = true
                message = resources.getString(R.string.dialog_mypage_error_carnum)
            } else if (!iserror && !Etc.checkCarnum(mypage_carnum_edit?.text.toString())) {
                iserror = true
                message = resources.getString(R.string.dialog_join_step3_carnumcheck)
            }
            if (!iserror && mypage_carsize_edit?.text.isNullOrBlank()) {
                iserror = true
                message = resources.getString(R.string.dialog_mypage_error_carsize)
            }
            if (!iserror && mypage_cartype_edit?.text.isNullOrBlank()) {
                iserror = true
                message = resources.getString(R.string.dialog_mypage_error_cartype)
            }
            if (!iserror && (mypage_address_edit1?.text.isNullOrBlank() || mypage_address_edit2?.text.isNullOrBlank())) {
                iserror = true
                message = resources.getString(R.string.dialog_mypage_error_address)
            }
            if (!iserror) {
                carnum = mypage_carnum_edit?.text.toString()
                cartype = mypage_cartype_edit?.text.toString()
                carsize = mypage_carsize_edit?.text.toString()
                address[0] = mypage_address_edit1?.text.toString()
                address[1] = mypage_address_edit2?.text.toString()
            }
        } else {
            if (!iserror && mypage_bizname_edit?.text.isNullOrBlank()) {
                iserror = true
                message = resources.getString(R.string.dialog_mypage_error_corpname)
            }
            if (!iserror && mypage_bizceo_edit?.text.isNullOrBlank()) {
                iserror = true
                message = resources.getString(R.string.dialog_mypage_error_corpceo)
            }
            if (!iserror && mypage_biznum_edit?.text.isNullOrBlank()) {
                iserror = true
                message = resources.getString(R.string.dialog_mypage_error_bizno)
            }
            if (!iserror && mypage_bizuptae_edit?.text.isNullOrBlank()) {
                iserror = true
                message = resources.getString(R.string.dialog_mypage_error_uptae)
            }
            if (!iserror && mypage_bizupjong_edit?.text.isNullOrBlank()) {
                iserror = true
                message = resources.getString(R.string.dialog_mypage_error_upjong)
            }
            if (!iserror && (mypage_bizaddress_edit1?.text.isNullOrBlank() || mypage_bizaddress_edit2?.text.isNullOrBlank())) {
                iserror = true
                message = resources.getString(R.string.dialog_mypage_error_bizaddress)
            }
            if (!iserror) {
                corpname = mypage_bizname_edit?.text.toString()
                corpceo = mypage_bizceo_edit?.text.toString()
                biznum = mypage_biznum_edit?.text.toString()
                uptae = mypage_bizuptae_edit?.text.toString()
                upjong = mypage_bizupjong_edit?.text.toString()
                bizaddress[0] = mypage_bizaddress_edit1?.text.toString()
                bizaddress[1] = mypage_bizaddress_edit2?.text.toString()
            }
        }


        if (iserror) {
            val dialog = AlertDialog(
                activity,
                AlertDialog.TYPE.OK,
                "",
                message,
                object : AlertDialog.Callback {
                    override fun onNo() {

                    }

                    override fun onYes() {

                    }
                })

            dialog.show()
        } else {
            val cwnum = Settings.getID(activity!!)
            val hp = AES256.encode(Info.sHP).toString()
            /*MemberHttp().setEdit(
                    cwnum,
                    hp,
                    carnum,
                    carsize,
                    cartype,
                    bizaddress[0],
                    bizaddress[1],
                    biznum,
                    corpname,
                    corpceo,
                    address[0],
                    address[1],
                    uptae,
                    upjong,
                    Http,
                    object : DefaultHttp.Callback {
                        override fun onError(
                                code: DefaultHttp.HttpCode,
                                message: String,
                                hcode: String
                        ) {
                            if (code != DefaultHttp.HttpCode.ERROR_700) {
                                try {
                                    toasts(resources.getString(R.string.toast_error_server))
                                } catch (e: Exception) {

                                }
                            }
                        }

                        override fun onSuccess(json: Any) {
                            Info.sCarNum = carnum
                            Info.sCartypeName = cartype
                            Info.sCartonName = carsize
                            Info.sAddress = address[0] + "|" + address[1]
                            Info.sComName = corpname
                            Info.sCeoName = corpceo
                            Info.bzNum = biznum
                            Info.sUptap = uptae
                            Info.sUpjang = upjong
                            Info.sComAddr = bizaddress[0] + "|" + bizaddress[1]

                            Settings.setInfo(activity!!, Info)

                            toasts(resources.getString(R.string.toast_mypage_editok))
                            changeView(Info, type, true)
                            changeViewEdit(Info, type, false)
                        }
                    })*/
        }
    }

    fun changeView(Info: UserInfo, type: Int, show: Boolean) {
        if (type == 1) {
            if (show) {
                mypage_carnum_arrow?.setImageResource(R.drawable.ico_arrowb_bottom)
                mypage_carnum_box?.visibility = View.VISIBLE
                mypage_biznum_arrow?.setImageResource(R.drawable.ico_arrowb_right)
                mypage_biznum_box?.visibility = View.GONE
                mypage_bank_arrow?.setImageResource(R.drawable.ico_arrowb_right)
                mypage_bank_box?.visibility = View.GONE

                mypage_carnum?.text = Info.sCarNum
                mypage_carnum_edit?.setText(Info.sCarNum)
                mypage_carsize?.text = Info.sCartonName
                mypage_carsize_edit?.text = Info.sCartonName
                mypage_cartype?.text = Info.sCartypeName
                mypage_cartype_edit?.text = Info.sCartypeName

                if (!Info.sAddress.isNullOrBlank()) {
                    val address = Info.sAddress.split("|")
                    mypage_address_edit1?.text = address[0]
                    mypage_address_edit2?.setText(address[1])
                    mypage_address?.text = address[0] + " " + address[1]
                } else {
                    mypage_address_edit1?.text = ""
                    mypage_address_edit2?.setText("")
                    mypage_address?.text = ""
                }

                mypage_carnum_edit?.visibility = View.GONE
                mypage_carsize_edit?.visibility = View.GONE
                mypage_cartype_edit?.visibility = View.GONE

//                mypage_carnum_cacle?.visibility     = View.GONE
                mypage_carnum_send?.visibility = View.GONE
                mypage_address_editbox?.visibility = View.GONE

                mypage_carnum?.visibility = View.VISIBLE
                mypage_carsize?.visibility = View.VISIBLE
                mypage_cartype?.visibility = View.VISIBLE
                mypage_address?.visibility = View.VISIBLE

                mypage_carnum_editgo?.visibility = View.VISIBLE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mypage_title2?.setTextColor(resources.getColor(R.color.colorUser2, null))
                    mypage_title3?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title4?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title6?.setTextColor(resources.getColor(R.color.colorUser6, null))
                } else {
                    mypage_title2?.setTextColor(resources.getColor(R.color.colorUser2))
                    mypage_title3?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title4?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title6?.setTextColor(resources.getColor(R.color.colorUser6))
                }
            } else {
                mypage_carnum_arrow?.setImageResource(R.drawable.ico_arrowb_right)
                mypage_carnum_box?.visibility = View.GONE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mypage_title2?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title3?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title4?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title6?.setTextColor(resources.getColor(R.color.colorUser6, null))
                } else {
                    mypage_title2?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title3?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title4?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title6?.setTextColor(resources.getColor(R.color.colorUser6))
                }
            }
        } else if (type == 2) {
            if (show) {
                mypage_carnum_arrow?.setImageResource(R.drawable.ico_arrowb_right)
                mypage_carnum_box?.visibility = View.GONE
                mypage_biznum_arrow?.setImageResource(R.drawable.ico_arrowb_bottom)
                mypage_biznum_box?.visibility = View.VISIBLE
                mypage_bank_arrow?.setImageResource(R.drawable.ico_arrowb_right)
                mypage_bank_box?.visibility = View.GONE

                mypage_bizname?.text = Info.sComName
                mypage_bizname_edit?.setText(Info.sComName)
                mypage_biznum?.text = Info.bzNum
                mypage_biznum_edit?.setText(Info.bzNum)
                mypage_bizceo?.text = Info.sCeoName
                mypage_bizceo_edit?.setText(Info.sCeoName)
                mypage_bizuptae?.text = Info.sUptap
                mypage_bizuptae_edit?.setText(Info.sUptap)
                mypage_bizupjong?.text = Info.sUpjang
                mypage_bizupjong_edit?.setText(Info.sUpjang)

                if (!Info.sComAddr.isNullOrBlank()) {
                    val address = Info.sComAddr.split("|")
                    mypage_bizaddress_edit1?.text = address[0]
                    mypage_bizaddress_edit2?.setText(address[1])
                    mypage_bizaddress?.text = address[0] + " " + address[1]
                } else {
                    mypage_bizaddress_edit1?.text = ""
                    mypage_bizaddress_edit2?.setText("")
                    mypage_bizaddress?.text = ""
                }

                mypage_bizname_edit?.visibility = View.GONE
                mypage_biznum_edit?.visibility = View.GONE
                mypage_bizceo_edit?.visibility = View.GONE
                mypage_bizuptae_edit?.visibility = View.GONE
                mypage_bizupjong_edit?.visibility = View.GONE
                mypage_bizemail_edit?.visibility = View.GONE
                mypage_bizaddress_editbox?.visibility = View.GONE

//                mypage_biznum_cacle?.visibility     = View.GONE
                mypage_biznum_send?.visibility = View.GONE

                mypage_bizname?.visibility = View.VISIBLE
                mypage_biznum?.visibility = View.VISIBLE
                mypage_bizceo?.visibility = View.VISIBLE
                mypage_bizuptae?.visibility = View.VISIBLE
                mypage_bizupjong?.visibility = View.VISIBLE
                mypage_bizemail?.visibility = View.VISIBLE
                mypage_bizaddress?.visibility = View.VISIBLE

                mypage_biznum_editgo?.visibility = View.VISIBLE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mypage_title2?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title3?.setTextColor(resources.getColor(R.color.colorUser2, null))
                    mypage_title4?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title6?.setTextColor(resources.getColor(R.color.colorUser6, null))
                } else {
                    mypage_title2?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title3?.setTextColor(resources.getColor(R.color.colorUser2))
                    mypage_title4?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title6?.setTextColor(resources.getColor(R.color.colorUser6))
                }
            } else {
                mypage_biznum_arrow?.setImageResource(R.drawable.ico_arrowb_right)
                mypage_biznum_box?.visibility = View.GONE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mypage_title2?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title3?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title4?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title6?.setTextColor(resources.getColor(R.color.colorUser6, null))
                } else {
                    mypage_title2?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title3?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title4?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title6?.setTextColor(resources.getColor(R.color.colorUser6))
                }
            }
        } else if (type == 3) {
            if (show) {
                mypage_carnum_arrow?.setImageResource(R.drawable.ico_arrowb_right)
                mypage_carnum_box?.visibility = View.GONE
                mypage_biznum_arrow?.setImageResource(R.drawable.ico_arrowb_bottom)
                mypage_biznum_box?.visibility = View.GONE
                mypage_bank_arrow?.setImageResource(R.drawable.ico_arrowb_right)
                mypage_bank_box?.visibility = View.VISIBLE

                mypage_bankname?.text = Info.sCmsBank
                mypage_bankname_edit?.text = Info.sCmsBank
                mypage_bankuser?.text = Info.sCmsName
                mypage_bankuser_edit?.setText(Info.sCmsName)
                mypage_banknum?.text = Info.nCmsAccount
                mypage_banknum_edit?.setText(Info.nCmsAccount)

                mypage_bankname_edit?.visibility = View.GONE
                mypage_bankuser_edit?.visibility = View.GONE
                mypage_banknum_edit?.visibility = View.GONE
                mypage_bank_send?.visibility = View.GONE

                mypage_bankname?.visibility = View.VISIBLE
                mypage_bankuser?.visibility = View.VISIBLE
                mypage_banknum?.visibility = View.VISIBLE
                mypage_bank_editgo?.visibility = View.VISIBLE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mypage_title2?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title3?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title4?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title6?.setTextColor(resources.getColor(R.color.colorUser2, null))
                } else {
                    mypage_title2?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title3?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title4?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title6?.setTextColor(resources.getColor(R.color.colorUser2))
                }
            } else {
                mypage_bank_arrow?.setImageResource(R.drawable.ico_arrowb_right)
                mypage_bank_box?.visibility = View.GONE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mypage_title2?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title3?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title4?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title6?.setTextColor(resources.getColor(R.color.colorUser6, null))
                } else {
                    mypage_title2?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title3?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title4?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title6?.setTextColor(resources.getColor(R.color.colorUser6))
                }
            }
        }
    }

    fun changeViewEdit(Info: UserInfo, type: Int, show: Boolean) {
        if (type == 1) {
            if (show) {
                mypage_carnum_edit?.setText(Info.sCarNum)
                mypage_carsize_edit?.text = Info.sCartonName
                mypage_cartype_edit?.text = Info.sCartypeName

                if (!Info.sAddress.isNullOrBlank()) {
                    val address = Info.sAddress.split("|")
                    mypage_address_edit1?.text = address[0]
                    mypage_address_edit2?.setText(address[1])
                    mypage_address?.text = address[0] + " " + address[1]
                } else {
                    mypage_address_edit1?.text = ""
                    mypage_address_edit2?.setText("")
                    mypage_address?.text = ""
                }

                mypage_carnum_edit?.visibility = View.VISIBLE
                mypage_carsize_edit?.visibility = View.VISIBLE
                mypage_cartype_edit?.visibility = View.VISIBLE
                mypage_address_editbox?.visibility = View.VISIBLE

//                mypage_carnum_cacle?.visibility     = View.VISIBLE
                mypage_carnum_send?.visibility = View.VISIBLE

                mypage_carnum?.visibility = View.GONE
                mypage_carsize?.visibility = View.GONE
                mypage_cartype?.visibility = View.GONE
                mypage_address?.visibility = View.GONE

                mypage_carnum_editgo?.visibility = View.GONE
            } else {
                mypage_carnum_edit?.visibility = View.GONE
                mypage_carsize_edit?.visibility = View.GONE
                mypage_cartype_edit?.visibility = View.GONE
                mypage_address_editbox?.visibility = View.GONE

//                mypage_carnum_cacle?.visibility     = View.GONE
                mypage_carnum_send?.visibility = View.GONE

                mypage_carnum?.visibility = View.VISIBLE
                mypage_carsize?.visibility = View.VISIBLE
                mypage_cartype?.visibility = View.VISIBLE
                mypage_address?.visibility = View.VISIBLE

                mypage_carnum_editgo?.visibility = View.VISIBLE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mypage_title2?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title3?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title4?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title6?.setTextColor(resources.getColor(R.color.colorUser6, null))
                } else {
                    mypage_title2?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title3?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title4?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title6?.setTextColor(resources.getColor(R.color.colorUser6))
                }
            }
        } else if (type == 2) {
            if (show) {
                mypage_bizname_edit?.setText(Info.sComName)
                mypage_biznum_edit?.setText(Info.bzNum)
                mypage_bizceo_edit?.setText(Info.sCeoName)
                mypage_bizuptae_edit?.setText(Info.sUptap)
                mypage_bizupjong_edit?.setText(Info.sUpjang)
                mypage_bizemail_edit?.setText(Info.sEmail)
                if (!Info.sComAddr.isNullOrBlank()) {
                    val address = Info.sComAddr.split("|")
                    mypage_bizaddress_edit1?.text = address[0]
                    mypage_bizaddress_edit2?.setText(address[1])
                    mypage_bizaddress?.text = address[0] + " " + address[1]
                } else {
                    mypage_bizaddress_edit1?.text = ""
                    mypage_bizaddress_edit2?.setText("")
                    mypage_bizaddress?.text = ""
                }

                mypage_bizname_edit?.visibility = View.VISIBLE
                mypage_biznum_edit?.visibility = View.VISIBLE
                mypage_bizceo_edit?.visibility = View.VISIBLE
                mypage_bizuptae_edit?.visibility = View.VISIBLE
                mypage_bizupjong_edit?.visibility = View.VISIBLE
                mypage_bizemail_edit?.visibility = View.VISIBLE
                mypage_bizaddress_editbox?.visibility = View.VISIBLE

//                mypage_biznum_cacle?.visibility     = View.VISIBLE
                mypage_biznum_send?.visibility = View.VISIBLE

                mypage_bizname?.visibility = View.GONE
                mypage_biznum?.visibility = View.GONE
                mypage_bizceo?.visibility = View.GONE
                mypage_bizuptae?.visibility = View.GONE
                mypage_bizupjong?.visibility = View.GONE
                mypage_bizemail?.visibility = View.GONE
                mypage_bizaddress?.visibility = View.GONE

                mypage_biznum_editgo?.visibility = View.GONE
            } else {
                mypage_bizname_edit?.visibility = View.GONE
                mypage_biznum_edit?.visibility = View.GONE
                mypage_bizceo_edit?.visibility = View.GONE
                mypage_bizuptae_edit?.visibility = View.GONE
                mypage_bizupjong_edit?.visibility = View.GONE
                mypage_bizemail_edit?.visibility = View.GONE
                mypage_bizaddress_editbox?.visibility = View.GONE

//                mypage_biznum_cacle?.visibility         = View.GONE
                mypage_biznum_send?.visibility = View.GONE

                mypage_bizname?.visibility = View.VISIBLE
                mypage_biznum?.visibility = View.VISIBLE
                mypage_bizceo?.visibility = View.VISIBLE
                mypage_bizuptae?.visibility = View.VISIBLE
                mypage_bizupjong?.visibility = View.VISIBLE
                mypage_bizemail?.visibility = View.VISIBLE
                mypage_bizaddress?.visibility = View.VISIBLE

                mypage_biznum_editgo?.visibility = View.VISIBLE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mypage_title2?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title3?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title4?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title6?.setTextColor(resources.getColor(R.color.colorUser6, null))
                } else {
                    mypage_title2?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title3?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title4?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title6?.setTextColor(resources.getColor(R.color.colorUser6))
                }
            }
        } else {
            if (show) {
                mypage_carnum_arrow?.setImageResource(R.drawable.ico_arrowb_right)
                mypage_carnum_box?.visibility = View.GONE
                mypage_biznum_arrow?.setImageResource(R.drawable.ico_arrowb_bottom)
                mypage_biznum_box?.visibility = View.GONE
                mypage_bank_arrow?.setImageResource(R.drawable.ico_arrowb_right)
                mypage_bank_box?.visibility = View.VISIBLE

                mypage_bankname?.text = Info.sCmsBank
                mypage_bankname_edit?.text = Info.sCmsBank
                mypage_bankuser?.text = Info.sCmsName
                mypage_bankuser_edit?.setText(Info.sCmsName)
                mypage_banknum?.text = Info.nCmsAccount
                mypage_banknum_edit?.setText(Info.nCmsAccount)

                mypage_bankname_edit?.visibility = View.VISIBLE
                mypage_bankuser_edit?.visibility = View.VISIBLE
                mypage_banknum_edit?.visibility = View.VISIBLE
                mypage_bank_send?.visibility = View.VISIBLE

                mypage_bankname?.visibility = View.GONE
                mypage_bankuser?.visibility = View.GONE
                mypage_banknum?.visibility = View.GONE
                mypage_bank_editgo?.visibility = View.GONE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mypage_title2?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title3?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title4?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title6?.setTextColor(resources.getColor(R.color.colorUser2, null))
                } else {
                    mypage_title2?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title3?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title4?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title6?.setTextColor(resources.getColor(R.color.colorUser2))
                }
            } else {
                mypage_bankname_edit?.visibility = View.GONE
                mypage_bankuser_edit?.visibility = View.GONE
                mypage_banknum_edit?.visibility = View.GONE
                mypage_bank_send?.visibility = View.GONE

                mypage_bankname?.visibility = View.VISIBLE
                mypage_bankuser?.visibility = View.VISIBLE
                mypage_banknum?.visibility = View.VISIBLE
                mypage_bank_editgo?.visibility = View.VISIBLE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mypage_title2?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title3?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title4?.setTextColor(resources.getColor(R.color.colorUser6, null))
                    mypage_title6?.setTextColor(resources.getColor(R.color.colorUser6, null))
                } else {
                    mypage_title2?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title3?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title4?.setTextColor(resources.getColor(R.color.colorUser6))
                    mypage_title6?.setTextColor(resources.getColor(R.color.colorUser6))
                }
            }
        }
    }

    fun getUserpoint(cwnum: String) {
        val info = Settings.getInfo(activity!!)

        /*MemberHttp().getUserPoint(cwnum!!, Http, object : DefaultHttp.Callback {
            override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                logd("getUserPoint error")
            }

            override fun onSuccess(json: Any) {
                val data = json as JSONObject
                info.point = data.getString("point")

                mypage_point?.text = if (info.point.isNullOrBlank()) {
                    "0"
                } else {
                    Etc.setComma(info.point.toInt())
                }
            }
        })*/

        if (!info.bzNum.isNullOrBlank() && info.bzNum != "_") {
            /* MemberHttp().getUserCPoint(cwnum!!, Http, object : DefaultHttp.Callback {
                 override fun onError(code: DefaultHttp.HttpCode, message: String, hcode: String) {
                     logd("getUserCPoint error")
                 }

                 override fun onSuccess(json: Any) {
                     val data = json as JSONObject

                     val cpoint = data.getString("count")

                     mypage_cpoint?.text = if (cpoint.isNullOrBlank()) {
                         "0"
                     } else {
                         Etc.setComma(cpoint.toInt())
                     } + "/10"

                     mypage_box1?.visibility = View.VISIBLE
                 }
             })*/
        }
    }

    override fun onResume() {
        super.onResume()

        val cwnum = Settings.getID(activity!!)
        if (!cwnum.isNullOrBlank()) {
            getUserpoint(cwnum)
        }
    }

    override fun onClick(v: View?) {
        val Info = App.userInfo

        when (v?.id) {
            R.id.header_back -> {
                activity?.finish()
            }
            R.id.mypage_goPoint -> {
               // UCargoActivity.showMyBank()
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.mypage_carnum_show -> {
                /*if (mypage_carnum_box?.visibility == View.VISIBLE) {
                    changeView(Info, 1, false)
                } else {
                    changeView(Info, 1, true)
                }*/
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.mypage_carnum_send -> {
                //setInfoEdit(1)
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.mypage_carnum_editgo -> {
                //changeViewEdit(Info, 1, true)
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.mypage_biznum_show -> {
                /*if (mypage_biznum_box?.visibility == View.VISIBLE) {
                    changeView(Info, 2, false)
                } else {
                    changeView(Info, 2, true)
                }*/
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.mypage_biznum_send -> {
                //setInfoEdit(2)
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.mypage_biznum_editgo -> {
                //changeViewEdit(Info, 2, true)
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.mypage_bank_show -> {
                /*if (mypage_bank_box?.visibility == View.VISIBLE) {
                    changeView(Info, 3, false)
                } else {
                    changeView(Info, 3, true)
                }*/
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.mypage_bankname_edit -> {
                /*val dialog = CargoSettingDialog(
                    activity,
                    CargoSettingDialog.TYPE.BANK,
                    "",
                    "",
                    "",
                    mypage_bankname?.text.toString(),
                    object : CargoSettingDialog.Callback {
                        override fun onNo() {

                        }

                        override fun onYes(data: JSONObject) {
                            val bank = data.getString("printtitle")
                            val code = data.getString("code")

                            mypage_bankname_edit?.text = bank
                            nbankcode = code
                        }
                    })

                dialog.show()*/
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.mypage_bank_send -> {
                //setInfoEdit2()
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.mypage_bank_editgo -> {
                //changeViewEdit(Info, 3, true)
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.mypage_pass_show -> {
                //UCargoActivity.showPassword()
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.mypage_address_edit1 -> {
                /*val dialog = CargoSettingDialog(
                    activity,
                    CargoSettingDialog.TYPE.ADDRADD,
                    "",
                    "",
                    "",
                    mypage_address_edit1?.text.toString(),
                    object : CargoSettingDialog.Callback {
                        override fun onNo() {

                        }

                        override fun onYes(data: JSONObject) {
                            val addr1 = data.getString("printtitle")
                            val addr2 = data.getString("addtext")
                            mypage_address_edit1?.text = addr1
                            mypage_address_edit2?.setText(addr2)
                        }
                    })

                dialog.show()*/
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.mypage_bizaddress_edit1 -> {
                /*val dialog = CargoSettingDialog(
                    activity,
                    CargoSettingDialog.TYPE.ADDRADD,
                    "",
                    "",
                    "",
                    mypage_bizaddress_edit1?.text.toString(),
                    object : CargoSettingDialog.Callback {
                        override fun onNo() {

                        }

                        override fun onYes(data: JSONObject) {
                            val addr1 = data.getString("printtitle")
                            val addr2 = data.getString("addtext")
                            mypage_bizaddress_edit1?.text = addr1
                            mypage_bizaddress_edit2?.setText(addr2)
                        }
                    })

                dialog.show()*/
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.mypage_carsize_edit -> {
                /*val dialog = CargoSettingDialog(
                    activity,
                    CargoSettingDialog.TYPE.CARSIZES,
                    "",
                    "",
                    "cut",
                    mypage_carsize_edit?.text.toString(),
                    object : CargoSettingDialog.Callback {
                        override fun onNo() {

                        }

                        override fun onYes(data: JSONObject) {
                            var code = data.getString("ncondition3")
                            var value = data.getString("printtitle")
                            mypage_carsize_edit?.text = value
                        }
                    })

                dialog.show()*/
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.mypage_cartype_edit -> {
                /*val dialog = CargoSettingDialog(
                    activity,
                    CargoSettingDialog.TYPE.CARTYPES,
                    "",
                    "",
                    "cut",
                    mypage_cartype_edit?.text.toString(),
                    object : CargoSettingDialog.Callback {
                        override fun onNo() {

                        }

                        override fun onYes(data: JSONObject) {
                            var code = data.getString("ncondition2")
                            var value = data.getString("printtitle")
                            mypage_cartype_edit?.text = value
                        }
                    })

                dialog.show()*/
                toasts(resources.getString(R.string.no_ready))
            }
            R.id.mypage_goEvent -> {
                //UCargoActivity.showEvent()
                toasts(resources.getString(R.string.no_ready))
            }
        }
    }
}