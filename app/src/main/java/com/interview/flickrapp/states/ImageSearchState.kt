package com.interview.flickrapp.states

import com.interview.flickrapp.model.ImageItem


sealed class ImageSearchState {
    object Loading : ImageSearchState()
    data class Success(val images: List<ImageItem>) : ImageSearchState()
    data class Error(val message: String) : ImageSearchState()
}
