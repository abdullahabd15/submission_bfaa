package com.abdullah.githubusers.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullah.githubusers.models.GithubUser
import com.abdullah.githubusers.repositories.GithubRepository
import com.abdullah.githubusers.state.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserFollowViewModel @Inject constructor(
    private val repository: GithubRepository
): ViewModel() {
    private val _userFollowers = MutableLiveData<RequestState<List<GithubUser>>>()
    val userFollowers get() = _userFollowers

    private val _userFollowing = MutableLiveData<RequestState<List<GithubUser>>>()
    val userFollowing get() = _userFollowing

    fun findUserFollowers() {
        _userFollowers.value = RequestState.Progress()
        viewModelScope.launch {
            val results = repository.findUserFollowers()
            if (results != null) {
                _userFollowers.value = RequestState.RequestSucceed(results)
            } else {
                _userFollowers.value = RequestState.RequestFailed("Failed to fetch data!")
            }
        }
    }

    fun findUserFollowing() {
        _userFollowing.value = RequestState.Progress()
        viewModelScope.launch {
            val results = repository.findUserFollowing()
            if (results != null) {
                _userFollowing.value = RequestState.RequestSucceed(results)
            } else {
                _userFollowing.value = RequestState.RequestFailed("Failed to fetch data!")
            }
        }
    }
}