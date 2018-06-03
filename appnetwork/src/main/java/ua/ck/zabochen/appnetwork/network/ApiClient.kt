package ua.ck.zabochen.appnetwork.network

import android.content.Context
import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ua.ck.zabochen.appnetwork.helper.SharedPreferencesHelper
import ua.ck.zabochen.appnetwork.utils.Constant
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object {

        private const val REQUEST_TIMEOUT: Long = 60

        fun getRetrofitClient(context: Context): Retrofit {
            return initRetrofit(initOkHttp(context))
        }

        private fun initRetrofit(httpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(httpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        private fun initOkHttp(context: Context): OkHttpClient {
            val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient().newBuilder()
                    .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)

            okHttpClientBuilder.addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain?): Response {
                    // Get request
                    val originalRequest: Request = chain!!.request()

                    // Add headers to request
                    val editRequestBuilder: Request.Builder = originalRequest
                            .newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/json")

                    // Get apiKey from SharedPreferences
                    if (!TextUtils.isEmpty(SharedPreferencesHelper.getApiKey(context))) {
                        editRequestBuilder
                                .addHeader("Authorization", SharedPreferencesHelper.getApiKey(context))
                    }

                    return chain.proceed(editRequestBuilder.build())
                }
            })

            return okHttpClientBuilder.build()
        }

    }

}

