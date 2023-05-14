package com.sreyans.discussondrawings.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sreyans.discussondrawings.R
import com.sreyans.discussondrawings.databinding.ItemMarkerViewholderBinding
import com.sreyans.discussondrawings.model.Marker
import com.sreyans.discussondrawings.viewholder.ItemMarkerViewHolder

class AllMarkersAdapter(context: Context, markers: ArrayList<Marker>) :
    RecyclerView.Adapter<ItemMarkerViewHolder>() {
    private val context: Context
    private val markers: ArrayList<Marker>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMarkerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemMarkerViewholderBinding: ItemMarkerViewholderBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_marker_viewholder, parent, false)
        return ItemMarkerViewHolder(itemMarkerViewholderBinding)
    }

    override fun getItemCount(): Int {
        return this.markers.size
    }

    fun setList(list: ArrayList<Marker>) {
        this.markers.clear()
        this.markers.addAll(list)
    }

    init {
        this.markers = markers
        this.context = context
    }

    override fun onBindViewHolder(holder: ItemMarkerViewHolder, position: Int) {
        (holder as ItemMarkerViewHolder).onBind(context, markers[position])
    }
}