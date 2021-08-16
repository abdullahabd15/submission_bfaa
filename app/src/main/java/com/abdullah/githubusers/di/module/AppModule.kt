package com.abdullah.githubusers.di.module

import android.content.Context
import com.abdullah.githubusers.repositories.GithubRepository
import com.abdullah.githubusers.repositories.GithubRepositoryImpl
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideGithubRepository(
        @ApplicationContext context: Context,
        gson: Gson
    ): GithubRepository = GithubRepositoryImpl(context, gson)
}