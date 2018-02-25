package com.example.abdallahsarayrah.events


import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.R.attr.defaultValue
import android.R.attr.key
import android.widget.*
import kotlinx.android.synthetic.main.fragment_employee_edit.*
import java.util.HashMap


/**
 * A simple [Fragment] subclass.
 */
class EmployeeEditFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view: View = inflater!!.inflate(R.layout.fragment_employee_edit, container, false)

        //this bundle to get the received value from other fragment
        val bundle = arguments

        var spinnerDepartmentID = 0

        //here i'll get the employee data and fill it in their editTex
        var eventsDB = EventsDB(activity)
        var eventsDBObj = eventsDB.writableDatabase
        var eventsDBCursor = eventsDBObj.rawQuery("select * from users where user_id = ?", arrayOf(bundle.getString("user_id")))
        eventsDBCursor.moveToFirst()

        //here i fill the editexts with the data
        var employeeEditEmployeeId = view.findViewById<EditText>(R.id.employee_edit_employee_id)
        employeeEditEmployeeId.setText(eventsDBCursor.getString(eventsDBCursor.getColumnIndex("user_id")))
        var employeeEditEmployeePassword = view.findViewById<EditText>(R.id.employee_edit_employee_password)
        employeeEditEmployeePassword.setText(eventsDBCursor.getString(eventsDBCursor.getColumnIndex("user_password")))
        var employeeEditEmployeeName = view.findViewById<EditText>(R.id.employee_edit_employee_name)
        employeeEditEmployeeName.setText(eventsDBCursor.getString(eventsDBCursor.getColumnIndex("user_name")))

        //bellow to fill the spinner from the DB with the departments
        eventsDBCursor = eventsDBObj.rawQuery("SELECT * FROM departments WHERE department_id != 0", null)
        eventsDBCursor.moveToFirst()
        var hashMapDepartments = HashMap<Int, String>()
        hashMapDepartments.put(0, "اختر المديرية")
        while (!eventsDBCursor.isAfterLast) {
            hashMapDepartments.put(eventsDBCursor.getInt(eventsDBCursor.getColumnIndex("department_id")), eventsDBCursor.getString(eventsDBCursor.getColumnIndex("department_name")))
            eventsDBCursor.moveToNext()
        }
        var arrayDepartments = hashMapDepartments.values.toTypedArray()
        val adapter = ArrayAdapter<CharSequence>(activity, android.R.layout.simple_spinner_item, arrayDepartments)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        var userEditSpinnerDepartments = view.findViewById<Spinner>(R.id.user_edit_spinner_departments)
        userEditSpinnerDepartments.adapter = adapter

        //i get the selected spinner item DB id
        userEditSpinnerDepartments.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                spinnerDepartmentID = hashMapDepartments.keys.toTypedArray()[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        var employeeEditEdit = view.findViewById<TextView>(R.id.employee_edit_edit)
        employeeEditEdit.setOnClickListener {
            eventsDBObj.execSQL("update users set user_password = ?, user_name = ?, user_department_id = ? where user_id = ?", arrayOf(employee_edit_employee_password.text.toString(), employee_edit_employee_name.text.toString(), spinnerDepartmentID, employee_edit_employee_id.text.toString()))

            var transaction = fragmentManager.beginTransaction()
            var transactionObj = EmployeesFragment()
            transaction.replace(R.id.fragment_conatiner, transactionObj)
            transaction.commit()

            Toast.makeText(activity, "تمت التعديل بنجاح", Toast.LENGTH_SHORT).show()
        }

        return view
    }

}// Required empty public constructor
