package edu.uc.muhammus.stara.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import edu.uc.muhammus.stara.MainActivity
import edu.uc.muhammus.stara.dto.*
import edu.uc.muhammus.stara.service.ActorService
import edu.uc.muhammus.stara.service.ScheduleService
import edu.uc.muhammus.stara.service.ShowService

class MainViewModel : ViewModel() {
    private lateinit var firestore: FirebaseFirestore
    private val firestoreCollectionFavorites = "favorites"
    private val firestoreCollectionUsers = "users"
    private var _favorites: MutableLiveData<ArrayList<Favorite>> = MutableLiveData<ArrayList<Favorite>>()

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        //listenToFavorites()
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

    /**
     * This will hear any updates from Firestore
     * Reference: https://youtu.be/65OX1cBqkzw
     */
    internal fun listenToFavorites(email: String) {
        firestore.collection(firestoreCollectionUsers)
            .document(email)
            .collection(firestoreCollectionFavorites).addSnapshotListener {
            snapshot, e ->
            // If there is an exception, we want to skip
            if (e != null) {
                Log.w(TAG, "Listen Failed", e)
                return@addSnapshotListener
            }
            // If we are here, we did not encounter an exception
            if (snapshot != null) {
                // Now, we have a populated snapshot
                val allFavorites = ArrayList<Favorite>()
                val documents = snapshot.documents
                documents.forEach {
                    val favorite = it.toObject(Favorite::class.java)
                    if (favorite != null) {
                        allFavorites.add(favorite!!)
                    }
                }
                _favorites.value = allFavorites
            }
        }
    }

    internal var favorites:MutableLiveData<ArrayList<Favorite>>
        get() {return _favorites}
        set(value) {_favorites = value}

    /**
     * Add show or actor to Firestore favorites
     * Reference: https://youtu.be/CuP1elpCuEA
     */
    fun addFavorite(favorite: Favorite, email: String) {
        firestore.collection(firestoreCollectionUsers)
            .document(email)
            .collection(firestoreCollectionFavorites)
                .document(favorite.id)
                .set(favorite)
                .addOnSuccessListener {
                    Log.d("Firebase", "Favorite succeeded")
                }
                .addOnFailureListener{
                    Log.d("Firebase", "Favorite failed")
                }
    }
}