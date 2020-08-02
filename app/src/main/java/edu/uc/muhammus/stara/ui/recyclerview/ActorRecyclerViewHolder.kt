/**
 * RecyclerView Holder for ActorJSON data.
 * Reference: https://www.youtube.com/watch?v=__gxd4IKVvk
 */
package edu.uc.muhammus.stara.ui.recyclerview

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.uc.muhammus.stara.MainActivity
import edu.uc.muhammus.stara.R
import edu.uc.muhammus.stara.dto.Actor
import edu.uc.muhammus.stara.dto.ActorJSON
import edu.uc.muhammus.stara.dto.Favorite
import edu.uc.muhammus.stara.ui.main.MainViewModel

class ActorRecyclerViewHolder(itemView: View, val viewModel: MainViewModel, private val myActivity: MainActivity) : RecyclerView.ViewHolder(itemView) {
    private val fileName = "ActorRecyclerViewHolder.kt"

    private val thumbnailImageView: ImageView = itemView.findViewById(R.id.list_thumbnail)
    private val titleTextView: TextView = itemView.findViewById(R.id.list_title)
    private val subtitleTextView: TextView = itemView.findViewById(R.id.list_subtitle)
    private val detailTextView: TextView = itemView.findViewById(R.id.list_detail)

    private val btnFavorite: ImageButton = itemView.findViewById(R.id.btnFavorite)
    private var alreadyFavorite: Boolean = false

    private val TRUNCATE_LENGTH = 29

    /**
     * This function will get called once for each item in the collection that we want to show in our recycler view.
     * Paint a single row of the recycler view with this actorJSON data class.
     */
    internal fun updateActorJSON(actorJSON: ActorJSON) {
        var actorName = actorJSON.actor.name
        val actorGender = actorJSON.actor.gender ?: "Gender Unknown"
        val actorCountry = actorJSON.actor.country?.name ?: "Country Unknown"

        // Truncate names to keep UI clean
        if (actorName.length > TRUNCATE_LENGTH) {
            actorName = actorName.substring(0, TRUNCATE_LENGTH).trim() + "..."
        }

        titleTextView.text = actorName
        subtitleTextView.text = actorGender
        detailTextView.text = actorCountry

        // If API gave image URL, display that image
        if (actorJSON.actor.image?.medium != null) {
            // Need to encrypt image URL. API returns http but supports https, Android only allows https by default.
            val encryptedImageURL = actorJSON.actor.image?.medium!!.replace("http://", "https://")

            // Using Picasso image library to load thumbnail asynchronously - https://square.github.io/picasso/
            // Picasso.get().isLoggingEnabled = true // Used for debugging Picasso
            Picasso.get().load(encryptedImageURL).placeholder(R.mipmap.ic_launcher_round).into(thumbnailImageView)
        }
        // Else display a placeholder indicating no image
        else {
            thumbnailImageView.setImageResource(android.R.drawable.ic_delete)
        }

        // Add or Remove Actor from Favorites when btnFavorite clicked.
        btnFavorite.setOnClickListener { addRemoveFavoriteActor(actorJSON.actor) }

        // Open TVMaze URL for Actor when thumbnail clicked.
        thumbnailImageView.setOnClickListener { openLink(actorJSON.actor.url) }
    }

    /**
     * Opens link for Actor in external browser
     */
    private fun openLink(url: String) {
        val openLinkIntent = Intent(Intent.ACTION_VIEW)
        openLinkIntent.data = Uri.parse(url)
        myActivity.startActivity(openLinkIntent)
    }

    /**
     * Add or Remove an actor from logged in User's favorites.
     * Add if not already in favorites, otherwise Remove.
     */
    private fun addRemoveFavoriteActor(favoriteActor: Actor) {
        Log.d(fileName, "Favorite imageButton clicked.")

        // Default value of var email is "email".
        // If this is not changed, user has not logged in.
        if (myActivity.email == "email") {
            myActivity.showToast("You can not add to favorites without logging in.", true)
            myActivity.logon()
            return
        }

        // Convert provided Actor into a Favorite object
        val favorite = Favorite().apply {
            id = "Actor_" + favoriteActor.id
            name = favoriteActor.name
            url = favoriteActor.url
            subtitle = favoriteActor.gender ?: "Gender Unknown"
            detail = favoriteActor.country?.name ?: "Country Unknown"
            if (favoriteActor.image?.medium != null) {
                image = favoriteActor.image?.medium
            }
        }

        // Add to Favorites if not already in favorites, otherwise Remove.
        if (!alreadyFavorite) {
            viewModel.addFavorite(favorite, myActivity.email)
            alreadyFavorite = true
            btnFavorite.setImageResource(android.R.drawable.star_big_on)
            myActivity.showToast(favorite.name + " added to favorites.")
        } else {
            viewModel.removeFavorite(favorite, myActivity.email)
            alreadyFavorite = false
            btnFavorite.setImageResource(android.R.drawable.star_big_off)
            myActivity.showToast(favorite.name + " removed from favorites.")
        }
    }
}
