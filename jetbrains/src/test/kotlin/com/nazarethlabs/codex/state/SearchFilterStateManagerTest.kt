package com.nazarethlabs.codex.state

import com.nazarethlabs.codex.enum.DateFilterEnum
import com.nazarethlabs.codex.enum.NoteColorEnum
import com.nazarethlabs.codex.listener.SearchFilterStateListener
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchFilterStateManagerTest {
    private lateinit var searchFilterStateManager: SearchFilterStateManager

    @Before
    fun setUp() {
        searchFilterStateManager = SearchFilterStateManager()
    }

    private class TestSearchFilterListener : SearchFilterStateListener {
        var callCount = 0

        override fun onFilterStateChanged() {
            callCount++
        }
    }

    @Test
    fun `should start with no active filters`() {
        assertNull(searchFilterStateManager.getActiveDateFilter())
        assertFalse(searchFilterStateManager.isFavoriteFilterActive())
        assertTrue(searchFilterStateManager.getActiveColorFilters().isEmpty())
        assertFalse(searchFilterStateManager.hasActiveFilters())
    }

    @Test
    fun `should toggle date filter on when activated`() {
        searchFilterStateManager.toggleDateFilter(DateFilterEnum.TODAY)

        assertEquals(DateFilterEnum.TODAY, searchFilterStateManager.getActiveDateFilter())
        assertTrue(searchFilterStateManager.hasActiveFilters())
    }

    @Test
    fun `should toggle date filter off when same filter toggled again`() {
        searchFilterStateManager.toggleDateFilter(DateFilterEnum.TODAY)
        searchFilterStateManager.toggleDateFilter(DateFilterEnum.TODAY)

        assertNull(searchFilterStateManager.getActiveDateFilter())
        assertFalse(searchFilterStateManager.hasActiveFilters())
    }

    @Test
    fun `should switch date filter when different filter is toggled`() {
        searchFilterStateManager.toggleDateFilter(DateFilterEnum.TODAY)
        searchFilterStateManager.toggleDateFilter(DateFilterEnum.THIS_WEEK)

        assertEquals(DateFilterEnum.THIS_WEEK, searchFilterStateManager.getActiveDateFilter())
    }

    @Test
    fun `should toggle favorite filter on`() {
        searchFilterStateManager.toggleFavoriteFilter()

        assertTrue(searchFilterStateManager.isFavoriteFilterActive())
        assertTrue(searchFilterStateManager.hasActiveFilters())
    }

    @Test
    fun `should toggle favorite filter off when toggled twice`() {
        searchFilterStateManager.toggleFavoriteFilter()
        searchFilterStateManager.toggleFavoriteFilter()

        assertFalse(searchFilterStateManager.isFavoriteFilterActive())
    }

    @Test
    fun `should add color filter when toggled`() {
        searchFilterStateManager.toggleColorFilter(NoteColorEnum.BLUE)

        assertTrue(searchFilterStateManager.getActiveColorFilters().contains(NoteColorEnum.BLUE))
        assertTrue(searchFilterStateManager.hasActiveFilters())
    }

    @Test
    fun `should remove color filter when toggled again`() {
        searchFilterStateManager.toggleColorFilter(NoteColorEnum.BLUE)
        searchFilterStateManager.toggleColorFilter(NoteColorEnum.BLUE)

        assertFalse(searchFilterStateManager.getActiveColorFilters().contains(NoteColorEnum.BLUE))
    }

    @Test
    fun `should support multiple active color filters`() {
        searchFilterStateManager.toggleColorFilter(NoteColorEnum.BLUE)
        searchFilterStateManager.toggleColorFilter(NoteColorEnum.PINK)

        val activeColors = searchFilterStateManager.getActiveColorFilters()
        assertEquals(2, activeColors.size)
        assertTrue(activeColors.contains(NoteColorEnum.BLUE))
        assertTrue(activeColors.contains(NoteColorEnum.PINK))
    }

    @Test
    fun `should clear all filters`() {
        searchFilterStateManager.toggleDateFilter(DateFilterEnum.TODAY)
        searchFilterStateManager.toggleFavoriteFilter()
        searchFilterStateManager.toggleColorFilter(NoteColorEnum.BLUE)

        searchFilterStateManager.clearAllFilters()

        assertNull(searchFilterStateManager.getActiveDateFilter())
        assertFalse(searchFilterStateManager.isFavoriteFilterActive())
        assertTrue(searchFilterStateManager.getActiveColorFilters().isEmpty())
        assertFalse(searchFilterStateManager.hasActiveFilters())
    }

    @Test
    fun `should notify listeners when date filter toggled`() {
        val listener = TestSearchFilterListener()
        searchFilterStateManager.addListener(listener)

        searchFilterStateManager.toggleDateFilter(DateFilterEnum.TODAY)

        assertEquals(1, listener.callCount)
    }

    @Test
    fun `should notify listeners when favorite filter toggled`() {
        val listener = TestSearchFilterListener()
        searchFilterStateManager.addListener(listener)

        searchFilterStateManager.toggleFavoriteFilter()

        assertEquals(1, listener.callCount)
    }

    @Test
    fun `should notify listeners when color filter toggled`() {
        val listener = TestSearchFilterListener()
        searchFilterStateManager.addListener(listener)

        searchFilterStateManager.toggleColorFilter(NoteColorEnum.BLUE)

        assertEquals(1, listener.callCount)
    }

    @Test
    fun `should notify all listeners on filter change`() {
        val listener1 = TestSearchFilterListener()
        val listener2 = TestSearchFilterListener()
        searchFilterStateManager.addListener(listener1)
        searchFilterStateManager.addListener(listener2)

        searchFilterStateManager.toggleDateFilter(DateFilterEnum.TODAY)

        assertEquals(1, listener1.callCount)
        assertEquals(1, listener2.callCount)
    }
}
