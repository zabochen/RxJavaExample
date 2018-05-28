package ua.ck.zabochen.rxjavaexample.view.operator.map

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import ua.ck.zabochen.rxjavaexample.R
import ua.ck.zabochen.rxjavaexample.model.User
import java.util.*

class FlatMap : Fragment(), AnkoLogger {

    /*
    *  We have a network call to fetch Users with name and gender.
    *  Then we have another network that gives you address of each user.
    *  Now the requirement is to create an Observable that emits Users with name, gender and address properties.
    *  To achieve this, we need to get the users first, then make separate network call for each user to fetch his address.
    *  This can be done easily using FlatMap operator.
    *  Choose FlatMap when the order is not important.
    */

    private lateinit var mDisposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_flat_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observable
        val userObservable: Observable<User> = getUserObservable()

        // Observer

        // Subscriber
        userObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(object : Function<User, Observable<User>> {
                    override fun apply(t: User): Observable<User> {
                        return getEmailObserver(t)
                    }
                })
                .subscribe(
                        object : Observer<User> {
                            override fun onSubscribe(d: Disposable) {
                                info { "Subscribe" }
                                mDisposable = d
                            }

                            override fun onNext(t: User) {
                                info { "Next => ID: ${t.id}, NAME: ${t.name}, EMAIL: ${t.email}" }
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
    }


    private fun getUserObservable(): Observable<User> {

        // User1 List => 10 Users
        val userList: ArrayList<User> = ArrayList()
        for (i in 1..10) {
            userList.add(User(i, "User_$i"))
        }

        return Observable.create(
                object : ObservableOnSubscribe<User> {
                    override fun subscribe(emitter: ObservableEmitter<User>) {
                        // onNext
                        if (!mDisposable.isDisposed) {
                            for (user in userList) {
                                emitter.onNext(user)
                            }
                        }

                        // onComplete
                        if (!mDisposable.isDisposed) {
                            emitter.onComplete()
                        }
                    }
                }
        )
    }

    private fun getEmailObserver(user: User): Observable<User> {

        // Email list
        val emailList: Array<String> = Array(10, { i -> "user_${i + 1}@mail.com" })

        return Observable.create(
                object : ObservableOnSubscribe<User> {
                    override fun subscribe(emitter: ObservableEmitter<User>) {
                        if (!mDisposable.isDisposed) {
                            user.email = emailList[Random().nextInt(10)]
                            Thread.sleep((Random().nextInt(5000) + 500).toLong())
                            emitter.onNext(user)
                            emitter.onComplete()
                        }
                    }
                }
        )
    }

}