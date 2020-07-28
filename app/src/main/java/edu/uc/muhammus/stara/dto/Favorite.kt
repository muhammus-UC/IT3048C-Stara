/**
 * Holds information about a favorite Actor or Show to add to favorites.
 *
 * @param Favorite.id - A string used to hold id of Actor or Show.
 * Ex for Show: "Show_318"
 * Ex for Actor: "Actor_11615"
 *
 * @param Favorite.name - A string used to hold name of Actor or Show.
 *
 * @param Favorite.subtitle - A string used to hold what will be displayed in subtitleTextView defined in list_item.xml
 * For Show it is Show.status. Ex: "Ended"
 * For Actor it is Actor.gender. Ex: "Male"
 *
 * @param Favorite.detail - A string used to hold what is shown in detailTextView defined in list_item.xml
 * For Show it is Show.language. Ex: "English"
 * For Actor it is Actor.country?.name. Ex: "Ireland"
 *
 * @param Favorite.image - A string used to hold URL to image of Actor or Show.
 */
package edu.uc.muhammus.stara.dto

data class Favorite(
    var id: String = "id not defined",
    var name: String = "Name not defined",
    var subtitle: String? = "Subtitle not defined",
    var detail: String? = "Detail not defined",
    var image: String? = null
) {
    override fun toString(): String {
        return name
    }
}