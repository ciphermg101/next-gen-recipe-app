package com.example.next_gen_recipe_app.data.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import com.example.next_gen_recipe_app.BuildConfig

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        // Retrieve the API key from BuildConfig (from gradle.properties)
        val apiKey = BuildConfig.SPOONACULAR_API_KEY

        // Append the API key to the request URL
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("apiKey", apiKey)
            .build()

        Log.d("ApiKeyInterceptor", "Request URL: $newUrl")

        val newRequest = originalRequest.newBuilder().url(newUrl).build()
        return chain.proceed(newRequest)
    }
}
