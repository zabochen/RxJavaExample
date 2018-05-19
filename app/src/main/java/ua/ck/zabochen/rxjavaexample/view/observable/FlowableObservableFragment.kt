package ua.ck.zabochen.rxjavaexample.view.observable

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
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
        val observer: Observer<Int> = getObserver()

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
        return Flowable.create(
                object : FlowableOnSubscribe<Int> {
                    override fun subscribe(emitter: FlowableEmitter<Int>) {
                    }
                },
                BackpressureStrategy.BUFFER
        )
    }

    private fun getObserver(): Observer<Int> {
        return object : Observer<Int> {
            override fun onSubscribe(d: Disposable) {
                mDisposable = d
                info { "onSubscribe" }
            }

            override fun onComplete() {
                info { "onComplete" }
            }

            override fun onNext(t: Int) {
                info { "onNext: $t" }
            }

            override fun onError(e: Throwable) {
                info { "onError => ${e.printStackTrace()}" }
            }
        }
    }
}