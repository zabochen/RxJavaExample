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

class MapFragment : Fragment(), AnkoLogger {

    private lateinit var mDisposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observable
        val observable: Observable<User> = getObservable()

        // Observer
        val observer: Observer<User> = getObserver()

        // Subscription
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(object : Function<User, User> {
                    override fun apply(t: User): User {
                        val user = t
                        user.name = t.name.toUpperCase()
                        return user
                    }

                })
                .subscribe(observer)
    }

    override fun onDetach() {
        super.onDetach()

        // Unsubscribe
        mDisposable.dispose()
    }

    private fun getObservable(): Observable<User> {

        // UserList
        val userList: ArrayList<User> = ArrayList()
        for (i in 1..10) {
            userList.add(User(i, "User_$i"))
        }

        // Observer
        return Observable.create(
                object : ObservableOnSubscribe<User> {
                    override fun subscribe(emitter: ObservableEmitter<User>) {
                        // onNext
                        if (!mDisposable.isDisposed) {
                            for (i in userList) {
                                emitter.onNext(i)
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

    private fun getObserver(): Observer<User> {
        return object : Observer<User> {
            override fun onSubscribe(d: Disposable) {
                mDisposable = d
                info { "Subscribe" }
            }

            override fun onNext(t: User) {
                info { "Next => ID: ${t.id}, NAME: ${t.name}" }
            }

            override fun onComplete() {
                info { "Complete" }
            }

            override fun onError(e: Throwable) {
                info { "Error => ${e.printStackTrace()}" }
            }
        }
    }

}
