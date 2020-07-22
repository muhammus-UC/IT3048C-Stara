/*
 * Interface used for communicating with TVMaze API via Retrofit to get Actor data
 *
 * @GET domain is defined in RetrofitClientInstance_TVMaze.kt
 * TVMaze API Reference: https://www.tvmaze.com/api
 */

package edu.uc.muhammus.stara.dao

import edu.uc.muhammus.stara.dto.ActorJSON
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IActorDAO_TVMaze {
    @GET("/search/people")
    fun getActors(@Query("q") actorName: String) : Call<ArrayList<ActorJSON>>
}