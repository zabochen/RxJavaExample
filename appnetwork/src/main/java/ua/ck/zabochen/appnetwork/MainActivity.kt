package ua.ck.zabochen.appnetwork

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import ua.ck.zabochen.appnetwork.network.ApiClient
import ua.ck.zabochen.appnetwork.network.ApiService
import ua.ck.zabochen.appnetwork.network.model.Note

class MainActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Init subscribers holder
        compositeDisposable = CompositeDisposable()

        network()
    }

    private fun network() {

        val apiService: ApiService = ApiClient.getRetrofitClient(this)
                .create(ApiService::class.java)

        compositeDisposable.add(apiService.getAllNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Note>>() {
                    override fun onSuccess(t: List<Note>) {
                        for (note in t) {
                            info { "NOTE => ID: ${note.id}, NAME: ${note.name}" }
                        }
                    }

                    override fun onError(e: Throwable) {
                        info { "Error => ${e.printStackTrace()}" }
                    }
                })
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clear subscription
        compositeDisposable.clear()
    }

}
