package com.abdullah.githubusers.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdullah.githubusers.BuildConfig
import com.abdullah.githubusers.databinding.FragmentUserFollowBinding
import com.abdullah.githubusers.models.GithubUser
import com.abdullah.githubusers.state.RequestState
import com.abdullah.githubusers.ui.adapter.GithubUserAdapter
import com.abdullah.githubusers.viewmodels.UserFollowViewModel
import dagger.hilt.android.AndroidEntryPoint

const val USER_FOLLOW_KEY = "${BuildConfig.APPLICATION_ID}.USER_FOLLOW_KEY"

@AndroidEntryPoint
class UserFollowFragment : Fragment() {
    private var binding: FragmentUserFollowBinding? = null
    private var adapter: GithubUserAdapter? = null
    private var flag: String = ""
    private val users: MutableList<GithubUser> = mutableListOf()

    private val userFollowViewModel: UserFollowViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            flag = it.getString(USER_FOLLOW_KEY, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserFollowBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        observeLiveData()
        when (flag) {
            USER_FOLLOWERS_FLAG -> userFollowViewModel.findUserFollowers()
            USER_FOLLOWING_FLAG -> userFollowViewModel.findUserFollowing()
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = GithubUserAdapter(users) {}
        binding?.rvUserFollow?.apply {
            this.layoutManager = layoutManager
            this.adapter = this@UserFollowFragment.adapter
        }
    }

    private fun observeLiveData() {
        userFollowViewModel.userFollowers.observe(viewLifecycleOwner, {
            if (it is RequestState.RequestSucceed) updateRecyclerViewContent(it.data)
        })
        userFollowViewModel.userFollowing.observe(viewLifecycleOwner, {
            if (it is RequestState.RequestSucceed) updateRecyclerViewContent(it.data)
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateRecyclerViewContent(githubUsers: List<GithubUser>?) {
        users.clear()
        users.addAll(githubUsers ?: listOf())
        adapter?.notifyDataSetChanged()
    }

    companion object {
        const val USER_FOLLOWERS_FLAG = "USER_FOLLOWERS_FLAG"
        const val USER_FOLLOWING_FLAG = "USER_FOLLOWING_FLAG"
        @JvmStatic
        fun newInstance(flag: String) =
            UserFollowFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_FOLLOW_KEY, flag)
                }
            }
    }
}