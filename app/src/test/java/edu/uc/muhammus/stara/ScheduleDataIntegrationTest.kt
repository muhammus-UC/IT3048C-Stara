package edu.uc.muhammus.stara

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.uc.muhammus.stara.dto.ScheduleJSON
import edu.uc.muhammus.stara.dto.Show
import edu.uc.muhammus.stara.ui.main.MainViewModel
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ScheduleDataIntegrationTest {
    @get: Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private lateinit var mvm: MainViewModel

    @Test
    fun scheduleDTO_isPopulated() {
        givenViewModelIsInitialized()
        whenJSONDataAreReadAndParsed()
        andWhenSleep()
        thenTheCollectionSizeShouldBeGreaterThanZero()
    }

    @Test
    fun scheduleDTO_containsShow() {
        givenViewModelIsInitialized()
        whenJSONDataAreReadAndParsed()
        andWhenSleep()
        thenTheCollectionShouldHaveShow()
    }

    private fun andWhenSleep() {
        Thread.sleep(2000)
    }

    private fun givenViewModelIsInitialized() {
        mvm = MainViewModel()
    }

    private fun whenJSONDataAreReadAndParsed() {
        mvm.fetchSchedule("US")
    }

    private fun thenTheCollectionSizeShouldBeGreaterThanZero() {
        var schedule = ArrayList<ScheduleJSON>()
        mvm.schedule.observeForever {
            schedule = it
        }
        assertNotNull(schedule)
        assertTrue(schedule.size > 0)
    }

    private fun thenTheCollectionShouldHaveShow() {
        var scheduleContainsShow = false
        var schedule = ArrayList<ScheduleJSON>()
        mvm.schedule.observeForever {
            schedule = it
        }
        if (schedule[0].show is Show) {
            scheduleContainsShow = true
        }
        assertTrue(scheduleContainsShow)
    }
}
