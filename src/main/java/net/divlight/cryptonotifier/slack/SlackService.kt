package net.divlight.cryptonotifier.slack

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SlackService {
    @FormUrlEncoded
    @POST("/api/chat.postMessage")
    fun postMessage(
        @Field("token") token: String,
        @Field("channel") channel: String,
        @Field("attachments") attachments: String
    ): Call<Void>

    companion object {
        private const val BASE_URL = "https://slack.com/"

        fun createService(): SlackService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create()
                )
            )
            .build()
            .create(SlackService::class.java)

        private fun createOkHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        }.build()
    }
}
