package ua.ck.zabochen.appnetwork.listener

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

class RecyclerViewTouchListener(
        context: Context,
        recyclerView: RecyclerView,
        clickListener: ClickListener
) : RecyclerView.OnItemTouchListener {

    private var mContext: Context = context
    private val mRecyclerView: RecyclerView = recyclerView
    private var mClickListener: ClickListener = clickListener

    private lateinit var mGestureDetector: GestureDetector

    init {
        mGestureDetector = GestureDetector(mContext, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent?) {
                super.onLongPress(e)
                val view: View = mRecyclerView.findChildViewUnder(e!!.x, e!!.y)

            }
        })
    }

    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {

    }

    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
        return true
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }

    interface ClickListener {
        fun onClick(view: View, position: Int)
        fun onLongClick(view: View, position: Int)
    }

}