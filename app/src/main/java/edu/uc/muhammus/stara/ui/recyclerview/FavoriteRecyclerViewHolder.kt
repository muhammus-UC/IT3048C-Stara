/**
 * RecyclerView Holder for Favorite data.
 * Reference: https://www.youtube.com/watch?v=__gxd4IKVvk
 */
package edu.uc.muhammus.stara.ui.recyclerview

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.uc.muhammus.stara.R
import edu.uc.muhammus.stara.dto.Favorite
import edu.uc.muhammus.stara.ui.main.MainViewModel

class FavoriteRecyclerViewHolder(itemView: View, val viewModel: MainViewModel): RecyclerView.ViewHolder(itemView) {
    private val thumbnailImageView: ImageView = itemView.findViewById(R.id.list_thumbnail)
    private val titleTextView: TextView = itemView.findViewById(R.id.list_title)
    private val subtitleTextView: TextView = itemView.findViewById(R.id.list_subtitle)
    private val detailTextView: TextView = itemView.findViewById(R.id.list_detail)
    //private val btnFavorite: ImageButton = itemView.findViewById(R.id.btnFavorite)

    /**
     * This function will get called once for each item in the collection that we want to show in our recycler view.
     * Paint a single row of the recycler view with this Favorite data class.
     */
    fun updateFavorite(favorite: Favorite) {
        var favoriteName = favorite.name
        var favoriteSubtitle = favorite.subtitle
        var favoriteDetail = favorite.detail
        var favoriteImage = favorite.image

        // Truncate names to keep UI clean
        if (favoriteName.length > 32) {
            favoriteName = favoriteName.substring(0, 32).trim() + "..."
        }

        titleTextView.text = favoriteName
        subtitleTextView.text = favoriteSubtitle
        detailTextView.text = favoriteDetail

        // If API gave image URL, display that image
        if (favoriteImage != null) {
            // Need to encrypt image URL. API returns http but supports https, Android only allows https by default.
            var encryptedImageURL = favoriteImage.replace("http", "https")

            // Using Picasso image library to load thumbnail asynchronously - https://square.github.io/picasso/
            // Picasso.get().isLoggingEnabled = true // Used for debugging Picasso
            Picasso.get().load(encryptedImageURL).placeholder(R.mipmap.ic_launcher_round).into(thumbnailImageView)
        }
        // Else display a placeholder indicating no image
        else {
            thumbnailImageView.setImageResource(android.R.drawable.ic_delete)
        }
    }
}