package com.abdullah.githubusers.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullah.githubusers.models.SearchUserData
import com.abdullah.githubusers.models.UserData
import com.abdullah.githubusers.repositories.GithubRepository
import com.abdullah.githubusers.state.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val githubRepository: GithubRepository
) : ViewModel() {

    private val _searchUserState =
        MutableLiveData<RequestState<SearchUserData>>(RequestState.Init())
    val searchUsersState get() = _searchUserState

    private val _findUsersState = MutableLiveData<RequestState<List<UserData>>?>()
    val findUsersState get() = _findUsersState

    private val _searchQuery = MutableLiveData<String?>()
    var searchQuery: String?
        get() = _searchQuery.value
        set(value) {
            _searchQuery.value = value
        }

    fun searchGithubUser(query: String, page: Int) {
        _searchUserState.value = RequestState.Progress()
        viewModelScope.launch {
            try {
                val results = githubRepository.searchUser(query, page)
                if (results != null) {
                    _searchUserState.value = RequestState.RequestSucceed(results)
                } else {
                    _searchUserState.value = RequestState.RequestFailed()
                }
            } catch (e: Exception) {
                _searchUserState.value = RequestState.RequestFailed(e.message)
            } finally {
                _searchUserState.value = RequestState.RequestFinished()
            }
        }
    }

    fun findUsers() {
        _findUsersState.value = RequestState.Progress()
        viewModelScope.launch {
            try {
                val results = githubRepository.findUsers()
                if (results != null) {
                    _findUsersState.value = RequestState.RequestSucceed(results)
                } else {
                    _findUsersState.value = RequestState.RequestFailed()
                }
            } catch (e: Exception) {
                _findUsersState.value = RequestState.RequestFailed(e.message)
            } finally {
                _findUsersState.value = RequestState.RequestFinished()
            }
        }
    }
}