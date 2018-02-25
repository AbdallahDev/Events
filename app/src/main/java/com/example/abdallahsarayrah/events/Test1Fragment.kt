package com.example.abdallahsarayrah.events


import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast


/**
 * A simple [Fragment] subclass.
 */
class Test1Fragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_test1, container, false)
        val textViewFragmentTest1 = view.findViewById<TextView>(R.id.textView_fragment_test1)

//        Toast.makeText(activity, arguments.getString("params"), Toast.LENGTH_SHORT).show()
        textViewFragmentTest1.text = arguments.getString("params")

        return view
    }

}// Required empty public constructor
