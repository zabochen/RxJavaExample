package ua.ck.zabochen.rxjavaexample.view.observable

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Flowable
import io.reactivex.FlowableSubscriber
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.reactivestreams.Subscription
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
        val observer: FlowableSubscriber<Int> = getObserver()

        // Observable
        val observable: Flowable<Int> = getObservable()

        // Subscription
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }

    override fun onDetach() {
        super.onDetach()

        // Unsubscribe
        mDisposable.dispose()
    }

    private fun getObservable(): Flowable<Int> {
        return Flowable.range(1, 100)
    }

    private fun getObserver(): FlowableSubscriber<Int> {
        return object : FlowableSubscriber<Int> {
            override fun onSubscribe(s: Subscription) {
                info { "onSubscribe" }
            }

            override fun onNext(t: Int?) {
                info { "onNext => $t" }
            }

            override fun onComplete() {
                info { "onComplete" }
            }

            override fun onError(t: Throwable?) {
                info { "onError => ${t.toString()}" }
            }
        }
    }
}