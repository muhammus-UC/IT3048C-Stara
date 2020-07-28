/**
 * Holds information about an actor/actress.
 *
 * @param ActorJSON.score - A double used to tell which result is closest.
 * @param ActorJSON.actor - An Actor object used to parse nested JSON about an actor/actress.
 *
 * @param ActorJSON.Actor.name - A String that holds actor/actress id for TVMaze. Ex: "11615"
 * @param ActorJSON.Actor.name - A String that holds actor/actress name. Ex: "Joel McHale"
 *
 * @param ActorJSON.Actor.country - A Country object used to parse nested JSON about birth country of actor/actress. NULLABLE
 * @param ActorJSON.Actor.country.name - A String that holds actor's country of birth's country name. Ex: "Italy" NULLABLE
 * @param ActorJSON.Actor.country.code - A String that holds actor's country of birth's country code. Ex: "IT" NULLABLE
 * @param ActorJSON.Actor.country.timezone - A String that holds actor's country of birth's time zone. Ex: "Europe/Rome" NULLABLE
 *
 * @param ActorJSON.Actor.gender - A String that holds actor's gender. Ex: Male
 *
 * @param ActorJSON.Actor.image - An ActorImageURL object that holds various links to images of Actor.
 * @param ActorJSON.Actor.image.medium - A String that holds URL to low resolution image.
 */

package edu.uc.muhammus.stara.dto

import com.google.gson.annotations.SerializedName


data class ActorJSON(var score: Double, @SerializedName("person") var actor: Actor)

data class Actor(var id: String,
                 var name: String,
                 var country: ActorCountry? = ActorCountry("Country Unknown", "Country Code Unknown", "Timezone Unknown"),
                 var gender: String? = "Gender Unknown",
                 var image: ActorImageURL? = null
) {
    override fun toString(): String {
        return name
    }
}
data class ActorCountry(var name: String? = "Country Unknown",
                        var code: String? = "Country Code Unknown",
                        var timezone: String? = "Timezone Unknown"
) {
    override fun toString(): String {
        return name!!
    }
}

data class ActorImageURL(var medium: String) {
    override fun toString(): String {
        return medium
    }
}