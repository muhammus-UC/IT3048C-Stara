package edu.uc.muhammus.stara.dto

import com.google.gson.annotations.SerializedName

data class ScheduleJSON(@SerializedName("name") var episodeName: String,
                        var airtime: String,
                        var show: Show) {
    override fun toString(): String {
        return "Episode $episodeName airs at $airtime for show " + show.name
    }
}