package com.baarton.kiwicomtestapp.ui.results

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.NetworkImageView
import com.baarton.kiwicomtestapp.R
import com.baarton.kiwicomtestapp.data.Flight


class FlightsAdapter(var flights: List<Flight>, private val imageLoader: ImageLoader?) : RecyclerView.Adapter<FlightsAdapter.ViewHolder>() {

    companion object {
        private const val IMAGE_REQUEST_URL = "https://images.kiwi.com/photos/600x330/%s.jpg"
    }

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.flight_list_item, parent, false)
        val viewHolder = ViewHolder(view)
        context = parent.context
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flight: Flight = flights[position]
        holder.flightImg.setDefaultImageResId(R.drawable.ic_launcher_background)
        val finalDestination = flight.routeList.last().mapIdTo
        if (imageLoader != null) {
            holder.flightImg.setImageUrl(String.format(IMAGE_REQUEST_URL, finalDestination), imageLoader)
        }
        holder.flightFromView.text = flight.flyFrom
        holder.flightToView.text = flight.flyTo
        holder.flightDurationView.text = flight.duration
        holder.flightPriceView.text = context!!.getString(R.string.card_flight_price_text, flight.price, flight.currency)
        holder.cardView.setOnClickListener {//TODO item detail fragment/animation?
            Toast.makeText(
                context,
                "The position is: $position",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int {
        return flights.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var flightImg: NetworkImageView = itemView.findViewById(R.id.flight_item_img)
        var flightFromView: TextView = itemView.findViewById(R.id.flight_item_from)
        var flightToView: TextView = itemView.findViewById(R.id.flight_item_to)
        var flightDurationView: TextView = itemView.findViewById(R.id.flight_item_duration)
        var flightPriceView: TextView = itemView.findViewById(R.id.flight_item_price)
        var cardView: CardView = itemView.findViewById(R.id.fight_item_card)
    }

}