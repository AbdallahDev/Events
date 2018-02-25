package com.example.abdallahsarayrah.events

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.HashMap
import kotlin.collections.ArrayList
import kotlin.collections.toTypedArray

class SignUpActivity : AppCompatActivity() {

    @SuppressLint("ApplySharedPref", "Recycle", "UseSparseArrays")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val eventsDB = EventsDB(this)
        val eventsDBObj = eventsDB.writableDatabase
        var eventsDBCursor: Cursor
        var spinnerDepartmentID = 0

        //bellow to fill the spinner from the DB with the departments
        eventsDBCursor = eventsDBObj.rawQuery("SELECT * FROM departments WHERE department_id != 0", null)
        eventsDBCursor.moveToFirst()
        val hashMapDepartments = HashMap<Int, String>()
        hashMapDepartments.put(0, "اختر المديرية")
        while (!eventsDBCursor.isAfterLast) {
            hashMapDepartments.put(eventsDBCursor.getInt(eventsDBCursor.getColumnIndex("department_id")), eventsDBCursor.getString(eventsDBCursor.getColumnIndex("department_name")))
            eventsDBCursor.moveToNext()
        }
        val arrayDepartments = hashMapDepartments.values.toTypedArray()
        val adapter = ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, arrayDepartments)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        employee_signUp_spinner_departments.adapter = adapter
        //i get the selected spinner item DB id
        employee_signUp_spinner_departments.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                spinnerDepartmentID = hashMapDepartments.keys.toTypedArray()[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        //bellow to fill the spinner with the user types
        val arrayListUserTypes = ArrayList<String>()
        arrayListUserTypes.add("اختر نوع المستخدم")
        arrayListUserTypes.add("ادمن")
        arrayListUserTypes.add("مستخدم عادي")
        val adapterUserTypes = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListUserTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        user_signUp_spinner_user_types.adapter = adapterUserTypes
        //i get the selected spinner item DB id
        var spinnerUserType = ""
        user_signUp_spinner_user_types.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                spinnerUserType = user_signUp_spinner_user_types.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        signUp_signUp.setOnClickListener {
            eventsDBCursor = eventsDBObj.rawQuery("select * from users where user_id = ?", arrayOf(signUp_user_id.text.toString()))
            eventsDBCursor.moveToFirst()
            if (eventsDBCursor.count == 0) {
                if (signUp_user_id.text.toString().isNotEmpty()) {
                    if (signUp_password.text.toString() == signUp_password_confirm.text.toString()) {
                        if (signUp_name.text.toString().isNotEmpty()) {
                            if (signUp_user_id.text.toString().toInt() != 0) {
                                if (spinnerUserType != "اختر نوع المستخدم") {
                                    if (spinnerDepartmentID != 0) {
                                        eventsDBObj.execSQL("insert into users values(?, ?, ?, ?, ?, ?)", arrayOf(signUp_user_id.text.toString(), signUp_password.text.toString(), signUp_name.text.toString(), spinnerDepartmentID, spinnerUserType, 0))

                                        //bellow i'll save in the shared preferences file that the user dosen't want to login next time he open's the app
                                        val sharedPreferences = getSharedPreferences("events_file", Context.MODE_PRIVATE)
                                        val editor = sharedPreferences.edit()
                                        editor.putString("login", "1")
                                        editor.commit()

                                        Users.userId = signUp_user_id.text.toString().toInt()//here i store the user id to be used again in the app

                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else Toast.makeText(this, "يرجى اختيار المديرية", Toast.LENGTH_SHORT).show()
                                } else Toast.makeText(this, "يرجى اختيار نوع المستخدم", Toast.LENGTH_SHORT).show()
                            } else {
                                eventsDBObj.execSQL("insert into users values(?, ?, ?, ?, ?, ?)", arrayOf(signUp_user_id.text.toString(), signUp_password.text.toString(), signUp_name.text.toString(), 0, "سوبر ادمن", 1))

                                //bellow i'll save in the shared preferences file that the user dosen't want to login next time he open's the app
                                val sharedPreferences = getSharedPreferences("events_file", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("login", "1")
                                editor.commit()

                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } else Toast.makeText(this, "يرجى تعبئة خانة الاسم", Toast.LENGTH_SHORT).show()
                    } else Toast.makeText(this, "الباسورد غير متطابق", Toast.LENGTH_SHORT).show()
                } else Toast.makeText(this, "يرجى تعبئة خانة رقم الموظف", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(this, "المستخدم قام بالتسجيل مسبقاً", Toast.LENGTH_SHORT).show()
        }
    }
}
