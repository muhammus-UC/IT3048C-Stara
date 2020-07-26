/**
 * RecyclerView Holder for ActorJSON data.
 * Reference: https://www.youtube.com/watch?v=__gxd4IKVvk
 */
package edu.uc.muhammus.stara.ui.recyclerview

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

class ActorRecyclerViewHolder(itemView: View, val viewModel: MainViewModel, val myActivity: MainActivity): RecyclerView.ViewHolder(itemView) {
    private val thumbnailImageView: ImageView = itemView.findViewById(R.id.list_thumbnail)
    private val titleTextView: TextView = itemView.findViewById(R.id.list_title)
    private val subtitleTextView: TextView = itemView.findViewById(R.id.list_subtitle)
    private val detailTextView: TextView = itemView.findViewById(R.id.list_detail)

    private val btnFavorite: ImageButton = itemView.findViewById(R.id.btnFavorite)
    private var alreadyFavorite: Boolean = false

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

        // If API gave image URL, display that image
        if (actorJSON.actor.image != null && actorJSON.actor.image?.medium != null) {
            // Need to encrypt image URL. API returns http but supports https, Android only allows https by default.
            val encryptedImageURL = actorJSON.actor.image?.medium!!.replace("http", "https")

            // Using Picasso image library to load thumbnail asynchronously - https://square.github.io/picasso/
            // Picasso.get().isLoggingEnabled = true // Used for debugging Picasso
            Picasso.get().load(encryptedImageURL).placeholder(R.mipmap.ic_launcher_round).into(thumbnailImageView)
        }
        // Else display a placeholder indicating no image
        else {
            thumbnailImageView.setImageResource(android.R.drawable.ic_delete)
        }


        btnFavorite.setOnClickListener{addRemoveFavoriteActor(actorJSON.actor)}
    }

    private fun addRemoveFavoriteActor(favoriteActor: Actor) {
        println("favorite clicked")

        // Default email is "email". This mean user has not logged in.
        if (myActivity.email == "email")
        {
            myActivity.showToast("You can not add to favorites without logging in.", true)
            myActivity.logon()
            return
        }

        var favorite = Favorite().apply {
            id = "Actor_" + favoriteActor.id
            name = favoriteActor.name
            subtitle = favoriteActor.gender ?: "Gender Unknown"
            detail = favoriteActor.country?.name ?: "Country Unknown"
            if (favoriteActor.image != null && favoriteActor.image?.medium != null)
            {
                image = favoriteActor.image?.medium
            }
        }

        if (!alreadyFavorite)
        {
            viewModel.addFavorite(favorite, myActivity.email)
            alreadyFavorite = true
            btnFavorite.setImageResource(android.R.drawable.star_big_on)
        }
        else
        {
            viewModel.removeFavorite(favorite, myActivity.email)
            alreadyFavorite = false
            btnFavorite.setImageResource(android.R.drawable.star_big_off)
        }
    }
}