package com.abdullah.githubusers.repositories

import com.abdullah.githubusers.models.GithubUser

interface GithubRepository {
    suspend fun findAllUsers(): List<GithubUser>?
    suspend fun findUserFollowers(): List<GithubUser>?
    suspend fun findUserFollowing(): List<GithubUser>?
}