package com.exzell.myfootball.io.internet

import android.content.Context
import com.exzell.myfootball.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object InternetManager {

    val baseUrl = "https://www.thesportsdb.com/"

    @JvmField
    val basePath = "api/v1/json/1/"

    @JvmField
    val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()


    private var retrofit: Retrofit? = null

    @Singleton
    @Provides
    fun getApi(context: Context): Retrofit {
        if (retrofit != null) return retrofit!!

        val newClient = client.newBuilder()
                .cache(createCache(context)).build()

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(newClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build().apply { retrofit = this }
    }

    private fun createCache(context: Context): Cache {
        val dir = if (BuildConfig.DEBUG)
            context.getExternalFilesDir("response_cache")
        else
            context.getDir("response_cache", Context.MODE_PRIVATE)
        return Cache(dir!!, 3L * 1000 * 1000)
    }
}