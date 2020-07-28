package edu.uc.muhammus.stara

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import edu.uc.muhammus.stara.dto.Show
import edu.uc.muhammus.stara.dto.ShowJSON
import edu.uc.muhammus.stara.service.ShowService
import edu.uc.muhammus.stara.ui.main.MainViewModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ShowDataUnitTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private lateinit var mvm: MainViewModel

    private var showService = mockk<ShowService>()

    @Test
    fun confirmCommunity_outputsCommunity () {
        val show = Show("318", "Community", "English", "Ended")
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
        val allShowsLiveData = MutableLiveData<ArrayList<ShowJSON>>()
        val allShows = ArrayList<ShowJSON>()
        // create and add shows to our collection
        val community = Show("318", "Community", "English", "Ended")
        val communityJSON = ShowJSON(50.0, community)
        allShows.add(communityJSON)
        val communityLife = Show("28145", "Community Life", "English", "To Be Determined")
        val communityLifeJSON = ShowJSON(25.0, communityLife)
        allShows.add(communityLifeJSON)
        val diplomatic = Show("6191", "Diplomatic Immunity", "English", "Ended")
        val diplomaticJSON = ShowJSON(12.5, diplomatic)
        allShows.add(diplomaticJSON)
        allShowsLiveData.postValue(allShows)
        every {showService.fetchShows("Community")} returns allShowsLiveData
        every {showService.fetchShows(not("Community"))} returns MutableLiveData<ArrayList<ShowJSON>>()
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
                if (it.show.id == "318" &&
                    it.show.name == "Community" &&
                    it.show.language == "English" &&
                    it.show.status == "Ended") {
                    communityFound = true
                }
            }
        }
        assertTrue(communityFound)
    }

    private fun thenVerifyFunctionsInvoked() {
        verify {showService.fetchShows("Community")}
        verify(exactly = 0) {showService.fetchShows("Loner")}
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

