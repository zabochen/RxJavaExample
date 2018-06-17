package ua.ck.zabochen.appnetwork.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import org.jetbrains.anko.AnkoLogger
import ua.ck.zabochen.appnetwork.R

class MainActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUi()
    }

    private fun setUi() {
        // Layout & ButterKnife
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
    }

}
