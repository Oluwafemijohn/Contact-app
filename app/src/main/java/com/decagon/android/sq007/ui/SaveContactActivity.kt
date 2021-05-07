package com.decagon.android.sq007.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.decagon.android.sq007.R
import com.decagon.android.sq007.validator.validate.mobileValidate
import com.google.firebase.database.FirebaseDatabase

class SaveContactActivity : AppCompatActivity() {

    lateinit var firstName: EditText
    lateinit var lastName: EditText
    lateinit var phoneNumber: EditText
    lateinit var saveButton: Button
    lateinit var backButtom: ImageView
    lateinit var phoneNumberValidation: EditText
    lateinit var fname: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_contact)
        //connecting to database
        var database = FirebaseDatabase.getInstance().reference


        firstName = findViewById(R.id.first_name)
        lastName = findViewById(R.id.last_name)
        phoneNumber = findViewById(R.id.saving_phone_number)
        saveButton = findViewById(R.id.save)
        backButtom = findViewById(R.id.go_back_arrow)
        fname = findViewById(R.id.first_name)

        //Save the contacts
        saveButton.setOnClickListener {
            Log.d("SaveContact", "onCreate:")
            var firstName = firstName.text.toString().trim()
            var lastName = lastName.text.toString().trim()
            var phoneNumber = phoneNumber.text.toString().trim()
            val contact = RecyclerModel(firstName = firstName, lastName = lastName, phoneNumber = phoneNumber)
            //creating id for the contact
            contact.id = database.child("CONTACT").push().key
            database.child("CONTACT").child(contact.id!!).setValue(contact)

            //moving to the details paage
            val intent = Intent(this, ContactDetailsActivity::class.java)
            intent.putExtra("FirstName", firstName)
            intent.putExtra("LastName", lastName)
            intent.putExtra("PhoneNumber", phoneNumber)
            startActivity(intent)
            finish()
        }
        //back button
        backButtom.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Validate phone number
        phoneNumberValidation()
        // Validate name
        firstNamevalidation()
    }

    // phone number validation function
    private fun phoneNumberValidation() {
        phoneNumberValidation = findViewById(R.id.saving_phone_number)
        phoneNumberValidation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            //Checking if the phone number is correct
            override fun afterTextChanged(s: Editable?) {
                if (mobileValidate(phoneNumberValidation.text.toString())) {
                    saveButton.isEnabled = true
                } else {
                   saveButton.isEnabled = false
                    phoneNumberValidation.error = "Invalid phone number"
                }
            }
        })
    }

    // Validating name
    private fun firstNamevalidation() {
        fname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                //Checking if the name has been entered
                if (fname.text.toString() == null) {
                    saveButton.isEnabled = true
                } else {
                   saveButton.isEnabled = false
                    fname.error = "Name can not be empty"
                }
            }
        })
    }
}
