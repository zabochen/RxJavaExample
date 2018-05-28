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

class Just : Fragment(), AnkoLogger {

    /*
    * Just() operator takes a list of arguments and converts the items into Observable items.
    * It takes arguments between one to ten.
    */

    private lateinit var mCompositeDisposable: CompositeDisposable

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCompositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_just, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCompositeDisposable.add(
                getObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableObserver<String>() {
                            override fun onNext(t: String) {
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

    private fun getObservable(): Observable<String> {
        return Observable.just("User_1", "User_2", "User_3", "User_4", "User_5")
    }

    override fun onDetach() {
        super.onDetach()
        mCompositeDisposable.clear()
    }

}