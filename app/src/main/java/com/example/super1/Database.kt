package com.example.super1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context) : SQLiteOpenHelper(context, "super1.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, email TEXT, password TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun insertUser(username: String, email: String, password: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("username", username)
        values.put("email", email)
        values.put("password", password)
        db.insert("users", null, values)
        db.close()
    }

    fun getUser(email: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", arrayOf(email, password))
        val result = cursor.count > 0
        cursor.close()
        db.close()
        return result
    }
}