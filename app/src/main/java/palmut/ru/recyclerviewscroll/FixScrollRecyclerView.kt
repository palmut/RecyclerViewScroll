package palmut.ru.recyclerviewscroll

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration

class FixScrollRecyclerView
@JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    private var scrollPointerId = INVALID_POINTER
    private var initialTouchX = 0
    private var initialTouchY = 0
    private var touchSlop = 0

    init {
        val vc = ViewConfiguration.get(context)
        touchSlop = vc.scaledTouchSlop
    }

    override fun setScrollingTouchSlop(slopConstant: Int) {
        super.setScrollingTouchSlop(slopConstant)

        val vc = ViewConfiguration.get(context)
        when (slopConstant) {
            TOUCH_SLOP_DEFAULT -> touchSlop = vc.scaledTouchSlop
            TOUCH_SLOP_PAGING -> touchSlop = vc.scaledPagingTouchSlop
        }
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        if (e == null) {
            return false
        }

        val action = e.actionMasked
        val actionIndex = e.actionIndex

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                scrollPointerId = e.getPointerId(0)
                initialTouchX = Math.round(e.x + 0.5f)
                initialTouchY = Math.round(e.y + 0.5f)
                scrollingHorizontally = false
                stopScroll()
                return super.onInterceptTouchEvent(e)
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                scrollPointerId = e.getPointerId(actionIndex)
                initialTouchX = Math.round(e.getX(actionIndex) + 0.5f)
                initialTouchY = Math.round(e.getY(actionIndex) + 0.5f)
                scrollingHorizontally = false
                stopScroll()
                return super.onInterceptTouchEvent(e)
            }

            MotionEvent.ACTION_MOVE -> {
                val index = e.findPointerIndex(scrollPointerId)
                if (index < 0) {
                    return super.onInterceptTouchEvent(e)
                }

                val x = Math.round(e.getX(index) + 0.5f)
                val y = Math.round(e.getY(index) + 0.5f)
                if (scrollState != RecyclerView.SCROLL_STATE_DRAGGING) {
                    val dx = x - initialTouchX
                    val dy = y - initialTouchY
                    var scrollingVertically = false

                    val canScrollHorizontally = layoutManager.canScrollHorizontally()
                    val canScrollVertically = layoutManager.canScrollVertically()

                    if (canScrollHorizontally && Math.abs(dx) > touchSlop
                            && (canScrollVertically || Math.abs(dx) > Math.abs(dy))) {
                        scrollingHorizontally = true
                    }

                    if (!scrollingHorizontally) {
                        if (canScrollVertically && Math.abs(dy) > touchSlop
                                && (canScrollHorizontally || Math.abs(dy) > Math.abs(dx))) {
                            scrollingVertically = true
                        }
                    }
                    return if (scrollingHorizontally) {
                        canScrollHorizontally
                    } else {
                        scrollingVertically and super.onInterceptTouchEvent(e)
                    }
                }
                return super.onInterceptTouchEvent(e)
            }

            else -> return super.onInterceptTouchEvent(e)
        }
    }

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        // do nothing
    }

    companion object {
        private const val INVALID_POINTER = -1
        private var scrollingHorizontally: Boolean = false
    }
}