/**
 * Use Retrofit for Actor Data via TVMaze API
 *
 * Path for BASE_URL in IActorDAO_TVMaze.kt
 * API Reference: https://www.tvmaze.com/api
 */
package edu.uc.muhammus.stara.service

import android.util.Log
import androidx.lifecycle.MutableLiveData
import edu.uc.muhammus.stara.dao.IActorDAO_TVMaze
import edu.uc.muhammus.stara.dto.ActorJSON
import edu.uc.muhammus.stara.retrofit.RetrofitClientInstance_TVMaze
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActorService {
    internal fun fetchActors(actorName: String) : MutableLiveData<ArrayList<ActorJSON>> {
        val _actors = MutableLiveData<ArrayList<ActorJSON>>()
        val service = RetrofitClientInstance_TVMaze.retrofitInstance?.create(IActorDAO_TVMaze::class.java)
        val call = service?.getActors(actorName)

        call?.enqueue(object : Callback<ArrayList<ActorJSON>> {
            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             */
            override fun onFailure(call: Call<ArrayList<ActorJSON>>, t: Throwable) {
                println("ActorService Response FAILED")
                Log.e("ActorService.kt", "ActorService Response FAILED")
            }

            /**
             * Invoked for a received HTTP response
             *
             * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call [Response.isSuccessful] to determine if the response indicates success.
             */
            override fun onResponse(
                call: Call<ArrayList<ActorJSON>>,
                response: Response<ArrayList<ActorJSON>>
            ) {
                if (response.isSuccessful) {
                    Log.d("ActorService.kt", "ActorService Response SUCCEEDED")
                    _actors.value = response.body()
                }
                else {
                    Log.e("ActorService.kt", "ActorService Response ERROR")
                }
            }
        })
        Thread.sleep(1000)
        return _actors
    }
}