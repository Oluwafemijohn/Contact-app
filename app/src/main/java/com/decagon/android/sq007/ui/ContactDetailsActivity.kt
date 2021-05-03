package com.decagon.android.sq007.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.decagon.android.sq007.R
import com.decagon.android.sq007.database.ContactColor

class ContactDetailsActivity : AppCompatActivity() {
    lateinit var contactName:TextView
    lateinit var phoneNumber:TextView
    lateinit var backGroundColor: ContactColor
    private lateinit var toolbarBackgroundColor: androidx.appcompat.widget.Toolbar
    lateinit var contacts: RecyclerModel
    lateinit var colors: ContactColor
    lateinit var done:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)
        var test = intent.getStringExtra("TEST")
        if (test != null && test != null){
            recyclerDetails()
        }else{
            savedContactDetails()
        }

        done = findViewById(R.id.done)
        done.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    private fun recyclerDetails(){

        contacts = intent.getSerializableExtra("CONTACTS") as RecyclerModel
        colors = intent.getSerializableExtra("COLORS") as ContactColor

        contactName = findViewById(R.id.contact_name_details)
        phoneNumber = findViewById(R.id.contact_details_phone_number)
        toolbarBackgroundColor = findViewById(R.id.contact_details_toolbar)
        contactName.text = contacts.name
        phoneNumber.text = contacts.phoneNumber
        toolbarBackgroundColor.setBackgroundColor(colors.color)
    }

    fun savedContactDetails(){
        var fNmae = intent.getStringExtra("FirstName")
        var lName = intent.getStringExtra("LastName")
        var pNumber = intent.getStringExtra("PhoneNumber")

        contactName = findViewById(R.id.contact_name_details)
        phoneNumber = findViewById(R.id.contact_details_phone_number)
        toolbarBackgroundColor = findViewById(R.id.contact_details_toolbar)

        contactName.text = fNmae + " "+ lName
        phoneNumber.text = pNumber
        var index:Int = (0..14).random()
//        toolbarBackgroungColor.setBackgroundColor(Colors[index])
    }
}