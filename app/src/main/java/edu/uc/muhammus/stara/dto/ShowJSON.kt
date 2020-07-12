/**
 * Holds information about a tv show
 *
 * @param ShowJSON.Score A Double used to tell which result is closest.
 *
 * @param ShowJSON.Show A Show object used to parse nested JSON about a show.
 * @param ShowJSON.Show.name A String that holds tv show name. Ex: Community
 * @param ShowJSON.Show.language A String that holds tv show language. Ex: English
 * @param ShowJSON.Show.status A String that holds tv show status. Ex: Ended
 *
 * References:
 * https://stackoverflow.com/questions/55067977/how-can-correctly-parse-nested-json-object-using-retrofit-2-0-kotlin
 */

package edu.uc.muhammus.stara.dto

data class ShowJSON(var score: Double, var show: Show) {
    override fun toString(): String {
        return this.show.name
    }
}

data class Show(var name: String,
                var language: String?,
                var status: String?,
                var image: Image? = null) {
    override fun toString(): String {
        return this.name
    }
}

data class Image(var medium: String, var original: String) {
    override fun toString(): String {
        return this.original
    }
}