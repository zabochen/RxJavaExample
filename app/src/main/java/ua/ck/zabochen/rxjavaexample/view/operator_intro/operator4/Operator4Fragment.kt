package ua.ck.zabochen.rxjavaexample.view.operator.operator4

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

class Operator4Fragment : Fragment(), AnkoLogger {

    private lateinit var subscriberHolder: CompositeDisposable

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        subscriberHolder = CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_operator4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userArray: Array<String> = arrayOf("User_1", "User_2", "User_3")

        // Operator just() - Makes only 1 emission
        val justObserver: DisposableObserver<Array<String>> = Observable.just(userArray)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Array<String>>() {
                    override fun onNext(t: Array<String>) {
                        for (i in t) {
                            info { "justObserver - onNext => $i" }
                        }
                    }

                    override fun onComplete() {
                        info { "justObserver - onComplete" }
                    }

                    override fun onError(e: Throwable) {
                    }
                })

        // Operator fromArray() - makes N emissions
        val fromArrayObserver: DisposableObserver<Array<String>> = Observable.fromArray(userArray)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        object : DisposableObserver<Array<String>>() {
                            override fun onNext(t: Array<String>) {
                                for (i in t) {
                                    info { "fromArrayObserver - onNext => $i" }
                                }
                            }

                            override fun onComplete() {
                                info { "fromArrayObserver - onComplete" }
                            }

                            override fun onError(e: Throwable) {
                            }

                        }
                )

        subscriberHolder.addAll(justObserver, fromArrayObserver)
    }

    override fun onDetach() {
        super.onDetach()

        // Unsubscribe
        subscriberHolder.clear()
    }

}