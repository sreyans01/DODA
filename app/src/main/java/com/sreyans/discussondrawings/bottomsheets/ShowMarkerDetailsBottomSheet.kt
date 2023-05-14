package com.sreyans.discussondrawings.bottomsheets

import android.text.TextUtils
import androidx.lifecycle.ViewModelProviders
import com.sreyans.discussondrawings.R
import com.sreyans.discussondrawings.databinding.BottomsheetAddMarkerBinding
import com.sreyans.discussondrawings.databinding.ItemMarkerViewholderBinding
import com.sreyans.discussondrawings.event.AddMarkerEvent
import com.sreyans.discussondrawings.helper.BottomSheet
import com.sreyans.discussondrawings.helper.Constants
import com.sreyans.discussondrawings.helper.Utils.toTimeAgo
import com.sreyans.discussondrawings.model.Marker
import com.sreyans.discussondrawings.viewmodel.DrawingsViewModel
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

class ShowMarkerDetailsBottomSheet(val marker: Marker) :
    BottomSheet<ItemMarkerViewholderBinding, DrawingsViewModel>() {

    override fun getBindingVariable(): Int {
        return 0
    }

    override fun getLayoutId(): Int {
        return R.layout.item_marker_viewholder
    }

    override fun getViewModel(): DrawingsViewModel {
        return ViewModelProviders.of(requireActivity(), viewModelFactory).get(
            DrawingsViewModel::class.java)
        //return new ViewModelProvider(requireActivity(), viewModelFactory).get(DrawingsViewModel.class);
    }

    override fun getTagName(): String {
        return Companion.TAG
    }

    override fun initView() {
        binding.title.setText(Constants.TITLE + " - " + marker.title)
        val date = SimpleDateFormat(Constants.DATE_PATTERN_NORMAL).parse(marker.createdOn)
        val timeString = date.time.toTimeAgo()
        binding.creationTime.setText(Constants.CREATED + " - " + timeString)
        binding.description.setText(Constants.DESCRIPTION + " - " + marker.description)
        binding.coordinates.setText(Constants.COORDINATES + " - " + "x: ${marker.x}, y: ${marker.y}")
    }

    override fun initData() {}

    companion object {
        const val TAG = "ShowMarkerDetailsBottomSheet"
    }
}