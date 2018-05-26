package ua.ck.zabochen.rxjavaexample.view.basic.basic4

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
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import ua.ck.zabochen.rxjavaexample.R
import ua.ck.zabochen.rxjavaexample.view.basic.basic4.model.User

class Basic4Fragment : Fragment(), AnkoLogger {

    // Disposable container
    private lateinit var compositeDisposable: CompositeDisposable

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        compositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_basic4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observer
        val userObserver: DisposableObserver<User> = getUserObserver()

        // Observable
        val userObservable: Observable<User> = getUserObservable()

        // Subscription
        compositeDisposable.add(
                userObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(object : Function<User, User> {
                            override fun apply(t: User): User {
                                val user = t
                                user.name = user.name.toUpperCase()
                                return user
                            }
                        })
                        .subscribeWith(userObserver)
        )

    }

    private fun getUserObservable(): Observable<User> {

        // Fill User1 List
        val userList: ArrayList<User> = ArrayList()
        for (i in 1..10) {
            userList.add(User(i, "User_$i"))
        }

        // Create Observable
        return Observable.create(
                object : ObservableOnSubscribe<User> {
                    override fun subscribe(emitter: ObservableEmitter<User>) {
                        // onNext
                        for (i in 0 until userList.size) {
                            if (!emitter.isDisposed) {
                                emitter.onNext(userList[i])
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

    private fun getUserObserver(): DisposableObserver<User> {
        return object : DisposableObserver<User>() {
            override fun onNext(t: User) {
                info { "fun onNext => ID: ${t.id}, NAME: ${t.name}" }
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