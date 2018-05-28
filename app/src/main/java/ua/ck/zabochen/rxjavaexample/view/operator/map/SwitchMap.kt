package ua.ck.zabochen.rxjavaexample.view.operator.map

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import ua.ck.zabochen.rxjavaexample.R
import java.util.concurrent.TimeUnit

class SwitchMap : Fragment(), AnkoLogger {

    /*
    * SwitchMap always return the latest Observable and emits the items from it.
    * 1. Writing an Instant Search app which sends search query to server each time user types something.
    * In this case multiple requests will be sent to server with multiple queries,
    * but we want to show the result of latest typed query only.
    * 2. Another use case of SwitchMap is, you have a feed screen in which feed is refreshed each
    * time user perform pull down to refresh. In this scenario, SwitchMap is best suited as it can
    * ignores the older feed response and consider only the latest request.
    */

    private lateinit var mDisposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_switch_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observable
        val observable: Observable<Int> = getObservable()

        // Subscription
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .switchMap(
                        object : Function<Int, ObservableSource<Int>> {
                            override fun apply(t: Int): ObservableSource<Int> {
                                return Observable
                                        .fromArray(t)
                                        .delay(2, TimeUnit.SECONDS)
                            }
                        }
                )
                .subscribe(
                        object : Observer<Int> {
                            override fun onSubscribe(d: Disposable) {
                                mDisposable = d
                                info { "Subscribe" }
                            }

                            override fun onNext(t: Int) {
                                info { "Next => $t" }
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
        info { "Unsubscribe" }
    }

    private fun getObservable(): Observable<Int> {
        return Observable.just(1, 2, 3, 4, 5)
    }

}