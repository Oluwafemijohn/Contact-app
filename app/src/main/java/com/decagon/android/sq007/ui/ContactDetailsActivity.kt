package com.decagon.android.sq007.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.decagon.android.sq007.R
import com.decagon.android.sq007.database.ContactColor
import com.decagon.android.sq007.implementation2.ContactModel
import com.google.firebase.database.FirebaseDatabase

class ContactDetailsActivity : AppCompatActivity() {
    private lateinit var contactName: TextView
    private lateinit var phoneNumber: TextView
    private lateinit var toolbarBackgroundColor: androidx.appcompat.widget.Toolbar
    private lateinit var contacts: RecyclerModel
    private lateinit var colors: ContactColor
    private lateinit var doneButton: TextView
    private lateinit var whatsAppNumber: TextView
    private lateinit var deleteButton: ImageView
    private lateinit var shareButton: ImageView
    private lateinit var editContact: ImageView
    private lateinit var makeCallButton: ImageView
    private val CALL_PHONE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)

        contactName = findViewById(R.id.contact_name_details)
        phoneNumber = findViewById(R.id.contact_details_phone_number)
        whatsAppNumber = findViewById(R.id.whatsap_details_phone_number)
        toolbarBackgroundColor = findViewById(R.id.contact_details_toolbar)
        shareButton = findViewById(R.id.contact_share_ic)
        makeCallButton = findViewById(R.id.make_call)
        editContact = findViewById(R.id.edit_icon)
        doneButton = findViewById(R.id.done)
        deleteButton = findViewById(R.id.delete_icon)
        makeCallButton = findViewById(R.id.make_call)
        shareButton = findViewById(R.id.contact_share_ic)

        // To test where the intent is coming from either from Contact list View or
        // Contact save page or from contact list of the implementation 2
        var test = intent.getStringExtra("TEST")
        var implementationIntent = intent.getStringExtra("IMPLEMENTATION")
        if (test != null && test != null) {
            firebaseDatabaseContactDetails()
        }
        else if (implementationIntent != null){
            myPhoneContactListDetails()
        }
        else {
            savedContactDetails()
        }

        // The to move to the main contact list view
        doneButton.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Delecte funtion
        deleteContactConfirmation()

        // Share contact
        shareContact()

        editContacts()
        // Make call
        makeCallAfterPermissionChecked()
    }


    //Display the details when view is click from Implementation 2 contact list
    private fun myPhoneContactListDetails(){
        //Getting the contact from the intent
        var implementationTwoContact = intent.getSerializableExtra("CONTACTS") as ContactModel
        var implementationTwoColor:ContactColor = intent.getSerializableExtra("COLORS") as ContactColor
        //attach the name and number to text field
        contactName.text = implementationTwoContact.contactName
        whatsAppNumber.text = implementationTwoContact.contactNumber
        phoneNumber.text = implementationTwoContact.contactNumber
        toolbarBackgroundColor.setBackgroundColor(implementationTwoColor.color)
    }


    //Display the details when the view is clicked from the implementation 2
    private fun firebaseDatabaseContactDetails(){
        //Getting the contact from the intent
        colors = intent.getSerializableExtra("COLORS") as ContactColor
        contacts = intent.getSerializableExtra("CONTACTS") as RecyclerModel
        //attaching it to the text field
        contactName.text = contacts.firstName + " " + contacts.lastName
        phoneNumber.text = contacts.phoneNumber
        whatsAppNumber.text = contacts.phoneNumber
        toolbarBackgroundColor.setBackgroundColor(colors.color)
    }


    //Display the details when the contact is saved
    fun savedContactDetails() {
        //Getting the contact when saved
        var fNmae = intent.getStringExtra("FirstName")
        var lName = intent.getStringExtra("LastName")
        var pNumber = intent.getStringExtra("PhoneNumber")
        //Setting the details
        contactName.text = fNmae + " " + lName
        phoneNumber.text = pNumber
        whatsAppNumber.text = pNumber
    }

    //Delete the contact
    fun deleteContact() {
            var contactId = contacts.id
            var mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("CONTACT").child(contactId!!)
            mPostReference.removeValue()
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
    }

    //Confirm to delete the contact
    private fun deleteContactConfirmation() {
        deleteButton.setOnClickListener {
            AlertDialog.Builder(this).also {
                it.setTitle("Contact will be deleted")
                it.setMessage("Are you sure you want delete the contact?")
                it.setIcon(R.drawable.ic_baseline_delete_24)
                it.setPositiveButton("DELETE") { dialog, which ->
                    deleteContact()
                    Toast.makeText(
                        this,
                        "Contact Deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                it.setNegativeButton("CANCEL") { dialog, which ->
                    dialog.cancel()
                }
            }.create().show()
        }
    }

    //share contact
    fun shareContact() {
        shareButton.setOnClickListener {
            val shareContactIntent = Intent()
            shareContactIntent.action = Intent.ACTION_SEND
            shareContactIntent.type = "text/plain"
            shareContactIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Share name is: ${contacts.firstName +
                    " " + contacts.lastName} and the number is ${contacts.phoneNumber}"
            )
            startActivity(Intent.createChooser(shareContactIntent, getString(R.string.send_to)))
        }
    }

    //Editing contact intent
    private fun editContacts() {
        editContact.setOnClickListener {
            val intent = Intent(this, EditContact::class.java)
            intent.putExtra("ContactName", contactName.text)
            intent.putExtra("PhoneNumber", phoneNumber.text)
            intent.putExtra("ContactId", contacts.id)

            startActivity(intent)
        }
    }

    //Check permission status and making call
    private fun makeCallAfterPermissionChecked() {
        makeCallButton.setOnClickListener() {
            checkForPermission(android.Manifest.permission.CALL_PHONE, "call", CALL_PHONE)
        }
    }

    //Checking the permission status
    private fun checkForPermission(permission: String, name: String, requestCode: Int, ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> {
                    makePhoneCall()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestCode)
                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    //Make call when permission is granted
    private fun makePhoneCall(){
        val contactNumber = phoneNumber.text.toString()
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel: $contactNumber")
        startActivity(callIntent)
    }

    //make call interface after the permission has been granted
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
                makePhoneCall()
            }
        }
        when (requestCode) {
            CALL_PHONE -> innerCheck("Calls ")
        }
    }
    //Show dialog box to request for permission when not granted the first time
    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("Ok") {
                dialog, which ->
                ActivityCompat.requestPermissions(this@ContactDetailsActivity, arrayOf(permission), requestCode)
            }
        }
        val dialog = builder.create()
        dialog.show()
    }
}
