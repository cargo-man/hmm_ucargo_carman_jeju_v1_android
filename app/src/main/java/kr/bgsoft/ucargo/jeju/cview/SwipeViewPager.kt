package kr.bgsoft.ucargo.jeju.cview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class SwipeViewPager : ViewPager {

    var swipe   = true
    var scroll  = true

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if(swipe) {
            return super.onInterceptTouchEvent(ev)
        } else {
            if (ev?.action == MotionEvent.ACTION_MOVE) {
                // ignore move action
            } else {
                if (super.onInterceptTouchEvent(ev)) {
                    super.onTouchEvent(ev)
                }
            }
            return false
        }
    }

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item, scroll)
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(item, scroll)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if(swipe) {
            return super.onTouchEvent(ev)
        } else {
            return ev?.action != MotionEvent.ACTION_MOVE && super.onTouchEvent(ev)
        }
    }

    fun setSwipeEnabled(on : Boolean) {
        swipe = on
    }
}