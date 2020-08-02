/**
 * Holds details about a location.
 * Used to get location data via GPS in LocationLiveData.kt and LocationViewModel.kt.
 *
 * @param longitude - A string used to hold longitude of location.
 * @param latitude - A string used to hold latitude of location.
 */
package edu.uc.muhammus.stara.dto

data class LocationDetails(val longitude: String, val latitude: String) {
    override fun toString(): String {
        return "Longitude: $longitude. Latitude: $latitude."
    }
}
