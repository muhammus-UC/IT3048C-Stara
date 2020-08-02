/**
 * Unit Tests for Favorite data class.
 * Relatively short as it is a simple class that has no service.
 */
package edu.uc.muhammus.stara

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.uc.muhammus.stara.dto.Favorite
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class FavoriteDataUnitTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun confirmFavoriteShow_outputsFavoriteShow() {
        val favoriteShow = Favorite(
            "11025", "Still Game", "http://www.tvmaze.com/shows/11025/still-game", "Ended", "English", "http://static.tvmaze.com/uploads/images/medium_portrait/37/92691.jpg"
        )

        assertEquals("11025", favoriteShow.id)
        assertEquals("Still Game", favoriteShow.name)
        assertEquals("http://www.tvmaze.com/shows/11025/still-game", favoriteShow.url)
        assertEquals("Ended", favoriteShow.subtitle)
        assertEquals("English", favoriteShow.detail)
        assertEquals("http://static.tvmaze.com/uploads/images/medium_portrait/37/92691.jpg", favoriteShow.image)

        assertEquals("Still Game", favoriteShow.toString())
    }

    @Test
    fun confirmFavoriteActor_outputsFavoriteActor() {
        val favoriteActor = Favorite(
            "56368", "Dylan Moran", "http://www.tvmaze.com/people/56368/dylan-moran", "Male", "Ireland", "http://static.tvmaze.com/uploads/images/medium_portrait/9/23040.jpg"
        )

        assertEquals("56368", favoriteActor.id)
        assertEquals("Dylan Moran", favoriteActor.name)
        assertEquals("http://www.tvmaze.com/people/56368/dylan-moran", favoriteActor.url)
        assertEquals("Male", favoriteActor.subtitle)
        assertEquals("Ireland", favoriteActor.detail)
        assertEquals("http://static.tvmaze.com/uploads/images/medium_portrait/9/23040.jpg", favoriteActor.image)

        assertEquals("Dylan Moran", favoriteActor.toString())
    }
}
