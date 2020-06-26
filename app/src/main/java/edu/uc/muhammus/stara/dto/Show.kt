/**
 * Holds information about a tv show
 *
 * @param name A String that holds tv show name. Ex: Community
 * @param language A String that holds tv show language. Ex: English
 * @param status A String that holds tv show status. Ex: Ended
 */

package edu.uc.muhammus.stara.dto

data class Show(var name: String, var language: String, var status: String) {
    override fun toString(): String {
        return this.name
    }
}