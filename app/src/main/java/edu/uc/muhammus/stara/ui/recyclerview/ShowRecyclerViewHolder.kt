/**
 * RecyclerView Holder for ShowJSON data.
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
import edu.uc.muhammus.stara.dto.Show
import edu.uc.muhammus.stara.dto.ShowJSON
import edu.uc.muhammus.stara.ui.main.MainViewModel

class ShowRecyclerViewHolder(itemView: View, val viewModel: MainViewModel): RecyclerView.ViewHolder(itemView) {
    private val thumbnailImageView: ImageView = itemView.findViewById(R.id.list_thumbnail)
    private val titleTextView: TextView = itemView.findViewById(R.id.list_title)
    private val subtitleTextView: TextView = itemView.findViewById(R.id.list_subtitle)
    private val detailTextView: TextView = itemView.findViewById(R.id.list_detail)
    private val btnFavorite: ImageButton = itemView.findViewById(R.id.btnFavorite)

    /**
     * This function will get called once for each item in the collection that we want to show in our recycler view.
     * Paint a single row of the recycler view with this showJSON data class.
     */
    fun updateShowJSON(showJSON: ShowJSON) {
        var showName = showJSON.show.name
        var showLanguage = showJSON.show.language ?: "Language Unknown"
        var showStatus = "Status: " + showJSON.show.status

        // Truncate names to keep UI clean
        if (showName.length > 32) {
            showName = showName.substring(0, 32).trim() + "..."
        }

        // If showStatus is "Status: ", we don't know the status
        if (showStatus.equals("Status: ")) {
            showStatus = "Status: Unknown"
        }

        titleTextView.text = showName
        subtitleTextView.text = showStatus
        detailTextView.text = showLanguage

        // If API gave image URL, display that image
        if (showJSON.show.image != null && showJSON.show.image?.medium != null) {
            // Need to encrypt image URL. API returns http but supports https, Android only allows https by default.
            var encryptedImageURL = showJSON.show.image?.medium!!.replace("http", "https")

            // Using Picasso image library to load thumbnail asynchronously - https://square.github.io/picasso/
            // Picasso.get().isLoggingEnabled = true // Used for debugging Picasso
            Picasso.get().load(encryptedImageURL).placeholder(R.mipmap.ic_launcher_round).into(thumbnailImageView)
        }
        // Else display a placeholder indicating no image
        else {
            thumbnailImageView.setImageResource(android.R.drawable.ic_delete)
        }

        btnFavorite.setOnClickListener{addShowToFavorites(showJSON.show)}
    }

    private fun addShowToFavorites(favoriteShow: Show) {
        println("favorite clicked")

        var favorite = Favorite().apply {
            id = "Show_" + favoriteShow.id
            name = favoriteShow.name
            subtitle = favoriteShow.status
            detail = favoriteShow.language
            if (favoriteShow.image != null && favoriteShow.image?.medium != null)
            {
                image = favoriteShow.image?.medium
            }
        }


        viewModel.addFavorite(favorite)
    }
}