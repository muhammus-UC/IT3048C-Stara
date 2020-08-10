/**
 * RecyclerView Holder for Favorite data.
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

class FavoriteRecyclerViewHolder(itemView: View, private val myActivity: MainActivity) : RecyclerView.ViewHolder(itemView) {
    private val fileName = "FavoriteRecyclerViewHolder.kt"

    private val thumbnailImageView: ImageView = itemView.findViewById(R.id.list_thumbnail)
    private val titleTextView: TextView = itemView.findViewById(R.id.list_title)
    private val subtitleTextView: TextView = itemView.findViewById(R.id.list_subtitle)
    private val detailTextView: TextView = itemView.findViewById(R.id.list_detail)

    private val btnFavorite: ImageButton = itemView.findViewById(R.id.btnFavorite)
    private var alreadyFavorite = true

    private val TRUNCATE_LENGTH = 29

    /**
     * This function will get called once for each item in the collection that we want to show in our recycler view.
     * Paint a single row of the recycler view with this Favorite data class.
     */
    internal fun updateFavorite(favorite: Favorite) {
        var favoriteName = favorite.name
        val favoriteSubtitle = favorite.subtitle
        val favoriteDetail = favorite.detail
        val favoriteImage = favorite.image

        // Truncate names to keep UI clean
        if (favoriteName.length > TRUNCATE_LENGTH) {
            favoriteName = favoriteName.substring(0, TRUNCATE_LENGTH).trim() + "..."
        }

        titleTextView.text = favoriteName
        subtitleTextView.text = favoriteSubtitle
        detailTextView.text = favoriteDetail

        // If API gave image URL, display that image
        if (favoriteImage != null) {
            // Need to encrypt image URL. API returns http but supports https, Android only allows https by default.
            val encryptedImageURL = favoriteImage.replace("http://", "https://")

            // Using Picasso image library to load thumbnail asynchronously - https://square.github.io/picasso/
            // Picasso.get().isLoggingEnabled = true // Used for debugging Picasso
            Picasso.get().load(encryptedImageURL).placeholder(R.mipmap.ic_launcher_round).into(thumbnailImageView)
        }
        // Else display a placeholder indicating no image
        else {
            thumbnailImageView.setImageResource(android.R.drawable.ic_delete)
        }

        // Since item is already a favorite, star should be on by default
        btnFavorite.setImageResource(android.R.drawable.star_big_on)

        // Add or Remove Favorite from Favorites when btnFavorite clicked.
        btnFavorite.setOnClickListener { addRemoveFavorite(favorite) }

        // Open TVMaze URL for Favorite when thumbnail clicked.
        thumbnailImageView.setOnClickListener { openLink(favorite.url) }
    }

    /**
     * Opens TVMaze link for Actor in external browser.
     */
    private fun openLink(url: String) {
        val openLinkIntent = Intent(Intent.ACTION_VIEW)
        openLinkIntent.data = Uri.parse(url)
        myActivity.startActivity(openLinkIntent)
    }

    /**
     * Add or remove a favorite from logged in User's favorites.
     * Add if not already in favorites, otherwise Remove.
     */
    private fun addRemoveFavorite(favorite: Favorite) {
        Log.d(fileName, "Favorite imageButton clicked.")

        // Default value of var email is "email".
        // If this is not changed, user has not logged in.
        if (myActivity.email == "email") {
            myActivity.logon()
            return
        }

        // Add to Favorites if not already in favorites, otherwise Remove.
        if (!alreadyFavorite) {
            myActivity.viewModel.addFavorite(favorite, myActivity.email)
            alreadyFavorite = true
            btnFavorite.setImageResource(android.R.drawable.star_big_on)
            myActivity.showToast(favorite.name + myActivity.getString(R.string.toast_favorite_added))
        } else {
            myActivity.viewModel.removeFavorite(favorite, myActivity.email)
            alreadyFavorite = false
            btnFavorite.setImageResource(android.R.drawable.star_big_off)
            myActivity.showToast(favorite.name + myActivity.getString(R.string.toast_favorite_removed))
        }
    }
}
