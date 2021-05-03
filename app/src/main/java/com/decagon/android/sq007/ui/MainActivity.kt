package com.decagon.android.sq007.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.R
import com.decagon.android.sq007.database.Colors
import com.decagon.android.sq007.database.ContactColor
import com.decagon.android.sq007.database.OnItemClickListener
import com.decagon.android.sq007.database.RecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class MainActivity : AppCompatActivity(), OnItemClickListener {
    var fireBaseDataArray:ArrayList<RecyclerModel> = ArrayList()
    lateinit var dataBaseReference: DatabaseReference
    private val adapter = RecyclerAdapter(fireBaseDataArray, this, Colors.color)
    lateinit var recyclerView: RecyclerView
    private lateinit var floatbutton:FloatingActionButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)






        initRecyclerView()

        recyclerView =findViewById(R.id.recycler_view)
        floatbutton = findViewById(R.id.floating_button)
        floatbutton.setOnClickListener{
            val intent = Intent(this, SaveContactActivity::class.java)
            startActivity(intent)
        }

        fireBaseDataArray = arrayListOf()

        displayfirebaseData()

    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager =
            LinearLayoutManager(
               this, // Context
                LinearLayoutManager.VERTICAL, // Orientation
                false
            ) // Reverse layout
        recyclerView.adapter = adapter
    }

    override fun onItemClick(position: Int, items:List<RecyclerModel>, color:List<ContactColor>) {
//        Toast.makeText(this, "$position", Toast.LENGTH_SHORT).show()
        val contacts = items[position]
        val colors =  color[position]
        val intent = Intent(this, ContactDetailsActivity::class.java)
        intent.putExtra("CONTACTS", contacts)
        intent.putExtra("COLORS", colors)
        intent.putExtra("TEST", "TESTME")
        startActivity(intent)


    }

    private fun displayfirebaseData(){
        dataBaseReference = FirebaseDatabase.getInstance().getReference("CONTACT")
        dataBaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                fireBaseDataArray.clear()
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val phoneContacts = userSnapshot.getValue<RecyclerModel>()
                        if (phoneContacts != null) {
                            fireBaseDataArray.add(phoneContacts)
                        }
                    }

                    Log.d("CHECKERS", "THIS $fireBaseDataArray")
                     val adapter = RecyclerAdapter(fireBaseDataArray, this@MainActivity, Colors.color)
                    val put = fireBaseDataArray as ArrayList<RecyclerModel>
                    recyclerView.adapter = adapter

                    //adapter.notifyDataSetChanged()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }



}

