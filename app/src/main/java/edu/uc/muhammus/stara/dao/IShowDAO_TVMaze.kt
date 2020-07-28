/*
 * Interface used for communicating with TVMaze API via Retrofit to get TV Show data
 *
 * @GET domain is defined in RetrofitClientInstance_TVMaze.kt
 * TVMaze API Reference: https://www.tvmaze.com/api
 */
package edu.uc.muhammus.stara.dao

import edu.uc.muhammus.stara.dto.ShowJSON
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IShowDAO_TVMaze {
    @GET("/search/shows")
    fun getShows(@Query("q") showName: String): Call<ArrayList<ShowJSON>>
}