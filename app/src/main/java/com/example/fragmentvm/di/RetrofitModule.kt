package com.example.fragmentvm.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.mmtr.data.api.network.RetrofitInterface
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofitInterface(retrofit: Retrofit): RetrofitInterface {
        return retrofit.create(RetrofitInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit {
        val logging = HttpLoggingInterceptor { message -> Timber.d(message) }
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

        val gson = GsonBuilder().setLenient().create()

        val requestInterceptor = Interceptor { chain ->
            val original: Request = chain.request()

            val requestBuilder: Request.Builder = original.newBuilder()
                .header(
                    "Content-Type",
                    "application/json"
                )
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(requestInterceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }
}