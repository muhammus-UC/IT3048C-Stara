package edu.uc.muhammus.stara

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import edu.uc.muhammus.stara.ui.main.FavoritesFragment
import edu.uc.muhammus.stara.ui.main.MainViewModel
import edu.uc.muhammus.stara.ui.main.ScheduleFragment
import edu.uc.muhammus.stara.ui.main.SearchFragment
import kotlinx.android.synthetic.main.favorites_fragment.*
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    // Used to switch fragments without having to initialize them again.
    private lateinit var searchFragment: SearchFragment
    private lateinit var scheduleFragment: ScheduleFragment
    private lateinit var favoritesFragment: FavoritesFragment

    // Keep track of which fragment is currently on screen or active
    private lateinit var activeFragment: Fragment

    // Firebase Authentication
    // Reference: https://youtu.be/cqEawweiLEM
    private val AUTH_REQUEST_CODE = 2002
    private var user: FirebaseUser? = null
    var userDisplayName: String? = "You"
    internal var email: String = "email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Ensure user is signed out
        FirebaseAuth.getInstance().signOut()

        // Initialize fragments in variables to keep them running when switching
        // Reference: https://stackoverflow.com/a/25151895
        searchFragment = SearchFragment.newInstance()
        scheduleFragment = ScheduleFragment.newInstance()
        favoritesFragment = FavoritesFragment.newInstance()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, scheduleFragment)
                .add(R.id.container, searchFragment)
                .add(R.id.container, favoritesFragment)
                .hide(searchFragment)
                .hide(favoritesFragment)
                .commitNow()
            activeFragment = scheduleFragment
        }

        // Configure bottom navigation bar buttons
        // Reference: https://stackoverflow.com/a/44611348
        staraBottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                // Schedule clicked
                R.id.action_schedule -> {
                    if (activeFragment == scheduleFragment)
                    {
                        showToast("Schedule already showing.", true)
                    }
                    else if (activeFragment == favoritesFragment)
                    {
                        supportFragmentManager.beginTransaction()
                            .hide(favoritesFragment)
                            .show(scheduleFragment)
                            .commitNow()
                        activeFragment = scheduleFragment
                    }
                    else if (activeFragment == searchFragment)
                    {
                        supportFragmentManager.beginTransaction()
                            .hide(searchFragment)
                            .show(scheduleFragment)
                            .commitNow()
                        activeFragment = scheduleFragment
                    }
                }
                // Favorites clicked
                R.id.action_favorites -> {
                    if (activeFragment == favoritesFragment)
                    {
                        showToast("Favorites already showing.", true)
                    }
                    else if (activeFragment == scheduleFragment)
                    {
                        supportFragmentManager.beginTransaction()
                            .hide(scheduleFragment)
                            .show(favoritesFragment)
                            .commitNow()
                        activeFragment = favoritesFragment

                        if (user == null){
                            logon()
                        }
                    }
                    else if (activeFragment == searchFragment)
                    {
                        supportFragmentManager.beginTransaction()
                            .hide(searchFragment)
                            .show(favoritesFragment)
                            .commitNow()
                        activeFragment = favoritesFragment
                        if (user == null){
                            logon()
                        }
                    }
                }
                // Search clicked
                R.id.action_search -> {
                    if (activeFragment == searchFragment)
                    {
                        showToast("Search already showing.", true)
                    }
                    else if (activeFragment == favoritesFragment)
                    {
                        supportFragmentManager.beginTransaction()
                            .hide(favoritesFragment)
                            .show(searchFragment)
                            .commitNow()
                        activeFragment = searchFragment
                    }
                    else if (activeFragment == scheduleFragment)
                    {
                        supportFragmentManager.beginTransaction()
                            .hide(scheduleFragment)
                            .show(searchFragment)
                            .commitNow()
                        activeFragment = searchFragment
                    }
                }
            }
            // Must return boolean for setOnNavigationItemSelectedListener
            true
        }
    }

    internal fun showToast(text: String, isLong: Boolean = false)
    {
        if (isLong)
        {
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
        }
        else
        {
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Firebase Authentication
     * Reference: https://youtu.be/cqEawweiLEM
     */
    internal fun logon() {
        var providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false).setAvailableProviders(providers).build(), AUTH_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK)
        {
            if (requestCode == AUTH_REQUEST_CODE)
            {
                user = FirebaseAuth.getInstance().currentUser
                if (user != null && !userDisplayName.isNullOrBlank())
                {
                    if (!userDisplayName.isNullOrBlank())
                    {
                        userDisplayName = user?.displayName
                        favoritesFragment.setDisplayName(userDisplayName)
                    }
                    email = user?.email!!
                }
            }
        }
    }
}