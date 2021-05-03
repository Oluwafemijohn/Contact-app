package com.decagon.android.sq007.database

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.R
import com.decagon.android.sq007.ui.RecyclerModel

class RecyclerAdapter(var items: List<RecyclerModel>, private val listener: OnItemClickListener, val color:List<ContactColor>) :
    RecyclerView.Adapter<RecyclerAdapter.CardViewHolder>() {
//
//    var items: List<RecyclerModel> = listOf(
//        RecyclerModel("Konga", "Oluwafemi","08130364839" ),
//        RecyclerModel("HDL", "Oluwafemi","08040675563"),
//        RecyclerModel("Cheki.com", "Oluwafemi","07060675563"),
//        RecyclerModel("Konga", "Oluwafemi","08130364839"),
//        RecyclerModel("HDL", "Oluwafemi","08040675563"),
//        RecyclerModel("AliExpress", "Oluwafemi","08138274963"),
//        RecyclerModel("Aviasales", "Oluwafemi","07130675563"),
//        RecyclerModel("Jumial", "Oluwafemi","09130675563"),
//        RecyclerModel("Konga", "Oluwafemi","08130364839"),
//        RecyclerModel("HDL", "Oluwafemi","08040675563"),
//        RecyclerModel("Cheki.com", "Oluwafemi","07060675563"),
//        RecyclerModel("AliExpress", "Oluwafemi","08138274963"),
//        RecyclerModel("Aviasales", "Oluwafemi","07130675563"),
//        RecyclerModel("Jumial", "Oluwafemi","09130675563"),
//        RecyclerModel("Konga", "Oluwafemi","08130364839"),
//        RecyclerModel("HDL", "Oluwafemi","08040675563"),
//        RecyclerModel("Cheki.com", "Oluwafemi","07060675563"),
//        RecyclerModel("Konga", "Oluwafemi","08130364839"),
//        RecyclerModel("HDL", "Oluwafemi","08040675563"),
//        RecyclerModel("Cheki.com", "Oluwafemi","07060675563"),
//        RecyclerModel("AliExpress", "Oluwafemi","08130675563"),
//        RecyclerModel("AliExpress", "Oluwafemi","08138274963")
//    )





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
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

    inner class CardViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val name: TextView = itemView.findViewById(R.id.contact_name)
        private val phoneNumber = itemView.findViewById<TextView>(R.id.phone_number)
        private val logo = itemView.findViewById<TextView>(R.id.name_logo)

        //Binding the data with the view
        fun bind(recyclerModel: RecyclerModel, color: ContactColor) {
            name.text = recyclerModel.name
            phoneNumber.text = recyclerModel.phoneNumber
            logo.text = recyclerModel.name!!.take(1)
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
    fun onItemClick(position: Int, items: List<RecyclerModel>, color:List<ContactColor>)
}




