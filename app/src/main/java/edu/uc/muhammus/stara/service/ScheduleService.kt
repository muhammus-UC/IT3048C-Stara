/**
 * Use Retrofit for TV Show Data via TVMaze API
 *
 * Path for BASE_URL in IShowDAO_TVMaze.kt
 * API Reference: https://www.tvmaze.com/api
 */

package edu.uc.muhammus.stara.service

import android.util.Log
import androidx.lifecycle.MutableLiveData
import edu.uc.muhammus.stara.dao.IScheduleDAO_TVMaze
import edu.uc.muhammus.stara.dto.ScheduleJSON
import edu.uc.muhammus.stara.retrofit.RetrofitClientInstance_TVMaze
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleService {
    internal fun fetchSchedule(countryCode: String) : MutableLiveData<ArrayList<ScheduleJSON>> {
        val _schedule = MutableLiveData<ArrayList<ScheduleJSON>>()
        val service = RetrofitClientInstance_TVMaze.retrofitInstance?.create(IScheduleDAO_TVMaze::class.java)
        val call = service?.getSchedule(countryCode)

        call?.enqueue(object: Callback<ArrayList<ScheduleJSON>> {
            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             */
            override fun onFailure(call: Call<ArrayList<ScheduleJSON>>, t: Throwable) {
                println("ScheduleService Response FAILED")
                Log.e("ScheduleService.kt", "ShowService Response FAILED")
            }

            /**
             * Invoked for a received HTTP response
             *
             * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call [Response.isSuccessful] to determine if the response indicates success.
             */
            override fun onResponse(
                call: Call<ArrayList<ScheduleJSON>>,
                response: Response<ArrayList<ScheduleJSON>>
            ) {
                if (response.isSuccessful) {
                    Log.d("ScheduleService.kt", "ScheduleService Response SUCCEEDED")
                    _schedule.value = response.body()
                }
                else {
                    Log.d("ScheduleService.kt", "ScheduleService Response ERROR")
                }
            }
        })
        Thread.sleep(1000)
        return _schedule
    }
}