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

class Skip : Fragment(), AnkoLogger {

    /*
    * Skip() operator skips the emission of first N items emitted by an Observable.
    * Let’s say you have an Observable that emits integers from 1-10 and if skip(4) is operator is used,
    * it skips 1-4 and emits the numbers 5, 6, 7, 8, 9, 10.
    */

    private lateinit var mCompositeDisposable: CompositeDisposable

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCompositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_skip, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCompositeDisposable.add(
                getObservable()
                        .skip(5)
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