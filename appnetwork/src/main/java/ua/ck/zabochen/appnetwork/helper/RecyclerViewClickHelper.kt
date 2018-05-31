package ua.ck.zabochen.appnetwork.helper

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

class RecyclerViewClickHelper(
        context: Context,
        recyclerView: RecyclerView,
        clickListener: ClickListener
) : RecyclerView.OnItemTouchListener {

    private var mContext: Context = context
    private val mRecyclerView: RecyclerView = recyclerView
    private var mClickListener: ClickListener = clickListener
    private var mGestureDetector: GestureDetector

    init {
        mGestureDetector = GestureDetector(mContext, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent?) {
                val childView: View = mRecyclerView.findChildViewUnder(e!!.x, e.y)
                mClickListener.onLongClick(childView, mRecyclerView.getChildAdapterPosition(childView))
            }
        })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
        val childView: View? = rv?.findChildViewUnder(e!!.x, e.y)
        if (childView != null && mGestureDetector.onTouchEvent(e)) {
            mClickListener.onClick(childView, rv.getChildAdapterPosition(childView))
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }

    interface ClickListener {
        fun onClick(view: View, position: Int)
        fun onLongClick(view: View, position: Int)
    }
}