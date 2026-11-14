package com.nazarethlabs.codex.ui.toolbar.button

import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ToolbarButtonFavoritesFilterComponentTest {
    @Test
    fun `should build favorites filter button`() {
        val component = ToolbarButtonFavoritesFilterComponent()

        val button = component.build()

        assertNotNull(button)
        assertNotNull(button.icon)
    }
}
