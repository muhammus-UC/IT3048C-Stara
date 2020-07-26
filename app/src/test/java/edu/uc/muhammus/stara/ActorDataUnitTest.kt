package edu.uc.muhammus.stara

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import edu.uc.muhammus.stara.dto.Actor
import edu.uc.muhammus.stara.dto.ActorCountry
import edu.uc.muhammus.stara.dto.ActorJSON
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
        val country = ActorCountry("Italy", "IT", "Europe/Rome")
        val actor = Actor("Joel McHale", country, "Male")
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
        val allActorsLiveData = MutableLiveData<ArrayList<ActorJSON>>()
        val allActors = ArrayList<ActorJSON>()
        // create and add actors to our collection
        val mcHaleCountry = ActorCountry ("Italy", "IT", "Europe/Rome")
        val mcHale = Actor("Joel McHale", mcHaleCountry, "Male")
        val mcHaleJSON = ActorJSON(50.0, mcHale)
        allActors.add(mcHaleJSON)
        val barriosCountry = ActorCountry("United States", "US", "America/New_York")
        val barrios = Actor("Joseph Barrios", barriosCountry, "Male")
        val barriosJSON = ActorJSON(25.0, barrios)
        allActors.add(barriosJSON)
        val kramer = Actor("Joel Michael Kramer", null, "Male")
        val kramerJSON = ActorJSON(12.5, kramer)
        allActors.add(kramerJSON)
        allActorsLiveData.postValue(allActors)
        every {actorService.fetchActors("Joel McHale")} returns allActorsLiveData
        every {actorService.fetchActors(not("Joel McHale"))} returns MutableLiveData<ArrayList<ActorJSON>>()
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
                if (it.actor.name == "Joel McHale" && it.actor.country?.name == "Italy" && it.actor.gender == "Male") {
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