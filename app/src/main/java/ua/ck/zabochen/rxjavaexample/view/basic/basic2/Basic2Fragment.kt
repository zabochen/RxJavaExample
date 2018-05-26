package ua.ck.zabochen.rxjavaexample.view.basic.basic2

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import ua.ck.zabochen.rxjavaexample.R

class Basic2Fragment : Fragment(), AnkoLogger {

    private lateinit var disposable: Disposable

    private lateinit var mTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_basic2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Views
        mTextView = view.findViewById(R.id.fragmentBasic2_textView)

        // Observer
        val observer = getObserver()

        // Observable
        val observable = getObservable()

        // Subscription
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter {
                    return@filter it.endsWith("1")
                }
                .subscribe(observer)
    }

    override fun onDetach() {
        super.onDetach()

        // Unsubscribe
        if (!disposable.isDisposed) {
            disposable.dispose()
            info { "fun onDetach => Unsubscribe" }
        }
    }

    private fun getObservable(): Observable<String> {
        return Observable
                .just("Basic2_1", "Basic2_2", "Basic2_1", "Basic2_1", "Basic2_3", "Basic2_5", "Basic2_Last")
    }

    private fun getObserver(): Observer<String> {
        return object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                info { "fun onSubscribe" }
                disposable = d
            }

            override fun onNext(t: String) {
                info { "fun onNext => $t" }
                mTextView.text = t
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