package ua.ck.zabochen.appflighttickets.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ua.ck.zabochen.appflighttickets.utils.Constant
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object {

        private const val TIMEOUT: Long = 60

        fun getClient(): Retrofit {
            return initRetrofit(getOkHttpClient())
        }

        private fun initRetrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
        }

        private fun getOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addInterceptor(object : Interceptor {
                        override fun intercept(chain: Interceptor.Chain?): Response {
                            val request: Request = chain!!.request().newBuilder()
                                    .addHeader("Accept", "application/json")
                                    .addHeader("Request-Type", "Android")
                                    .addHeader("Content-Type", "application/json")
                                    .build()
                            return chain.proceed(request)
                        }
                    })
                    .build()
        }

    }

}