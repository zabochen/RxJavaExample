package ua.ck.zabochen.rxjavaexample.view.basic.basic1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import ua.ck.zabochen.rxjavaexample.R

class Basic1Fragment : androidx.fragment.app.Fragment(), AnkoLogger {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_basic1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observable
        val observable: Observable<String> = getObservable()

        // Observer
        val observer: Observer<String> = getObserver()

        // Subscription
        observable
                // Tell the Observable to run the task on a background thread
                .subscribeOn(Schedulers.io())
                // Tells the Observer to receive the data on android UI thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)

    }

    private fun getObservable(): Observable<String> {
        return Observable.just("Basic_1", "Basic_2", "Basic_3")
    }

    private fun getObserver(): Observer<String> {
        return object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                info { "fun onSubscribe" }
            }

            override fun onNext(t: String) {
                info { "fun onNext => $t" }
            }

            override fun onComplete() {
                info { "fun onComplete" }
            }

            override fun onError(e: Throwable) {
                info { "fun onError => ${e.stackTrace}" }
            }
        }
    }

}