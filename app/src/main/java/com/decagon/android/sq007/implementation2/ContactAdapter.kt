package com.decagon.android.sq007.implementation2

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.R
import com.decagon.android.sq007.database.ContactColor

// class ContactAdapter {
// }

class ContactAdapter(var items: List<ContactModel>, private val listener: OnItemClickListener, val color: List<ContactColor>) :
    RecyclerView.Adapter<ContactAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        Log.d("Adapter", "onCreateViewHolder: $items")
        return CardViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_cardview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        Log.d("Adapter", "onCreateViewHolder: ${items[position]}")

        holder.bind(items[position], color.random())
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class CardViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val name: TextView = itemView.findViewById(R.id.contact_name)
        private val phoneNumber = itemView.findViewById<TextView>(R.id.phone_number)
        private val logo = itemView.findViewById<TextView>(R.id.name_logo)

        // Binding the data with the view
        fun bind(contactModel: ContactModel, color: ContactColor) {
            name.text = contactModel.contactName
            phoneNumber.text = contactModel.contactNumber
            logo.text = contactModel.contactName!!.take(1)
            logo.setBackgroundColor(color.color)
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position, items, color)
            }
        }
    }
}

interface OnItemClickListener {
    fun onItemClick(position: Int, items: List<ContactModel>, color: List<ContactColor>)
}
