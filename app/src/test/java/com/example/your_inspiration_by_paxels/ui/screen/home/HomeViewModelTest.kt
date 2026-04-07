package com.example.your_inspiration_by_paxels.ui.screen.home

import com.example.your_inspiration_by_paxels.data.model.Photo
import com.example.your_inspiration_by_paxels.data.repository.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val repository: PhotoRepository = mock()
    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Stubbing repository methods to return empty lists by default instead of null
        whenever(repository.getPhotos()).thenReturn(flowOf(emptyList()))
        runTest {
            whenever(repository.fetchCuratedPhotos(anyInt())).thenReturn(emptyList())
            whenever(repository.searchRemotePhotos(anyString(), anyInt())).thenReturn(emptyList())
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        viewModel = HomeViewModel(repository)
        
        assertEquals(emptyList<Photo>(), viewModel.photos.value)
        assertEquals(false, viewModel.isLoading.value)
        assertEquals("All", viewModel.selectedCategory.value)
    }

    @Test
    fun `setCategory updates selectedCategory and resets photos`() = runTest {
        viewModel = HomeViewModel(repository)
        val category = "Nature"
        
        viewModel.setCategory(category)
        
        assertEquals(category, viewModel.selectedCategory.value)
        // In setCategory, photos are explicitly set to emptyList()
        assertEquals(emptyList<Photo>(), viewModel.photos.value)
    }
}
