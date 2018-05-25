package ua.ck.zabochen.rxjavaexample.view.operator

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

class BufferFragment : androidx.fragment.app.Fragment(), AnkoLogger {

    /*
    * Buffer gathers items emitted by an Observable into batches and emit the batch instead of emitting one item at a time.
    * Observable that emits integers from 1-9. When buffer(3) is used, it emits 3 integers at a time.
    */

    private lateinit var mDisposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_buffer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observable
        val observable: Observable<Int> = getObservable()

        // Subscription
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .buffer(3)
                .subscribe(
                        object : Observer<List<Int>> {
                            override fun onSubscribe(d: Disposable) {
                                mDisposable = d
                                info { "Subscribe" }
                            }

                            override fun onNext(t: List<Int>) {
                                info { "List:" }
                                for (i in t) {
                                    info { "Next => $i" }
                                }
                            }

                            override fun onComplete() {
                                info { "Complete" }
                            }

                            override fun onError(e: Throwable) {
                                info { "Error => ${e.printStackTrace()}" }
                            }
                        }
                )
    }

    override fun onDetach() {
        super.onDetach()

        // Unsubscribe
        mDisposable.dispose()
        info { "Unsubscribe" }
    }

    private fun getObservable(): Observable<Int> {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    }

}