package com.sreyans.discussondrawings.viewholder

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sreyans.discussondrawings.R
import com.sreyans.discussondrawings.databinding.ItemDrawingViewholderBinding
import com.sreyans.discussondrawings.event.OnItemClickEvent
import com.sreyans.discussondrawings.event.ShowAllMarkersEvent
import com.sreyans.discussondrawings.helper.Constants
import com.sreyans.discussondrawings.helper.Utils.toTimeAgo
import com.sreyans.discussondrawings.model.Drawing
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat

class ItemDrawingViewHolder(binding: ItemDrawingViewholderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var binding: ItemDrawingViewholderBinding
    private val context: Context? = null

    fun onBind(context: Context, drawing: Drawing) {
        binding.parent.setOnClickListener {
            EventBus.getDefault().post(OnItemClickEvent(drawing))
        }
        binding.viewAllMarkersBtn.setOnClickListener {
            EventBus.getDefault().post(ShowAllMarkersEvent(drawing))
        }
        try {
            // Circular Progress Drawable to show while Glide loads image
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 10f
            circularProgressDrawable.centerRadius = 48f
            circularProgressDrawable.setColorSchemeColors(Color.BLACK)
            circularProgressDrawable.start()

            // Set the image using Glide library
            Glide.with(context)
                .load(drawing.drawingImageUrl)
                .apply(RequestOptions()
                    .placeholder(circularProgressDrawable))
                .apply(RequestOptions()
                    .fitCenter())
                .apply(RequestOptions()
                    .error(R.drawable.ic_add_icon))
                .into(binding.drawingImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.title.setText(drawing.title)
        val date = SimpleDateFormat(Constants.DATE_PATTERN_WITH_SECONDS).parse(drawing.createdOn)
        val timeString = date.time.toTimeAgo()
        binding.creationTime.setText(timeString)
        binding.markersInfo.setText(drawing.markers.size.toString())
    }

    init {
        this.binding = binding as ItemDrawingViewholderBinding
        binding.lifecycleOwner = context as LifecycleOwner?
    }
}
