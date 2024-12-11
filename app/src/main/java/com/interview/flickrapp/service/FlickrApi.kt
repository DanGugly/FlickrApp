package com.interview.flickrapp.service

import com.interview.flickrapp.model.FlickrResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {
    @GET("services/feeds/photos_public.gne")
    suspend fun searchImages(@Query("tags") tags: String, @Query("format") format: String = "json", @Query("nojsoncallback") noJsonCallback: Int = 1): FlickrResponse
}

//https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1&tags=por
