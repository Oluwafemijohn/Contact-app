package com.decagon.android.sq007.ui

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.decagon.android.sq007.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SaveContactActivity : AppCompatActivity() {

    lateinit var firstName: EditText
    lateinit var lastName: EditText
    lateinit var phoneNumber: EditText
    lateinit var save: TextView
    lateinit var back: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_contact)

        var database = FirebaseDatabase.getInstance().reference
//        database.setValue("Oluwafemi")

        firstName = findViewById(R.id.first_name)
        lastName = findViewById(R.id.last_name)
        phoneNumber = findViewById(R.id.saving_phone_number)
        save = findViewById(R.id.save)
        back = findViewById(R.id.go_back_arrow)


        save.setOnClickListener {
            var firstName = firstName.text.toString()
            var lastName = lastName.text.toString()
            var phoneNumber = phoneNumber.text.toString()
            database.child("CONTACT").child(firstName+lastName).setValue(RecyclerModel(firstName, lastName, phoneNumber))


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

    }


}