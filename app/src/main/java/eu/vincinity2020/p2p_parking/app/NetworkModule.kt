package eu.vincinity2020.p2p_parking.app

import android.content.Context
import dagger.Module
import dagger.Provides
import eu.vincinity2020.p2p_parking.common.ApplicationScope
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
class NetworkModule {

    @ApplicationScope
    @Provides
    @Named("backend")
    fun providesRetrofit(okHttpClient: OkHttpClient,
                         rxJava2CallAdapterFactory: RxJava2CallAdapterFactory
                         , gsonConverterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .baseUrl("https://api.github.com")
                .client(okHttpClient)
                .build()
    }

    @ApplicationScope
    @Provides
    @Named("openStreetMap")
    fun providesRetrofitOpenStreetMap(okHttpClient: OkHttpClient,
                                      rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
                                      gsonConverterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .baseUrl(" https://nominatim.openstreetmap.org/")
                .client(okHttpClient)
                .build()
    }

    @ApplicationScope
    @Provides
    fun providesGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @ApplicationScope
    @Provides
    fun providesRxJava2CallAdapter(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
    }

    @ApplicationScope
    @Provides
    fun providesCache(context: Context): Cache {
        val cacheSize: Long = 10 * 1024 * 1024 // 10 mb
        return Cache(context.cacheDir, cacheSize)
    }

    @ApplicationScope
    @Provides
    fun providesOkhttp(cache: Cache, httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.cache(cache)
        builder.addInterceptor(httpLoggingInterceptor)
        return builder.build()
    }


    @Provides
    @ApplicationScope
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @ApplicationScope
    @Provides
    fun providesNetworkClient(@Named("backend") retrofit: Retrofit): NetworkService {
        return retrofit.create(NetworkService::class.java)
    }

    @ApplicationScope
    @Provides
    fun providesOpenStreetMapService(@Named("openStreetMap") retrofit: Retrofit): OpenStreetMapService {
        return retrofit.create(OpenStreetMapService::class.java)
    }
}
