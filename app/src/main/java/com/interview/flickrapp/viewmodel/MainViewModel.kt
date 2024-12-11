package com.interview.flickrapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.flickrapp.model.ImageItem
import com.interview.flickrapp.repository.FlickrRepository
import com.interview.flickrapp.states.ImageSearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: FlickrRepository) : ViewModel() {

    private val _state = MutableStateFlow<ImageSearchState>(ImageSearchState.Loading)
    val state: StateFlow<ImageSearchState> = _state

    init {
        // You can call searchImages with a default query if needed.
        searchImages("porcupine")
    }

    fun searchImages(tags: String) {
        viewModelScope.launch {
            _state.value = ImageSearchState.Loading
            try {
                // Assuming repository.searchImages returns a raw response
                val response = repository.searchImages(tags)
                Log.d("API Response", response.toString()) // Log raw response
                _state.value = ImageSearchState.Success(response)
            } catch (e: Exception) {
                Log.e("error fl", e.message.toString())
                _state.value = ImageSearchState.Error("Failed to load images")
            }
        }
    }

}
