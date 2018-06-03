package ua.ck.zabochen.rxjavaexample.view.observable

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleObserver
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import ua.ck.zabochen.rxjavaexample.R
import ua.ck.zabochen.rxjavaexample.model.Note

class SingleObservable : Fragment(), AnkoLogger {

    /*
    Single always emits only one value or throws an error.
    Single Observable is more useful in making network calls
    */

    /*
    Observable   Observer	      Emissions
    Single	     SingleObserver	  One
    */

    // Subscriber holder
    private lateinit var disposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_observable_single, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observer
        val observer = getObserver()

        // Observable
        val observable = getObservable()

        // Subscribe
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }

    override fun onDetach() {
        super.onDetach()

        // Unsubscribe
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    private fun getObservable(): Single<Note> {
        return Single.create(
                object : SingleOnSubscribe<Note> {
                    override fun subscribe(emitter: SingleEmitter<Note>) {
                        if (!disposable.isDisposed) {
                            emitter.onSuccess(Note(1, "Just do it!"))
                        }
                    }
                }
        )
    }

    private fun getObserver(): SingleObserver<Note> {
        return object : SingleObserver<Note> {
            override fun onSubscribe(d: Disposable) {
                info { "Subscribe" }
                disposable = d
            }

            override fun onSuccess(t: Note) {
                info { "Success => NOTE: id => ${t.id}, title => ${t.title}" }
            }

            override fun onError(e: Throwable) {
                info { "Error => ${e.printStackTrace()}" }
            }
        }
    }
}