package com.abdullah.githubusers.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.abdullah.githubusers.ui.fragments.UserFollowFragment

class UserFollowPagerAdapter(
    fragment: Fragment
): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UserFollowFragment.newInstance(UserFollowFragment.USER_FOLLOWERS_FLAG)
            1 -> UserFollowFragment.newInstance(UserFollowFragment.USER_FOLLOWING_FLAG)
            else -> throw Exception("Unknown Position")
        }
    }
}