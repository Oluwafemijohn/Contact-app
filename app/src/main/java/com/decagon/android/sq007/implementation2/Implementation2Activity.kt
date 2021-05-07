package com.decagon.android.sq007.implementation2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.R
import com.decagon.android.sq007.database.Colors
import com.decagon.android.sq007.database.ContactColor
import com.decagon.android.sq007.ui.ContactDetailsActivity

class Implementation2Activity : AppCompatActivity(), OnItemClickListener {
    private lateinit var recyclerView2: RecyclerView
    private var contactsList: ArrayList<ContactModel> = ArrayList()
    private lateinit var readRecyclerAdapter: RecyclerAdapter
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    companion object {
        private const val READ_CONTACT = 102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_implementation2)

        // creating the instance of the adapter
        readRecyclerAdapter = RecyclerAdapter(contactsList, this, Colors.color)
        recyclerView2 = findViewById(R.id.recycler_view2)

        // Checking for permission and then reading the contact
        checkForPermision(Manifest.permission.READ_CONTACTS, "FemiApp", READ_CONTACT)

        toolbar = findViewById(R.id.contact_one_tool_bar2)

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.implementation_2 -> {
                    finish()
                    true
                }
                R.id.grant_permission -> {
                    checkForPermision(Manifest.permission.READ_CONTACTS, "FemiApp", READ_CONTACT)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    fun readContact() {
        val myContact = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        // Setting limit to the number of contact to load
        var count = 0
        while (myContact?.moveToNext() == true && count < 100) {
            // Getting the contact loaded
            val name =
                myContact.getString(myContact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val number =
                myContact.getString(myContact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val modelObj = ContactModel(contactName = name, contactNumber = number)
            contactsList.add(modelObj)
            count++
        }
        // Attaching adapter
        readRecyclerAdapter = RecyclerAdapter(contactsList, this, Colors.color)
        recyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView2.adapter = readRecyclerAdapter

        myContact?.close()
    }

    // checking for permission
    private fun checkForPermision(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // call read contact function
                    readContact()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(
                    permission,
                    name,
                    requestCode
                )
                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    // check for permission and make call
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT)
                    .show()
                readContact()
            }
        }
        when (requestCode) {
            READ_CONTACT -> innerCheck("FemiApp")
        }
    }

    // Show dialog for permission dialog
    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("Ok") { dialog, which ->
                ActivityCompat.requestPermissions(
                    this@Implementation2Activity,
                    arrayOf(permission),
                    requestCode
                )
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    // onCLick listener to item view
    override fun onItemClick(position: Int, items: List<ContactModel>, color: List<ContactColor>) {
        val contacts = items[position]
        val colors = color[position]
        val intent = Intent(this, ContactDetailsActivity::class.java)
        intent.putExtra("CONTACTS", contacts)
        intent.putExtra("COLORS", colors)
        intent.putExtra("IMPLEMENTATION", "TESTME")
        startActivity(intent)
    }

//    //Popup menu inflater
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater: MenuInflater = menuInflater
//        inflater.inflate(R.menu.implementation_two_top_menu, menu)
//        return true
//    }
//
//    //item selection
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle item selection
//        return when (item.itemId) {
//            R.id.implementation_2 -> {
// //                var intent = Intent(this, MainActivity::class.java)
// //                startActivity(intent)
//                onBackPressed()
//                true
//            }
//            R.id.grant_permission -> {
//                checkForPermision(Manifest.permission.READ_CONTACTS, "FemiApp", READ_CONTACT)
//                true
//            }
//            else -> {
//                super.onOptionsItemSelected(item)
//            }
//        }
//    }
}
