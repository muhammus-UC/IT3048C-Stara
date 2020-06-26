/**
 * Holds information about an actor/actress
 *
 * @param name - A String that holds actor name. Ex: Joel McHale
 * @param country - A String that holds actor's country of birth. Ex: Italy NULLABLE
 * @param gender - A String that holds actor's gender. Ex: Male
 */

package edu.uc.muhammus.stara.dto

import com.google.gson.annotations.SerializedName


data class ActorJSON(var score: Double, @SerializedName("person") var actor: Actor)

data class Actor(var name: String, var country: ActorCountry?, var gender: String) {
    override fun toString(): String {
        return this.name
    }
}

data class ActorCountry (var name: String, var code: String, var timezone: String)