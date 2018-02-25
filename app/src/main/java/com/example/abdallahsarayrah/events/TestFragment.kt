package com.example.abdallahsarayrah.events

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView

/**
 * A simple [Fragment] subclass.
 */

class TestFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_test, container, false)
        val editTextAutocomplete = view.findViewById<AutoCompleteTextView>(R.id.editText_autocomplete)
        val textViewAutocomplete = view.findViewById<TextView>(R.id.textView_autocomplete)

        val array = ArrayList<String>()
        val array1 = ArrayList<String>()
        array.add("add")
        array.add("adding")
        array.add("added")

        for (i in 0 until array1.size)
            textViewAutocomplete.text = textViewAutocomplete.text.toString() + "-" + array1[i]

//        val url = "http://10.152.70.4/android/android_events/web_apis/committees_contain_name_get.php?committeeName="

        var adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, array)
        editTextAutocomplete.setAdapter(adapter)

        editTextAutocomplete.setOnItemClickListener { parent, _, position, _ ->
            (0 until array.size)
                    .filter { array[it].toLowerCase().contains(parent.getItemAtPosition(position).toString()) }
                    .forEach { array1.add(array[it]) }

            adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, array1)
            editTextAutocomplete.setAdapter(adapter)

            textViewAutocomplete.text = parent.getItemAtPosition(position).toString()
        }

        return view
    }

}// Required empty public constructor
