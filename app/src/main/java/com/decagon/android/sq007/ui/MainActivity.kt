package com.decagon.android.sq007.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.R
import com.decagon.android.sq007.database.Colors
import com.decagon.android.sq007.database.ContactColor
import com.decagon.android.sq007.database.OnItemClickListener
import com.decagon.android.sq007.database.RecyclerAdapter
import com.decagon.android.sq007.implementation2.Implementation2Activity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class MainActivity : AppCompatActivity(), OnItemClickListener {
    var fireBaseDataArray: ArrayList<RecyclerModel> = ArrayList()
    lateinit var dataBaseReference: DatabaseReference
    private val adapter = RecyclerAdapter(fireBaseDataArray, this, Colors.color)
    lateinit var recyclerView: RecyclerView
    private lateinit var floatbutton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Getting ids
        recyclerView = findViewById(R.id.recycler_view)
        floatbutton = findViewById(R.id.floating_button)
        // Connect to database
        dataBaseReference = FirebaseDatabase.getInstance().getReference("CONTACT")
        // Load the itemViews
        initRecyclerView()

        setSupportActionBar(findViewById(R.id.contact_one_tool_bar))
        floatbutton.setOnClickListener {
            val intent = Intent(this, SaveContactActivity::class.java)
            startActivity(intent)
        }

        // Read and display the contacts from database
        displayfirebaseData()
    }
    // Load the itemViews
    private fun initRecyclerView() {
//        recyclerView.layoutManager = LinearLayoutManager(this )
        recyclerView.adapter = adapter
    }

    // Setting the listener to the view
    override fun onItemClick(position: Int, items: List<RecyclerModel>, color: List<ContactColor>) {
        val contacts = items[position]
        val colors = color[position]
        // Going to the contact details page
        val intent = Intent(this, ContactDetailsActivity::class.java)
        intent.putExtra("CONTACTS", contacts)
        intent.putExtra("COLORS", colors)
        intent.putExtra("TEST", "TESTME")
        startActivity(intent)
    }
    // Read and display the contacts from database
    fun displayfirebaseData() {
        dataBaseReference.addValueEventListener(object : ValueEventListener {
            // Looping through the contacts in firebase and adding it to local storage
            override fun onDataChange(snapshot: DataSnapshot) {
                // clearing the previous contacts before looping and adding to the lis
                fireBaseDataArray.clear()
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val phoneContacts = userSnapshot.getValue<RecyclerModel>()
                        // To check if the contact is not null
                        if (phoneContacts != null) {
                            fireBaseDataArray.add(phoneContacts)
                        }
                    }
                    // Sorting the contacts in the list and attaching it to the adapter
                    fireBaseDataArray.sortWith(compareBy { it.firstName })
                    val adapter =
                        RecyclerAdapter(fireBaseDataArray, this@MainActivity, Colors.color)
                    recyclerView.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // To inflate the menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.implementation -> {
                var intent = Intent(this, Implementation2Activity::class.java)
                startActivity(intent)

                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}
