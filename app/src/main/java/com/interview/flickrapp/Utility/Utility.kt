package com.interview.flickrapp.Utility

import com.google.gson.Gson
import com.interview.flickrapp.model.ImageItem
import java.net.URLDecoder

object Utility {
    fun serializeImageItem(imageItem: ImageItem): String {
        val gson = Gson()
        return gson.toJson(imageItem)
    }

    fun parseImageItem(jsonString: String): ImageItem {
        val gson = Gson()
        return gson.fromJson(URLDecoder.decode(jsonString, "UTF-8"), ImageItem::class.java)
    }

}