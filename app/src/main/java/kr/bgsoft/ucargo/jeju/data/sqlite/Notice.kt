package kr.bgsoft.ucargo.jeju.data.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kr.bgsoft.ucargo.jeju.data.model.Notice
import kr.bgsoft.ucargo.jeju.utils.Etc
import java.util.*
import kotlin.collections.ArrayList

class Notice(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    val set_name    = "Notice"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE " + set_name + "(" +
                "intSeq INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "intType INTEGER, " +
                "txtMid TEXT, " +
                "txtTitle TEXT, " +
                "txtInfo TEXT, " +
                "txtData TEXT, " +
                "intReg INTEGER, " +
                "intUse INTEGER " +
                ");")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insert(type: Int, mid: String, title: String, info: String, data: String, use: Boolean = false) {
        if(!upcheck(mid)) {
            val db = writableDatabase
            db?.execSQL("INSERT INTO " + set_name + " " +
                    " VALUES (null, " +
                    "'" + type + "', " +
                    "'" + mid + "', " +
                    "'" + title.replace("'", "\\\'") + "', " +
                    "'" + info.replace("'", "\\\'") + "', " +
                    "'" + data + "', " +
                    "'" + Date().time + "', " +
                    " " + if(use) { "1" } else { "0" } + "); ")
            db.close()
        }
    }

    fun select() : ArrayList<Notice> {
        val db = writableDatabase
        val rtnValue = ArrayList<Notice>()

        val cursor = db.rawQuery("SELECT " +
                "intSeq, intType, txtMid, txtTitle, txtInfo, txtData, intReg, intUse " +
                "FROM " + set_name + " " +
                "WHERE intUse = 0 " +
                "ORDER BY intSeq DESC;", null)

        while (cursor.moveToNext()) {
            val data = Notice()

            data.intSeq     = cursor.getInt(0)
            data.intType    = cursor.getInt(1)
            data.txtMid     = cursor.getString(2)
            data.txtTitle   = cursor.getString(3)
            data.txtInfo    = cursor.getString(4)
            data.txtData    = cursor.getString(5)
            data.txtReg     = cursor.getInt(6)
            data.intUse     = cursor.getInt(7) != 0

            rtnValue.add(data)
        }

        cursor.close()
        db.close()

        return rtnValue
    }

    fun select(idx: Int) : Notice {
        val db = writableDatabase
        val rtnValue = Notice()

        val cursor = db.rawQuery("SELECT " +
                "intSeq, intType, txtMid, txtTitle, txtInfo, txtData, intReg, intUse " +
                "FROM " + set_name + " " +
                "WHERE intSeq = "+ idx.toString() +";", null)

        while (cursor.moveToNext()) {
            rtnValue.intSeq     = cursor.getInt(0)
            rtnValue.intType    = cursor.getInt(1)
            rtnValue.txtMid     = cursor.getString(2)
            rtnValue.txtTitle   = cursor.getString(3)
            rtnValue.txtInfo    = cursor.getString(4)
            rtnValue.txtData    = cursor.getString(5)
            rtnValue.txtReg     = cursor.getInt(6)
            rtnValue.intUse     = cursor.getInt(7) != 0
        }

        cursor.close()
        db.close()

        return rtnValue
    }


    fun upcheck(mid: String) : Boolean {
        val db = writableDatabase

        val cursor = db.rawQuery("SELECT " +
                "intSeq " +
                "FROM " + set_name + " " +
                "WHERE txtMid = '"+ mid +"' " +
                "ORDER BY intSeq DESC;", null)

        var i = 0
        while (cursor.moveToNext()) {
            i++
        }

        cursor.close()
        db.close()

        //App.Logd("dbcount: " + i)

        return i > 0
    }

    fun updateUse(seq : Int) {
        val db = writableDatabase

        db?.execSQL("UPDATE " + set_name + " SET " +
                "intUse = 1 " +
                "WHERE intSeq = "+ seq.toString() +"; ")

        db.close()
    }

    fun delete(seq: Int) {
        val db = writableDatabase

        db?.execSQL("DELETE " +
                "FROM " + set_name + " " +
                "WHERE intSeq = "+ seq.toString() +"; ")

        db.close()
    }

    fun weekdelete() {
        val db = writableDatabase
        val date = Etc.addDay(Date(), -7).time

        db?.execSQL("DELETE " +
                "FROM " + set_name + " " +
                "WHERE intReg <= "+ date.toString() +"; ")

        db.close()
    }

    fun getLastSeq() : Int {
        var rtnValue = 0
        val db = writableDatabase

        val cursor = db.rawQuery("SELECT " +
                "intSeq " +
                "FROM " + set_name + " " +
                "ORDER BY intSeq DESC " +
                "LIMIT 1;", null)

        while (cursor.moveToNext()) {
            rtnValue = cursor.getInt(0)
            break
        }

        cursor.close()
        db.close()

        return rtnValue
    }
}