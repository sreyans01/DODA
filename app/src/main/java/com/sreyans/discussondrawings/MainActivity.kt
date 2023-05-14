package com.sreyans.discussondrawings

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.sreyans.discussondrawings.bottomsheets.AddMarkerBottomSheetDialog
import com.sreyans.discussondrawings.databinding.ActivityMainBinding
import com.sreyans.discussondrawings.helper.DoubleClickListener
import com.sreyans.discussondrawings.helper.UIUtils


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

//        binding.drawingView.setOnClickListener(object : DoubleClickListener() {
//            override fun onDoubleClick(v: View?) {
//                Toast.makeText(applicationContext,"Double Click",Toast.LENGTH_SHORT).show()
//            }
//        })

        var firstTouch = false
        var time = System.currentTimeMillis()

        binding.drawingView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if(firstTouch && (System.currentTimeMillis() - time) <= 300) {
                            //do stuff here for double tap
                            Log.e("** DOUBLE TAP**"," second tap ");
                            firstTouch = false;

                            //UIUtils.showBottomSheet(this@MainActivity, AddMarkerBottomSheetDialog())

                            var image = ImageView(this@MainActivity)
                            image.layoutParams = ViewGroup.LayoutParams(20,20)
                            //image.requestLayout()
                            image.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_marker_red))
                            image.scaleType = ImageView.ScaleType.FIT_CENTER
                            image.layoutParams.width = 20
                            image.layoutParams.height = 20
                            image.requestLayout()
                            val x = event.x
                            val y = event.y
                            val params = FrameLayout.LayoutParams(80,80)
                            image.x = x
                            image.y = y
                            image.adjustViewBounds = true
                            image.setOnTouchListener(touchListener)
                            binding.parent.addView(image, params)

                        } else {
                            firstTouch = true;
                            time = System.currentTimeMillis();
                            Log.e("** SINGLE  TAP**"," First Tap time  "+time);
                            return false;
                        }




                        //Log.i("KKKKKKK", " Inside Action Down : x = ${x}, y = ${y}")

                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

    }

    private var xDelta: Float = 0.0f
    private var yDelta: Float = 0.0f
    private val touchListener = OnTouchListener { view, event ->
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


}