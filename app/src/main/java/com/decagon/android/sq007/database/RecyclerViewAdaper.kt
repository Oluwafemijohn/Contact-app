package com.decagon.android.sq007.database

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.R
import com.decagon.android.sq007.ui.RecyclerModel

class RecyclerAdapter(var items: List<RecyclerModel>, private val listener: OnItemClickListener, val color: List<ContactColor>) :
    RecyclerView.Adapter<RecyclerAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        // call the inner class
        return CardViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_cardview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(items[position], color.random())
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class CardViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        // Setting View listener to the item in the list
        View.OnClickListener {
        private val name: TextView = itemView.findViewById(R.id.contact_name)
        private val phoneNumber = itemView.findViewById<TextView>(R.id.phone_number)
        private val logo = itemView.findViewById<TextView>(R.id.name_logo)

        // Binding the data with the view
        fun bind(recyclerModel: RecyclerModel, color: ContactColor) {
            name.text = recyclerModel.firstName + " " + recyclerModel.lastName
            phoneNumber.text = recyclerModel.phoneNumber
            // The taking the first letter of the name to logo
            logo.text = recyclerModel.firstName!!.take(1)
            logo.setBackgroundColor(color.color)
        }
        // Setting onclick listener to the itemView

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
// clicked item interface
interface OnItemClickListener {
    fun onItemClick(position: Int, items: List<RecyclerModel>, color: List<ContactColor>)
}
