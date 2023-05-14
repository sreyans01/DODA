package com.sreyans.discussondrawings.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sreyans.discussondrawings.R
import com.sreyans.discussondrawings.adapter.AllMarkersAdapter
import com.sreyans.discussondrawings.databinding.ActivityAllMarkersBinding
import com.sreyans.discussondrawings.helper.Constants
import com.sreyans.discussondrawings.model.Drawing
import com.sreyans.discussondrawings.model.Marker

class AllMarkersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllMarkersBinding
    private var markersList: ArrayList<Marker> = ArrayList()
    private lateinit var adapter: AllMarkersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_markers)

        val drawing: Drawing = intent.getSerializableExtra(Constants.KEY_DRAWINGS) as Drawing
        markersList = drawing.markers
        initRecyclerView()
    }

    private fun initRecyclerView() {
        try {
            adapter = AllMarkersAdapter(this, markersList)
            binding.recyclerView.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}