package com.abdullah.githubusers.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdullah.githubusers.BuildConfig
import com.abdullah.githubusers.const.Const
import com.abdullah.githubusers.databinding.FragmentUserFollowBinding
import com.abdullah.githubusers.extenstion.gone
import com.abdullah.githubusers.extenstion.showErrorMessage
import com.abdullah.githubusers.extenstion.visible
import com.abdullah.githubusers.models.UserData
import com.abdullah.githubusers.state.RequestState
import com.abdullah.githubusers.ui.adapter.GithubUserAdapter
import com.abdullah.githubusers.viewmodels.UserFollowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.ceil

const val USER_FOLLOW_KEY = "${BuildConfig.APPLICATION_ID}.USER_FOLLOW_KEY"
const val USER_DATA = "${BuildConfig.APPLICATION_ID}.USER_DATA"

@AndroidEntryPoint
class UserFollowFragment : Fragment() {
    private var binding: FragmentUserFollowBinding? = null
    private var adapter: GithubUserAdapter? = null
    private var flag: String = ""
    private var user: UserData? = null
    private var currentPage = 1
    private var totalUserFollow = 0
    private var totalPage = 0
    private var isLoading = false
    private val users: MutableList<UserData> = mutableListOf()

    private val userFollowViewModel: UserFollowViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            flag = it.getString(USER_FOLLOW_KEY, "")
            user = it.getParcelable(USER_DATA)
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
        requestUserFollow(currentPage)
        observeLiveData()

        totalUserFollow =
            if (flag == USER_FOLLOWERS_FLAG) user?.followers ?: 0 else user?.following ?: 0
        totalPage = ceil(totalUserFollow.toDouble() / Const.DEFAULT_REQUEST_PER_PAGE).toInt()
    }

    private fun requestUserFollow(page: Int) {
        user?.login?.let {
            when (flag) {
                USER_FOLLOWERS_FLAG -> userFollowViewModel.findUserFollowers(it, page)
                USER_FOLLOWING_FLAG -> userFollowViewModel.findUserFollowing(it, page)
            }
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = GithubUserAdapter(users) {
            navigateToUserDetail(it)
        }
        binding?.rvUserFollow?.apply {
            this.layoutManager = layoutManager
            this.adapter = this@UserFollowFragment.adapter
            this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    onRecyclerViewScrolled(recyclerView, dy)
                }
            })
        }
    }

    private fun observeLiveData() {
        userFollowViewModel.userFollowersState.observe(viewLifecycleOwner, {
            onRequestStateChange(it)
        })

        userFollowViewModel.userFollowingState.observe(viewLifecycleOwner, {
            onRequestStateChange(it)
        })
    }

    private fun onRequestStateChange(state: RequestState<List<UserData>>) {
        when (state) {
            is RequestState.Progress -> {
                binding?.animatedLoadingPaging?.visible()
                isLoading = true
            }
            is RequestState.RequestSucceed -> {
                val users = state.data
                if (!users.isNullOrEmpty()) {
                    updateRecyclerViewContent(users)
                }
            }
            is RequestState.RequestFailed -> showErrorMessage(state.message)
            is RequestState.RequestFinished -> {
                isLoading = false
                binding?.animatedLoadingPaging?.gone()
            }
            else -> Unit
        }
    }

    private fun updateRecyclerViewContent(users: List<UserData>) {
        this.users.addAll(users)
        adapter?.notifyAllItemChanged()
    }

    private fun navigateToUserDetail(userData: UserData) {
        val action = UserDetailFragmentDirections.actionUserDetailFragmentSelf(userData)
        findNavController().navigate(action)
    }

    private fun onRecyclerViewScrolled(recyclerView: RecyclerView, dy: Int) {
        if (dy > 0) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val itemCount = layoutManager.itemCount
            val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()
            val isLastVisible = itemCount.minus(1) == lastVisible
            if (isLastVisible && (currentPage < totalPage) && !isLoading) {
                currentPage++
                requestUserFollow(currentPage)
            }
        }
    }

    companion object {
        const val USER_FOLLOWERS_FLAG = "USER_FOLLOWERS_FLAG"
        const val USER_FOLLOWING_FLAG = "USER_FOLLOWING_FLAG"

        @JvmStatic
        fun newInstance(flag: String, user: UserData?) =
            UserFollowFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_DATA, user)
                    putString(USER_FOLLOW_KEY, flag)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}