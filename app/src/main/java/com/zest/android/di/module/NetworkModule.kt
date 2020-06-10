package com.zest.android.di.module

import android.app.Application
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.zest.android.BuildConfig
import com.zest.android.BuildConfig.DEBUG
import com.zest.android.data.source.remote.APIService
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {

    @Provides
    @JvmStatic
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.d("network", message) })

        httpLoggingInterceptor.level = if (DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        return httpLoggingInterceptor
    }

    @Provides
    @JvmStatic
    fun provideOkhttpCache(app: Application): Cache = Cache(app.cacheDir, 50_000_000)

    @Provides
    @Singleton
    @JvmStatic
    fun provideClient(
            loggingInterceptor: HttpLoggingInterceptor,
            cache: Cache
    ): OkHttpClient {
        return OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(loggingInterceptor)
                .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideMoshi() = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()


    @Provides
    @JvmStatic
    fun provideMoshiConverter(moshi: Moshi) = MoshiConverterFactory.create(moshi)


    @Provides
    @Singleton
    @JvmStatic
    fun provideRetrofit(
            moshiConverterFactory: MoshiConverterFactory,
            okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(moshiConverterFactory)
                .client(okHttpClient)
                .build()
    }

    @Provides
    fun provideService(retrofit: Retrofit): APIService = retrofit.create(APIService::class.java)
}