package ua.ck.zabochen.rxjavaexample.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.FrameLayout
import ua.ck.zabochen.rxjavaexample.R
import ua.ck.zabochen.rxjavaexample.view.operator.MergeFragment

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
                .replace(fragmentHolder.id, MergeFragment())
                .commit()
    }

}