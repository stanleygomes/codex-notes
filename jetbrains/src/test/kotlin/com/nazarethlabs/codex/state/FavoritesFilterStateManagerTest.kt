package com.nazarethlabs.codex.state

import com.nazarethlabs.codex.listener.FavoritesFilterStateListener
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavoritesFilterStateManagerTest {
    private lateinit var favoritesFilterStateManager: FavoritesFilterStateManager

    @Before
    fun setUp() {
        favoritesFilterStateManager = FavoritesFilterStateManager()
    }

    private class TestFavoritesFilterListener : FavoritesFilterStateListener {
        var lastFilteringState: Boolean? = null
        var callCount = 0

        override fun onFavoritesFilterChanged(isFilteringFavorites: Boolean) {
            lastFilteringState = isFilteringFavorites
            callCount++
        }

        fun reset() {
            lastFilteringState = null
            callCount = 0
        }
    }

    @Test
    fun `should start with favorites filter disabled`() {
        assertFalse(favoritesFilterStateManager.isFilteringFavorites())
    }

    @Test
    fun `should enable favorites filter when toggled once`() {
        favoritesFilterStateManager.toggleFavoritesFilter()

        assertTrue(favoritesFilterStateManager.isFilteringFavorites())
    }

    @Test
    fun `should disable favorites filter when toggled twice`() {
        favoritesFilterStateManager.toggleFavoritesFilter()
        favoritesFilterStateManager.toggleFavoritesFilter()

        assertFalse(favoritesFilterStateManager.isFilteringFavorites())
    }

    @Test
    fun `should add listener to listeners list`() {
        val listener = TestFavoritesFilterListener()

        favoritesFilterStateManager.addListener(listener)
        favoritesFilterStateManager.toggleFavoritesFilter()

        assertTrue(listener.lastFilteringState == true)
        assertTrue(listener.callCount == 1)
    }

    @Test
    fun `should notify all listeners when filter toggled`() {
        val listener1 = TestFavoritesFilterListener()
        val listener2 = TestFavoritesFilterListener()

        favoritesFilterStateManager.addListener(listener1)
        favoritesFilterStateManager.addListener(listener2)
        favoritesFilterStateManager.toggleFavoritesFilter()

        assertTrue(listener1.lastFilteringState == true)
        assertTrue(listener2.lastFilteringState == true)
        assertTrue(listener1.callCount == 1)
        assertTrue(listener2.callCount == 1)
    }

    @Test
    fun `should notify listeners with correct state on multiple toggles`() {
        val listener = TestFavoritesFilterListener()

        favoritesFilterStateManager.addListener(listener)

        favoritesFilterStateManager.toggleFavoritesFilter()
        assertTrue(listener.lastFilteringState == true)
        assertTrue(listener.callCount == 1)

        favoritesFilterStateManager.toggleFavoritesFilter()
        assertTrue(listener.lastFilteringState == false)
        assertTrue(listener.callCount == 2)

        favoritesFilterStateManager.toggleFavoritesFilter()
        assertTrue(listener.lastFilteringState == true)
        assertTrue(listener.callCount == 3)
    }
}
