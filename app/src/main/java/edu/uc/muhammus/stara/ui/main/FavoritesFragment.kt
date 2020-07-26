package edu.uc.muhammus.stara.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uc.muhammus.stara.MainActivity
import edu.uc.muhammus.stara.R
import edu.uc.muhammus.stara.ui.recyclerview.FavoriteRecyclerViewAdapter
import edu.uc.muhammus.stara.ui.recyclerview.ShowsRecyclerViewAdapter
import kotlinx.android.synthetic.main.favorites_fragment.*
import kotlinx.android.synthetic.main.search_fragment.*

class FavoritesFragment : StaraFragment() {

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var myActivity: MainActivity
    private var fragmentTitle = "Stara - Favorites"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorites_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Updated deprecated code: https://stackoverflow.com/q/57534730
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        myActivity = (activity as MainActivity)

        // Configure recycler view options with sane defaults
        favoritesRecyclerView.hasFixedSize()
        favoritesRecyclerView.layoutManager = LinearLayoutManager(context)
        favoritesRecyclerView.itemAnimator = DefaultItemAnimator()

    }

    fun populateFavorites() {
        viewModel.listenToFavorites(myActivity.email)

        viewModel.favorites.observe(viewLifecycleOwner, Observer{
                favorites -> favoritesRecyclerView.adapter = FavoriteRecyclerViewAdapter(favorites, R.layout.list_item_favorite, viewModel, myActivity)
        })
    }

    fun setDisplayName(displayName : String?)
    {
        txtFavoritesSubtitle.text = displayName
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        // If fragment is NOT hidden
        if (!hidden) {
            activity?.title = fragmentTitle
        }
    }

}