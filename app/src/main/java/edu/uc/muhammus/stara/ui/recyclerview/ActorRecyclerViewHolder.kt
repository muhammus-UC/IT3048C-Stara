/**
 * RecyclerView Holder for ActorJSON data.
 * Reference: https://www.youtube.com/watch?v=__gxd4IKVvk
 */
package edu.uc.muhammus.stara.ui.recyclerview

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.uc.muhammus.stara.R
import edu.uc.muhammus.stara.dto.ActorJSON

class ActorRecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val thumbnailImageView: ImageView = itemView.findViewById(R.id.list_thumbnail)
    private val titleTextView: TextView = itemView.findViewById(R.id.list_title)
    private val subtitleTextView: TextView = itemView.findViewById(R.id.list_subtitle)
    private val detailTextView: TextView = itemView.findViewById(R.id.list_detail)

    /**
     * This function will get called once for each item in the collection that we want to show in our recycler view.
     * Paint a single row of the recycler view with this actorJSON data class.
     */
    fun updateActorJSON(actorJSON: ActorJSON) {
        var actorName = actorJSON.actor.name
        val actorGender = actorJSON.actor.gender ?: "Gender Unknown"
        val actorCountry = actorJSON.actor.country?.name ?: "Country Unknown"

        // Truncate names to keep UI clean
        if (actorName.length > 32) {
            actorName = actorName.substring(0, 32).trim() + "..."
        }

        titleTextView.text = actorName
        subtitleTextView.text = actorGender
        detailTextView.text = actorCountry

        if (actorJSON.actor.image != null && actorJSON.actor.image?.medium != null) {
            // Need to encrypt image URL. API returns http but supports https, Android only allows https by default.
            val encryptedImageURL = actorJSON.actor.image?.medium!!.replace("http", "https")

            // Using Picasso image library to load thumbnail asynchronously - https://square.github.io/picasso/
            // Picasso.get().isLoggingEnabled = true // Used for debugging Picasso
            Picasso.get().load(encryptedImageURL).placeholder(R.mipmap.ic_launcher_round).into(thumbnailImageView)
        }
        else {
            thumbnailImageView.setImageResource(android.R.drawable.ic_delete)
        }

    }
}