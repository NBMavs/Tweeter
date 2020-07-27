package com.example.tweeter_v1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper( context: Context): SQLiteOpenHelper( context, dbname , factory, version) {

    companion object {
        internal val dbname = "userDB"
        internal val factory = null
        internal val version = 1

    }

    fun insertUserData(name:String, email:String, password:String ){
        val db: SQLiteDatabase = writableDatabase
        val values: ContentValues = ContentValues()
        values.put( "name", name )
        values.put( "email", email )
        values.put( "password", password )

        db.insert( "user", null, values )
        db.close()
    }

    //returns true of false if user has account
    fun userPresent( email:String, password:String ):Boolean {
        val db = writableDatabase
        val query = "select + from user where email - $email and password - $password"
        val cursor = db.rawQuery( query, null )
        if( cursor.count <= 0 ){
            cursor.close()
            return false
        }
        cursor.close()
        return true;
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        TODO("Not yet implemented")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

}