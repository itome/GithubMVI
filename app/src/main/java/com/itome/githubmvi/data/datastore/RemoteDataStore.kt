package com.itome.githubmvi.data.datastore

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


private const val BASE_URL = "https://api.github.com"

abstract class RemoteDataStore {

    protected companion object {

        fun <T> createService(
                service: Class<T>,
                accessToken: String?,
                baseUrl: String = BASE_URL
        ) = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(createClient(accessToken))
                .build()
                .create(service)!!

        private fun createClient(accessToken: String?): OkHttpClient =
                OkHttpClient.Builder().also({ builder ->
                    builder.addInterceptor { chain ->
                        val requestBuilder = chain.request().newBuilder()
                        accessToken?.let {
                            requestBuilder.addHeader("Authorization", "token " + accessToken)
                        }

                        chain.proceed(requestBuilder.build())
                    }
                }).build()
    }
}
