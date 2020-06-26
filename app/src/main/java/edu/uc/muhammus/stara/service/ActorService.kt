package edu.uc.muhammus.stara.service

import androidx.lifecycle.MutableLiveData
import edu.uc.muhammus.stara.dto.Actor

class ActorService {
    fun fetchActors(actorName: String) : MutableLiveData<ArrayList<Actor>> {
        return MutableLiveData<ArrayList<Actor>>()
    }
}