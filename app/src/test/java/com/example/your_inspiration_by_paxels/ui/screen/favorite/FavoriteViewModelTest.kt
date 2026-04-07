package com.example.your_inspiration_by_paxels.ui.screen.favorite

import com.example.your_inspiration_by_paxels.data.model.Photo
import com.example.your_inspiration_by_paxels.data.repository.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteViewModelTest {

    private val repository: PhotoRepository = mock()
    private lateinit var viewModel: FavoriteViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `favoritePhotos returns photos from repository`() = runTest {
        // Arrange
        val photos = listOf(Photo(1, "url", "photographer", "alt"))
        whenever(repository.getFavoritePhotos()).thenReturn(flowOf(photos))
        
        // Act
        viewModel = FavoriteViewModel(repository)
        
        // Assert
        // Gunakan .first() agar StateFlow mulai mengumpulkan data (karena WhileSubscribed)
        val result = viewModel.favoritePhotos.first()
        
        assertEquals(photos, result)
    }
}
