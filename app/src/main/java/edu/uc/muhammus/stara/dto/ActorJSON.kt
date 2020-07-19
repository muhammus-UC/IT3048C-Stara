/**
 * Holds information about an actor/actress
 *
 * @param ActorJSON.Score A double used to tell which result is closest.
 *
 * @param ActorJSON.Actor An Actor object used to parse nested JSON about an actor/actress.
 * @param ActorJSON.Actor.name A String that holds actor/actress name. Ex: Joel McHale
 *
 * @param ActorJSON.Actor.country - A Country object used to parse nested JSON about birth country of actor/actress. NULLABLE
 * @param ActorJSON.Actor.country.name - A String that holds actor's country of birth's country name. Ex: Italy NULLABLE
 * @param ActorJSON.Actor.country.code - A String that holds actor's country of birth's country code. Ex: IT NULLABLE
 * @param ActorJSON.Actor.country.timezone - A String that holds actor's country of birth's time zone. Ex: Europe/Rome NULLABLE
 *
 * @param ActorJSON.Actor.gender - A String that holds actor's gender. Ex: Male
 */

package edu.uc.muhammus.stara.dto

import com.google.gson.annotations.SerializedName


data class ActorJSON(var score: Double, @SerializedName("person") var actor: Actor)

data class Actor(var name: String,
                 var country: ActorCountry?,
                 var gender: String = "",
                 var image: ImageURL? = null) {
    override fun toString(): String {
        return this.name
    }
}
data class ActorCountry(var name: String?, var code: String?, var timezone: String?)

data class ImageURL(var medium: String, var original: String)
