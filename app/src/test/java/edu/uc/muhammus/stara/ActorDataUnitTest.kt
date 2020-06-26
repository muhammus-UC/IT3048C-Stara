package edu.uc.muhammus.stara

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import edu.uc.muhammus.stara.dto.Actor
import edu.uc.muhammus.stara.service.ActorService
import edu.uc.muhammus.stara.ui.main.MainViewModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

import org.junit.Assert.*
import org.junit.rules.TestRule

class ActorDataUnitTest {
    @get: Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var mvm: MainViewModel

    var actorService = mockk<ActorService>()

    @Test
    fun confirmJoelMcHale_outputsJoelMcHale () {
        var actor: Actor = Actor("Joel McHale", "Italy", "Male")
        assertEquals("Joel McHale", actor.toString())
    }

    @Test
    fun searchForJoelMcHale_returnsJoelMcHale() {
        givenAFeedOfMockedCastDataIsAvailable()
        whenSearchForJoelMcHale()
        thenResultContainsJoelMcHale()
        thenVerifyFunctionsInvoked()
    }

    @Test
    fun searchForGarbage_returnsNothing() {
        givenAFeedOfMockedCastDataIsAvailable()
        whenSearchForGarbageData()
        thenGetZeroResults()
    }

    private fun givenAFeedOfMockedCastDataIsAvailable() {
        mvm = MainViewModel()
        createMockData()
    }

    private fun createMockData() {
        var allActorsLiveData = MutableLiveData<ArrayList<Actor>>()
        var allActors = ArrayList<Actor>()
        // create and add actors to our collection
        var mchale = Actor("Joel McHale", "Italy", "Male")
        allActors.add(mchale)
        var barrios = Actor("Joseph Barrios", "United States", "Male")
        allActors.add(barrios)
        var kramer = Actor("Joel Michael Kramer", null, "Male")
        allActors.add(kramer)
        allActorsLiveData.postValue(allActors)
        every {actorService.fetchActors("Joel McHale")} returns allActorsLiveData
        every {actorService.fetchActors(not("Joel McHale"))} returns MutableLiveData<ArrayList<Actor>>()
        mvm.actorService = actorService
    }

    private fun whenSearchForJoelMcHale() {
        mvm.fetchActors("Joel McHale")
    }

    private fun thenResultContainsJoelMcHale() {
        var mchaleFound = false
        mvm.actors.observeForever {
            assertNotNull(it)
            assertTrue(it.size > 0)
            it.forEach {
                if (it.name == "Joel McHale" && it.country == "Italy" && it.gender == "Male") {
                    mchaleFound = true
                }
            }
        }
        assertTrue(mchaleFound)
    }

    private fun thenVerifyFunctionsInvoked() {
        verify { actorService.fetchActors("Joel McHale") }
        verify(exactly = 0) {actorService.fetchActors("Jim Rash")}
        confirmVerified(actorService)
    }

    private fun whenSearchForGarbageData() {
        mvm.fetchActors("sklujapouetllkjsdau")
    }

    private fun thenGetZeroResults() {
        mvm.actors.observeForever {
            assertEquals(0, it.size)
        }
    }
}