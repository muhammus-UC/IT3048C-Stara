package edu.uc.muhammus.stara.ui.main


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.uc.muhammus.stara.R
import edu.uc.muhammus.stara.dto.ActorJSON
import edu.uc.muhammus.stara.dto.Show
import edu.uc.muhammus.stara.dto.ShowJSON
import edu.uc.muhammus.stara.ui.misc.ActorListViewAdapter
import edu.uc.muhammus.stara.ui.misc.ShowListViewAdapter
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Updated deprecated code: https://stackoverflow.com/q/57534730
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // AutocompleteTextView Code
        /*
        viewModel.shows.observe(viewLifecycleOwner, Observer{
            shows -> actSearch.setAdapter(ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, shows))
        })
        viewModel.fetchShows("Community")
        */

        btnSearch.setOnClickListener{populateSearchListView()}
    }

    // When fragment is hidden or shown
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        // If fragment is NOT hidden
        if(!hidden) {
            activity?.title = "Stara - Search"
        }
    }

    private fun populateSearchListView() {
        var searchTerm = actSearch.text.toString()

        if (searchRadioShow.isChecked)
        {
            viewModel.fetchShows(searchTerm)

            var allShowsJSON = ArrayList<ShowJSON>()
            viewModel.shows.observeForever {
                it.forEach{
                    allShowsJSON.add(it)
                }
            }

            viewModel.shows.observe(viewLifecycleOwner, Observer{
                searchListView.adapter = ShowListViewAdapter(requireContext(), allShowsJSON)
            })
        }
        else if (searchRadioActor.isChecked)
        {
            viewModel.fetchActors(searchTerm)

            var allActorsJSON = ArrayList<ActorJSON>()
            viewModel.actors.observeForever {
                it.forEach{
                    allActorsJSON.add(it)
                }
            }

            viewModel.actors.observe(viewLifecycleOwner, Observer {
                searchListView.adapter = ActorListViewAdapter(requireContext(), allActorsJSON)
            })
        }
    }

    // Used for debugging, in case API is not working
    private val listOfShows = listOf(
        Show("Community", "English", "Ended"),
        Show("Still Game", "Japanese", "Ended"),
        Show("Bobs Burgers", "Korean", "Ended"),
        Show("Archer", "Arabic", "Ended"),
        Show("HIMYM", "Urdu", "Ended"),
        Show("My name is Earl", "French", "Ended"),
        Show("Psych", "Hindi", "Ended"),
        Show("Monk", "Punjabi", "Ended"),
        Show("The Mentalist", "Spanish", "Ended"),
        Show("White Collar", "Chinese", "Ended")
    )

    companion object {
        fun newInstance() = SearchFragment()
    }
}