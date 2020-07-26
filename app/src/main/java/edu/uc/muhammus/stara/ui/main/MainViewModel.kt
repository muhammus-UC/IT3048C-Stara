package edu.uc.muhammus.stara.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import edu.uc.muhammus.stara.dto.ActorJSON
import edu.uc.muhammus.stara.dto.ScheduleJSON
import edu.uc.muhammus.stara.dto.Show
import edu.uc.muhammus.stara.dto.ShowJSON
import edu.uc.muhammus.stara.service.ActorService
import edu.uc.muhammus.stara.service.ScheduleService
import edu.uc.muhammus.stara.service.ShowService

class MainViewModel : ViewModel() {
    private lateinit var firestore: FirebaseFirestore

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    var shows: MutableLiveData<ArrayList<ShowJSON>> = MutableLiveData<ArrayList<ShowJSON>>()
    var showService: ShowService = ShowService()

    fun fetchShows(showName: String) {
        shows = showService.fetchShows(showName)
    }

    var actors: MutableLiveData<ArrayList<ActorJSON>> = MutableLiveData<ArrayList<ActorJSON>>()
    var actorService: ActorService = ActorService()

    fun fetchActors(actorName: String) {
        actors = actorService.fetchActors(actorName)
    }

    var schedule: MutableLiveData<ArrayList<ScheduleJSON>> = MutableLiveData<ArrayList<ScheduleJSON>>()
    var scheduleService: ScheduleService = ScheduleService()

    fun fetchSchedule(countryCode: String) {
        schedule = scheduleService.fetchSchedule(countryCode)
    }

    // Add favorite show to Firebase
    fun favorite(show: Show) {
        firestore.collection("favorites")
            .document("Show_" + show.id)
            .set(show)
            .addOnSuccessListener {
                Log.d("Firebase", "Favorite show succeeded")
            }
            .addOnFailureListener{
                Log.d("Firebase", "Favorite show failed")
            }
    }
}