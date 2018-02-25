package com.example.abdallahsarayrah.events


import android.app.AlertDialog
import android.app.Fragment
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast
import android.widget.AdapterView


/**
 * A simple [Fragment] subclass.
 */
class DepartmentsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view: View = inflater!!.inflate(R.layout.fragment_departments, container, false)

        //the code bellow to get all the departments from the DB and insert them in array
        var eventsDBObj = EventsDB(activity)
        var eventsDB = eventsDBObj.readableDatabase
        var eventsDBCursor = eventsDB.rawQuery("SELECT * FROM departments WHERE department_id != 0", null)
        eventsDBCursor.moveToFirst()
        var arrayDepartments = arrayListOf<String>()
        while (!eventsDBCursor.isAfterLast) {
            arrayDepartments.add(eventsDBCursor.getString(eventsDBCursor.getColumnIndex("department_name")))
            eventsDBCursor.moveToNext()
        }
        eventsDBCursor.close()
        //bellow i create an adapter and fill it with the array data
        var adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, arrayDepartments)
        var listViewDepartments = view.findViewById<ListView>(R.id.list_view_departments)
        listViewDepartments.adapter = adapter


        //bellow i try to delete department from the DB when the user longClick the department on the list VIew
        listViewDepartments.setOnItemLongClickListener { parent, view, position, id ->
            var alertDialog = AlertDialog.Builder(activity).create()
            alertDialog.setTitle("الحذف")
            alertDialog.setMessage("هل أنت متأكد من الحذف")
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "نعم", { dialog: DialogInterface?, which: Int ->
                run {
                    adapter.remove(arrayDepartments[position])
                    var eventsDB = EventsDB(activity)
                    var eventsDBObj = eventsDB.writableDatabase
                    var eventsDBCursor = eventsDBObj.rawQuery("select * from departments", null)
                    eventsDBCursor.moveToPosition(position)
                    eventsDBObj.execSQL("delete from departments where department_id = ?", arrayOf(eventsDBCursor.getInt(eventsDBCursor.getColumnIndex("department_id"))))
                    adapter.notifyDataSetChanged()
                    listViewDepartments.adapter = adapter
                    Toast.makeText(activity, "تم الحذف بنجاح", Toast.LENGTH_SHORT).show()
                }
            })
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "لا", { dialog: DialogInterface?, which: Int ->
                dialog?.dismiss()
            })
            alertDialog.show()
            true
        }

        //bellow i view the department insert fragment in the main activity after the user click the create new department button
        var departmentsFragmentDepartmentInsert = view.findViewById<TextView>(R.id.departments_fragment_department_insert)
        departmentsFragmentDepartmentInsert.setOnClickListener {
            var transaction = fragmentManager.beginTransaction()
            var transactionObj = DepartmentInsertFragment()
            transaction.replace(R.id.fragment_conatiner, transactionObj)
            transaction.commit()
        }

        return view
    }

}// Required empty public constructor


