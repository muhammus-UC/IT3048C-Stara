package edu.uc.muhammus.stara

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import edu.uc.muhammus.stara.ui.main.MainViewModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule

class ShowDataUnitTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var mvm: MainViewModel

    var showService = mockk<ShowService>()

    @Test
    fun confirmCommunity_outputsCommunity () {
        var show: Show = Show("Community", "English", "Ended")
        assertEquals("Community", show.toString())
    }

    @Test
    fun searchForCommunity_returnsCommunity() {
        givenAFeedOfMockedShowDataIsAvailable()
        whenSearchForCommunity()
        thenResultContainsCommunity()
        thenVerifyFunctionsInvoked()
    }

    @Test
    fun searchForGarbage_returnsNothing() {
        givenAFeedOfMockedShowDataIsAvailable()
        whenSearchForGarbageData()
        thenGetZeroResults()
    }

    private fun givenAFeedOfMockedShowDataIsAvailable() {
        mvm = MainViewModel()
        createMockData()
    }

    private fun createMockData() {
        var allShowsLiveData = MutableLiveData<ArrayList<Show>>()
        var allShows = ArrayList<Show>()
        // create and add shows to our collection
        var community = Show("Community", "English", "Ended")
        allShows.add(community)
        var communityLife = Show("Community Life", "English", "To Be Determined")
        allShows.add(communityLife)
        var diplomatic = Show("Diplomatic Immunity", "English", "Ended")
        allShows.add(diplomatic)
        allShowsLiveData.postValue(allShows)
        every {showService.fetchShows("Community")} returns allShowsLiveData
        every {showService.fetchShows(not("Community"))} returns MutableLiveData<ArrayList<Show>>()
        mvm.showService = showService

    }

    private fun whenSearchForCommunity() {
        mvm.fetchShows("Community")
    }

    private fun thenResultContainsCommunity() {
        var communityFound = false
        mvm.shows.observeForever {
            assertNotNull(it)
            assertTrue(it.size > 0)
            it.forEach {
                if (it.name == "Community" && it.language == "English" && it.status == "Ended") {
                    communityFound = true
                }
            }
        }
        assertTrue(communityFound)
    }

    private fun thenVerifyFunctionsInvoked() {
        verify {showService.fetchShows("Community")}
        verify(exactly = 0) {showService.fetchPlants("Loner")}
        confirmVerified(showService)
    }

    private fun whenSearchForGarbageData() {
        mvm.fetchShows("sklujapouetllkjsdau")
    }

    private fun thenGetZeroResults() {
        mvm.shows.observeForever {
            assertEquals(0, it.size)
        }
    }


}

