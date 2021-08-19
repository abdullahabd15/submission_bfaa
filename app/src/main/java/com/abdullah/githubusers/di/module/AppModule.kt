package com.abdullah.githubusers.di.module

import com.abdullah.githubusers.BuildConfig
import com.abdullah.githubusers.api.GithubApi
import com.abdullah.githubusers.repositories.GithubRepository
import com.abdullah.githubusers.repositories.GithubRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGithubRepository(
        api: GithubApi
    ): GithubRepository = GithubRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideGithubApi(
        retrofitBuilder: Retrofit.Builder
    ): GithubApi =
        retrofitBuilder.baseUrl(BuildConfig.BASE_URL).build().create(GithubApi::class.java)

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        urlHeaderInterceptor: Interceptor
    ): Retrofit.Builder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().addInterceptor(urlHeaderInterceptor).build())

    @Provides
    @Singleton
    fun provideUrlHeaderInterceptor(): Interceptor = Interceptor { chain ->
        val request: Request = chain.request().newBuilder()
            .addHeader("Accept", "application/vnd.github.v3+json")
            .build()
        chain.proceed(request)
    }
}