package com.abdullah.githubusers.state

sealed class SplashState {
    object Initial: SplashState()
    object Progress : SplashState()
    object Done : SplashState()
}
