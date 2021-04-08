package kr.bgsoft.ucargo.jeju.data.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class IsRead(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    val set_name = "Board"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE " + set_name + "(" +
                "intSeq INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nType INTEGER, " +
                "intIdx INTEGER " +
                ");")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insert(type: Int, idx: Int) {
        if(!select(type, idx)) {
            val db = writableDatabase
            db?.execSQL("INSERT INTO " + set_name + " " +
                    " VALUES (null, " +
                    "'" + type + "', " +
                    "'" + idx + "' " +
                    "); ")
            db.close()
        }
    }

    fun select(type: Int, idx: Int) : Boolean {
        val db = writableDatabase

        val cursor = db.rawQuery("SELECT " +
                " intSeq " +
                "FROM " + set_name + " " +
                "WHERE intIdx = '"+ idx +"' AND nType = '"+ type + "' " +
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

    fun select(type: Int) :Boolean {
        val db = writableDatabase
        val cursor = db.rawQuery("SELECT " +
                " intSeq " +
                "FROM " + set_name + " " +
                "WHERE nType = '"+ type + "' " +
                "ORDER BY intSeq DESC;", null)

        var i = 0
        while (cursor.moveToNext()) {
            i++
            break
        }

        cursor.close()
        db.close()

        return i > 0
    }

}