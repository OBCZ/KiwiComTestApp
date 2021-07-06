package com.baarton.kiwicomtestapp.ui.results

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.baarton.kiwicomtestapp.R
import com.baarton.kiwicomtestapp.data.Flight


class FlightsAdapter(var flights: List<Flight>) : RecyclerView.Adapter<FlightsAdapter.ViewHolder>() {

    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.flight_list_item, parent, false)
        val viewHolder = ViewHolder(view)
        context = parent.context
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flight: Flight = flights[position]
        holder.flightFromView.text = flight.flyFrom
        holder.flightToView.text = flight.flyTo
        holder.flightDurationView.text = flight.duration
        holder.flightPriceView.text = flight.price
        holder.cardView.setOnClickListener {//TODO item detail fragment/animation?
            Toast.makeText(
                context,
                "The position is:$position",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int {
        return flights.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var flightFromView: TextView = itemView.findViewById<View>(R.id.flight_item_from) as TextView
        var flightToView: TextView = itemView.findViewById<View>(R.id.flight_item_to) as TextView
        var flightDurationView: TextView = itemView.findViewById<View>(R.id.flight_item_duration) as TextView
        var flightPriceView: TextView = itemView.findViewById<View>(R.id.flight_item_price) as TextView
        var cardView: CardView = itemView.findViewById<View>(R.id.fight_item_card) as CardView
    }

}