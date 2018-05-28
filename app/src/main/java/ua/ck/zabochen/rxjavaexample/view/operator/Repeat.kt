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

class Repeat : Fragment(), AnkoLogger {

    /*
    * Repeat() creates an Observable that emits an item or series of items repeatedly.
    * You can also pass an argument to limit the number of repetitions.
    */

    private lateinit var mCompositeDisposable: CompositeDisposable

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        // Disposable init
        mCompositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repeat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCompositeDisposable.add(
                getObservable()
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
        return Observable
                .range(1, 10)
                .repeat(5)
    }

    override fun onDetach() {
        super.onDetach()
        // Disposable clear
        mCompositeDisposable.clear()
    }

}