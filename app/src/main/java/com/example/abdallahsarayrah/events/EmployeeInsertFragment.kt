package com.example.abdallahsarayrah.events


import android.annotation.SuppressLint
import android.os.Bundle
import android.app.Fragment
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.fragment_employee_insert.*
import java.util.HashMap

/**
 * A simple [Fragment] subclass.
 */
class EmployeeInsertFragment : Fragment() {

    @SuppressLint("UseSparseArrays")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view: View = inflater!!.inflate(R.layout.fragment_employee_insert, container, false)

        var employeeInsertInsert = view.findViewById<TextView>(R.id.employee_insert_insert)
        var employeeInsertSpinnerDepartments = view.findViewById<Spinner>(R.id.employee_insert_spinner_departments)
        var eventsDB = EventsDB(activity)
        var eventsDBObj = eventsDB.writableDatabase
        var eventsDBCursor: Cursor
        var spinnerDepartmentID = 0


        //bellow to fill the spinner from the DB with the departments
        eventsDBCursor = eventsDBObj.rawQuery("SELECT * FROM departments WHERE department_id != 0", null)
        eventsDBCursor.moveToFirst()
        val hashMapDepartments = HashMap<Int, String>()
        hashMapDepartments.put(0, "اختر المديرية")
        while (!eventsDBCursor.isAfterLast) {
            hashMapDepartments.put(eventsDBCursor.getInt(eventsDBCursor.getColumnIndex("department_id")), eventsDBCursor.getString(eventsDBCursor.getColumnIndex("department_name")))
            eventsDBCursor.moveToNext()
        }
        val arrayDepartments = hashMapDepartments.values.toTypedArray()
        val adapter = ArrayAdapter<CharSequence>(activity, android.R.layout.simple_spinner_item, arrayDepartments)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        employeeInsertSpinnerDepartments.adapter = adapter

        //i get the selected spinner item DB id
        employeeInsertSpinnerDepartments.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                spinnerDepartmentID = hashMapDepartments.keys.toTypedArray()[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        //i insert the employee to the DB when the user clicks on the button
        employeeInsertInsert.setOnClickListener {
            if (spinnerDepartmentID != 0) {
                eventsDBObj.execSQL("insert into users(user_id, user_password, user_name, user_department_id) values(?, ?, ?, ?)", arrayOf(employee_insert_employee_id.text.toString(), employee_insert_employee_password.text.toString(), employee_insert_employee_name.text.toString(), spinnerDepartmentID))

                val transaction = fragmentManager.beginTransaction()
                val transactionObj = EmployeesFragment()
                transaction.replace(R.id.fragment_conatiner, transactionObj)
                transaction.commit()

                Toast.makeText(activity, "تمت الاضافة بنجاح", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(activity, "يرجى اختيار المديرية", Toast.LENGTH_SHORT).show()
        }

        return view
    }

}// Required empty public constructor
