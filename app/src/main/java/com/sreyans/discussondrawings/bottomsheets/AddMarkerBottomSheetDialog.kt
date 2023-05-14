package com.sreyans.discussondrawings.bottomsheets

import android.text.TextUtils
import androidx.lifecycle.ViewModelProviders
import com.sreyans.discussondrawings.R
import com.sreyans.discussondrawings.databinding.BottomsheetAddMarkerBinding
import com.sreyans.discussondrawings.event.AddMarkerEvent
import com.sreyans.discussondrawings.helper.BottomSheet
import com.sreyans.discussondrawings.helper.Constants
import com.sreyans.discussondrawings.model.Marker
import com.sreyans.discussondrawings.viewmodel.DrawingsViewModel
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

class AddMarkerBottomSheetDialog(val x: Float, val y: Float) :
    BottomSheet<BottomsheetAddMarkerBinding, DrawingsViewModel>() {

    override fun getBindingVariable(): Int {
        return 0
    }

    override fun getLayoutId(): Int {
        return R.layout.bottomsheet_add_marker
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
        binding.addMarker.setOnClickListener {
            val sdf = SimpleDateFormat(Constants.DATE_PATTERN_NORMAL)
            val currentDateAndTime: String = sdf.format(Date())
            if (!TextUtils.isEmpty(binding.title.text) && !TextUtils.isEmpty(binding.description.text)) {
                val title = binding.title.text.toString()
                val desc = binding.description.text.toString()
                EventBus.getDefault()
                    .post(AddMarkerEvent(Marker(this.x, this.y, title, desc, currentDateAndTime)))
                dismissAllowingStateLoss()
            }
        }
    }

    override fun initData() {}

    companion object {
        const val TAG = "AddMarkerBottomSheetDialog"
    }
}