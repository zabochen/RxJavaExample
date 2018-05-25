package ua.ck.zabochen.rxjavaexample.view.observable

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.CompletableObserver
import io.reactivex.CompletableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import ua.ck.zabochen.rxjavaexample.R

class CompletableObservableFragment : androidx.fragment.app.Fragment(), AnkoLogger {

    /*
    This observable can be used when you want to perform some task and not expect any value.
    A use case - making a PUT request to server to update something where you are not expecting
    any response but the success status
    */

    /*
    Observable    Observer	            Emissions
    Completable   CompletableObserver   None
    */

    lateinit var mDisposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_observable_completable, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observer
        val observer: CompletableObserver = getObserver()

        // Observable
        val observable: Completable = getObservable()

        // Subscription
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)
    }

    override fun onDetach() {
        super.onDetach()

        // Unsubscribe
        mDisposable.dispose()
    }

    private fun getObservable(): Completable {
        return Completable.create(
                object : CompletableOnSubscribe {
                    override fun subscribe(emitter: CompletableEmitter) {
                        if (!mDisposable.isDisposed) {
                            emitter.onComplete()
                        }
                    }
                }
        )
    }

    private fun getObserver(): CompletableObserver {
        return object : CompletableObserver {
            override fun onSubscribe(d: Disposable) {
                mDisposable = d
                info { "Subscribe" }
            }

            override fun onComplete() {
                info { "Complete" }
            }

            override fun onError(e: Throwable) {
                info { "Error => ${e.printStackTrace()}" }
            }
        }
    }

}