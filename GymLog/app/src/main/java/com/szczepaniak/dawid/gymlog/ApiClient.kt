package com.szczepaniak.dawid.gymlog

import android.annotation.SuppressLint
import android.content.Context
import com.szczepaniak.dawid.gymlog.models.Exercise
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


@SuppressLint("StaticFieldLeak")
object RetrofitClient{
    private const val BASE_URL = "https://api.api-ninjas.com/v1/"
    lateinit var context: Context

    val retrofit: Retrofit by lazy {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("X-Api-Key", context.getString(R.string.API_KEY))
                .method(original.method(), original.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }

}

object ApiClient {
    val apiService: ApiService by lazy {
        RetrofitClient.retrofit.create(ApiService::class.java)
    }
}

interface ApiService {
    @GET("exercises")
    fun getExercises(@Query("muscle") muscle: String, @Query("name") name: String = "", @Query("offset") offset: Int = 0 ): Call<Array<Exercise>>
}