package edu.uc.muhammus.stara.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
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

        // Configure recycler view options with sane defaults
        favoritesRecyclerView.hasFixedSize()
        favoritesRecyclerView.layoutManager = LinearLayoutManager(context)
        favoritesRecyclerView.itemAnimator = DefaultItemAnimator()

        btnFavorites.setOnClickListener{
            viewModel.listenToFavorites()

            viewModel.favorites.observe(viewLifecycleOwner, Observer{
                    favorites -> favoritesRecyclerView.adapter = FavoriteRecyclerViewAdapter(favorites, R.layout.list_item, viewModel)
                println("button finished")
            })
        }

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        // If fragment is NOT hidden
        if (!hidden) {
            activity?.title = fragmentTitle
        }
    }

}