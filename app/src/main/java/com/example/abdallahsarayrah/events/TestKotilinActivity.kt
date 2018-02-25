package com.example.abdallahsarayrah.events

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_test_kotilin.*

class TestKotilinActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_kotilin)

        textView_testKotilin_viewDateTime.text = Events.year.toString() + "-${Events.month}-${Events.day} -- ${Events.hour}:${Events.minute}"

        button_testKotilin_dateTimeFragment_pick.setOnClickListener {
            val obj = DateTimePickerFragment()
            obj.show(fragmentManager, "DateTime")
        }
    }
}
