/**
 * Holds information about a tv show
 *
 * @param ShowJSON.score - A Double used to tell which result is closest.
 * @param ShowJSON.show - A Show object used to parse nested JSON about a show.
 *
 * @param ShowJSON.Show.id - A String that holds show id for TVMaze. Ex: "318"
 * @param ShowJSON.Show.name - A String that holds tv show name. Ex: "Community"
 * @param ShowJSON.Show.language? - A String that holds tv show language. Ex: "English"
 * @param ShowJSON.Show.status? - A String that holds tv show status. Ex: "Ended"
 *
 * @param ShowJSON.image? - A ShowImageURL object that holds various links to images of Show.
 * @param ShowJSON.Show.image.medium - A String that holds URL to low resolution image.
 *
 * References:
 * https://stackoverflow.com/questions/55067977/how-can-correctly-parse-nested-json-object-using-retrofit-2-0-kotlin
 */

package edu.uc.muhammus.stara.dto

data class ShowJSON(var score: Double, var show: Show) {
    override fun toString(): String {
        return show.name
    }
}

data class Show(var id: String,
                var name: String,
                var language: String? = "Language Unknown",
                var status: String? = "Status Unknown",
                var image: ShowImageURL? = null
) {
    override fun toString(): String {
        return name
    }
}

data class ShowImageURL(var medium: String) {
    override fun toString(): String {
        return this.medium
    }
}