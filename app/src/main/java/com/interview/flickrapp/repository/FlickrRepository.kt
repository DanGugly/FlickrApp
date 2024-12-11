package com.interview.flickrapp.repository

import com.interview.flickrapp.model.ImageItem
import com.interview.flickrapp.service.FlickrApi
import javax.inject.Inject

class FlickrRepository @Inject constructor(private val api: FlickrApi) {
    suspend fun searchImages(tags: String): List<ImageItem> {
        return api.searchImages(tags).items
    }
}
