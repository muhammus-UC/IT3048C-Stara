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
    private lateinit var mvm: MainViewModel

    @Test
    fun showDTO_isPopulated() {
        givenViewModelIsInitialized()
        whenJSONDataAreReadAndParsed()
        andWhenSleep()
        thenTheCollectionSizeShouldBeGreaterThanZero()
    }

    @Test
    fun searchForBlackBooks_returnsBlackBooks() {
        givenViewModelIsInitialized()
        whenSearchForBlackBooks()
        andWhenSleep()
        thenResultsShouldContainBlackBooks()
    }

    @Test
    fun searchForGarbage_returnsNothing() {
        givenViewModelIsInitialized()
        whenSearchForGarbage()
        andWhenSleep()
        thenGetZeroResults()
    }

    private fun andWhenSleep() {
        Thread.sleep(1000)
    }

    private fun givenViewModelIsInitialized() {
        mvm = MainViewModel()
    }

    private fun whenJSONDataAreReadAndParsed() {
        mvm.fetchShows("Community")
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
    }

    private fun thenResultsShouldContainBlackBooks() {
        var blackBooksFound = false
        mvm.shows.observeForever { arrayList ->
            assertNotNull(arrayList)
            assertTrue(arrayList.size > 0)
            arrayList.forEach{
                if (it.show.id == "1641" &&
                    it.show.name == "Black Books" &&
                    it.show.language == "English" &&
                    it.show.status == "Ended" &&
                    it.show.image?.medium == "http://static.tvmaze.com/uploads/images/medium_portrait/81/204617.jpg") {
                    blackBooksFound = true
                }
            }
        }
        assertTrue(blackBooksFound)
    }

    private fun whenSearchForGarbage() {
        mvm.fetchShows("sklujapouetllkjsdau")
    }

    private fun thenGetZeroResults() {
        mvm.shows.observeForever {
            assertEquals(0, it.size)
        }
    }
}