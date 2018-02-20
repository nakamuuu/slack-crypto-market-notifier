package net.divlight.cryptonotifier.bitflyer

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface BitFlyerService {
    @GET("/v1/executions")
    fun getExecutions(
        @Query("product_code") productCode: String? = null,
        @Query("count") count: Int = 10
    ): Call<List<Execution>>

    companion object {
        private const val BASE_URL = "https://api.bitflyer.jp/"
        private const val RESPONSE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

        fun createService(): BitFlyerService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .setDateFormat(RESPONSE_DATE_FORMAT)
                        .create()
                )
            )
            .build()
            .create(BitFlyerService::class.java)

        private fun createOkHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
        }.build()
    }
}
