package com.orbits.masterdisplay.helper

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.Scroller
import androidx.appcompat.widget.AppCompatTextView
import com.orbits.masterdisplay.helper.PrefUtils.getAppConfig
import java.util.Locale

class ScrollTextView(context: Context?, attrs: AttributeSet?, defStyle: Int) : AppCompatTextView(
    context!!, attrs, defStyle) {

    // Scrolling feature
    private var mSlr: Scroller? = null

    // Milliseconds for a round of scrolling
    var rndDuration: Int = 20000

    // The X offset when paused
    private var mXPaused = 0

    // Whether it's being paused
    var isPaused: Boolean = true
        private set

    // Constructor
    constructor(context: Context?) : this(context, null) {
        initTextView()
    }

    // Constructor
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, android.R.attr.textViewStyle) {
        initTextView()
    }

    init {
        initTextView()
    }

    // Initialize the TextView
    private fun initTextView() {
        setSingleLine()
        ellipsize = null
        visibility = INVISIBLE
    }

    /**
     * Begin to scroll the text from the original position
     */
    fun startScroll() {
        mXPaused = if (context?.getAppConfig()?.isScrollArabic == true) {
            width // Start from the right side for Arabic text
        } else {
            -1 * width // Start from the left side for other languages
        }
        isPaused = true
        resumeScroll()
    }

    /**
     * Resume the scroll from the pausing point
     */
    fun resumeScroll() {
        if (!isPaused) return

        // Enable horizontal scrolling
        setHorizontallyScrolling(true)

        // Use LinearInterpolator for steady scrolling
        mSlr = Scroller(this.context, LinearInterpolator())
        setScroller(mSlr)

        val scrollingLen = calculateScrollingLen()
        val distance = if (context?.getAppConfig()?.isScrollArabic == true) {
            scrollingLen + mXPaused // Right to left scrolling for Arabic
        } else {
            scrollingLen - (width + mXPaused) // Left to right scrolling for other languages
        }

        // Adjust duration according to the distance
        val duration = (rndDuration * distance * 1.0 / scrollingLen).toInt()
        visibility = VISIBLE
        mSlr!!.startScroll(mXPaused, 0, if (context?.getAppConfig()?.isScrollArabic == true) -distance else distance, 0, duration)
        isPaused = false
    }

    /**
     * Calculate the scrolling length of the text in pixels
     *
     * @return the scrolling length in pixels
     */
    private fun calculateScrollingLen(): Int {
        val tp = paint
        val rect = Rect()
        val strTxt = text.toString()
        tp.getTextBounds(strTxt, 0, strTxt.length, rect)
        return rect.width() + width
    }

    /**
     * Pause scrolling the text
     */
    fun pauseScroll() {
        if (mSlr == null || isPaused) return

        isPaused = true
        mXPaused = mSlr!!.currX
        mSlr!!.abortAnimation()
    }

    /**
     * Override the computeScroll to restart scrolling when finished to ensure the text scrolls forever
     */
    override fun computeScroll() {
        super.computeScroll()
        if (mSlr != null && mSlr!!.isFinished && !isPaused) {
            startScroll()
        }
    }

}
