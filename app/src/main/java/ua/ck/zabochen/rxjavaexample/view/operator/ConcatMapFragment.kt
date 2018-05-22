package ua.ck.zabochen.rxjavaexample.view.operator

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

class ConcatMapFragment : Fragment(), AnkoLogger {

    /*
    * FlatMap & ConcatMap produces the same output but the sequence the data emitted changes
    * ConcatMap maintains the order of items and waits for the current Observable to complete its job before emitting the next one.
    * ConcatMap is more suitable when you want to maintain the order of execution
    */

    private lateinit var mDisposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_concat_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observable
        val userObservable: Observable<User> = getUserObservable()

        // Subscription
        userObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .concatMap(
                        object : Function<User, Observable<User>> {
                            override fun apply(t: User): Observable<User> {
                                return getAddressObservable(t)
                            }
                        })
                .subscribe(
                        object : Observer<User> {
                            override fun onSubscribe(d: Disposable) {
                                mDisposable = d
                                info { "Subscribe" }
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

        // User List
        val userList: ArrayList<User> = ArrayList()
        for (i in 1..10) {
            userList.add(User(i, "User_$i"))
        }

        // Observable
        return Observable.create(
                object : ObservableOnSubscribe<User> {
                    override fun subscribe(emitter: ObservableEmitter<User>) {
                        if (!mDisposable.isDisposed) {
                            // onNext
                            for (user in userList) {
                                emitter.onNext(user)
                            }

                            // onComplete
                            emitter.onComplete()
                        }
                    }
                }
        )
    }

    private fun getAddressObservable(user: User): Observable<User> {
        return Observable.create(
                object : ObservableOnSubscribe<User> {
                    override fun subscribe(emitter: ObservableEmitter<User>) {
                        if (!mDisposable.isDisposed) {
                            // Thread sleep
                            val threadSleepDuration: Long = (Random().nextInt(5000) + 500).toLong()
                            Thread.sleep(threadSleepDuration)

                            // onNext
                            user.name.toUpperCase()
                            user.email = "user_$threadSleepDuration@mail.com"
                            emitter.onNext(user)

                            // onComplete
                            emitter.onComplete()
                        }
                    }
                }
        )
    }

}