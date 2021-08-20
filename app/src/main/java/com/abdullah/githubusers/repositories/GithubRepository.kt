package com.abdullah.githubusers.repositories

import com.abdullah.githubusers.models.SearchUserData
import com.abdullah.githubusers.models.UserData

interface GithubRepository {
    suspend fun findUsers(): List<UserData>?
    suspend fun searchUser(query: String, page: Int): SearchUserData?
    suspend fun getUser(userName: String): UserData?
    suspend fun findUserFollowers(userName: String, page: Int): List<UserData>?
    suspend fun findUserFollowing(userName: String, page: Int): List<UserData>?
}