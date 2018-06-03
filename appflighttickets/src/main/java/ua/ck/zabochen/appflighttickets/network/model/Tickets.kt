package ua.ck.zabochen.appflighttickets.network.model

import com.google.gson.annotations.SerializedName

data class Ticket(
        @SerializedName("from") var from: String,
        @SerializedName("to") var to: String,
        @SerializedName("flight_number") var flightNumber: String,
        @SerializedName("departure") var departure: String,
        @SerializedName("arrival") var arrival: String,
        @SerializedName("duration") var duration: String,
        @SerializedName("instructions") var instructions: String,
        @SerializedName("stops") var stops: Int,
        @SerializedName("airline") var airline: Airline,
        var price: Price
)

data class Airline(
        @SerializedName("id") var id: Int,
        @SerializedName("name") var name: String,
        @SerializedName("logo") var logo: String
)

data class Price(
        @SerializedName("price") var price: Int,
        @SerializedName("seats") var seats: Int,
        @SerializedName("currency") var currency: String,
        @SerializedName("flight_number") var flightNumber: String,
        @SerializedName("from") var from: String,
        @SerializedName("to") var to: String
)