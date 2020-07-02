/**
 * Use Retrofit for Actor Data via TVMaze API
 *
 * Path for BASE_URL in IActorDAOTVMaze.kt
 * API Reference: https://www.tvmaze.com/api
 */
package edu.uc.muhammus.stara.service

import android.util.Log
import androidx.lifecycle.MutableLiveData
import edu.uc.muhammus.stara.dao.IActorDAOTVMaze
import edu.uc.muhammus.stara.dto.ActorJSON
import edu.uc.muhammus.stara.retrofit.RetrofitClientInstanceTVMaze
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActorService {
    internal fun fetchActors(actorName: String) : MutableLiveData<ArrayList<ActorJSON>> {
        var _actors = MutableLiveData<ArrayList<ActorJSON>>()
        var service = RetrofitClientInstanceTVMaze.retrofitInstance?.create(IActorDAOTVMaze::class.java)
        val call = service?.getActors(actorName)

        call?.enqueue(object: Callback<ArrayList<ActorJSON>> {
            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             */

            override fun onFailure(call: Call<ArrayList<ActorJSON>>, t: Throwable) {
                println("ActorService Response FAILED")
                Log.e("tag", "ShowService Response FAILED")
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
                _actors.value = response.body()
            }
        })
        return _actors
    }
}