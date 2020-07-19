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
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SearchFragment.newInstance())
                    .commitNow()
        }

        // Configure bottom navigation bar buttons
        staraBottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                // Schedule clicked
                R.id.action_schedule -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ScheduleFragment.newInstance())
                        .commitNow()
                }
                // Favorites clicked
                R.id.action_favorites -> {
                    Toast.makeText(applicationContext, "Favorites Under Construction", Toast.LENGTH_SHORT).show()
                }
                // Search clicked
                R.id.action_search -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, SearchFragment.newInstance())
                        .commitNow()
                }
            }
            // Must return boolean for setOnNavigationItemSelectedListener
            true
        }
    }
}