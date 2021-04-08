package kr.bgsoft.ucargo.jeju.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.IBinder
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import kr.bgsoft.ucargo.jeju.data.model.AppSetting
import java.io.UnsupportedEncodingException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset
import java.nio.charset.CodingErrorAction


object Etc {
    fun intRand(start: Int, end: Int): Int? {
        return (Math.floor(Math.random() * (end - start)) + start).toInt()
    }

    fun setComma(value: Int?): String? {
        if(value != null) {
            val comma = DecimalFormat("#,###")
            return (comma.format(value)).toString()
        } else {
            return ""
        }
    }

    fun checkEmail(value: String): Boolean {
        val regx = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+\$"
        return Pattern.matches(regx, value)
    }

    fun checkMobile(value: String): Boolean {
        val regx = "^[0-9]{10,13}\$"
        return Pattern.matches(regx, value.replace("-", ""))
    }

    fun checkNumber(value: String): Boolean {
        val regx = "^[0-9]*\$"
        return Pattern.matches(regx, value.replace(",", ""))
    }
    fun checkPasscheck(value: String): Boolean {
        val regx = "^(?=.*[0-9])(?=.*[a-zA-Z]).{4,}$"
        return Pattern.matches(regx, value.replace(",", ""))
    }

    fun checkCarnum(value: String): Boolean {
        val regx = "([가-힣]{1,4})*[0-9]{2}[가-힣]{1}[0-9]{4}$"
        return Pattern.matches(regx, value.replace(",", ""))
    }

