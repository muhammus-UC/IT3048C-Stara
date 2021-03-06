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
    private lateinit var mvm: MainViewModel

    @Test
    fun actorDTO_isPopulated() {
        givenViewModelIsInitialized()
        whenJSONDataAreReadAndParsed()
        andWhenSleep()
        thenTheCollectionSizeShouldBeGreaterThanZero()
    }

    @Test
    fun searchForTomHanks_returnsTomHanks() {
        givenViewModelIsInitialized()
        whenSearchForTomHanks()
        andWhenSleep()
        thenResultsShouldContainTomHanks()
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
        mvm.fetchActors("Joel McHale")
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
    }

    private fun thenResultsShouldContainTomHanks() {
        var tomHanksFound = false
        mvm.actors.observeForever { arrayList ->
            assertNotNull(arrayList)
            assertTrue(arrayList.size > 0)
            arrayList.forEach {
                if (it.actor.id == "46432" &&
                    it.actor.name == "Tom Hanks" &&
                    it.actor.url == "http://www.tvmaze.com/people/46432/tom-hanks" &&
                    it.actor.country?.name == "United States" &&
                    it.actor.gender == "Male" &&
                    it.actor.image?.medium == "http://static.tvmaze.com/uploads/images/medium_portrait/28/72307.jpg"
                ) {
                    tomHanksFound = true
                }
            }
        }
        assertTrue(tomHanksFound)
    }

    private fun whenSearchForGarbage() {
        mvm.fetchActors("sklujapouetllkjsdau")
    }

    private fun thenGetZeroResults() {
        mvm.shows.observeForever {
            assertEquals(0, it.size)
        }
    }
}
