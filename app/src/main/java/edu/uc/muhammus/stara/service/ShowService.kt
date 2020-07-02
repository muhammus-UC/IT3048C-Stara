/**
 * Use Retrofit for TV Show Data via TVMaze API
 *
 * Path for BASE_URL in IShowDAOTVMaze.kt
 * API Reference: https://www.tvmaze.com/api
 */

package edu.uc.muhammus.stara.service

import android.util.Log
import androidx.lifecycle.MutableLiveData
import edu.uc.muhammus.stara.dao.IShowDAOTVMaze
import edu.uc.muhammus.stara.dto.ShowJSON
import edu.uc.muhammus.stara.retrofit.RetrofitClientInstanceTVMaze
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowService {
    internal fun fetchShows(showName: String) : MutableLiveData<ArrayList<ShowJSON>> {
        var _shows = MutableLiveData<ArrayList<ShowJSON>>()
        val service = RetrofitClientInstanceTVMaze.retrofitInstance?.create(IShowDAOTVMaze::class.java)
        val call = service?.getShows(showName)

        call?.enqueue(object: Callback<ArrayList<ShowJSON>> {
            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             */
            override fun onFailure(call: Call<ArrayList<ShowJSON>>, t: Throwable) {
                println("ShowService Response FAILED")
                Log.e("tag", "ShowService Response FAILED")
            }

            /**
             * Invoked for a received HTTP response
             *
             * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call [Response.isSuccessful] to determine if the response indicates success.
             */
            override fun onResponse(
                call: Call<ArrayList<ShowJSON>>,
                response: Response<ArrayList<ShowJSON>>
            ) {
                _shows.value = response.body()
            }
        })
        return _shows
    }
}