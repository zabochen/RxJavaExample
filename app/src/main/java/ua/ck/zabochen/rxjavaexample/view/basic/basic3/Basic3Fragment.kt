package ua.ck.zabochen.rxjavaexample.view.basic.basic3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import ua.ck.zabochen.rxjavaexample.R

class Basic3Fragment : androidx.fragment.app.Fragment(), AnkoLogger {

    // Disposable container
    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_basic3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // CompositeDisposable init
        compositeDisposable = CompositeDisposable()

        // Observable
        val observable: Observable<String> = getObservable()

        // Observers
        val adminObserver: DisposableObserver<String> = getAdminObserver()
        val userObserver: DisposableObserver<String> = getUserObserver()

        // Admin Subscription
        compositeDisposable.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter {
                            return@filter it.startsWith("A")
                        }
                        .map(object : Function<String, String> {
                            override fun apply(t: String): String {
                                return t.toUpperCase()
                            }
                        })
                        .subscribeWith(adminObserver)
        )

        // User Subscription
        compositeDisposable.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter {
                            return@filter it.startsWith("U")
                        }
                        .map(object : Function<String, String> {
                            override fun apply(t: String): String {
                                return t.toLowerCase()
                            }
                        })
                        .subscribeWith(userObserver)
        )

    }

    override fun onDetach() {
        super.onDetach()

        // Don't send data when fragment detach
        compositeDisposable.clear()
    }

    private fun getObservable(): Observable<String> {
        return Observable.fromArray("Admin_1", "User_1", "User_3", "Admin_4", "User_5")
    }

    private fun getAdminObserver(): DisposableObserver<String> {
        return object : DisposableObserver<String>() {
            override fun onNext(t: String) {
                info { "Admin: $t" }
            }

            override fun onComplete() {
                info { "getAdminObserver() => onComplete" }
            }

            override fun onError(e: Throwable) {
            }
        }
    }

    private fun getUserObserver(): DisposableObserver<String> {
        return object : DisposableObserver<String>() {
            override fun onNext(t: String) {
                info { "User: $t" }
            }

            override fun onComplete() {
                info { "getUserObserver() => onComplete" }
            }

            override fun onError(e: Throwable) {
            }
        }
    }

}