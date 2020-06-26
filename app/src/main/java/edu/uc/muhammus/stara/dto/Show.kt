/**
 * Holds information about a tv show
 *
 * @param name A String that holds tv show name. Ex: Community
 * @param language A String that holds tv show language. Ex: English
 * @param status A String that holds tv show status. Ex: Ended
 *
 * References:
 * https://stackoverflow.com/questions/55067977/how-can-correctly-parse-nested-json-object-using-retrofit-2-0-kotlin
 */

package edu.uc.muhammus.stara.dto

import com.google.gson.annotations.SerializedName

data class ShowJSON(var score: Double,
                    var show: Show)

data class Show(var name: String,
                var language: String,
                var status: String) {
    override fun toString(): String {
        return this.name
    }
}