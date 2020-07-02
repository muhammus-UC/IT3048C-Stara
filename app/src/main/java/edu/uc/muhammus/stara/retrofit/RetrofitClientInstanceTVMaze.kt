/**
 * Setup Retrofit for use with TVMaze API
 *
 * Path for BASE_URL in IShowDAOTVMaze.kt and IActorDAOTVMaze.kt
 * TVMaze API Reference: https://www.tvmaze.com/api
 */
package edu.uc.muhammus.stara.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientInstanceTVMaze {
    private var retrofit: Retrofit? = null
    private val BASE_URL = "https://api.tvmaze.com"

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