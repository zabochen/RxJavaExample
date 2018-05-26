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

class MergeFragment : Fragment(), AnkoLogger {

    /*
    * Merge also merges multiple Observables into a single Observable but it won’t maintain the sequential execution.
    */

    private lateinit var mCompositeDisposable: CompositeDisposable

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCompositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_merge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mCompositeDisposable.add(Observable.merge(getMaleObservable(), getFemaleObservable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<User>() {
                    override fun onNext(t: User) {
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

    override fun onDetach() {
        super.onDetach()
        mCompositeDisposable.clear()
    }

    private fun getMaleObservable(): Observable<User> {
        // Male List
        val maleList: ArrayList<User> = ArrayList()
        for (i in 1..10) {
            maleList.add(User(i, "User_$i", "Male"))
        }

        return Observable
                .create(object : ObservableOnSubscribe<User> {
                    override fun subscribe(emitter: ObservableEmitter<User>) {
                        if (!emitter.isDisposed) {
                            // onNext
                            for (male in maleList) {
                                Thread.sleep(2000)
                                emitter.onNext(male)
                            }
                            // onComplete
                            emitter.onComplete()
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
    }

    private fun getFemaleObservable(): Observable<User> {
        // Female List
        val femaleList: ArrayList<User> = ArrayList()
        for (i in 1..10) {
            femaleList.add(User(i, "User_$i", "Female"))
        }

        return Observable
                .create(object : ObservableOnSubscribe<User> {
                    override fun subscribe(emitter: ObservableEmitter<User>) {
                        if (!emitter.isDisposed) {
                            // onNext
                            for (female in femaleList) {
                                Thread.sleep(1000)
                                emitter.onNext(female)
                            }
                            // onComplete
                            emitter.onComplete()
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
    }
}
