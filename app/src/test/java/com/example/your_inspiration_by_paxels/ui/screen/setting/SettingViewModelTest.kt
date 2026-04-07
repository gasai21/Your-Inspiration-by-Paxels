package com.example.your_inspiration_by_paxels.ui.screen.setting

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SettingViewModelTest {

    private lateinit var viewModel: SettingViewModel

    @Before
    fun setup() {
        viewModel = SettingViewModel()
    }

    @Test
    fun `initial isDarkMode is false`() {
        assertEquals(false, viewModel.isDarkMode.value)
    }

    @Test
    fun `setDarkMode updates isDarkMode`() {
        viewModel.setDarkMode(true)
        assertEquals(true, viewModel.isDarkMode.value)
    }
}
