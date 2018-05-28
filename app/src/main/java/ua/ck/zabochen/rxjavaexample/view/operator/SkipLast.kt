package ua.ck.zabochen.rxjavaexample.view.operator

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import ua.ck.zabochen.rxjavaexample.R

class SkipLast : Fragment(), AnkoLogger {

    /*
    * SkipLast() skips the last N emissions from an Observable.
    * Let’s say you have an Observable that emits integers from 1-10,
    * SkipLast(4) skips the emission of 7-10 and emits only 1, 2, 3, 4, 5, 6
    */

    private lateinit var mCompositeDisposable: CompositeDisposable

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCompositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_skip_last, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCompositeDisposable.add(
                getObservable()
                        .skipLast(5)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableObserver<Int>() {
                            override fun onNext(t: Int) {
                                Thread.sleep(1000)
                                info { "onNext => $t" }
                            }

                            override fun onComplete() {
                                info { "onComplete" }
                            }

                            override fun onError(e: Throwable) {
                                info { "onError => ${e.printStackTrace()}" }
                            }
                        })
        )
    }

    private fun getObservable(): Observable<Int> {
        return Observable.range(1, 10)
    }

    override fun onDetach() {
        super.onDetach()
        mCompositeDisposable.clear()
    }

}