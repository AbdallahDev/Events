//in this activity i try to get the users from the online database (MySQL)
package com.example.abdallahsarayrah.events


import android.app.Fragment
import android.app.ProgressDialog
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley


/**
 * A simple [Fragment] subclass.
 */
class UsersFragment : Fragment() {

    private val usersArray = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_users, container, false)

        val listViewUsers = view.findViewById<ListView>(R.id.listView_usersFragment_users)

        val pd = ProgressDialog(activity)
        pd.setMessage("يرجى الانتظار...")
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)

        //connect ot the mysql DB to get all the users
        val requestQueue = Volley.newRequestQueue(activity)
        val jasonArrayRequest = JsonArrayRequest(Request.Method.GET, "http://10.152.154.135/android/android_events/prl/users_get_android.php", null,
                Response.Listener { response ->
                    if (response != null) {
                        pd.hide()
                        (0 until response.length()).mapTo(usersArray) { response.getJSONObject(it).getString("name") }
                        val adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, usersArray)
                        listViewUsers.adapter = adapter
                    }
                }, Response.ErrorListener { error ->
            pd.hide()
            Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
        })
        requestQueue.add(jasonArrayRequest)

        registerForContextMenu(listViewUsers)

        return view
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menu?.add(1, 1, 1, "تعديل")
        menu?.add(1, 2, 2, "حذف")
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        val menuInfo = item?.menuInfo as AdapterView.AdapterContextMenuInfo

        //connect ot the mysql DB to get all the users
        val requestQueue = Volley.newRequestQueue(activity)
        val jasonArrayRequest = JsonArrayRequest(Request.Method.GET, "http://193.188.88.148/android/android_events/prl/users_get.php", null,
                Response.Listener { response ->
                    if (response != null) {
                        (0 until response.length()).mapTo(usersArray) { response.getJSONObject(it).getString("name") }

                        when (item.itemId) {
                        //here when the user clicks on the edit context menu item, i send the id of the employee to be edited to the edit fragment
                            1 -> {
                                val bundle = Bundle()
//                                bundle.putString("user_id", "${}")

                                val fragmentManager = activity.fragmentManager
                                val fragmentTransaction = fragmentManager.beginTransaction()
                                val employeeEditFragment = EmployeeEditFragment()
                                employeeEditFragment.arguments = bundle
                                fragmentTransaction.replace(R.id.fragment_conatiner, employeeEditFragment)
                                fragmentTransaction.commit()
                            }

                        //here when the user clicks on the delete context menu item, i delete the chosen employee from the DB and remove from the list
                            2 -> {
//                                val alertDialog = AlertDialog.Builder(activity).create()
//                                alertDialog.setTitle("الحذف")
//                                alertDialog.setMessage("هل انت متأكد من الحذف")
//                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "نعم", { _: DialogInterface?, _: Int ->
//                                    eventsDBObj.execSQL("delete from users where user_id = ?", arrayOf(eventsDBCursor.getInt(eventsDBCursor.getColumnIndex("user_id"))))
//                                    Toast.makeText(activity, "تم الحذف بنجاح", Toast.LENGTH_SHORT).show()
//
//                                    val transaction = fragmentManager.beginTransaction()
//                                    val transactionObj = EmployeesFragment()
//                                    transaction.replace(R.id.fragment_conatiner, transactionObj)
//                                    transaction.commit()
//                                })
//                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "لا", { dialog, _ -> dialog.dismiss() })
//                                alertDialog.show()
                            }
                        }
                    }
                }, Response.ErrorListener { error ->
            Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
        })
        requestQueue.add(jasonArrayRequest)

        return super.onContextItemSelected(item)
    }

}// Required empty public constructor
