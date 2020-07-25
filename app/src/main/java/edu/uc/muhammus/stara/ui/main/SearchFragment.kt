/**
 * Allows user to search for actor or show data via TVMaze API.
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
import edu.uc.muhammus.stara.R
import edu.uc.muhammus.stara.dto.ShowJSON
import edu.uc.muhammus.stara.ui.adapter.ShowsRecyclerViewAdapter
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : StaraFragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var showsRecyclerViewAdapter: ShowsRecyclerViewAdapter
    private var fragmentTitle = "Stara - Search"
    private var _shows = ArrayList<ShowJSON>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Updated deprecated code: https://stackoverflow.com/q/57534730
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Configure recycler view options with sane defaults
        searchRecyclerView.hasFixedSize()
        searchRecyclerView.layoutManager = LinearLayoutManager(context)
        searchRecyclerView.itemAnimator = DefaultItemAnimator()

        // Configure recycler view adapters
        showsRecyclerViewAdapter = ShowsRecyclerViewAdapter(_shows, R.layout.list_item_show)

        // Set default recycler view adapter
        searchRecyclerView.adapter = showsRecyclerViewAdapter

        viewModel.shows.observe(viewLifecycleOwner, Observer {
            shows ->
                // Remove all shows in there
                _shows.removeAll(_shows)
                // Update with the new shows that we have observed
                _shows.addAll(shows)
                // Tell recycler view to update
                searchRecyclerView.adapter = showsRecyclerViewAdapter
        })
        btnSearch.setOnClickListener{populateSearchListView()}
    }

    private fun populateSearchListView() {
        var searchTerm = editSearch.text.toString()

        if (searchRadioShow.isChecked)
        {
            viewModel.fetchShows(searchTerm)

            viewModel.shows.observe(viewLifecycleOwner, Observer{
                shows -> searchRecyclerView.adapter = ShowsRecyclerViewAdapter(shows, R.layout.list_item_show)
            })

            showsRecyclerViewAdapter.notifyDataSetChanged()
        }
        else if (searchRadioActor.isChecked)
        {
            viewModel.fetchActors(searchTerm)

            viewModel.actors.observe(viewLifecycleOwner, Observer {
                //actors -> searchListView.adapter = ActorListViewAdapter(requireContext(), actors)
            })
        }

        hideKeyboard()
    }

    // When fragment is hidden or shown
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        // If fragment is NOT hidden
        if(!hidden) {
            activity?.title = fragmentTitle
        }
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}