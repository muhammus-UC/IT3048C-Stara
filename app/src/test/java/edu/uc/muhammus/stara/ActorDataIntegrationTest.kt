package edu.uc.muhammus.stara

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.uc.muhammus.stara.dto.ActorJSON
import edu.uc.muhammus.stara.ui.main.MainViewModel
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ActorDataIntegrationTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var mvm: MainViewModel

    @Test
    fun actorDTO_isPopulated() {
        givenViewModelIsInitialized()
        whenJSONDataAreReadAndParsed()
        thenTheCollectionSizeShouldBeGreaterThanZero()
    }

    @Test
    fun searchForTomHanks_returnsTomHanks() {
        givenViewModelIsInitialized()
        whenSearchForTomHanks()
        thenResultsShouldContainTomHanks()
    }

    @Test
    fun searchForGarbage_returnsNothing() {
        givenViewModelIsInitialized()
        whenSearchForGarbage()
        thenGetZeroResults()
    }

    private fun givenViewModelIsInitialized() {
        mvm = MainViewModel()
    }

    private fun whenJSONDataAreReadAndParsed() {
        mvm.fetchActors("Joel McHale")
        Thread.sleep(5000) // Give time to complete network request
    }

    private fun thenTheCollectionSizeShouldBeGreaterThanZero() {
        var allActors = ArrayList<ActorJSON>()
        mvm.actors.observeForever {
            allActors = it
        }
        assertNotNull(allActors)
        assertTrue(allActors.size > 0)
    }

    private fun whenSearchForTomHanks() {
        mvm.fetchActors("Tom Hanks")
        Thread.sleep(5000) // Give time to complete network request
    }

    private fun thenResultsShouldContainTomHanks() {
        var tomHanksFound = false
        mvm.actors.observeForever {
            assertNotNull(it)
            assertTrue(it.size > 0)
            it.forEach{
                if (it.actor.name == "Tom Hanks" && it.actor.country?.name == "United States" && it.actor.gender == "Male")
                {
                    tomHanksFound = true
                }
            }
        }
        assertTrue(tomHanksFound)
    }

    private fun whenSearchForGarbage() {
        mvm.fetchActors("sklujapouetllkjsdau")
        Thread.sleep(5000) // Give time to complete network request
    }

    private fun thenGetZeroResults() {
        mvm.shows.observeForever {
            assertEquals(0, it.size)
        }
    }

}