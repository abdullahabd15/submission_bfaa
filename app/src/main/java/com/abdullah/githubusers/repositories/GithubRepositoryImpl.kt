package com.abdullah.githubusers.repositories

import com.abdullah.githubusers.api.GithubApi
import com.abdullah.githubusers.models.SearchUserData
import com.abdullah.githubusers.models.UserData
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    private val api: GithubApi
): GithubRepository {

    override suspend fun findUsers(): List<UserData>? {
        return try {
            val response = api.findUsers()
            if (response.isSuccessful) {
                response.body()
            } else {
                throw Exception(response.message())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(e.message)
        }
    }

    override suspend fun searchUser(query: String, page: Int): SearchUserData? {
        return try {
            val response = api.searchGithubUser(query, page)
            if (response.isSuccessful) {
                response.body()
            } else {
                throw Exception(response.message())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(e.message)
        }
    }

    override suspend fun getUser(userName: String): UserData? {
        return try {
            val response = api.getUser(userName)
            if (response.isSuccessful) {
                response.body()
            } else {
                throw Exception(response.message())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(e.message)
        }
    }

    override suspend fun findUserFollowers(userName: String, page: Int): List<UserData>? {
        return try {
            val response = api.findUserFollowers(userName, page)
            if (response.isSuccessful) {
                response.body()
            } else {
                throw Exception(response.message())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(e.message)
        }
    }

    override suspend fun findUserFollowing(userName: String, page: Int): List<UserData>? {
        return try {
            val response = api.findUserFollowing(userName, page)
            if (response.isSuccessful) {
                response.body()
            } else {
                throw Exception(response.message())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(e.message)
        }
    }
}