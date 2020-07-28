/**
 * Fragment for user to search for actor or show data via TVMaze API.
 */
package edu.uc.muhammus.stara.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uc.muhammus.stara.MainActivity
import edu.uc.muhammus.stara.R
import edu.uc.muhammus.stara.ui.recyclerview.ActorsRecyclerViewAdapter
import edu.uc.muhammus.stara.ui.recyclerview.ShowsRecyclerViewAdapter
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : StaraFragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var myActivity: MainActivity

    // Title of Fragment currently shown. Used to set title when Fragment is shown from hide state.
    private var fragmentTitle = "Stara - Search"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Updated deprecated code: https://stackoverflow.com/q/57534730
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.initializeFirebase()

        myActivity = (activity as MainActivity)

        // Configure recycler view options with sane defaults
        searchRecyclerView.hasFixedSize()
        searchRecyclerView.layoutManager = LinearLayoutManager(context)
        searchRecyclerView.itemAnimator = DefaultItemAnimator()

        // Populate SearchRecyclerView when user searches for show or actor
        btnSearch.setOnClickListener { populateSearchRecyclerView() }

        // Change hint text based on which radio button is clicked by adding a listener
        changeSearchHintText()
    }

    /**
     * Populate searchRecyclerView with search results for Actor or Show as requested via TVMazeAPI.
     */
    private fun populateSearchRecyclerView() {
        val searchTerm = editSearch.text.toString()

        if (searchRadioShow.isChecked) {
            // Remove observers since we keep adding one when button is pressed.
            viewModel.shows.removeObservers(viewLifecycleOwner)

            viewModel.fetchShows(searchTerm)

            viewModel.shows.observe(
                viewLifecycleOwner,
                Observer {
                    shows ->
                        searchRecyclerView.adapter = ShowsRecyclerViewAdapter(shows, R.layout.list_item_favorite, viewModel, myActivity)
                }
            )
        } else if (searchRadioActor.isChecked) {
            // Remove observers since we keep adding one when button is pressed.
            viewModel.actors.removeObservers(viewLifecycleOwner)

            viewModel.fetchActors(searchTerm)

            viewModel.actors.observe(
                viewLifecycleOwner,
                Observer {
                    actors ->
                        searchRecyclerView.adapter = ActorsRecyclerViewAdapter(actors, R.layout.list_item_favorite, viewModel, myActivity)
                }
            )
        }

        hideKeyboard()
    }

    /**
     * Change hint text of editSearch depending on which radio button is selected by adding a Listener.
     */
    private fun changeSearchHintText() {
        searchRadioGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener(
                fun (_, checkedId) {
                    if (checkedId == R.id.searchRadioShow) {
                        editSearch.hint = getString(R.string.search_hint_show)
                    } else if (checkedId == R.id.searchRadioActor) {
                        editSearch.hint = getString(R.string.search_hint_actor)
                    }
                }
            )
        )
    }

    /**
     * Runs when Fragment is hidden or shown via FragmentManager.
     * Used to set proper title.
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        // If fragment is NOT hidden
        if (!hidden) {
            activity?.title = fragmentTitle
        }
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}