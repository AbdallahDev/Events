package com.example.abdallahsarayrah.events


import android.app.DialogFragment
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker


/**
 * A simple [Fragment] subclass.
 */
class DateTimePickerFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_date_time_picker, container, false)
        val datePickerDateTimePickerFragmentDate = view.findViewById<DatePicker>(R.id.datePicker_dateTimePickerFragment_date)
        val datePickerDateTimePickerFragmentTime = view.findViewById<TimePicker>(R.id.datePicker_dateTimePickerFragment_time)
        val buttonDateTimePickerFragmentPick = view.findViewById<Button>(R.id.button_dateTimePickerFragment_pick)

        buttonDateTimePickerFragmentPick.setOnClickListener {
            Events.year = datePickerDateTimePickerFragmentDate.year
            Events.month = datePickerDateTimePickerFragmentDate.month
            Events.day = datePickerDateTimePickerFragmentDate.dayOfMonth
            Events.hour = datePickerDateTimePickerFragmentTime.currentHour
            Events.minute = datePickerDateTimePickerFragmentTime.currentMinute
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

        return view
    }

}// Required empty public constructor
