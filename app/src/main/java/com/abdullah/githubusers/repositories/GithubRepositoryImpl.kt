package com.abdullah.githubusers.repositories

import android.content.Context
import com.abdullah.githubusers.baseclass.BaseRepository
import com.abdullah.githubusers.models.GithubUser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    context: Context,
    private val gson: Gson
): BaseRepository(context), GithubRepository {

    override suspend fun findAllUsers(): List<GithubUser>? {
        return try {
            val jsonGithubUser = assetsFileToJson("json/github_users.json")
            val listType = object : TypeToken<List<GithubUser>>() {}.type
            gson.fromJson(jsonGithubUser, listType)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun findUserFollowers(): List<GithubUser>? {
        return try {
            val jsonGithubUser = assetsFileToJson("json/user_followers.json")
            val listType = object : TypeToken<List<GithubUser>>() {}.type
            gson.fromJson(jsonGithubUser, listType)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun findUserFollowing(): List<GithubUser>? {
        return try {
            val jsonGithubUser = assetsFileToJson("json/user_following.json")
            val listType = object : TypeToken<List<GithubUser>>() {}.type
            gson.fromJson(jsonGithubUser, listType)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}