    fun convertDpToPixel(dp: Float, context: Context) : Float? {
        val resources = context.resources
        val matrics = resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, matrics)
    }

    fun convertPixelsToDp(px: Float, context: Context) : Float? {
        val resources = context.resources
        val matrics = resources.displayMetrics

        return px / (matrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT).toFloat()
    }

    fun hideKeyboard(context: Context, ib: IBinder) {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(ib, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun hideKeyboard(activity: Activity) {
        try {
            val ib = activity.currentFocus!!.windowToken
            hideKeyboard(activity.applicationContext, ib)
        } catch (e: Exception) {

        }
    }

    fun hideKeyboard(dialog: Dialog) {
        try {
            val ib = dialog.currentFocus!!.windowToken
            hideKeyboard(dialog.context, ib)
        } catch (e: Exception) {

        }
    }

    fun convertDateToString(value: Date?) : String {
        return convertDateToString(value, "yyyy-MM-dd HH:mm:ss")
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateToString(value: Date?, regx: String) : String {
        var rtnvalue    = ""

        try {
            if(value != null) {
                val transFormat = SimpleDateFormat(regx)
                rtnvalue = transFormat.format(value)
            }
        } catch (e: Exception) {

        }
        return rtnvalue
    }

    fun convertStringToDate(value: String) : Date? {
        return convertStringToDate(value, "yyyy-MM-dd HH:mm:ss")
    }

    @SuppressLint("SimpleDateFormat")
    fun convertStringToDate(value: String, regx: String) : Date? {
        var rtnvalue: Date? = null

        try {
            val transFormat = SimpleDateFormat(regx)
            rtnvalue = transFormat.parse(value)
        } catch (e: Exception) {

        }
        return rtnvalue
    }

    fun addYearToString(value: Date, add: Int) : String {
        return addDateToString(value, Calendar.YEAR, add, "yyyy-MM-dd HH:mm:ss")
    }

    fun addYearToString(value: Date, add: Int, regx: String) : String {
        return addDateToString(value, Calendar.YEAR, add, regx)
    }

    fun addMonthToString(value: Date, add: Int) : String {
        return addDateToString(value, Calendar.MONTH, add, "yyyy-MM-dd HH:mm:ss")
    }

    fun addMonthToString(value: Date, add: Int, regx: String) : String {
        return addDateToString(value, Calendar.MONTH, add, regx)
    }

    fun addDayToString(value: Date, add: Int) : String {
        return addDateToString(value, Calendar.DATE, add, "yyyy-MM-dd HH:mm:ss")
    }

    fun addDayToString(value: Date, add: Int, regx: String) : String {
        return addDateToString(value, Calendar.DATE, add, regx)
    }

    @SuppressLint("SimpleDateFormat")
    fun addDateToString(value: Date, type: Int, add: Int, regx: String) : String {
        val cal = Calendar.getInstance()
        val df  = SimpleDateFormat(regx)
        cal.time = value
        cal.add(type, add)

        return df.format(cal.time)
    }

    fun addYear(value: Date, add: Int) : Date {
        return addDate(value, Calendar.YEAR, add)
    }

    fun addMonth(value: Date, add: Int) : Date {
        return addDate(value, Calendar.MONTH, add)
    }

    fun addDay(value: Date, add: Int) : Date {
        return addDate(value, Calendar.DATE, add)
    }

    fun addHour(value: Date, add: Int) : Date {
        return addDate(value, Calendar.HOUR, add)
    }

    fun addMin(value: Date, add: Int) : Date {
        return addDate(value, Calendar.MINUTE, add)
    }

    fun addSec(value: Date, add: Int) : Date {
        return addDate(value, Calendar.SECOND, add)
    }


    fun addDate(value: Date, type: Int, add: Int) : Date {
        val cal = Calendar.getInstance()
        cal.time = value
        cal.add(type, add)

        return cal.time
    }

    fun IntToDate(year: Int, month: Int, day: Int, hour: Int, min: Int, sec: Int) : Date {
        val cal = Calendar.getInstance()
        cal.set(year, month, day, hour, min, sec)
        return cal.time
    }

    fun getYear(value: Date) : Int {
        return getDateToInt(value, Calendar.YEAR)
    }
    fun getMonth(value: Date) : Int {
        return getDateToInt(value, Calendar.MONTH)
    }
    fun getDay(value: Date) : Int {
        return getDateToInt(value, Calendar.DATE)
    }
    fun getHour(value: Date) : Int {
        return getDateToInt(value, Calendar.HOUR_OF_DAY)
    }
    fun getMin(value: Date) : Int {
        return getDateToInt(value, Calendar.MINUTE)
    }
    fun getSec(value: Date) : Int {
        return getDateToInt(value, Calendar.SECOND)
    }

    fun getWeek(value: Date) : Int {
        return getDateToInt(value, Calendar.DAY_OF_WEEK)
    }

    fun getDateToInt(value: Date, type: Int) : Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = value.time
        return cal.get(type)
    }

    fun getCorpNum(value: String) : String {
        return if(value.length >= 10) {
            value.substring(0, 3) + "-" + value.substring(3, 5) + "-" + value.substring(5, 10)
        } else {
            ""
        }
    }

    fun getTelNum(value: String) : String {
        var rtnvalue = ""

        if(!value.isNullOrBlank()) {
            val value2 = value.replace("-", "")

            if (value2.length >= 9) {
                if (value2.take(2) == "02") {
                    if (value2.length == 9) {
                        rtnvalue = value2.substring(0, 2) + "-" + value2.substring(2, 5) + "-" + value2.substring(5, 9)
                    } else {
                        rtnvalue = value2.substring(0, 2) + "-" + value2.substring(2, 6) + "-" + value2.substring(6, 10)
                    }
                } else {
                    if (value2.length == 7) {
                        rtnvalue = value2.substring(0, 3) + "-" + value2.substring(3, 6)
                    } else if (value2.length == 8) {
                        rtnvalue = value2.substring(0, 4) + "-" + value2.substring(4, 8)
                    } else if (value2.length == 9) {
                        rtnvalue = value2.substring(0, 2) + "-" + value2.substring(2, 5) + "-" + value2.substring(5, 9)
                    } else if (value2.length == 10) {
                        rtnvalue = value2.substring(0, 3) + "-" + value2.substring(3, 6) + "-" + value2.substring(6, 10)
                    } else {
                        rtnvalue = value2.substring(0, 3) + "-" + value2.substring(3, 7) + "-" + value2.substring(7, 11)
                    }
                }
            }
        }

        return rtnvalue
    }

    fun isInstall(pkg: String, context: Context) : Boolean {
        var value = false

        val pm = context.getPackageManager()
        val list = pm?.getInstalledApplications(0)

        if (list != null) {
            for (applicationInfo in list) {
                if (applicationInfo.packageName.trim() == pkg.trim()) {
                    value = true
                    break
                }
            }
        }

        return value
    }


    fun setSectoTime(sec: Int, ext: String = "{hour}:{min}:{sec}") : String {
        var value   = ""
        var maxsec  = sec
        var hour    = 0
        var min     = 0

        //hour
        if(ext.indexOf("{hour}") > -1) {
            hour = maxsec / (60 * 60)
            maxsec = maxsec % (60 * 60)

            val array = ext.split("{min}")

            if(hour > 0) {
                if (array.size > 1) {
                    value = array[0].replace("{hour}", hour.toString()) + "{min}" + array[1]
                } else {
                    value = array[0].replace("{hour}", hour.toString())
                }
            } else {
                if (array.size > 1) {
                    value = "{min}" + array[1]
                }
            }
        } else {
            value = ext
        }

        //min
        if(ext.indexOf("{min}") > -1) {
            min = maxsec / 60
            maxsec = maxsec % 60

            val array = value.split("{sec}")

            if(min > 0) {
                if (array.size > 1) {
                    value = array[0].replace("{min}", min.toString()) + "{sec}" + array[1]
                } else {
                    value = array[0].replace("{min}", min.toString())
                }
            } else if(hour > 0 && min <= 0) {
                val array = value.split("{min}")
                value = array[0]
            } else {
                if (array.size > 1) {
                    value += "{sec}" + array[1]
                }
            }
        } else if(value.isNullOrBlank()) {
            value = ext
        }


        if(ext.indexOf("{sec}") > -1) {
            val sec = maxsec

            if(sec > 0) {
                value.replace("{seq}", sec.toString())
            } else if((hour > 0 || min > 0) && sec <= 0) {
                val array = value.split("{sec}")
                value = array[0]
            }
        }

        return value
    }

    fun svSend(context: Context, key: String, value: String) {
        val spref   = PreferenceManager.getDefaultSharedPreferences(context)
        val editor  = spref.edit()
        val vkey    = AES256.encode(key)
        val vvalue  = AES256.encode(value)

        editor.putString(vkey, vvalue)
        editor.commit()
    }

    fun svGet(context: Context, key: String) : String? {
        val spref   = PreferenceManager.getDefaultSharedPreferences(context)
        val vkey    = AES256.encode(key)
        val vvalue  = spref.getString(vkey, "")

        return if(!vvalue.equals("")) {AES256.decode(vvalue!!) } else { "" }
    }

    fun svDelete(context: Context, key: String) {
        val spref   = PreferenceManager.getDefaultSharedPreferences(context)
        val editor  = spref.edit()
        val vkey    = AES256.encode(key)
        editor.remove(vkey)
        editor.commit()
    }

    fun getNight(appSetting: AppSetting): Boolean {
        val now         = Date()
        var rtnValue    = false

        if(appSetting.themeType == 1) {
            val month   = getMonth(now)
            val hour    = getHour(now)

            when(month) {
                11, 0, 1 -> {
                    if(hour <= 7 || hour >= 17) {
                        rtnValue = true
                    }
                }
                2, 3, 4 -> {
                    if(hour <= 6 || hour >= 18) {
                        rtnValue = true
                    }
                }
                5, 6 -> {
                    if(hour <= 5 || hour >= 19) {
                        rtnValue = true
                    }
                }
                7, 8 -> {
                    if(hour <= 5 || hour >= 20) {
                        rtnValue = true
                    }
                }
                9, 10 -> {
                    if(hour <= 6 || hour >= 18) {
                        rtnValue = true
                    }
                }
            }
        } else if(appSetting.themeType == 3) {
            rtnValue = true
        }

        return rtnValue
    }

    /* fun toasts(context: Context, message: String) {
         val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
         val view = toast.view
         val text = view.findViewById<TextView>(android.R.id.message)
         view.setBackgroundResource(R.drawable.box_type1_5)

         val left = if(Etc.convertDpToPixel(15f, context) != null) { Etc.convertDpToPixel(15f, context)!!.toInt() } else { 0 }
         val top = if(Etc.convertDpToPixel(10f, context) != null) { Etc.convertDpToPixel(10f, context)!!.toInt() } else { 0 }
         view.setPadding(left, top, left, top)

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             text.setTextColor(context.resources.getColor(android.R.color.white, null))
         } else {
             text.setTextColor(context.resources.getColor(android.R.color.white))
         }
         toast.show()
     }

     fun toastl(context: Context, message: String) {
         val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
         val view = toast.view
         val text = view.findViewById<TextView>(android.R.id.message)
         view.setBackgroundResource(R.drawable.box_type1_5)

         val left = if(Etc.convertDpToPixel(15f, context) != null) { Etc.convertDpToPixel(15f, context)!!.toInt() } else { 0 }
         val top = if(Etc.convertDpToPixel(10f, context) != null) { Etc.convertDpToPixel(10f, context)!!.toInt() } else { 0 }
         view.setPadding(left, top, left, top)

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             text.setTextColor(context.resources.getColor(android.R.color.white, null))
         } else {
             text.setTextColor(context.resources.getColor(android.R.color.white))
         }
         toast.show()
     }
 */
    fun substring(parameterName: String, maxLength: Int): String {
        var parameterName = parameterName

        val utf8Charset = Charset.forName("UTF-8")
        val cd = utf8Charset.newDecoder()

        try {
            val sba = parameterName.toByteArray(charset("UTF-8"))
            // Ensure truncating by having byte buffer = DB_FIELD_LENGTH
            val bb = ByteBuffer.wrap(sba, 0, maxLength) // len in [B]
            val cb = CharBuffer.allocate(maxLength) // len in [char] <= # [B]
            // Ignore an incomplete character
            cd.onMalformedInput(CodingErrorAction.IGNORE)
            cd.decode(bb, cb, true)
            cd.flush(cb)
            parameterName = String(cb.array(), 0, cb.position())
        } catch (e: UnsupportedEncodingException) {
            System.err.println("### 지원하지 않는 인코딩입니다.$e")
        }

        return parameterName
    }

    // 문자열 인코딩에 따라서 글자수 체크
    fun length(sequence: CharSequence): Int {
        var count = 0
        var i = 0
        val len = sequence.length
        while (i < len) {
            val ch = sequence[i]

            if (ch.toInt() <= 0x7F) {
                count++
            } else if (ch.toInt() <= 0x7FF) {
                count += 2
            } else if (Character.isHighSurrogate(ch)) {
                count += 4
                ++i
            } else {
                count += 3
            }
            i++
        }
        return count
    }

}