package edu.uc.muhammus.stara.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import edu.uc.muhammus.stara.dto.ActorJSON
import edu.uc.muhammus.stara.dto.Favorite
import edu.uc.muhammus.stara.dto.ScheduleJSON
import edu.uc.muhammus.stara.dto.ShowJSON
import edu.uc.muhammus.stara.service.ActorService
import edu.uc.muhammus.stara.service.ScheduleService
import edu.uc.muhammus.stara.service.ShowService

class MainViewModel : ViewModel() {
    private lateinit var firestore: FirebaseFirestore
    private val firestoreCollectionFavorites = "favorites"
    private val firestoreCollectionUsers = "users"
    private var _favorites: MutableLiveData<ArrayList<Favorite>> = MutableLiveData<ArrayList<Favorite>>()

    /**
     * Initialize necessary variables for Firebase usage.
     * Can not use built in init as it causes unit & integration tests to crash.
     * This needs to be manually run whenever Firebase will be interacted with.
     */
    internal fun initializeFirebase() {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    /**
     * shows - Hold latest show search results from TVMaze API
     * showService - Used to get search results via TVMaze API
     * fetchShows() - Get show search results for search term given
     */
    var shows: MutableLiveData<ArrayList<ShowJSON>> = MutableLiveData<ArrayList<ShowJSON>>()
    var showService: ShowService = ShowService()
    internal fun fetchShows(showName: String) {
        shows = showService.fetchShows(showName)
    }

    /**
     * actors - Hold latest actors search results from TVMaze API
     * actorService - Used to get search results via TVMaze API
     * fetchActors() - Get actor search results for search term given
     */
    var actors: MutableLiveData<ArrayList<ActorJSON>> = MutableLiveData<ArrayList<ActorJSON>>()
    var actorService: ActorService = ActorService()
    internal fun fetchActors(actorName: String) {
        actors = actorService.fetchActors(actorName)
    }

    /**
     * schedule - Holds today's schedule for premiering episodes from TVMaze API
     * scheduleService - Used to get schedule via TVMaze API
     * fetchSchedule() - Get schedule for country code given
     */
    var schedule: MutableLiveData<ArrayList<ScheduleJSON>> = MutableLiveData<ArrayList<ScheduleJSON>>()
    var scheduleService: ScheduleService = ScheduleService()
    internal fun fetchSchedule(countryCode: String) {
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
                            allFavorites.add(favorite)
                        }
                    }
                    _favorites.value = allFavorites
                }
            }
    }

    /**
     * Allow getting and setting private variable that holds logged in user's favorite shows and actors.
     */
    internal var favorites: MutableLiveData<ArrayList<Favorite>>
        get() { return _favorites }
        set(value) { _favorites = value }

    /**
     * Add show or actor to Firestore favorites for user logged in
     * Reference: https://youtu.be/CuP1elpCuEA
     */
    internal fun addFavorite(favorite: Favorite, email: String) {
        firestore.collection(firestoreCollectionUsers)
            .document(email)
            .collection(firestoreCollectionFavorites)
            .document(favorite.id)
            .set(favorite)
            .addOnSuccessListener {
                Log.d("Firebase", "Favorite add succeeded")
            }
            .addOnFailureListener {
                Log.w("Firebase", "Favorite add failed")
            }
    }

    /**
     * Remove show or actor from Firestore favorites for user logged in
     */
    internal fun removeFavorite(favorite: Favorite, email: String) {
        firestore.collection(firestoreCollectionUsers)
            .document(email)
            .collection(firestoreCollectionFavorites)
            .document(favorite.id)
            .delete()
            .addOnSuccessListener {
                Log.d("Firebase", "Favorite deletion succeeded")
            }
            .addOnFailureListener {
                Log.w("Firebase", "Favorite deletion failed")
            }
    }
}