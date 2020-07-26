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
    lateinit var mvm: MainViewModel

    var scheduleService = mockk<ScheduleService>()

    @Test
    fun confirmDaisy_returnsDaisy() {
        var schedule = ScheduleJSON("Daisy Chain Gang", "09:00", Show("42183","LEGO City Adventures"))
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
        var scheduleLiveData = MutableLiveData<ArrayList<ScheduleJSON>>()
        var schedule = ArrayList<ScheduleJSON>()
        // create and add episodes to schedule
        var backdraft = ScheduleJSON("Backdraft to School", "09:15", Show("42183", "LEGO City Adventures"))
        schedule.add(backdraft)
        var outdoor = ScheduleJSON("Outdoor Dishes", "11:00", Show("42750", "Delicious Miss Brown"))
        schedule.add(outdoor)
        var thrill = ScheduleJSON("The Thrill of Pizza on a Grill", "12:30", Show("48531", "Symon's Dinners Cooking Out"))
        schedule.add(thrill)
        scheduleLiveData.postValue(schedule)
        every {scheduleService.fetchSchedule("US")} returns scheduleLiveData
        every {scheduleService.fetchSchedule(not("US"))} returns MutableLiveData<ArrayList<ScheduleJSON>>()
        mvm.scheduleService = scheduleService
    }

    private fun whenFetchSchedule() {
        mvm.fetchSchedule("US")
    }

    private fun thenResultContainsBackdraft() {
        var backdraftFound = false
        mvm.schedule.observeForever{
            assertNotNull(it)
            assertTrue(it.size > 0)
            it.forEach{
                if (it.episodeName == "Backdraft to School" &&
                    it.airtime == "09:15" &&
                    it.show.id == "42183" &&
                    it.show.name == "LEGO City Adventures") {
                    backdraftFound = true
                }
            }
        }
        assertTrue(backdraftFound)
    }

    private fun thenVerifyFunctionsInvoked() {
        verify {scheduleService.fetchSchedule("US")}
        verify(exactly = 0) {scheduleService.fetchSchedule("GB")}
        confirmVerified(scheduleService)
    }
}