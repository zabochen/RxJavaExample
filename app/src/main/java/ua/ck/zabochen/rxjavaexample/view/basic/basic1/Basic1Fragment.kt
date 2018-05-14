package ua.ck.zabochen.rxjavaexample.view.basic.basic1

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ua.ck.zabochen.rxjavaexample.R

class Basic1Fragment : Fragment() {

    private val TAG: String = Basic1Fragment::class.java.simpleName

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_basic1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observable
        val observable: Observable<String> = Observable.just("Basic_1", "Basic_2", "Basic_3")

        // Observer
        val observer: Observer<String> = object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                Log.i(TAG, "fun onSubscribe")
            }

            override fun onNext(t: String) {
                Log.i(TAG, "fun onNext => $t")
                Thread.sleep(3000)
            }

            override fun onComplete() {
                Log.i(TAG, "fun onComplete")
            }

            override fun onError(e: Throwable) {
                Log.i(TAG, "fun onError => ${e.stackTrace}")
            }
        }

        // Subscription
        observable
                // Tell the Observable to run the task on a background thread
                .subscribeOn(Schedulers.io())
                // Tells the Observer to receive the data on android UI thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)

    }

}