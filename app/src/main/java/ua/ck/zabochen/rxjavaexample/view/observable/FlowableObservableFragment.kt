package ua.ck.zabochen.rxjavaexample.view.observable

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Flowable
import io.reactivex.MaybeObserver
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import ua.ck.zabochen.rxjavaexample.R

class FlowableObservableFragment : Fragment(), AnkoLogger {

    /*
    Used when an Observable is generating huge amount of events/data.
    When the source is generating 10k+ events and subscriber can’t consume it all.
    */

    /*
    Observable   Observer   Emissions
    Flowable	 Observer   Multiple or None
    */

    lateinit var mDisposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_observable_flowable, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observer
        val observer: MaybeObserver<Int> = getObserver()

        // Observable
        val observable: Flowable<Int> = getObservable()

        // Subscription
        observable
                .reduce(object : BiFunction<Int, Int, Int> {
                    override fun apply(t1: Int, t2: Int): Int {
                        return t1 + t2
                    }
                })
                .subscribe(observer)
    }

    override fun onDetach() {
        super.onDetach()

        // Unsubscribe
        mDisposable.dispose()
    }

    private fun getObservable(): Flowable<Int> {
        return Flowable.just(1, 2, 3, 4, 5)
    }

    private fun getObserver(): MaybeObserver<Int> {
        return object : MaybeObserver<Int> {
            override fun onSubscribe(d: Disposable) {
                mDisposable = d
                info { "onSubscribe" }
            }

            override fun onSuccess(t: Int) {
                info { "onSuccess => $t" }
            }

            override fun onComplete() {
                info { "onComplete" }
            }

            override fun onError(e: Throwable) {
                info { "onError => ${e.stackTrace}" }
            }
        }
    }
}