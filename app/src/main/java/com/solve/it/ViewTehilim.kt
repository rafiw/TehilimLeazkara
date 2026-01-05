package com.solve.it

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.edit

class ViewTehilim : AppCompatActivity() {
    private var textSize: Float = Constants.DEFAULT_FONT_SIZE_FLOAT
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    private var lastScaleFactor = 1f

    private val scrollView: ScrollView by lazy {
        findViewById(R.id.scrollViewThilim)
    }

    private val textView: TextView by lazy {
        findViewById(R.id.thilimtext)
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            // Smooth scaling factor
            val scaleFactor = (detector.scaleFactor - 1) * 0.7f + 1

            // Calculate new text size
            var newSize = textSize * scaleFactor

            // Constrain text size between minimum and maximum values
            newSize = newSize.coerceIn(12f, 40f)

            if (newSize != textSize) {
                textSize = newSize
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
                saveSettings()
            }

            lastScaleFactor = scaleFactor
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_thilim_new)

        // Make view clickable to support performClick
        scrollView.isClickable = true

        setupGestureDetector()
        setupTextView()
        setupTouchHandling()
    }

    private fun setupGestureDetector() {
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchHandling() {
        var isScaling = false

        scrollView.setOnTouchListener { view, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    isScaling = false

                }
                MotionEvent.ACTION_MOVE -> {
                    // Handle both scroll and scale
                    scaleGestureDetector.onTouchEvent(event)
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                    isScaling = true
                }
                MotionEvent.ACTION_POINTER_UP -> {
                    isScaling = false
                    lastScaleFactor = 1f
                }
                MotionEvent.ACTION_UP -> {
                    if (!isScaling) {
                        view.performClick()
                    }
                    isScaling = false
                }
            }
            false // This allows scrolling to work
        }

        scrollView.isClickable = true
    }

    private fun setupTextView() {
        intent?.extras?.let { bundle ->
            // Font handling (your existing code)
            bundle.getBoolean(Constants.TEXT_BLACK).let {
                val fg = ContextCompat.getColor(this, if (it) R.color.black else R.color.white)
                val bg = ContextCompat.getColor(this, if (it) R.color.white else R.color.black)

                textView.setTextColor(fg)
                textView.setBackgroundColor(bg)
                scrollView.setBackgroundColor(bg)
            }
            bundle.getString(Constants.FONT_TYPE)?.let { fontValue ->
                try {
                    val typeface = when (fontValue) {
                        "0" -> Typeface.DEFAULT
                        else -> {
                            Typeface.createFromAsset(assets, "font/$fontValue")
                        }
                    }
                    textView.typeface = typeface
                } catch (e: Exception) {
                    Log.e("ViewTehilim", "Error setting font: $fontValue", e)
                    Toast.makeText(this, "Error loading font", Toast.LENGTH_SHORT).show()
                    textView.typeface = Typeface.DEFAULT
                }
            }

            // Set text content
            // add 2 blank lines at the end due to buttons
            val text = SpannableStringBuilder(bundle.getCharSequence(Constants.TEXT_KEY)).apply {
                append("\n\n")
            }
            textView.text = text
        }

        // Load saved text size
        loadSettings()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putInt("scroll_y", scrollView.scrollY)
            putInt("scroll_x", scrollView.scrollX)
            putFloat("text_size", textSize)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.run {
            val posY = getInt("scroll_y", 0)
            val posX = getInt("scroll_x", 0)
            textSize = getFloat("text_size", Constants.DEFAULT_FONT_SIZE_FLOAT)
            scrollView.post {
                scrollView.scrollTo(posX, posY)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
            }
        }
    }

    private fun saveSettings() {
        getPreferences(MODE_PRIVATE).edit {
            putFloat(Constants.ACT_SAVE_FONT_KEY, textSize)
        }
    }

    private fun loadSettings() {
        textSize = getPreferences(MODE_PRIVATE)
            .getFloat(Constants.ACT_SAVE_FONT_KEY, Constants.DEFAULT_FONT_SIZE_FLOAT)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
    }
}