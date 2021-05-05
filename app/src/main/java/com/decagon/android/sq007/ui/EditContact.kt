package com.decagon.android.sq007.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.decagon.android.sq007.R
import com.google.firebase.database.FirebaseDatabase

class EditContact : AppCompatActivity() {

    lateinit var edtFirstName: EditText
    lateinit var edtLastName: EditText
    lateinit var phoneNumber: EditText
    lateinit var save: TextView
    lateinit var back: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_contact)

        val headerName = findViewById<TextView>(R.id.create_contact)
        headerName.text = getString(R.string.editContact)
        var database = FirebaseDatabase.getInstance().reference



        edtFirstName = findViewById(R.id.first_name)
        edtLastName = findViewById(R.id.last_name)
        phoneNumber = findViewById(R.id.saving_phone_number)
        save = findViewById(R.id.save)
        back = findViewById(R.id.go_back_arrow)

        val fullName =  intent.getStringExtra("ContactName")
        val phoneNumberr = intent.getStringExtra("PhoneNumber")
        val contactId = intent.getStringExtra("ContactId")

        val( firstName, lastName) = fullName?.split( " ")!!
        edtFirstName.setText(firstName)
        edtLastName.setText(lastName)
        phoneNumber.setText(phoneNumberr)

        save.setOnClickListener {

            var newFirstName = edtFirstName.text.toString().trim()
            var newLastName = edtLastName.text.toString().trim()
            var phoneNumber = phoneNumber.text.toString().trim()
//            Log.d("EditContact", "onCreate: $contactId $newFirstName $newLastName")
            database.child("CONTACT").child(contactId!!).setValue(RecyclerModel(id = contactId,firstName = newFirstName, lastName = newLastName, phoneNumber = phoneNumber))

            val intent = Intent(this, ContactDetailsActivity::class.java)
            intent.putExtra("FirstName", newFirstName)
            intent.putExtra("LastName", newLastName)
            intent.putExtra("PhoneNumber", phoneNumber)
            startActivity(intent)
        }

        back.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
