/**
 * Setup Retrofit for use with TVMaze API
 *
 * Path for BASE_URL in IShowDAO_TVMaze.kt and IActorDAO_TVMaze.kt
 * TVMaze API Reference: https://www.tvmaze.com/api
 */
package edu.uc.muhammus.stara.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientInstance_TVMaze {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://api.tvmaze.com"

    val retrofitInstance: Retrofit?
        get() {
            // Has this object been created yet?
            if (retrofit == null) {
                // If not, then create it
                retrofit = retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            // Object created or already exists, return it
            return retrofit
        }
}