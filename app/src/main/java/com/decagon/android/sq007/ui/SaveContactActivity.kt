package com.decagon.android.sq007.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.decagon.android.sq007.R
import com.decagon.android.sq007.validator.validate.mobileValidate
import com.google.firebase.database.FirebaseDatabase

class SaveContactActivity : AppCompatActivity() {

    lateinit var firstName: EditText
    lateinit var lastName: EditText
    lateinit var phoneNumber: EditText
    lateinit var save: TextView
    lateinit var back: ImageView
    lateinit var phoneNumberValidation: EditText
    lateinit var fname: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_contact)

        var database = FirebaseDatabase.getInstance().reference



        firstName = findViewById(R.id.first_name)
        lastName = findViewById(R.id.last_name)
        phoneNumber = findViewById(R.id.saving_phone_number)
        save = findViewById(R.id.save)
        back = findViewById(R.id.go_back_arrow)

        save.setOnClickListener {
            Log.d("SaveContact", "onCreate:")
            var firstName = firstName.text.toString().trim()
            var lastName = lastName.text.toString().trim()
            var phoneNumber = phoneNumber.text.toString().trim()
            val contact = RecyclerModel(firstName = firstName,lastName = lastName,phoneNumber = phoneNumber)
            contact.id = database.child("CONTACT").push().key
            database.child("CONTACT").child(contact.id!!).setValue(contact)

            val intent = Intent(this, ContactDetailsActivity::class.java)
            intent.putExtra("FirstName", firstName)
            intent.putExtra("LastName", lastName)
            intent.putExtra("PhoneNumber", phoneNumber)
            startActivity(intent)
        }

        back.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Validate phone number
        phoneNumberValidation()
        //Validate name
        firstNamevalidation()
    }

    //phone number validation function
    private fun phoneNumberValidation(){
        phoneNumberValidation = findViewById(R.id.saving_phone_number)
        phoneNumberValidation.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (mobileValidate(phoneNumberValidation.text.toString())){
                    save.isEnabled = true
                } else{
                    save.isEnabled = false
                    phoneNumberValidation.error = "Invalid phone number"
                }
            }
        })
    }

    private fun firstNamevalidation(){
        fname = findViewById(R.id.first_name)
        fname.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (fname.text.toString() == null){
                    save.isEnabled = true
                } else{
                    save.isEnabled = false
                    fname.error = "Invalid name, please enter a name"
                }
            }
        })
    }
}
