package edu.uc.muhammus.stara

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.uc.muhammus.stara.dto.Show
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

    private fun givenViewModelIsInitialized()
    {
        mvm = MainViewModel()
    }

    private fun whenJSONDataAreReadAndParsed() {
        mvm.fetchShows("Community")
    }

    private fun thenTheCollectionSizeShouldBeGreaterThanZero() {
        var allShows = ArrayList<Show>()
        mvm.shows.observeForever {
            allShows = it
        }
        Thread.sleep(5000)
        assertNotNull(allShows)
        assertTrue(allShows.size > 0)
    }
}