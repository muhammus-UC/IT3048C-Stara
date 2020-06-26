package edu.uc.muhammus.stara

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.uc.muhammus.stara.dto.ShowJSON
import edu.uc.muhammus.stara.ui.main.MainViewModel

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ShowDataIntegrationTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var mvm: MainViewModel

    @Test
    fun showDTO_isPopulated() {
        givenViewModelIsInitialized()
        whenJSONDataAreReadAndParsed()
        thenTheCollectionSizeShouldBeGreaterThanZero()
    }

    @Test
    fun searchForBlackBooks_returnsBlackBooks() {
        givenViewModelIsInitialized()
        whenSearchForBlackBooks()
        thenResultsShouldContainBlackBooks()
    }

    private fun givenViewModelIsInitialized()
    {
        mvm = MainViewModel()
    }

    private fun whenJSONDataAreReadAndParsed() {
        mvm.fetchShows("Community")
        Thread.sleep(5000) // Give time to complete network request
    }

    private fun thenTheCollectionSizeShouldBeGreaterThanZero() {
        var allShows = ArrayList<ShowJSON>()
        mvm.shows.observeForever {
            allShows = it
        }
        assertNotNull(allShows)
        assertTrue(allShows.size > 0)
    }

    private fun whenSearchForBlackBooks() {
        mvm.fetchShows("Black Books")
        Thread.sleep(5000) // Give time to complete network request
    }

    private fun thenResultsShouldContainBlackBooks() {
        var blackBooksFound = false
        mvm.shows.observeForever {
            assertNotNull(it)
            assertTrue(it.size > 0)
            it.forEach{
                if (it.show.name == "Black Books" && it.show.language == "English" && it.show.status == "Ended") {
                    blackBooksFound = true
                }
            }
        }
        assertTrue(blackBooksFound)
    }
}