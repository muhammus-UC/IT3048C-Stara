package edu.uc.muhammus.stara

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import edu.uc.muhammus.stara.dto.ScheduleJSON
import edu.uc.muhammus.stara.dto.Show
import edu.uc.muhammus.stara.service.ScheduleService
import edu.uc.muhammus.stara.ui.main.MainViewModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ScheduleDataUnitTest {
    @get: Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private lateinit var mvm: MainViewModel

    private var scheduleService = mockk<ScheduleService>()

    @Test
    fun confirmDaisy_returnsDaisy() {
        val schedule = ScheduleJSON(
            "Daisy Chain Gang",
            "09:00",
            "https://www.tvmaze.com/episodes/1896300/lego-city-adventures-2x09-daisy-chain-gang",
            Show("42183", "LEGO City Adventures", "https://www.tvmaze.com/shows/42183/lego-city-adventures")
        )
        assertEquals("Episode Daisy Chain Gang airs at 09:00 for show LEGO City Adventures", schedule.toString())
    }

    @Test
    fun fetchForSchedule_returnsSchedule() {
        givenAFeedOfMockedScheduleDataIsAvailable()
        whenFetchSchedule()
        thenResultContainsBackdraft()
        thenVerifyFunctionsInvoked()
    }

    private fun givenAFeedOfMockedScheduleDataIsAvailable() {
        mvm = MainViewModel()
        createMockData()
    }

    private fun createMockData() {
        val scheduleLiveData = MutableLiveData<ArrayList<ScheduleJSON>>()
        val schedule = ArrayList<ScheduleJSON>()
        // create and add episodes to schedule
        val backdraft = ScheduleJSON(
            "Backdraft to School",
            "09:15",
            "https://www.tvmaze.com/episodes/1896300/lego-city-adventures-2x09-daisy-chain-gang",
            Show("42183", "LEGO City Adventures", "https://www.tvmaze.com/shows/42183/lego-city-adventures")

        )
        schedule.add(backdraft)
        val outdoor = ScheduleJSON(
            "Outdoor Dishes",
            "11:00",
            "https://www.tvmaze.com/episodes/1902174/delicious-miss-brown-2x15-outdoor-dishes",
            Show("42750", "Delicious Miss Brown", "https://www.tvmaze.com/shows/42750/delicious-miss-brown")
        )
        schedule.add(outdoor)
        val thrill = ScheduleJSON(
            "The Thrill of Pizza on a Grill",
            "12:30",
            "https://www.tvmaze.com/episodes/1902269/symons-dinners-cooking-out-1x07-the-thrill-of-pizza-on-a-grill",
            Show("48531", "Symon's Dinners Cooking Out", "https://www.tvmaze.com/shows/48531/symons-dinners-cooking-out")
        )
        schedule.add(thrill)
        scheduleLiveData.postValue(schedule)
        every { scheduleService.fetchSchedule("US") } returns scheduleLiveData
        every { scheduleService.fetchSchedule(not("US")) } returns MutableLiveData<ArrayList<ScheduleJSON>>()
        mvm.scheduleService = scheduleService
    }

    private fun whenFetchSchedule() {
        mvm.fetchSchedule("US")
    }

    private fun thenResultContainsBackdraft() {
        var backdraftFound = false
        mvm.schedule.observeForever { arrayList ->
            assertNotNull(arrayList)
            assertTrue(arrayList.size > 0)
            arrayList.forEach {
                if (it.episodeName == "Backdraft to School" &&
                    it.airtime == "09:15" &&
                    it.url == "https://www.tvmaze.com/episodes/1896300/lego-city-adventures-2x09-daisy-chain-gang" &&
                    it.show.id == "42183" &&
                    it.show.name == "LEGO City Adventures" &&
                    it.show.url == "https://www.tvmaze.com/shows/42183/lego-city-adventures"
                ) {
                    backdraftFound = true
                }
            }
        }
        assertTrue(backdraftFound)
    }

    private fun thenVerifyFunctionsInvoked() {
        verify { scheduleService.fetchSchedule("US") }
        verify(exactly = 0) { scheduleService.fetchSchedule("GB") }
        confirmVerified(scheduleService)
    }
}
