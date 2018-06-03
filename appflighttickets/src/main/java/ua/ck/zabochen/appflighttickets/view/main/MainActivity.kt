package ua.ck.zabochen.appflighttickets.view.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import ua.ck.zabochen.appflighttickets.R
import ua.ck.zabochen.appflighttickets.network.ApiClient
import ua.ck.zabochen.appflighttickets.network.ApiService
import ua.ck.zabochen.appflighttickets.network.model.Ticket

class MainActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService = ApiClient.getClient().create(ApiService::class.java)

        val observable: SingleObserver<List<Ticket>> = apiService.getTicketList("UA", "CK")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        object : SingleObserver<List<Ticket>> {
                            override fun onSubscribe(d: Disposable) {
                                info { "Subscribe" }
                            }

                            override fun onSuccess(t: List<Ticket>) {
                                for (ticket in t) {
                                    info { "TICKET => ${ticket.airline.name}" }
                                }
                            }

                            override fun onError(e: Throwable) {
                                info { "Error => ${e.printStackTrace()}" }
                            }

                        }
                )

    }
}
