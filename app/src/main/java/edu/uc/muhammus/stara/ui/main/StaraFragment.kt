/**
 * Superclass used to make fragments for this app.
 * Makes it easier to manage common code.
 */
package edu.uc.muhammus.stara.ui.main

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import edu.uc.muhammus.stara.dto.Show

open class StaraFragment : Fragment() {

    // Code to hide soft keyboard - Part 1/2
    // Reference: https://stackoverflow.com/a/45857155
    protected fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    // Code to hide soft keyboard - Part 2/2
    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Used to quickly show Toasts within a Fragment.
     */
    protected fun showToast(text: String, isLong: Boolean = false) {
        if (isLong) {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    // Used for debugging, in case API is not working
    protected val listOfShows = listOf(
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
}
