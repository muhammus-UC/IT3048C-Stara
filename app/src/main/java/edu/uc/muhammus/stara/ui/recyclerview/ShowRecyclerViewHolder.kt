/**
 * RecyclerView Holder for ShowJSON data.
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
import edu.uc.muhammus.stara.dto.Favorite
import edu.uc.muhammus.stara.dto.Show
import edu.uc.muhammus.stara.dto.ShowJSON

class ShowRecyclerViewHolder(itemView: View, private val myActivity: MainActivity) : RecyclerView.ViewHolder(itemView) {
    private val fileName = "ShowRecyclerViewHolder.kt"

    private val thumbnailImageView: ImageView = itemView.findViewById(R.id.list_thumbnail)
    private val titleTextView: TextView = itemView.findViewById(R.id.list_title)
    private val subtitleTextView: TextView = itemView.findViewById(R.id.list_subtitle)
    private val detailTextView: TextView = itemView.findViewById(R.id.list_detail)

    private val btnFavorite: ImageButton = itemView.findViewById(R.id.btnFavorite)
    private var alreadyFavorite: Boolean = false

    private val TRUNCATE_LENGTH = 29

    /**
     * This function will get called once for each item in the collection that we want to show in our recycler view.
     * Paint a single row of the recycler view with this showJSON data class.
     */
    internal fun updateShowJSON(showJSON: ShowJSON) {
        var showName = showJSON.show.name
        val showLanguage = showJSON.show.language ?: "Language Unknown"
        var showStatus = "Status: " + showJSON.show.status

        // Truncate names to keep UI clean
        if (showName.length > TRUNCATE_LENGTH) {
            showName = showName.substring(0, TRUNCATE_LENGTH).trim() + "..."
        }

        // If showStatus is "Status: ", we don't know the status
        if (showStatus == "Status: ") {
            showStatus = "Status: Unknown"
        }

        titleTextView.text = showName
        subtitleTextView.text = showStatus
        detailTextView.text = showLanguage

        // If API gave image URL, display that image
        if (showJSON.show.image?.medium != null) {
            // Need to encrypt image URL. API returns http but supports https, Android only allows https by default.
            val encryptedImageURL = showJSON.show.image?.medium!!.replace("http://", "https://")

            // Using Picasso image library to load thumbnail asynchronously - https://square.github.io/picasso/
            // Picasso.get().isLoggingEnabled = true // Used for debugging Picasso
            Picasso.get().load(encryptedImageURL).placeholder(R.mipmap.ic_launcher_round).into(thumbnailImageView)
        }
        // Else display a placeholder indicating no image
        else {
            thumbnailImageView.setImageResource(android.R.drawable.ic_delete)
        }

        // Add or Remove Show from Favorites when btnFavorite clicked.
        btnFavorite.setOnClickListener { addRemoveFavoriteShow(showJSON.show) }

        // Open TVMaze URL for Show when thumbnail clicked.
        thumbnailImageView.setOnClickListener { openLink(showJSON.show.url) }
    }

    /**
     * Opens TVMaze link for Show in external browser
     */
    private fun openLink(url: String) {
        val openLinkIntent = Intent(Intent.ACTION_VIEW)
        openLinkIntent.data = Uri.parse(url)
        myActivity.startActivity(openLinkIntent)
    }

    /**
     * Add or Remove a show from logged in User's favorites.
     * Add if not already in favorites, otherwise Remove.
     */
    private fun addRemoveFavoriteShow(favoriteShow: Show) {
        Log.d(fileName, "Favorite imageButton clicked.")

        // Default value of var email is "email".
        // If this is not changed, user has not logged in.
        if (myActivity.email == "email") {
            myActivity.showToast("You can not add to favorites without logging in.", true)
            myActivity.logon()
            return
        }

        // Convert provided Show into a Favorite object.
        val favorite = Favorite().apply {
            id = "Show_" + favoriteShow.id
            name = favoriteShow.name
            url = favoriteShow.url
            subtitle = "Status: " + favoriteShow.status
            detail = favoriteShow.language ?: "Language Unknown"
            if (favoriteShow.image?.medium != null) {
                image = favoriteShow.image?.medium
            }
        }

        // Add to Favorites if not already in favorites, otherwise Remove.
        if (!alreadyFavorite) {
            myActivity.viewModel.addFavorite(favorite, myActivity.email)
            alreadyFavorite = true
            btnFavorite.setImageResource(android.R.drawable.star_big_on)
            myActivity.showToast(favorite.name + " added to favorites.")
        } else {
            myActivity.viewModel.removeFavorite(favorite, myActivity.email)
            alreadyFavorite = false
            btnFavorite.setImageResource(android.R.drawable.star_big_off)
            myActivity.showToast(favorite.name + " removed from favorites.")
        }
    }
}
