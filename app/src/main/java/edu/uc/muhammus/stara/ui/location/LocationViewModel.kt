/**
 * Used to get Location. (View Model)
 * Reference: https://youtu.be/VgZZemAwLTk
 */
package edu.uc.muhammus.stara.ui.location

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val locationLiveData = LocationLiveData(application)
    fun getLocationLiveData() = locationLiveData
}