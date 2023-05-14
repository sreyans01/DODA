package com.sreyans.discussondrawings.bottomsheets

import android.text.TextUtils
import android.util.Log
import android.view.View
import com.sreyans.discussondrawings.helper.BottomSheet
import com.sreyans.discussondrawings.viewmodel.DrawingsViewModel
import com.sreyans.discussondrawings.R
import androidx.lifecycle.ViewModelProviders
import com.sreyans.discussondrawings.databinding.BottomsheetAddMarkerBinding
import com.sreyans.discussondrawings.event.AddMarkerEvent
import com.sreyans.discussondrawings.model.Marker
import com.sreyans.discussondrawings.repository.DrawingFunctions
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

class AddMarkerBottomSheetDialog(x: Float, y: Float) : BottomSheet<BottomsheetAddMarkerBinding, DrawingsViewModel>() {
    val x = x
    val y = y
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
            val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
            val currentDateAndTime: String = sdf.format(Date())
            if (!TextUtils.isEmpty(binding.title.text) && !TextUtils.isEmpty(binding.description.text)) {
                val title = binding.title.text.toString()
                val desc = binding.description.text.toString()
                EventBus.getDefault().post(AddMarkerEvent(Marker(x, y, title, desc, currentDateAndTime)))
                dismissAllowingStateLoss()
            }
        }
    }
    override fun initData() {}

    companion object {
        const val TAG = "AddMarkerBottomSheetDialog"
    }
}