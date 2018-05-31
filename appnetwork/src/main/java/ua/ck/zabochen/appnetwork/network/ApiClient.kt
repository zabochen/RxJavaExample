package ua.ck.zabochen.appnetwork.network

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ua.ck.zabochen.appnetwork.utils.Constant
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object {

        private const val REQUEST_TIMEOUT: Long = 60
        private lateinit var mRetrofit: Retrofit
        private lateinit var mOkHttpClient: OkHttpClient

        fun getRetrofitClient(): Retrofit {
            return initRetrofit()
        }

        private fun initOkHttp(context: Context): OkHttpClient {
            return OkHttpClient().newBuilder()
                    .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .build()
        }

        private fun initRetrofit(): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

    }

}

