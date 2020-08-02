/*
 * Interface used for communicating with TVMaze API via Retrofit to get TV Schedule data
 *
 * @GET domain is defined in RetrofitClientInstance_TVMaze.kt
 * TVMaze API Reference: https://www.tvmaze.com/api
 */
package edu.uc.muhammus.stara.dao

import edu.uc.muhammus.stara.dto.ScheduleJSON
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IScheduleDAO_TVMaze {
    @GET("/schedule")
    fun getSchedule(@Query("country") countryCode: String): Call<ArrayList<ScheduleJSON>>
}
