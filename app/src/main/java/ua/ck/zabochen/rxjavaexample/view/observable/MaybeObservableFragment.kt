package ua.ck.zabochen.rxjavaexample.view.observable

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Maybe
import io.reactivex.MaybeEmitter
import io.reactivex.MaybeObserver
import io.reactivex.MaybeOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import ua.ck.zabochen.rxjavaexample.R
import ua.ck.zabochen.rxjavaexample.model.Note

class MaybeObservableFragment : androidx.fragment.app.Fragment(), AnkoLogger {

    /*
    There is possibility of not finding the note by ID in the db
    In this situation, MayBe can be used
    */

    /*
    Observable   Observer	     Emissions
    Maybe	     MaybeObserver   One or None
    */

    // Subscribe holder
    lateinit var disposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_observable_maybe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observer
        val observer: MaybeObserver<Note> = getObserver()

        // Observable
        val observable: Maybe<Note> = getObservable()

        // Subscription
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)
    }

    override fun onDetach() {
        super.onDetach()

        // Unsubscribe
        disposable.dispose()
    }

    private fun getObservable(): Maybe<Note> {
        return Maybe.create(
                object : MaybeOnSubscribe<Note> {
                    override fun subscribe(emitter: MaybeEmitter<Note>) {
                        if (!disposable.isDisposed) {
                            emitter.onSuccess(Note(1, "Maybe note!"))
                        }
                        if (!disposable.isDisposed) {
                            emitter.onComplete()
                        }
                    }
                }
        )
    }

    private fun getObserver(): MaybeObserver<Note> {
        return object : MaybeObserver<Note> {
            override fun onSubscribe(d: Disposable) {
                disposable = d
                info { "Subscribe" }
            }

            override fun onSuccess(t: Note) {
                info { "Success => ID: ${t.id}, TITLE: ${t.title}" }
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