/**
 * Fragment shown by default.
 * If location access denied, shows a schedule of new episodes premiering today for United States via TVMaze API.
 * If location access granted, shows a schedule of new episodes premiering today for country device is located via TVMaze API.
 */
package edu.uc.muhammus.stara.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uc.muhammus.stara.R
import edu.uc.muhammus.stara.ui.location.LocationViewModel
import edu.uc.muhammus.stara.ui.recyclerview.SchedulesRecyclerViewAdapter
import kotlinx.android.synthetic.main.schedule_fragment.*
import java.util.*

class ScheduleFragment : StaraFragment() {
    private val fileName = "ScheduleFragment.kt"

    private lateinit var viewModel: MainViewModel
    private var fragmentTitle = "Stara - Schedule"

    private val LOCATION_PERMISSION_REQUEST_CODE = 2000
    private lateinit var locationViewModel: LocationViewModel

    private lateinit var longitude: String
    private lateinit var latitude: String
    private lateinit var countryCode: String
    private lateinit var countryName: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.schedule_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.title = fragmentTitle

        // Updated deprecated code: https://stackoverflow.com/q/57534730
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.initializeFirebase()

        // Configure recycler view options with sane defaults
        scheduleRecyclerView.hasFixedSize()
        scheduleRecyclerView.layoutManager = LinearLayoutManager(context)
        scheduleRecyclerView.itemAnimator = DefaultItemAnimator()

        /**
         * Check for location permissions.
         * If permission granted, get TV premieres schedule for country user is in.
         * If permission not granted, get TV premieres schedule for United States.
         */
        prepRequestLocationUpdates()
    }

    // Prepare to get location by ensuring user has granted permission.
    private fun prepRequestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocationUpdates()
        } else {
            val permissionRequest = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(permissionRequest, LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    // User has granted permission to access location, time to use it to get user's country's schedule.
    private fun requestLocationUpdates() {
        showToast("Getting Schedule...")

        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        locationViewModel.getLocationLiveData().observe(
            viewLifecycleOwner,
            Observer {
                latitude = it.latitude
                longitude = it.longitude
                Log.d(fileName, "Latitude: $latitude")
                Log.d(fileName, "Longitude: $longitude")

                // Use Geocoder to convert GPS coordinates into a countryName and countryCode
                val geocoder = Geocoder(context, Locale.getDefault())
                val address = geocoder.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1)
                countryCode = address[0].countryCode
                countryName = address[0].countryName
                Log.d(fileName, "Country code is: $countryCode")
                Log.d(fileName, "Country name is: $countryName")

                // Populate view with schedule for country user is in.
                populateScheduleRecyclerView(countryCode)
                // Update text to indicate which country schedule is being gotten for
                txtScheduleSubtitle.text = countryName

                // Don't expect to update country schedule often. Remove Observer after one location received.
                //locationViewModel.getLocationLiveData().removeObservers(viewLifecycleOwner)
            }
        )
    }

    /**
     * Whenever any permission is requested, use this function to handle it
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocationUpdates()
                } else {
                    showToast("Unable to update location without permission. Showing USA Schedule.", true)
                    populateScheduleRecyclerView("US")
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    /**
     * Fetch schedule for countryCode specified
     */
    private fun populateScheduleRecyclerView(countryCode: String) {
        viewModel.fetchSchedule(countryCode)

        viewModel.schedule.observe(
            viewLifecycleOwner,
            Observer {
                schedule ->
                    scheduleRecyclerView.adapter = SchedulesRecyclerViewAdapter(schedule, R.layout.list_item)
            }
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
        fun newInstance() = ScheduleFragment()
    }
}