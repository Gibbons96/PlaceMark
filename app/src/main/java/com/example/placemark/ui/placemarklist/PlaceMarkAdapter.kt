package com.example.placemark.ui.placemarklist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_placemark.view.*
import com.example.placemark.R
import com.example.placemark.helpers.readImageFromPath
import com.example.placemark.models.PlaceMarkModel

interface PlaceMarkListener {
    fun onPlaceMarkClick(placeMark: PlaceMarkModel)
}

class PlaceMarkAdapter(
    private var placemarks: List<PlaceMarkModel>,
    private val listener: PlaceMarkListener
) : RecyclerView.Adapter<PlaceMarkAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_placemark, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val placeMark = placemarks[holder.adapterPosition]
        holder.bind(placeMark, listener)
    }

    override fun getItemCount(): Int = placemarks.size

    class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(placeMark: PlaceMarkModel, listener: PlaceMarkListener) {
            Log.d("TAG", "bind: ${placeMark.title} : ${placeMark.image}")
            itemView.placemark_title.text = placeMark.title
            itemView.placemark_description.text = placeMark.description
            if (placeMark.image.isNotBlank()) {
                itemView.placemarkImageView.setImageBitmap(
                    readImageFromPath(
                        itemView.context,
                        placeMark.image
                    )
                )
            }
            itemView.setOnClickListener { listener.onPlaceMarkClick(placeMark) }
        }
    }
}