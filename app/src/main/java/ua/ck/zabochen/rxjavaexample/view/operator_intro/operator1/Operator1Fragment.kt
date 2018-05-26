package ua.ck.zabochen.rxjavaexample.view.operator.operator1

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import ua.ck.zabochen.rxjavaexample.R

class Operator1Fragment : Fragment(), AnkoLogger {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_operator1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intArray: IntArray = intArrayOf(1, 10, 24, 2, 23, 52, 2, 5, 6)

        Observable.fromArray(intArray)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<IntArray>() {
                    override fun onNext(t: IntArray) {
                        for (i in t) {
                            info { "onNext: $i" }
                        }
                    }

                    override fun onComplete() {
                        info { "onComplete" }
                    }

                    override fun onError(e: Throwable) {
                    }
                })

    }

}