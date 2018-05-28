package ua.ck.zabochen.rxjavaexample.view.operator

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import ua.ck.zabochen.rxjavaexample.R
import ua.ck.zabochen.rxjavaexample.model.User

class Concat : Fragment(), AnkoLogger {

    /*
    * Concat operator combines output of two or more Observables into a single Observable.
    * Concat operator always maintains the sequential execution without interleaving the emissions.
    * So the first Observables completes its emission before the second starts and so forth if there are more observables.
    */

    private lateinit var mCompositeDisposable: CompositeDisposable

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCompositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_concat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observer
        val disposableObserver: DisposableObserver<User> =
                object : DisposableObserver<User>() {
                    override fun onNext(t: User) {
                        info { "onNext => ID: ${t.id}, NAME: ${t.name}, GENDER: ${t.gender}" }
                    }

                    override fun onComplete() {
                        info { "onComplete" }
                    }

                    override fun onError(e: Throwable) {
                        info { "onError => ${e.printStackTrace()}" }
                    }
                }
        // Add to holder
        mCompositeDisposable.add(disposableObserver)

        // Observable & Subscription
        Observable.concat(getMaleObservable(), getFemaleObservable())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver)
    }

    override fun onDetach() {
        super.onDetach()
        mCompositeDisposable.clear()
    }

    private fun getMaleObservable(): Observable<User> {
        val maleList: ArrayList<User> = ArrayList()
        for (i in 1..10) {
            maleList.add(User(i, "User_$i", "Male"))
        }

        return Observable.create(
                object : ObservableOnSubscribe<User> {
                    override fun subscribe(emitter: ObservableEmitter<User>) {
                        // onNext
                        for (user in maleList) {
                            if (!emitter.isDisposed) {
                                emitter.onNext(user)
                                Thread.sleep(1000)
                            }
                        }

                        // onComplete
                        if (!emitter.isDisposed) {
                            emitter.onComplete()
                        }
                    }
                }
        )
    }

    private fun getFemaleObservable(): Observable<User> {
        val femaleList: ArrayList<User> = ArrayList()
        for (i in 1..10) {
            femaleList.add(User(i, "User_$i", "Female"))
        }

        return Observable.create(
                object : ObservableOnSubscribe<User> {
                    override fun subscribe(emitter: ObservableEmitter<User>) {
                        // onNext
                        for (user in femaleList) {
                            if (!emitter.isDisposed) {
                                emitter.onNext(user)
                            }
                        }

                        // onComplete
                        if (!emitter.isDisposed) {
                            emitter.onComplete()
                        }
                    }
                }
        )
    }

}