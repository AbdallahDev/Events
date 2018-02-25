package com.example.abdallahsarayrah.events

import android.app.ProgressDialog
import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_events.*

/**
 * A simple [Fragment] subclass.
 */
class EventsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_events, container, false)

        val url = "http://10.152.154.135/android/android_events/web_apis/events.php"
        val eventsList = ArrayList<String>()
        val requestQueue = Volley.newRequestQueue(activity)
        val arrayRequest = JsonArrayRequest(Request.Method.GET, url, null, Response.Listener { response ->
            (0 until response.length() - 1).mapTo(eventsList) { response.getJSONObject(it).getString("event_entity_name") + " | " + response.getJSONObject(it).getString("event_date") + " | " + response.getJSONObject(it).getString("time") + " | " + response.getJSONObject(it).getString("event_place") + " | " + response.getJSONObject(it).getString("subject") }
            val eventsAdapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, eventsList)
            listView_events.adapter = eventsAdapter
        }, Response.ErrorListener { error ->
//            Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
        })
        requestQueue.add(arrayRequest)

        val buttonEventsNewEvent = view.findViewById<Button>(R.id.button_events_newEvent)

        buttonEventsNewEvent.setOnClickListener {
            val transaction = fragmentManager.beginTransaction()
            val transactionObj = EventInsertFragment()
            transaction.replace(R.id.fragment_conatiner, transactionObj)
            transaction.commit()
        }

        return view
    }

}// Required empty public constructor
