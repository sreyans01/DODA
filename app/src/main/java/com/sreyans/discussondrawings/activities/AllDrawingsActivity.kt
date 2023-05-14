package com.sreyans.discussondrawings.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.sreyans.discussondrawings.R
import com.sreyans.discussondrawings.adapter.AllDrawingsAdapter
import com.sreyans.discussondrawings.databinding.ActivityAllDrawingsBinding
import com.sreyans.discussondrawings.event.OnItemClickEvent
import com.sreyans.discussondrawings.helper.Constants
import com.sreyans.discussondrawings.helper.UIUtils
import com.sreyans.discussondrawings.model.Drawing
import com.sreyans.discussondrawings.viewmodel.DrawingsViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AllDrawingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllDrawingsBinding
    private var drawingsList: ArrayList<Drawing> = ArrayList()
    private val viewModel: DrawingsViewModel by viewModels()
    private lateinit var adapter: AllDrawingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        FirebaseAuth.getInstance().signInAnonymously()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_drawings)

        initRecyclerView()
        getAllDrawings()
        handleListeners()
    }


    fun getAllDrawings() {
        try {
            val mObserver = Observer<Any> { allDrawings ->
                drawingsList.clear()
                for (drawing in allDrawings as ArrayList<Drawing>) {
                    drawingsList.add(drawing)
                }
                drawingsList.reverse()
                adapter.notifyDataSetChanged()
            }
            viewModel.getAllDrawings().observe(this, mObserver)
        } catch (e: Exception) {
            e.printStackTrace()
            UIUtils.showToast(this@AllDrawingsActivity, R.string.unable_to_fetch_drawings)
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

    private fun handleListeners() {
        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddDrawingsActivity::class.java))
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onItemClickEvent(event: OnItemClickEvent) {
        if (event != null && event.data is Drawing) {
            val drawing: Drawing = event.data as Drawing
            val intent = Intent(this@AllDrawingsActivity, ShowDrawingActivity::class.java)
            intent.putExtra(Constants.KEY_DRAWINGS, drawing)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}