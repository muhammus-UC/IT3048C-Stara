/**
 * RecyclerView Holder for ScheduleJSON data.
 * Reference: https://www.youtube.com/watch?v=__gxd4IKVvk
 */
package edu.uc.muhammus.stara.ui.recyclerview

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.uc.muhammus.stara.R
import edu.uc.muhammus.stara.dto.ScheduleJSON
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ScheduleRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val thumbnailImageView: ImageView = itemView.findViewById(R.id.list_thumbnail)
    private val titleTextView: TextView = itemView.findViewById(R.id.list_title)
    private val subtitleTextView: TextView = itemView.findViewById(R.id.list_subtitle)
    private val detailTextView: TextView = itemView.findViewById(R.id.list_detail)

    /**
     * This function will get called once for each item in the collection that we want to show in our recycler view.
     * Paint a single row of the recycler view with this scheduleJSON data class.
     */
    internal fun updateScheduleJSON(scheduleJSON: ScheduleJSON) {
        var showName = scheduleJSON.show.name
        var episodeName = scheduleJSON.episodeName
        var airtime = scheduleJSON.airtime

        // Truncate names to keep UI clean
        if (showName.length > 32) {
            showName = showName.substring(0, 32).trim() + "..."
        }
        if (episodeName.length > 24) {
            episodeName = episodeName.substring(0, 24).trim() + "..."
        }

        episodeName = "Episode: $episodeName"

        // Convert 24 hour time to 12 hour
        // Reference: https://stackoverflow.com/a/49326758
        airtime = LocalTime.parse(airtime).format(DateTimeFormatter.ofPattern("hh:mm a"))

        titleTextView.text = showName
        subtitleTextView.text = episodeName
        detailTextView.text = airtime

        // If API gave image URL, display that image
        if (scheduleJSON.show.image != null && scheduleJSON.show.image?.medium != null) {
            // Need to encrypt image URL. API returns http but supports https, Android only allows https by default.
            val encryptedImageURL = scheduleJSON.show.image?.medium!!.replace("http://", "https://")

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
