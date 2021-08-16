package com.abdullah.githubusers.viewmodels

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abdullah.githubusers.state.SplashState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(): ViewModel() {
    private val _splashState = MutableLiveData<SplashState>(SplashState.Initial)
    val splashState get() = _splashState

    fun splashScreenDelay(delayInMillis: Long) {
        _splashState.value = SplashState.Progress
        Handler(Looper.getMainLooper()).postDelayed({
            _splashState.value = SplashState.Done
        }, delayInMillis)
    }
}