/**
 * Used to get Location. (Live Data)
 * Reference: https://youtu.be/VgZZemAwLTk
 */
package edu.uc.muhammus.stara.ui.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import edu.uc.muhammus.stara.dto.LocationDetails

class LocationLiveData(context: Context): LiveData<LocationDetails>() {

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // Data has no observers
    override fun onInactive() {
        super.onInactive()
        // Turn off location updates
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // Data has observers
    @SuppressLint("MissingPermission") // We will check for permission within fragment.
    override fun onActive() {
        super.onActive()
        fusedLocationClient.lastLocation.addOnSuccessListener {
            location: Location ->
                location.also {
                    setLocationData(it)
                }
        }
        startLocationUpdates()
    }

    @SuppressLint("MissingPermission") // We will check for permission within fragment.
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            locationResult ?: return

            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }

    /**
     * If we've received a location update, this function will be called
     */
    private fun setLocationData(location: Location) {
        value = LocationDetails(location.longitude.toString(), location.latitude.toString())
    }

    companion object {
        val ONE_MINUTE: Long = 60000
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = ONE_MINUTE
            fastestInterval = ONE_MINUTE / 4
            priority = LocationRequest.PRIORITY_LOW_POWER // We only need to know country, so LOW_POWER is enough for our needs
        }
    }
}