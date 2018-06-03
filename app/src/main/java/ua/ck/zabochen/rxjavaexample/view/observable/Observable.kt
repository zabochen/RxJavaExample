package ua.ck.zabochen.rxjavaexample.view.observable

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
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import ua.ck.zabochen.rxjavaexample.R
import ua.ck.zabochen.rxjavaexample.model.Note

class Observable : Fragment(), AnkoLogger {

    /*
    Observable   Observer   Emissions
    Observable   Observer   Multiple or None
    */

    private lateinit var disposable: Disposable
    private var noteList: ArrayList<Note> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_observable, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observer
        val observer: Observer<Note> = getObserver()

        // Observable
        val observable: Observable<Note> = getObservable()
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }

    override fun onDetach() {
        super.onDetach()
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    private fun getObservable(): Observable<Note> {

        // Fill postList
        for (i in 1..10) {
            noteList.add(Note(i, "Note_$i"))
        }

        return Observable.create(
                object : ObservableOnSubscribe<Note> {
                    override fun subscribe(emitter: ObservableEmitter<Note>) {
                        // OnNext
                        if (!emitter.isDisposed) {
                            for (i in noteList) {
                                emitter.onNext(i)
                            }
                        }

                        // OnComplete
                        if (!emitter.isDisposed) {
                            emitter.onComplete()
                        }
                    }
                }
        )
    }

    private fun getObserver(): Observer<Note> {
        return object : Observer<Note> {
            override fun onSubscribe(d: Disposable) {
                disposable = d
                info { "onSubscribe" }
            }

            override fun onNext(t: Note) {
                info { "onNext => ID: ${t.id}, TITLE: ${t.title}" }
            }

            override fun onComplete() {
                info { "onComplete" }
            }

            override fun onError(e: Throwable) {
                info { "onError => ${e.stackTrace}" }
            }
        }
    }

}