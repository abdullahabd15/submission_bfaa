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
class UserDetailViewModel @Inject constructor(
    private val githubRepository: GithubRepository
) : ViewModel() {

    private val _githubUserState = MutableLiveData<RequestState<UserData>>()
    val githubUserState get() = _githubUserState

    fun getGithubUser(userName: String) {
        _githubUserState.value = RequestState.Progress()
        viewModelScope.launch {
            try {
                val result = githubRepository.getUser(userName)
                if (result != null) {
                    _githubUserState.value = RequestState.RequestSucceed(result)
                } else {
                    _githubUserState.value = RequestState.RequestFailed()
                }
            } catch (e: Exception) {
                _githubUserState.value = RequestState.RequestFailed(e.message)
            } finally {
                _githubUserState.value = RequestState.RequestFinished()
            }
        }
    }
}