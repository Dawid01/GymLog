package com.szczepaniak.dawid.gymlog

import com.szczepaniak.dawid.gymlog.models.Exercise
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


object RetrofitClient{
    private const val BASE_URL = "https://api.api-ninjas.com/v1/exercises"
    val retrofit: Retrofit by lazy {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("X-Api-Key", R.string.API_KEY.toString())
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
    @GET("posts/{id}")
    fun getPostById(@Path("id") postId: Int): Call<Exercise>
}