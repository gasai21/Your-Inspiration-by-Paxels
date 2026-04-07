package com.example.your_inspiration_by_paxels.ui.screen.detail

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
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    private val repository: PhotoRepository = mock()
    private lateinit var viewModel: DetailViewModel
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
    fun `getPhotoById returns photo from repository`() = runTest {
        // Arrange
        val photo = Photo(1, "url", "photographer", "alt")
        whenever(repository.getPhotoById(1)).thenReturn(flowOf(photo))
        viewModel = DetailViewModel(repository)

        // Act & Assert
        // Gunakan .first() untuk menunggu emisi data pertama yang bukan null
        val result = viewModel.getPhotoById(1).first { it != null }
        
        assertEquals(photo, result)
    }

    @Test
    fun `toggleFavorite calls repository toggleFavorite`() = runTest {
        // Arrange
        val photo = Photo(1, "url", "photographer", "alt")
        viewModel = DetailViewModel(repository)
        
        // Act
        viewModel.toggleFavorite(photo)
        
        // Assert
        verify(repository).toggleFavorite(photo)
    }
}
