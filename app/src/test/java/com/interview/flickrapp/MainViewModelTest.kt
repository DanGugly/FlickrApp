package com.interview.flickrapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.interview.flickrapp.model.ImageItem
import com.interview.flickrapp.model.Media
import com.interview.flickrapp.model.FlickrResponse
import com.interview.flickrapp.states.ImageSearchState
import com.interview.flickrapp.repository.FlickrRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel

    private val repository: FlickrRepository = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = MainViewModel(repository)
    }

    @Test
    fun `searchImages updates state to Success when data is fetched`() = runTest {
        // Given a successful response from the repository with mocked data
        val expectedImages = listOf(
            ImageItem(
                title = "Dogs of Colombia.",
                link = "https://www.flickr.com/photos/djbdjb/54197184922/",
                media = Media(m = "https://live.staticflickr.com/65535/54197184922_dbd1fb70fe_m.jpg"),
                date_taken = "2024-01-30T17:20:59-08:00",
                description = "<p><a href=\"https://www.flickr.com/people/djbdjb/\">burbadj</a> posted a photo:</p>",
                published = "2024-12-11T18:32:25Z",
                author = "nobody@flickr.com (\"burbadj\")",
                tags = "",
                author_id = ""
            )
        )

        // Mocking the repository to return a FlickrResponse containing expected images
        coEvery { repository.searchImages(any()) } returns FlickrResponse(expectedImages)

        // When searchImages is called
        viewModel.searchImages("dog")

        // Then the state should be updated to Success with expected images
        assertEquals(ImageSearchState.Success(expectedImages), viewModel.state.value)
    }


    @Test
    fun `searchImages updates state to Error when fetching fails`() = runTest {
        // Given an exception thrown by the repository
        coEvery { repository.searchImages(any()) } throws Exception("Network error")

        // When searchImages is called
        viewModel.searchImages("dog")

        // Then the state should be updated to Error with appropriate message
        assertEquals(ImageSearchState.Error("Failed to load images"), viewModel.state.value)
    }
}
