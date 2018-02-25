//in this activity i try to login with the users from the online database (MySQL)
package com.example.abdallahsarayrah.events

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login_user.*

class LoginUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)

        val pd = ProgressDialog(this)
        pd.setMessage("يرجى الانتظار...")
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)

        textView_loginUserActivity_login.setOnClickListener {
            //            pd.show()

            val rq = Volley.newRequestQueue(this)
            val jor = JsonObjectRequest(Request.Method.GET, "http://192.168.0.29/android/android_events/web_apis/login_page_ctl.php?userId=${editText_loginUserActivity_user_id.text}&password=${editText_loginUserActivity_password.text}", null,
                    Response.Listener { response ->
                        if (response != null) {
                            pd.hide()
                            Users.userId = response.getString("user_id").toInt()
                            Users.userName = response.getString("name")
                            Users.userDirectorate = response.getInt("directorate")
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else Toast.makeText(this, "اما رقم المستخدم او كلمة السر خاطئة", Toast.LENGTH_SHORT).show()
                    }, Response.ErrorListener { error ->
                pd.hide()
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            })

            rq.add(jor)
        }

    }
}
