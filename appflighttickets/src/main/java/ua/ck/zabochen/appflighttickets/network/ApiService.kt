package ua.ck.zabochen.appflighttickets.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ua.ck.zabochen.appflighttickets.network.model.Price
import ua.ck.zabochen.appflighttickets.network.model.Ticket

interface ApiService {

    // To fetch the tickets
    // https://api.androidhive.info/json/airline-tickets.php
    // https://api.androidhive.info/json/airline-tickets.php?from=DEL&to=CHE
    @GET("airline-tickets.php")
    fun getTicketList(@Query("from") from: String,
                      @Query("to") to: String
    ): Single<List<Ticket>>

    // To fetch individual ticket price
    // https://api.androidhive.info/json/airline-tickets-price.php
    // https://api.androidhive.info/json/airline-tickets-price.php?flight_number=6E-ARIfrom=DEL&to=CHE
    @GET("airline-tickets-price.php")
    fun getTicketPrice(@Query("flight_number") flightNumber: String,
                       @Query("from") from: String,
                       @Query("to") to: String
    ): Single<Price>

}