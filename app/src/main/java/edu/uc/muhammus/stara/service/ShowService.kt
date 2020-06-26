package edu.uc.muhammus.stara.service

import androidx.lifecycle.MutableLiveData
import edu.uc.muhammus.stara.dto.Show

class ShowService {
    fun fetchShows(showName: String) : MutableLiveData<ArrayList<Show>> {
        return MutableLiveData<ArrayList<Show>>()
    }
}