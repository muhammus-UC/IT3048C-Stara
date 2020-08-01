/**
 * Fragment that displays currently logged in user's favorite shows.
 * In MainViewModel, Gets shows from Firebase Firestore.
 * In MainActivity, User is logged in via Firebase Authentication.
 */
package edu.uc.muhammus.stara.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uc.muhammus.stara.MainActivity
import edu.uc.muhammus.stara.R
import edu.uc.muhammus.stara.ui.recyclerview.FavoritesRecyclerViewAdapter
import kotlinx.android.synthetic.main.favorites_fragment.*

class FavoritesFragment : StaraFragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var myActivity: MainActivity
    private var fragmentTitle = "Stara - Favorites"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorites_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Updated deprecated code: https://stackoverflow.com/q/57534730
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.initializeFirebase()

        myActivity = (activity as MainActivity)

        // Configure recycler view options with sane defaults
        favoritesRecyclerView.hasFixedSize()
        favoritesRecyclerView.layoutManager = LinearLayoutManager(context)
        favoritesRecyclerView.itemAnimator = DefaultItemAnimator()
    }

    // Populate with Favorites for logged in user
    internal fun populateFavorites() {
        viewModel.listenToFavorites(myActivity.email)

        viewModel.favorites.observe(
            viewLifecycleOwner,
            Observer {
                favorites ->
                    favoritesRecyclerView.adapter = FavoritesRecyclerViewAdapter(favorites, R.layout.list_item_favorite, viewModel, myActivity)
            }
        )
    }

    /**
     * Uses name from Firebase Authentication.
     * Used in Main Activity after user logs in.
     */
    internal fun setDisplayName(displayName: String?) {
        txtFavoritesSubtitle.text = displayName
    }

    /**
     * Runs when Fragment is hidden or shown via FragmentManager.
     * Used to update title of activity to correspond to running Fragment.
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        // If fragment is NOT hidden
        if (!hidden) {
            activity?.title = fragmentTitle
        }
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }
}