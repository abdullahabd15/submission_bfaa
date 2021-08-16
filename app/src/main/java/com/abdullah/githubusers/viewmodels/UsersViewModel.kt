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
class UsersViewModel @Inject constructor(
    private val githubRepository: GithubRepository
): ViewModel() {

    private val _githubUsersState = MutableLiveData<RequestState<List<GithubUser>>>()
    val githubUsersState get() = _githubUsersState

    fun findGithubUsers() {
        _githubUsersState.value = RequestState.Progress()
        viewModelScope.launch {
            val githubUsers = githubRepository.findAllUsers()
            if (githubUsers != null) {
                _githubUsersState.value = RequestState.RequestSucceed(githubUsers)
            } else {
                _githubUsersState.value = RequestState.RequestFailed("Failed to fetch data!")
            }
        }
    }
}