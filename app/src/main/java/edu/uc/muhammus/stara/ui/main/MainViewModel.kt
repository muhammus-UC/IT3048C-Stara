package edu.uc.muhammus.stara.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.uc.muhammus.stara.dto.Actor
import edu.uc.muhammus.stara.dto.Show
import edu.uc.muhammus.stara.service.ActorService
import edu.uc.muhammus.stara.service.ShowService

class MainViewModel : ViewModel() {
    var shows: MutableLiveData<ArrayList<Show>> = MutableLiveData<ArrayList<Show>>()
    var showService: ShowService = ShowService()

    fun fetchShows(showName: String) {
        shows = showService.fetchShows(showName)
    }

    var actors: MutableLiveData<ArrayList<Actor>> = MutableLiveData<ArrayList<Actor>>()
    var actorService: ActorService = ActorService()

    fun fetchActors(actorName: String) {
        actors = actorService.fetchActors(actorName)
    }
}