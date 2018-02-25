package com.example.abdallahsarayrah.events

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //this code bellow to insert custom action bar title
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.titlebar)

        textView_mainActivity_name.text = Users.userName
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_superadmin, menu)

        if (Events.year != -1 && Events.month != -1 && Events.day != -1 && Events.hour != -1 && Events.minute != -1) {
            val transaction = fragmentManager.beginTransaction()
            val bundle = Bundle()
            bundle.putString("year", Events.year.toString())
            bundle.putString("month", Events.month.toString())
            bundle.putString("day", Events.day.toString())
            bundle.putString("hour", Events.hour.toString())
            bundle.putString("minute", Events.minute.toString())
            val transactionObj = EventInsertFragment()
            transactionObj.arguments = bundle
            transaction.replace(R.id.fragment_conatiner, transactionObj)
            transaction.commit()
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_test -> {
                val transaction = fragmentManager.beginTransaction()
                val transactionObject = TestFragment()
                transaction.replace(R.id.fragment_conatiner, transactionObject)
                transaction.commit()
            }
            R.id.item_users -> {
                val transaction = fragmentManager.beginTransaction()
                val transactionObject = UsersFragment()
                transaction.replace(R.id.fragment_conatiner, transactionObject)
                transaction.commit()
            }
            R.id.item_events -> {
                val transaction = fragmentManager.beginTransaction()
                val transactionObject = EventsFragment()
                transaction.replace(R.id.fragment_conatiner, transactionObject)
                transaction.commit()
            }
            R.id.item_employees -> {
                val transaction = fragmentManager.beginTransaction()
                val transactionObject = EmployeesFragment()
                transaction.replace(R.id.fragment_conatiner, transactionObject)
                transaction.commit()
            }
            R.id.item_departments -> {
                val transaction = fragmentManager.beginTransaction()
                val transactionObject = DepartmentsFragment()
                transaction.replace(R.id.fragment_conatiner, transactionObject)
                transaction.commit()
            }
            R.id.item_logout -> {
                //bellow i'll clear the login value in the shared preferences file, so the user will go to the login activity later after he open's the app, because he logged out
                val sharedPreferences = getSharedPreferences("events_file", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.commit()

                Users.userId = -1
                Users.userName = ""

                val intent = Intent(this, LoginUserActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

}
