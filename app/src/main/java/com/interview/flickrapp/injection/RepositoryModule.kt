package com.interview.flickrapp.injection

import com.interview.flickrapp.repository.FlickrRepository
import com.interview.flickrapp.service.FlickrApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFlickrRepository(api: FlickrApi): FlickrRepository {
        return FlickrRepository(api)
    }
}
