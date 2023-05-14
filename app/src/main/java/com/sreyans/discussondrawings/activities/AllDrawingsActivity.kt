package com.sreyans.discussondrawings.activities

import AllDrawingsAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sreyans.discussondrawings.R
import com.sreyans.discussondrawings.databinding.ActivityAllDrawingsBinding
import com.sreyans.discussondrawings.model.Drawing
import com.sreyans.discussondrawings.viewmodel.DrawingsViewModel

class AllDrawingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllDrawingsBinding
    private var drawingsList: ArrayList<Drawing> = ArrayList()
    private val viewModel: DrawingsViewModel by viewModels()
    private lateinit var adapter: AllDrawingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_drawings)

        initRecyclerView()
        getAllDrawings()
    }


    fun getAllDrawings() {
        try {
            val mObserver = Observer<Any> { allDrawings ->
                drawingsList.clear()
                for (drawing in allDrawings as ArrayList<Drawing>) {
                    drawingsList.add(drawing)
                }
                adapter.notifyDataSetChanged()
            }
            viewModel.getAllDrawings().observe(this, mObserver)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this,
                getString(R.string.unable_to_fetch_drawings),
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRecyclerView() {
        try {
            adapter = AllDrawingsAdapter(this, drawingsList)
            binding.recyclerView.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}