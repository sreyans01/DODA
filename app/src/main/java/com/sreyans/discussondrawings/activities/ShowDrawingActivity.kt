package com.sreyans.discussondrawings.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.android.autelsdk.util.Status
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.sreyans.discussondrawings.R
import com.sreyans.discussondrawings.bottomsheets.AddMarkerBottomSheetDialog
import com.sreyans.discussondrawings.bottomsheets.ShowMarkerDetailsBottomSheet
import com.sreyans.discussondrawings.databinding.ActivityShowDrawingBinding
import com.sreyans.discussondrawings.event.AddMarkerEvent
import com.sreyans.discussondrawings.helper.Constants
import com.sreyans.discussondrawings.helper.UIUtils
import com.sreyans.discussondrawings.helper.Utils.observeOnce
import com.sreyans.discussondrawings.model.Drawing
import com.sreyans.discussondrawings.viewmodel.DrawingsViewModel
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class ShowDrawingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowDrawingBinding
    private val viewModel: DrawingsViewModel by viewModels()
    private var firstTouch = false
    private var time = System.currentTimeMillis()
    private lateinit var drawing: Drawing


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        FirebaseAuth.getInstance().signInAnonymously()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_drawing)

        initView()
        handleListeners()

    }

    private fun initView() {
        drawing = getIntent().getSerializableExtra(Constants.KEY_DRAWINGS) as Drawing
        binding.drawingView.doubleTapEnabled = false
        binding.drawingView.displayType = ImageViewTouchBase.DisplayType.NONE
        // Set the image using Glide library
        Glide.with(this).load(drawing.drawingImageUrl).into(binding.drawingView)
        binding.title.setText(drawing.title)
        if (drawing.markers != null)
            viewModel.markers = drawing.markers
        else
            viewModel.markers = ArrayList()

        for (marker in viewModel.markers) {
            addMarkersToDrawing(marker.x, marker.y)
        }


    }

    fun handleListeners() {


        binding.updateDrawingBtn.setOnClickListener {
            if (!TextUtils.isEmpty(binding.title.text)) {
                val title = binding.title.text.toString()
                lifecycleScope.launch {
                    viewModel.updateDrawing(Drawing(drawing.drawingImageUrl,
                        title,
                        drawing.createdOn,
                        drawing.markers))
                        .observeOnce(this@ShowDrawingActivity, { msg ->
                            when (msg.status) {
                                Status.SUCCESS -> {
                                    finish()
                                }
                                Status.ERROR -> {
                                    UIUtils.showToast(this@ShowDrawingActivity, msg.message)
                                }
                                else -> {

                                }
                            }
                        })
                }
            } else {
                UIUtils.showToast(this@ShowDrawingActivity, R.string.title_cannot_be_empty)
            }
        }

        binding.drawingView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val x = event.x
                        val y = event.y
                        if (firstTouch && (System.currentTimeMillis() - time) <= 300) {
                            firstTouch = false;
                            addMarkersToDrawing(x, y)
                            val addMarkerBottomSheet = AddMarkerBottomSheetDialog(x, y)
                            addMarkerBottomSheet.isCancelable = false
                            UIUtils.showBottomSheet(this@ShowDrawingActivity, addMarkerBottomSheet)

                        } else {
                            firstTouch = true;
                            time = System.currentTimeMillis();
                            return false;
                        }
                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })


    }

    //For Enabling Drag Feature we can use it. Currently, disabling it!
    private var xDelta: Float = 0.0f
    private var yDelta: Float = 0.0f
    private val touchListener = View.OnTouchListener { view, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                xDelta = view.getX() - event.getRawX();
                yDelta = view.getY() - event.getRawY();

            }

            MotionEvent.ACTION_MOVE -> {
                view.animate()
                    .x(event.getRawX() + xDelta)
                    .y(event.getRawY() + yDelta)
                    .setDuration(0)
                    .start();
            }
            else -> false
        }
        binding.parent.invalidate()
        true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAddMarkerEvent(event: AddMarkerEvent) {
        if (event != null) {
            this.drawing.markers.add(event.marker)
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    private fun addMarkersToDrawing(x: Float, y: Float) {
        var image = ImageView(this@ShowDrawingActivity)
        image.layoutParams = ViewGroup.LayoutParams(20, 20)
        image.setImageDrawable(ContextCompat.getDrawable(this@ShowDrawingActivity,
            R.drawable.ic_marker_red))
        image.scaleType = ImageView.ScaleType.FIT_CENTER
        image.layoutParams.width = 20
        image.layoutParams.height = 20
        image.requestLayout()
        val params = FrameLayout.LayoutParams(80, 80)
        image.x = x
        image.y = y
        image.adjustViewBounds = true
        //disabling marker drag feature currently
        //image.setOnTouchListener(touchListener)
        binding.parent.addView(image, params)

        image.setOnClickListener {
            for (marker in drawing.markers) {
                if (marker.x == x && marker.y == y) {
                    UIUtils.showBottomSheet(this@ShowDrawingActivity,
                        ShowMarkerDetailsBottomSheet(marker))
                    break;
                }
            }
        }
    }


}