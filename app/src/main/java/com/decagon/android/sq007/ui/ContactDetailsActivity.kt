package com.decagon.android.sq007.ui

import android.Manifest.permission.CALL_PHONE
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
import com.google.firebase.database.FirebaseDatabase

class ContactDetailsActivity : AppCompatActivity() {
    lateinit var contactName: TextView
    lateinit var phoneNumber: TextView
    lateinit var backGroundColor: ContactColor
    private lateinit var toolbarBackgroundColor: androidx.appcompat.widget.Toolbar
    private lateinit var contacts: RecyclerModel
    lateinit var colors: ContactColor
    lateinit var done: TextView
    lateinit var whatsappNumber:TextView
    lateinit var delete:ImageView
    lateinit var shareButton:ImageView
    lateinit var editContact : ImageView
    lateinit var makeCall:ImageView
    val CALL_PHONE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)

//        contacts = intent.getSerializableExtra("CONTACTS") as RecyclerModel

        //To test where the intent is coming from either from Contact list View or Contact save page
        var test = intent.getStringExtra("TEST")
        if (test != null && test != null) {
            recyclerDetails()
        } else {
            savedContactDetails()
        }

        editContact = findViewById(R.id.edit_icon)

        //The to move to the main contact list view
        done = findViewById(R.id.done)
        done.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }




        //Delecte funtion
        deleteItem()

        //Share contact
        shareContact()

        editContacts()
        //Make call
        buttonTaps()
    }

    private fun recyclerDetails() {
        colors = intent.getSerializableExtra("COLORS") as ContactColor
        contacts = intent.getSerializableExtra("CONTACTS") as RecyclerModel
        contactName = findViewById(R.id.contact_name_details)
        phoneNumber = findViewById(R.id.contact_details_phone_number)
        whatsappNumber = findViewById(R.id.whatsap_details_phone_number)
        toolbarBackgroundColor = findViewById(R.id.contact_details_toolbar)

        contactName.text = contacts.firstName + " " + contacts.lastName
        phoneNumber.text = contacts.phoneNumber
        whatsappNumber.text = contacts.phoneNumber
        toolbarBackgroundColor.setBackgroundColor(colors.color)
    }

    fun savedContactDetails() {
        var fNmae = intent.getStringExtra("FirstName")
        var lName = intent.getStringExtra("LastName")
        var pNumber = intent.getStringExtra("PhoneNumber")

        contactName = findViewById(R.id.contact_name_details)
        phoneNumber = findViewById(R.id.contact_details_phone_number)
        whatsappNumber = findViewById(R.id.whatsap_details_phone_number)
        toolbarBackgroundColor = findViewById(R.id.contact_details_toolbar)

        contactName.text = fNmae + " " + lName
        phoneNumber.text = pNumber
        whatsappNumber.text = pNumber
//        var index: Int = (0..14).random()
//        toolbarBackgroungColor.setBackgroundColor(Colors[index])
    }

    fun deleteItem(){
        //To delete an item
        delete = findViewById(R.id.delete_icon)
        delete.setOnClickListener{

                var contactId =contacts.id
                var mPostReference = FirebaseDatabase.getInstance().getReference()
                    .child("CONTACT").child(contactId!!)
                mPostReference.removeValue()
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
        }
    }


    fun shareContact(){
        shareButton = findViewById(R.id.contact_share_ic)
        shareButton.setOnClickListener{
            val shareContactIntent = Intent()
            shareContactIntent.action = Intent.ACTION_SEND
            shareContactIntent.type="text/plain"
            shareContactIntent.putExtra(Intent.EXTRA_TEXT, "Share name is: ${contacts.firstName
                    + " " + contacts.lastName} and the number is ${contacts.phoneNumber}");
            startActivity(Intent.createChooser(shareContactIntent,getString(R.string.send_to)))
        }
    }

    private fun editContacts(){
        editContact.setOnClickListener {
            val intent = Intent(this, EditContact::class.java)
            intent.putExtra("ContactName",contactName.text)
            intent.putExtra("PhoneNumber",phoneNumber.text)
            intent.putExtra("ContactId",contacts.id)

            startActivity(intent)
        }
    }

    private fun buttonTaps(){
        makeCall = findViewById(R.id.make_call)
        makeCall.setOnClickListener(){
                checkForPermision(android.Manifest.permission.CALL_PHONE, "call", CALL_PHONE)
            }
    }
    private fun checkForPermision(permission:String, name:String, requestCode:Int){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> {
//                    Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
                    val phoneNumber =  contacts.phoneNumber
                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel: $phoneNumber")
                    startActivity(callIntent)
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestCode)
                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fun innerCheck(name: String){
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT).show()
            }else{
//                Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
            }
        }
        when(requestCode){
            CALL_PHONE -> innerCheck("Calls ")
        }
    }
    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("Ok"){
                    dialog, which ->
                ActivityCompat.requestPermissions(this@ContactDetailsActivity, arrayOf(permission), requestCode)
            }
        }
        val dialog = builder.create()
        dialog.show()
    }
}
