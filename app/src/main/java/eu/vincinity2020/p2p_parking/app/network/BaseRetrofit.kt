package eu.vincinity2020.p2p_parking.app.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object BaseRetrofit {

    var baseUrl:String? = null

    private fun getAcceptHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                    .header("Accept", "application/json")
                    .method(originalRequest.method(), originalRequest.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    fun <S> createService(getHeaders: ((request: Request) -> Request.Builder)? = null): NetworkService {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.HEADERS
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClientBuilder = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addNetworkInterceptor(getAcceptHeaderInterceptor())

        getHeaders?.let {
            okHttpClientBuilder.addNetworkInterceptor(getAuthHeaderInterceptor(it))
        }


        val okHttpClient = okHttpClientBuilder.build()
        okHttpClient.dispatcher().maxRequests = 1
        okHttpClient.dispatcher().maxRequestsPerHost = 1

        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()

        return retrofit.create(NetworkService::class.java)
    }

    private fun getAuthHeaderInterceptor(getHeaders: (request: Request) -> Request.Builder): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = getHeaders(originalRequest)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }
}