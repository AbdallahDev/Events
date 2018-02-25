package com.example.abdallahsarayrah.events

import android.app.Fragment
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.fragment_event_insert.*
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class EventInsertFragmentTest : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_event_insert, container, false)
//        val spinnerEventInsertCommittees = view.findViewById<Spinner>(R.id.spinner_event_insert_committees)

        //bellow to fill the spinner with the committees from the mysql DB
        var url = "http://10.152.70.4/android/android_events/web_apis/committees_get.php?directorateId=${Users.userDirectorate}&committeeId=${Users.userDirectorate}"
        var requestQueue = Volley.newRequestQueue(activity)
        val hashMapCommittees = HashMap<Int, String>()//this hashmap to store the id and the name of the committees from the DB
        hashMapCommittees.put(2, "اختر جهة النشاط")//here i set the first value of the hashmap to make appears as the first value in the dropdown list
        var requestType = JsonArrayRequest(Request.Method.GET, url, null, Response.Listener { response ->
            for (index in 0 until response.length()) {
                hashMapCommittees.put(response.getJSONObject(index).getInt("committee_id"), response.getJSONObject(index).getString("committee_name"))
            }
            val array = hashMapCommittees.values.toTypedArray()
            val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, array)
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
//            spinnerEventInsertCommittees.adapter = adapter
//            spinnerEventInsertCommittees.setSelection(Events.selectedCommitteePosition)//here i set the last selected committee from the spinner, so the user when choose the date and time won't need to choose the committee again
        }, Response.ErrorListener { error ->
            Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
        })
        requestQueue.add(requestType)

        var committeeId = 1//this variable is to store the selected committee id from the spinner, and i set the default value to 1
        //bellow i get the selected committee id from the spinner
//        spinnerEventInsertCommittees.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                committeeId = hashMapCommittees.keys.toTypedArray()[position]
//                Events.selectedCommitteePosition = position //here i get the selected committee from the spinner, so the user when return to the fragment won't need to choose the committee again
//
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {}
//        }

        val editTextEventInsertEventEntityName = view.findViewById<EditText>(R.id.editText_eventInsert_eventEntityName)//here i declare the editText object so i can access it in the fragment
        editTextEventInsertEventEntityName.setText(Events.selectedCommitteeName)

        val buttonEventInsertPickDateTime = view.findViewById<Button>(R.id.button_eventInsert_pickDateTime)//this object to enable me to access the dateTimePick button in the fragment

        buttonEventInsertPickDateTime.setOnClickListener {
            val dialogFragmentObject = DateTimePickerFragment()
            dialogFragmentObject.show(fragmentManager, "DateTimePickerFragment")

            Events.selectedCommitteeName = editTextEventInsertEventEntityName.text.toString()//here i save the committee name that the user typed it in the edit box to the static selectedCommitteeName variable, so the user when he returns to the activity won't need to type it again
        }

        //bellow i'll fill the spinner with the halls from the mysql DB
        val spinnerEventInsertHalls = view.findViewById<Spinner>(R.id.spinner_event_insert_halls)//this object to make me able to access the halls spinner in the fragment
        url = "http://10.152.70.4/android/android_events/web_apis/halls_get.php"
        requestQueue = Volley.newRequestQueue(activity)
        val hashMapHalls = HashMap<Int, String>()//this hashmap to store the id and the name of the halls from the DB
        hashMapHalls.put(0, "اختر قاعة النشاط")//here i set the first value of the hashmap to make appears as the first value in the dropdown list
        requestType = JsonArrayRequest(Request.Method.GET, url, null, Response.Listener { response ->
            for (index in 0 until response.length()) {
                hashMapHalls.put(response.getJSONObject(index).getInt("hall_id"), response.getJSONObject(index).getString("hall_name"))
            }
            val array = hashMapHalls.values.toTypedArray()
            val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, array)
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            spinnerEventInsertHalls.adapter = adapter
        }, Response.ErrorListener { error ->
            Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
        })
        requestQueue.add(requestType)

        var hallId = 0//this variable is to store the selected committee id from the spinner, and i set the default value to zero
        //bellow i get the selected committee id from the spinner
        spinnerEventInsertHalls.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                hallId = hashMapHalls.keys.toTypedArray()[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val textViewEventInsertCreate = view.findViewById<TextView>(R.id.textView_eventInsert_create)
        textViewEventInsertCreate.setOnClickListener {
            val progressDialog = ProgressDialog(activity)
            progressDialog.setMessage("يرجى الانتظار...")
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog.show()

            var eventEntityName = ""//this is the variable to store the event entity name when the user dosen't choose from the dropdown menu
            if (committeeId == 2) {//here i check if the committeeId variable value is equal to 2, by that it means the user hasn't choose the event entity from the dropdown menu, but instead he inserted it in the edit text
                eventEntityName = editText_eventInsert_eventEntityName.text.toString()
            }

            val editTextEventInsertEventSubject = view.findViewById<EditText>(R.id.editText_eventInsert_eventSubject)//this object to enable me to access the subject editText in the fragment

            val url = "http://10.152.70.4/android/android_events/web_apis/event_insert.php?committeeId=$committeeId&eventEntityName=$eventEntityName&eventTime=${Events.hour}:${Events.minute}&subject=${editTextEventInsertEventSubject.text}&event_date=${Events.year}-${Events.month + 1/* +1 to the month because the date picker return the month with a value minus one, ex: december = 11 */}-${Events.day}&hall_id=$hallId"
            val requestType = StringRequest(Request.Method.GET, url, Response.Listener {
                progressDialog.hide()
                val transaction = fragmentManager.beginTransaction()
                val transactionObject = EventsFragment()
                transaction.replace(R.id.fragment_conatiner, transactionObject)
                transaction.commit()
            }, Response.ErrorListener { error ->
                progressDialog.hide()
                Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
            })
            requestQueue.add(requestType)

        }

        return view
    }

}// Required empty public constructor
