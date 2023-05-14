package com.sreyans.discussondrawings.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.android.autelsdk.util.Status
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.sreyans.discussondrawings.R
import com.sreyans.discussondrawings.bottomsheets.AddMarkerBottomSheetDialog
import com.sreyans.discussondrawings.bottomsheets.ShowMarkerDetailsBottomSheet
import com.sreyans.discussondrawings.databinding.ActivityAddDrawingsBinding
import com.sreyans.discussondrawings.event.AddMarkerEvent
import com.sreyans.discussondrawings.helper.UIUtils
import com.sreyans.discussondrawings.helper.Utils.observeOnce
import com.sreyans.discussondrawings.model.Marker
import com.sreyans.discussondrawings.viewmodel.DrawingsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class AddDrawingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddDrawingsBinding
    private val viewModel: DrawingsViewModel by viewModels()
    private var firstTouch = false
    private var time = System.currentTimeMillis()
    private var markers: ArrayList<Marker> = ArrayList()

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            lifecycleScope.launch(Dispatchers.Main) {
                viewModel.getDrawingImageUrl(uri)
                    .observeOnce(this@AddDrawingsActivity, Observer { msg ->
                        when (msg.status) {
                            Status.SUCCESS -> {
                                viewModel.imageUrl.postValue(msg.data)
                            }
                            Status.ERROR -> {
                                Toast.makeText(this@AddDrawingsActivity,
                                    msg.message,
                                    Toast.LENGTH_LONG)
                            }
                            else -> {

                            }
                        }

                    })
            }
        } else {
            Toast.makeText(this, getString(R.string.select_drawing), Toast.LENGTH_LONG)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        FirebaseAuth.getInstance().signInAnonymously()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_drawings)

        handleListeners()

    }

    fun handleListeners() {
        binding.addDrawingBtn.setOnClickListener {
            pickAndCropImage()
        }

        viewModel.imageUrl.observe(this@AddDrawingsActivity, { url ->
            if (url.isEmpty()) {
                binding.addDrawingLayoutParent.visibility = View.VISIBLE
                binding.drawingView.visibility = View.GONE
            } else {
                binding.addDrawingLayoutParent.visibility = View.GONE
                binding.drawingView.visibility = View.VISIBLE
                Glide.with(this@AddDrawingsActivity).load(url).into(binding.drawingView)
            }
        })

        binding.uploadDrawingBtn.setOnClickListener {
            if (!TextUtils.isEmpty(binding.title.text)) {
                if (binding.addDrawingLayoutParent.visibility == View.GONE) {
                    val title = binding.title.text.toString()
                    lifecycleScope.launch {

                        viewModel.uploadDrawing(title, viewModel.imageUrl.value.toString(), markers)
                            .observeOnce(this@AddDrawingsActivity, { msg ->
                                when (msg.status) {
                                    Status.SUCCESS -> {
                                        finish()
                                    }
                                    Status.ERROR -> {
                                        Toast.makeText(this@AddDrawingsActivity,
                                            msg.message,
                                            Toast.LENGTH_LONG)
                                    }
                                    else -> {

                                    }
                                }
                            })
                    }
                } else {
                    Toast.makeText(this@AddDrawingsActivity,
                        getString(R.string.select_drawing),
                        Toast.LENGTH_LONG)
                }
            } else {
                Toast.makeText(this@AddDrawingsActivity,
                    getString(R.string.title_cannot_be_empty),
                    Toast.LENGTH_LONG)
            }
        }

        binding.drawingView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (firstTouch && (System.currentTimeMillis() - time) <= 300) {
                            firstTouch = false;

                            val x = event.x
                            val y = event.y

                            var image = ImageView(this@AddDrawingsActivity)
                            image.layoutParams = ViewGroup.LayoutParams(20, 20)
                            image.setImageDrawable(ContextCompat.getDrawable(this@AddDrawingsActivity,
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
                                for (marker in markers) {
                                    if (marker.x == x && marker.y == y) {
                                        UIUtils.showBottomSheet(this@AddDrawingsActivity, ShowMarkerDetailsBottomSheet(marker))
                                        break;
                                    }
                                }
                            }

                            val addMarkerBottomSheet = AddMarkerBottomSheetDialog(x, y)
                            addMarkerBottomSheet.isCancelable = false
                            UIUtils.showBottomSheet(this@AddDrawingsActivity, addMarkerBottomSheet)

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

    fun pickAndCropImage() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
            this.markers.add(event.marker)
            viewModel.markers.add(event.marker)
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}