package edu.uc.muhammus.stara.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.uc.muhammus.stara.dto.ActorJSON
import edu.uc.muhammus.stara.dto.ScheduleJSON
import edu.uc.muhammus.stara.dto.ShowJSON
import edu.uc.muhammus.stara.service.ActorService
import edu.uc.muhammus.stara.service.ScheduleService
import edu.uc.muhammus.stara.service.ShowService

class MainViewModel : ViewModel() {
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
}