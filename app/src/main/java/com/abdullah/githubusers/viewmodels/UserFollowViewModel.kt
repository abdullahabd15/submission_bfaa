package com.abdullah.githubusers.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullah.githubusers.models.UserData
import com.abdullah.githubusers.repositories.GithubRepository
import com.abdullah.githubusers.state.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserFollowViewModel @Inject constructor(
    private val githubRepository: GithubRepository
) : ViewModel() {
    private val _userFollowersState = MutableLiveData<RequestState<List<UserData>>>()
    val userFollowersState get() = _userFollowersState

    private val _userFollowingState = MutableLiveData<RequestState<List<UserData>>>()
    val userFollowingState get() = _userFollowingState

    fun findUserFollowers(userName: String, page: Int) {
        _userFollowersState.value = RequestState.Progress()
        viewModelScope.launch {
            try {
                val results = githubRepository.findUserFollowers(userName, page)
                if (results != null) {
                    _userFollowersState.value = RequestState.RequestSucceed(results)
                } else {
                    _userFollowersState.value = RequestState.RequestFailed()
                }
            } catch (e: Exception) {
                _userFollowersState.value = RequestState.RequestFailed(e.message)
            } finally {
                _userFollowersState.value = RequestState.RequestFinished()
            }
        }
    }

    fun findUserFollowing(userName: String, page: Int) {
        _userFollowingState.value = RequestState.Progress()
        viewModelScope.launch {
            try {
                val results = githubRepository.findUserFollowing(userName, page)
                if (results != null) {
                    _userFollowingState.value = RequestState.RequestSucceed(results)
                } else {
                    _userFollowingState.value = RequestState.RequestFailed()
                }
            } catch (e: Exception) {
                _userFollowingState.value = RequestState.RequestFailed(e.message)
            } finally {
                _userFollowingState.value = RequestState.RequestFinished()
            }
        }
    }
}