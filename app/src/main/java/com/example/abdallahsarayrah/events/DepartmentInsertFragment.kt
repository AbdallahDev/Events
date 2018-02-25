//this fragment to insert Departments in the DB

package com.example.abdallahsarayrah.events


import android.app.Activity
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import android.R.attr.x


/**
 * A simple [Fragment] subclass.
 */
open class DepartmentInsertFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view: View = inflater!!.inflate(R.layout.fragment_department_insert, container, false)

        var departmentInsertDepartmentName = view.findViewById<EditText>(R.id.department_insert_department_name)
        var employeeInsertInsert = view.findViewById<TextView>(R.id.department_insert_insert)

        //here i insert the department from the text box when the user click on the button
        employeeInsertInsert.setOnClickListener {
            var eventsDBObj = EventsDB(activity)
            var eventsDB = eventsDBObj.writableDatabase
            eventsDB.execSQL("insert into departments(department_name) values(?)", arrayOf(departmentInsertDepartmentName.text.toString()))

            //here i redirect the user to the departments list after he inserted the department
            var transaction = fragmentManager.beginTransaction()
            var transactionObject = DepartmentsFragment()
            transaction.replace(R.id.fragment_conatiner, transactionObject)
            transaction.commit()

            Toast.makeText(activity, "تمت الاضافة بنجاح", Toast.LENGTH_SHORT).show()
        }

        return view
    }

}// Required empty public constructor
