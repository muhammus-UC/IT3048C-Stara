/**
 * Holds information about an actor/actress
 *
 * @param name - A String that holds actor name. Ex: Joel McHale
 * @param country - A String that holds actor's country of birth. Ex: Italy NULLABLE
 * @param gender - A String that holds actor's gender. Ex: Male
 */

package edu.uc.muhammus.stara.dto

import java.util.*

data class Actor(var name: String, var country: String?, var gender: String) {
    override fun toString(): String {
        return this.name
    }
}