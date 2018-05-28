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
import io.reactivex.functions.Predicate
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import ua.ck.zabochen.rxjavaexample.R
import ua.ck.zabochen.rxjavaexample.model.User

class Filter : Fragment(), AnkoLogger {

    /*
    * Filter() allows the Observable to emit the only values those passes a test.
    * The Filter() method takes a Predicate test and apply the test on each item that is in the list.
    */

    private lateinit var mCompositeDisposable: CompositeDisposable

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCompositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCompositeDisposable.add(
                getObservable()
                        .filter(object : Predicate<User> {
                            override fun test(t: User): Boolean {
                                if (t.gender == "Female") {
                                    return true
                                }
                                return false
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableObserver<User>() {
                            override fun onNext(t: User) {
                                Thread.sleep(1000)
                                info { "onNext => ID: ${t.id}, NAME: ${t.name}, GENDER: ${t.gender}" }
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

    private fun getObservable(): Observable<User> {
        // User List
        val userList = ArrayList<User>()

        // Add Male
        for (i in 1..10) {
            userList.add(User(i, "User_$i", "Male"))
        }

        // Add Female
        for (i in 11..30) {
            userList.add(User(i, "User_$i", "Female"))
        }

        return Observable.create(object : ObservableOnSubscribe<User> {
            override fun subscribe(emitter: ObservableEmitter<User>) {
                if (!emitter.isDisposed) {

                    // Shuffle
                    userList.shuffle()

                    // onNext
                    for (user in userList) {
                        emitter.onNext(user)
                    }

                    // onComplete
                    emitter.onComplete()
                }
            }
        })
    }

    override fun onDetach() {
        super.onDetach()
        mCompositeDisposable.clear()
    }

}