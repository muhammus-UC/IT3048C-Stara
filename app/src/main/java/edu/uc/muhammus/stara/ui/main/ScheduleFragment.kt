/**
 * Fragment shown by default.
 * Shows a schedule of new episodes premiering today in United States via TVMaze API.
 * Feature to add: Get user location and get schedule data for country they are in.
 */
package edu.uc.muhammus.stara.ui.main

import android.Manifest
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.uc.muhammus.stara.MainActivity
import edu.uc.muhammus.stara.R
import edu.uc.muhammus.stara.ui.location.LocationViewModel
import edu.uc.muhammus.stara.ui.misc.ScheduleListViewAdapter
import kotlinx.android.synthetic.main.schedule_fragment.*

class ScheduleFragment : Fragment() {

    companion object {
        fun newInstance() = ScheduleFragment()
    }

    private val LOCATION_PERMISSION_REQUEST_CODE = 2000
    private lateinit var viewModel: MainViewModel
    private lateinit var locationViewModel: LocationViewModel

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

        // Check for location permissions
        prepRequestLocationUpdates()
    }

    private fun prepRequestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocationUpdates()
        }
        else {
            val permissionRequest = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(permissionRequest, LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    private fun requestLocationUpdates() {
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        locationViewModel.getLocationLiveData().observe(viewLifecycleOwner, Observer {
            Log.d("ScheduleFragment.kt", "Latitude: " + it.latitude)
            Log.d("ScheduleFragment.kt", "Longitude: " + it.longitude)
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocationUpdates()
                } else {
                    Toast.makeText(context, "Unable to update location without permission", Toast.LENGTH_LONG).show()
                }
            }
        }
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