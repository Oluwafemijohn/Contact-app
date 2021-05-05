package com.decagon.android.sq007.implementation2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ActionMenuView
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
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Implementation2Activity : AppCompatActivity(), OnItemClickListener {
    lateinit var floating_button2: FloatingActionButton
    lateinit var recyclerView2: RecyclerView
    lateinit var implementiontion2: ActionMenuView
    lateinit var contactModel: ContactModel
    companion object {
        private val TAG = "permission"
        private val CALL_PHONE = 101
        private val READ_CONTACT = 102
        const val readContacts_requestCode = 23
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_implementation2)

        checkForPermision(Manifest.permission.READ_CONTACTS, "Read_CONTACT", READ_CONTACT)

        floating_button2 = findViewById(R.id.floating_button2)
        recyclerView2 = findViewById(R.id.recycler_view2)

//        readContact()
    }

    fun readContact() {
        recyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        setUpPermissions(android.Manifest.permission.READ_CONTACTS, "myContacts", readContacts_requestCode)
        val myContactList: MutableList<ContactModel> = ArrayList()
        val myContact = contentResolver?.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        while (myContact?.moveToNext()!!) {
            val Name = myContact.getString(myContact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val Number = myContact.getString(myContact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val modelObj = ContactModel()
            modelObj.contactName = Name
            modelObj.contactNumber = Number
            myContactList.add(modelObj)
        }
        recyclerView2.adapter = ContactAdapter(myContactList, this, Colors.color)
        myContact.close()
    }

    fun setUpPermissions(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(this, "$name Permission Granted", Toast.LENGTH_LONG).show()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestCode)
                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }
    private fun checkForPermision(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> {
//                    Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
                    readContact()
                    val phoneNumber = contactModel.contactNumber
                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel: $phoneNumber")
                    startActivity(callIntent)
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestCode)
                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT).show()
            } else {
//                Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
                readContact()
            }
        }
        when (requestCode) {
            READ_CONTACT -> innerCheck("Read_CONTACT")
        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("Ok") {
                dialog, which ->
                ActivityCompat.requestPermissions(this@Implementation2Activity, arrayOf(permission), requestCode)
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onItemClick(position: Int, items: List<ContactModel>, color: List<ContactColor>) {
        val contacts = items[position]
        val colors = color[position]
        val intent = Intent(this, ContactDetailsActivity::class.java)
        intent.putExtra("CONTACTS", contacts)
        intent.putExtra("COLORS", colors)
        intent.putExtra("TEST", "TESTME")
        startActivity(intent)
    }
}
