package kr.bgsoft.ucargo.jeju.data.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Infos(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    val set_name = "Infos"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE " + set_name + "(" +
                "intSeq INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "strType TEXT, " +
                "strValue TEXT " +
                ");")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insert(type: String, value: String) {
        if(!check(type)) {
            val db = writableDatabase
            db?.execSQL("INSERT INTO " + set_name + " " +
                    " VALUES (null, " +
                    "'" + type + "', " +
                    "'" + value + "' " +
                    "); ")
            db.close()
        } else {
            val db = writableDatabase
            db?.execSQL("UPDATE " + set_name + " SET " +
                    " strValue = '"+ value +"' " +
                    " WHERE strType = '" + type + "' ")
            db.close()
        }
    }

    fun check(type: String) : Boolean {
        val db = writableDatabase

        val cursor = db.rawQuery("SELECT " +
                " intSeq " +
                "FROM " + set_name + " " +
                "WHERE strType = '"+ type + "' " +
                "ORDER BY intSeq DESC;", null)

        var i = 0
        while (cursor.moveToNext()) {
            i++
        }

        cursor.close()
        db.close()

        return i > 0
    }

    fun select(type: String) : String {
        val db = writableDatabase
        val cursor = db.rawQuery("SELECT " +
                " strValue " +
                "FROM " + set_name + " " +
                "WHERE strType = '"+ type + "' " +
                "ORDER BY intSeq DESC;", null)

        while (cursor.moveToNext()) {
            return cursor.getString(0)
            break
        }

        cursor.close()
        db.close()

        return ""
    }

}