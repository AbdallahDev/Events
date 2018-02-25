package com.example.abdallahsarayrah.events

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //bellow i declare the variable regarded the shared preferences file, so it can be used later without declaration
        var sharedPreferences = getSharedPreferences("events_file", Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()

        //bellow i declare the variable that used withe DB connection
        var eventsDBObj = EventsDB(this)
        var eventsDB = eventsDBObj.writableDatabase

        var intent: Intent // i declare the intent variable so it can be user later without declaration

        //bellow i'll check if the user has before choose using the checkbox (no to login again) when he open's the app
        if (sharedPreferences.getString("login", "") == "1")//here if the value of the login column in the shared preferences file is one, that means the user has choose no to login again when he/she opens the app, and by that i'll will direct him to the main activity without going to login activity
        {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        //this code bellow to insert custom action bar title
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.titlebar)

        login_login.setOnClickListener {
            //this code bellow will check if the employee id and the password is for an exist employee
            var eventsDBCursor = eventsDB.rawQuery("select * from users where user_id = ? and user_password = ?", arrayOf(login_user_id.text.toString(), login_password.text.toString()))
            if (eventsDBCursor.count > 0) {
                //bellow i'll save in the shared preferences file that the user dosen't want to login next time he open's the app, and that if he checked the check box
                if (checkBox_login_save_password.isChecked) {
                    editor.putString("login", "1")
                    editor.commit()
                }

                Users.userId = login_user_id.text.toString().toInt()//here i store the user id to be used again in the app

                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else Toast.makeText(this, "أما الرقم الوظيفي او كلمة السر خاطئة", Toast.LENGTH_SHORT).show()
        }

        //bellow when the user clicks on the new user text view i'll direct him the sign up activity
        textView_signUp_new_user.setOnClickListener {
            intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
