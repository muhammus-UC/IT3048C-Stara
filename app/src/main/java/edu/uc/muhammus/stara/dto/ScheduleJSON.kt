/**
 * Holds information about TV schedule for new episodes premiering today
 *
 * @param ScheduleJSON.episodeName - A string to hold new episode name. Ex: "Daisy Chain Gang"
 * @param ScheduleJSON.airtime - A string to hold premiere time in 24-Hour format. Ex: "19:00"
 *
 * @param ScheduleJSON.show - A Show object used to parse nested JSON about a show. Defined in ShowJSON.kt
 */

package edu.uc.muhammus.stara.dto

import com.google.gson.annotations.SerializedName

data class ScheduleJSON(@SerializedName("name") var episodeName: String,
                        var airtime: String,
                        var show: Show) {
    override fun toString(): String {
        return "Episode $episodeName airs at $airtime for show " + show.name
    }
}