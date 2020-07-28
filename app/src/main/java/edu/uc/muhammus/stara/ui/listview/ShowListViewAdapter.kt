/**
 * No longer used. Kept for reference purposes. Refer to recyclerview package for what is actually used.
 * Used to format how Show List View looks like.
 * Reference: https://www.raywenderlich.com/155-android-listview-tutorial-with-kotlin
 */
package edu.uc.muhammus.stara.ui.listview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import edu.uc.muhammus.stara.R
import edu.uc.muhammus.stara.dto.ShowJSON
import kotlinx.android.synthetic.main.list_item_favorite.view.*

class ShowListViewAdapter(context: Context, private val dataSource: ArrayList<ShowJSON>) : BaseAdapter() {
    private val inflater: LayoutInflater
        = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        // Using ViewHolder to optimize performance for ListView, making it function similar to RecyclerView.
        // Without ViewHolder, ListView recreates elements instead of recycling them on scroll.
        val holder: ViewHolder

        // Check if view already exists. If it does not, create it
        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item_favorite, parent, false)

            // Use ViewHolder private class to optimize performance
            holder = ViewHolder()
            holder.thumbnailImageView = view.list_thumbnail as ImageView
            holder.titleTextView = view.list_title as TextView
            holder.subtitleTextView = view.list_subtitle as TextView
            holder.detailTextView = view.list_detail as TextView

            // Used for recycling view when it already exists.
            view.tag = holder
        }
        // View does already exist, recycle it instead of recreating it unnecessarily.
        else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val titleTextView = holder.titleTextView
        val subtitleTextView = holder.subtitleTextView
        val detailTextView = holder.detailTextView
        val thumbnailImageView = holder.thumbnailImageView

        val showJSON = getItem(position) as ShowJSON
        var showName = showJSON.show.name
        val showLanguage = showJSON.show.language ?: "Language Unknown"
        var showStatus = "Status: " + showJSON.show.status

        // Truncate names to keep UI clean
        if (showName.length > 32) {
            showName = showName.substring(0, 32).trim() + "..."
        }

        // If showStatus is "Status: ", we don't know the status
        if (showStatus == "Status: ") {
            showStatus = "Status: Unknown"
        }

        titleTextView.text = showName
        subtitleTextView.text = showStatus
        detailTextView.text = showLanguage

        // If API gave image URL, display that image
        if (showJSON.show.image != null && showJSON.show.image?.medium != null) {
            // Need to encrypt image URL. API returns http but supports https, Android only allows https by default.
            val encryptedImageURL = showJSON.show.image?.medium!!.replace("http", "https")

            // Using Picasso image library to load thumbnail asynchronously - https://square.github.io/picasso/
            // Picasso.get().isLoggingEnabled = true // Used for debugging Picasso
            Picasso.get().load(encryptedImageURL).placeholder(R.mipmap.ic_launcher_round).into(thumbnailImageView)
        }
        // Else display a placeholder indicating no image
        else {
            thumbnailImageView.setImageResource(android.R.drawable.ic_delete)
        }

        return view
    }

    // Same components as used in getView()
    private class ViewHolder {
        lateinit var titleTextView: TextView
        lateinit var subtitleTextView: TextView
        lateinit var detailTextView: TextView
        lateinit var thumbnailImageView: ImageView
    }
}