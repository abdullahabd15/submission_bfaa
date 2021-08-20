package com.abdullah.githubusers.api

import com.abdullah.githubusers.models.SearchUserData
import com.abdullah.githubusers.models.UserData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    @GET("users")
    suspend fun findUsers(): Response<List<UserData>>

    @GET("search/users")
    suspend fun searchGithubUser(
        @Query("q")query: String,
        @Query("page") page: Int
    ): Response<SearchUserData>

    @GET("users/{userName}")
    suspend fun getUser(
        @Path("userName") userName: String
    ): Response<UserData>

    @GET("users/{username}/followers")
    suspend fun findUserFollowers(
        @Path("username") userName: String,
        @Query("page") page: Int
    ): Response<List<UserData>>

    @GET("users/{username}/following")
    suspend fun findUserFollowing(
        @Path("username") userName: String,
        @Query("page") page: Int
    ): Response<List<UserData>>
}