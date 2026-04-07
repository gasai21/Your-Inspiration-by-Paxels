package com.example.your_inspiration_by_paxels.ui.screen.search

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
class SearchViewModelTest {

    private val repository: PhotoRepository = mock()
    private lateinit var viewModel: SearchViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
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
        viewModel = SearchViewModel(repository)
        
        assertEquals("", viewModel.query.value)
        assertEquals(emptyList<Photo>(), viewModel.searchResult.value)
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `onQueryChange updates query state`() = runTest {
        viewModel = SearchViewModel(repository)
        val newQuery = "Nature"
        
        viewModel.onQueryChange(newQuery)
        
        assertEquals(newQuery, viewModel.query.value)
    }
}
