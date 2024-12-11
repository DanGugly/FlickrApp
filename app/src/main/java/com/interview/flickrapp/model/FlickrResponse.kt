package com.interview.flickrapp.model

data class FlickrResponse(
    val title: String,
    val link: String,
    val description: String,
    val modified: String,
    val generator: String,
    val items: List<ImageItem>
)

data class ImageItem(
    val title: String,
    val link: String,
    val media: Media,
    val date_taken: String,
    val description: String,
    val published: String,
    val author: String,
    val author_id: String,
    val tags: String
)

data class Media(
    val m: String // URL of the image
)

