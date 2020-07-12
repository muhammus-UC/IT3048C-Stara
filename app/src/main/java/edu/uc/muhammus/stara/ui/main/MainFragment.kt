package edu.uc.muhammus.stara.ui.main


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.uc.muhammus.stara.R
import edu.uc.muhammus.stara.dto.Show
import edu.uc.muhammus.stara.dto.ShowJSON
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.title = "Stara - Search"

        // Updated deprecated code: https://stackoverflow.com/q/57534730
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // AutocompleteTextView Code
        /*
        viewModel.shows.observe(viewLifecycleOwner, Observer{
            shows -> actSearch.setAdapter(ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, shows))
        })
        viewModel.fetchShows("Community")
        Thread.sleep(5000)
        viewModel.fetchShows("Community")
        */

        btnSearch.setOnClickListener{fillSearchListView()}
    }

    private fun fillSearchListView() {
        var allShows: ArrayList<Show> = ArrayList<Show>()

        var searchTerm = actSearch.text.toString()
        viewModel.fetchShows(searchTerm)

        viewModel.shows.observeForever {
            it.forEach{
                allShows.add(it.show)
            }
        }

        viewModel.shows.observe(viewLifecycleOwner, Observer{
            searchListView.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, allShows)
        })
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
        fun newInstance() = MainFragment()
    }
}