/**
 * Fragment shown by default.
 * Shows a schedule of new episodes premiering today in United States via TVMaze API.
 * Feature to add: Get user location and get schedule data for country they are in.
 */
package edu.uc.muhammus.stara.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.uc.muhammus.stara.R
import edu.uc.muhammus.stara.ui.misc.ScheduleListViewAdapter
import kotlinx.android.synthetic.main.schedule_fragment.*

class ScheduleFragment : Fragment() {

    companion object {
        fun newInstance() = ScheduleFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.schedule_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.title = "Stara - Schedule"

        // Updated deprecated code: https://stackoverflow.com/q/57534730
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        populateScheduleListView()
    }

    // When fragment is hidden or shown
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        // If fragment is NOT hidden
        if(!hidden) {
            activity?.title = "Stara - Schedule"
        }
    }

    private fun populateScheduleListView() {
        viewModel.fetchSchedule("US")

        viewModel.schedule.observe(viewLifecycleOwner, Observer {
            schedule -> scheduleListView.adapter = ScheduleListViewAdapter(requireContext(), schedule)
        })
    }

}