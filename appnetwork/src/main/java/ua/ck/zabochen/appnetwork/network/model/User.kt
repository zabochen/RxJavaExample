package ua.ck.zabochen.appnetwork.network.model

import com.google.gson.annotations.SerializedName

open class User : BaseResponse() {

    @SerializedName("api_key")
    lateinit var apiKey: String

}