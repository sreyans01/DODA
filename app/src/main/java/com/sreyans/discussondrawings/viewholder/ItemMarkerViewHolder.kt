package com.sreyans.discussondrawings.viewholder

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.sreyans.discussondrawings.databinding.ItemMarkerViewholderBinding
import com.sreyans.discussondrawings.event.OnItemClickEvent
import com.sreyans.discussondrawings.helper.Constants
import com.sreyans.discussondrawings.helper.Utils.toTimeAgo
import com.sreyans.discussondrawings.model.Marker
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat

class ItemMarkerViewHolder(binding: ItemMarkerViewholderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var binding: ItemMarkerViewholderBinding
    private val context: Context? = null

    fun onBind(context: Context, marker: Marker) {
        binding.parent.setOnClickListener {
            EventBus.getDefault().post(OnItemClickEvent(marker))
        }

        binding.title.setText(Constants.TITLE + " - " + marker.title)
        val date = SimpleDateFormat(Constants.DATE_PATTERN_NORMAL).parse(marker.createdOn)
        val timeString = date.time.toTimeAgo()
        binding.creationTime.setText(Constants.CREATED + " - " + timeString)
        binding.description.setText(Constants.DESCRIPTION + " - " + marker.description)
        binding.coordinates.setText(Constants.COORDINATES + " - " + "x: ${marker.x}, y: ${marker.y}")
    }

    init {
        this.binding = binding as ItemMarkerViewholderBinding
        binding.lifecycleOwner = context as LifecycleOwner?
    }
}
