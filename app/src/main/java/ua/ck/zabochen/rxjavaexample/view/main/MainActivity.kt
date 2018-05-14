package ua.ck.zabochen.rxjavaexample.view.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import ua.ck.zabochen.rxjavaexample.R
import ua.ck.zabochen.rxjavaexample.view.basic.basic1.Basic1Fragment

class MainActivity : AppCompatActivity() {

    private val fragmentHolder: FrameLayout by lazy { findViewById<FrameLayout>(R.id.activityMain_fragmentHolder) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUi()
        setFragment()
    }

    private fun setUi() {
        // Layout
        setContentView(R.layout.activity_main)
    }

    private fun setFragment() {
        supportFragmentManager
                .beginTransaction()
                .replace(fragmentHolder.id, Basic1Fragment())
                .commit()
    }

}