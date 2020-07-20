package edu.uc.muhammus.stara

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.uc.muhammus.stara.ui.main.ScheduleFragment
import edu.uc.muhammus.stara.ui.main.SearchFragment
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Initialize fragments in variables to keep them running when switching
        // Reference: https://stackoverflow.com/a/25151895
        var searchFragment: SearchFragment = SearchFragment.newInstance()
        var scheduleFragment: ScheduleFragment = ScheduleFragment.newInstance()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, scheduleFragment)
                    .add(R.id.container, searchFragment)
                    .hide(searchFragment)
                    .commitNow()
        }

        // Configure bottom navigation bar buttons
        // Reference: https://stackoverflow.com/a/44611348
        staraBottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                // Schedule clicked
                R.id.action_schedule -> {
                    supportFragmentManager.beginTransaction()
                        .hide(searchFragment)
                        .show(scheduleFragment)
                        .commitNow()
                }
                // Favorites clicked
                R.id.action_favorites -> {
                    Toast.makeText(applicationContext, "Favorites Under Construction", Toast.LENGTH_SHORT).show()
                }
                // Search clicked
                R.id.action_search -> {
                    supportFragmentManager.beginTransaction()
                        .hide(scheduleFragment)
                        .show(searchFragment)
                        .commitNow()
                }
            }
            // Must return boolean for setOnNavigationItemSelectedListener
            true
        }
    }
}