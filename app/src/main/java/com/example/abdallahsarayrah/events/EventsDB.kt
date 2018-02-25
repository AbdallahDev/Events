package com.example.abdallahsarayrah.events

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by abdallah.sarayrah on 10/18/2017.
 */

class EventsDB(context: Context) : SQLiteOpenHelper(context, "Events.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table users(user_id integer primary key, user_password text, user_name text, user_department_id integer, user_type integer, user_valid integer)")
        db?.execSQL("create table departments(department_id integer primary key, department_name text)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

}
