package edu.uc.muhammus.stara.ui.misc
// Reference: https://www.raywenderlich.com/155-android-listview-tutorial-with-kotlin

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import edu.uc.muhammus.stara.R
import edu.uc.muhammus.stara.dto.ShowJSON
import kotlinx.android.synthetic.main.list_item_show.view.*
import org.w3c.dom.Text

class ShowListViewAdapter(private val context: Context, private val dataSource: ArrayList<ShowJSON>) : BaseAdapter() {
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
        val rowView = inflater.inflate(R.layout.list_item_show, parent, false)

        val titleTextView = rowView.list_title as TextView
        val subtitleTextView = rowView.list_subtitle as TextView
        val detailTextView = rowView.list_detail as TextView
        val thumbnailImageView = rowView.list_thumbnail as ImageView

        val showJSON = getItem(position) as ShowJSON

        titleTextView.text = showJSON.show.name
        subtitleTextView.text = showJSON.show.status
        detailTextView.text = showJSON.show.language

        if (showJSON.show.image != null) {
            var encryptedImageURL = showJSON.show.image?.medium!!.replace("http", "https")

            // Picasso.get().isLoggingEnabled = true // Used for debugging Picasso
            Picasso.get().load(encryptedImageURL).placeholder(R.mipmap.ic_launcher_round).into(thumbnailImageView)
        }

        return rowView
    }
}

