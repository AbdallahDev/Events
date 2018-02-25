package com.example.abdallahsarayrah.events

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Fragment
import android.content.DialogInterface
import android.database.Cursor
import android.os.Bundle
import android.view.*
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

/**
 * A simple [Fragment] subclass.
 */
class EmployeesFragment : Fragment() {

    @SuppressLint("Recycle")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.fragment_employees, container, false)

        val listViewEmployees = view.findViewById<ListView>(R.id.list_view_employees)

        registerForContextMenu(listViewEmployees)

        //bellow i get the employees from the DB and view them in the list view
        val eventsDBObj = EventsDB(activity)
        val eventsDB = eventsDBObj.readableDatabase
        val eventsDBCursor: Cursor
        val employeesFragmentEmployees = view.findViewById<TextView>(R.id.employees_fragment_employees)
        if (Users.userId == 0) {//here i check if the user id is 0 for the super admin to view all the users, else i'll view just on user information
            eventsDBCursor = eventsDB.rawQuery("SELECT * FROM users inner join departments on user_department_id = department_id", null)
            employeesFragmentEmployees.text = "معلومات المستخدمين"
        } else {
            eventsDBCursor = eventsDB.rawQuery("SELECT * FROM users inner join departments on user_department_id = department_id where user_id = ?", arrayOf(Users.userId.toString()))
            employeesFragmentEmployees.text = "معلومات المستخدم"
        }
        eventsDBCursor.moveToFirst()
        val employeesArray = ArrayList<String>()
        while (!eventsDBCursor.isAfterLast) {
            if (eventsDBCursor.getInt(eventsDBCursor.getColumnIndex("user_id")) != 0) employeesArray.add("رقم المستخدم: " + eventsDBCursor.getInt(eventsDBCursor.getColumnIndex("user_id")) + " - " + "اسم المستخدم: " + eventsDBCursor.getString(eventsDBCursor.getColumnIndex("user_name")) + " - " + "المديرية: " + eventsDBCursor.getString(eventsDBCursor.getColumnIndex("department_name")) + " - " + "نوع المستخدم: " + eventsDBCursor.getString(eventsDBCursor.getColumnIndex("user_type")))
            else {
                if (eventsDBCursor.getInt(eventsDBCursor.getColumnIndex("user_valid")) != 0) employeesArray.add("رقم المستخدم: " + eventsDBCursor.getInt(eventsDBCursor.getColumnIndex("user_id")) + " - " + "اسم المستخدم: " + eventsDBCursor.getString(eventsDBCursor.getColumnIndex("user_name")) + " - " + "نوع المستخدم: " + eventsDBCursor.getString(eventsDBCursor.getColumnIndex("user_type")) + "  المستخدم غير مفعل")
                else employeesArray.add("رقم المستخدم: " + eventsDBCursor.getInt(eventsDBCursor.getColumnIndex("user_id")) + " - " + "اسم المستخدم: " + eventsDBCursor.getString(eventsDBCursor.getColumnIndex("user_name")) + " - " + "نوع المستخدم: " + eventsDBCursor.getString(eventsDBCursor.getColumnIndex("user_type")))
            }
            eventsDBCursor.moveToNext()
        }
        eventsDBCursor.close()
        val adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, employeesArray)
        listViewEmployees.adapter = adapter

        return view
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menu?.add(1, 1, 1, "تعديل")
        menu?.add(1, 2, 2, "حذف")
    }

    @SuppressLint("CommitTransaction", "Recycle")
    override fun onContextItemSelected(item: MenuItem?): Boolean {
        val menuInfo = item?.menuInfo as AdapterContextMenuInfo

        //bellow i get the employees from the DB to get the selected one to delete or edit it
        val eventsDB = EventsDB(activity)
        val eventsDBObj = eventsDB.writableDatabase
        val eventsDBCursor = eventsDBObj.rawQuery("select * from users", null)
        eventsDBCursor.moveToPosition(menuInfo.id.toInt())
        when (item.itemId) {
        //here when the user clicks on the edit context menu item, i send the id of the employee to be edited to the edit fragment
            1 -> {
                val bundle = Bundle()
                bundle.putString("user_id", "${eventsDBCursor.getInt(eventsDBCursor.getColumnIndex("user_id"))}")

                val fragmentManager = activity.fragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                val employeeEditFragment = EmployeeEditFragment()
                employeeEditFragment.arguments = bundle
                fragmentTransaction.replace(R.id.fragment_conatiner, employeeEditFragment)
                fragmentTransaction.commit()
            }

        //here when the user clicks on the delete context menu item, i delete the chosen employee from the DB and remove from the list
            2 -> {
                val alertDialog = AlertDialog.Builder(activity).create()
                alertDialog.setTitle("الحذف")
                alertDialog.setMessage("هل انت متأكد من الحذف")
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "نعم", { _: DialogInterface?, _: Int ->
                    eventsDBObj.execSQL("delete from users where user_id = ?", arrayOf(eventsDBCursor.getInt(eventsDBCursor.getColumnIndex("user_id"))))
                    Toast.makeText(activity, "تم الحذف بنجاح", Toast.LENGTH_SHORT).show()

                    val transaction = fragmentManager.beginTransaction()
                    val transactionObj = EmployeesFragment()
                    transaction.replace(R.id.fragment_conatiner, transactionObj)
                    transaction.commit()
                })
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "لا", { dialog, _ -> dialog.dismiss() })
                alertDialog.show()
            }
        }

        return super.onContextItemSelected(item)
    }
}// Required empty public constructor
