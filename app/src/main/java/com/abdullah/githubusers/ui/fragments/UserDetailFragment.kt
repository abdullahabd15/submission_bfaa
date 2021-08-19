package com.abdullah.githubusers.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.abdullah.githubusers.R
import com.abdullah.githubusers.const.Const
import com.abdullah.githubusers.databinding.FragmentUserDetailBinding
import com.abdullah.githubusers.extenstion.gone
import com.abdullah.githubusers.extenstion.loadImageByUrl
import com.abdullah.githubusers.extenstion.showErrorMessage
import com.abdullah.githubusers.extenstion.visible
import com.abdullah.githubusers.models.UserData
import com.abdullah.githubusers.state.RequestState
import com.abdullah.githubusers.ui.adapter.UserFollowPagerAdapter
import com.abdullah.githubusers.utils.StringUtil
import com.abdullah.githubusers.viewmodels.UserDetailViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailFragment : Fragment() {
    private var binding: FragmentUserDetailBinding? = null
    private var pagerAdapter: UserFollowPagerAdapter? = null
    private val githubUser get() = args.githubUser

    private val args: UserDetailFragmentArgs by navArgs()
    private val userDetailViewModel: UserDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()

        observeGithubUser()
        githubUser.login?.let { userDetailViewModel.getGithubUser(it) }
    }

    private fun initToolbar() {
        binding?.apply {
            collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT)
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.menu_share) {
                    shareUserProfile()
                }
                false
            }
        }
    }

    private fun shareUserProfile() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = Const.INTENT_TYPE_TEXT
            var shareMessage = getString(R.string.greetings_share)
            shareMessage = "${shareMessage}${githubUser.htmlUrl}"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, getString(R.string.choose_one)))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun observeGithubUser() {
        userDetailViewModel.githubUserState.observe(viewLifecycleOwner, {
            when (it) {
                is RequestState.Progress -> showLoading()
                is RequestState.RequestSucceed -> {
                    hideLoading()
                    loadDataToView(it.data)
                    setupViewPager(it.data)
                }
                is RequestState.RequestFailed -> {
                    hideLoading()
                    showErrorMessage(it.message)
                }
                else -> Unit
            }
        })
    }

    private fun loadDataToView(userDetail: UserData?) {
        binding?.apply {
            imgUser.loadImageByUrl(userDetail?.avatarUrl)
            tvFullName.text = userDetail?.name
            val userName = "@${userDetail?.login}"
            tvUserName.text = userName
            userDetail?.run {
                location?.let {
                    tvLocation.visible()
                    tvLocation.text = it
                }
                company?.let {
                    tvCompany.visible()
                    tvCompany.text = it
                }
                bio?.let {
                    tvBio.visible()
                    tvBio.text = it
                }
                name?.let {
                    toolbar.title = it
                }
            }
        }
    }

    private fun setupViewPager(userData: UserData?) {
        pagerAdapter = UserFollowPagerAdapter(this, userData)
        val followersCount = StringUtil.formatWithNumericUnit(context, userData?.followers)
        val followingCount = StringUtil.formatWithNumericUnit(context, userData?.following)
        val followersTitle = "$followersCount\n${getString(R.string.followers)}"
        val followingTitle = "$followingCount\n${getString(R.string.following)}"
        binding?.apply {
            viewPager.adapter = pagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
                when (pos) {
                    0 -> tab.text = followersTitle
                    1 -> tab.text = followingTitle
                }
            }.attach()
        }
    }

    private fun showLoading() {
        binding?.apply {
            animatedLoading.visible()
            layoutUser.gone()
            scrollView.gone()
        }
    }

    private fun hideLoading() {
        binding?.apply {
            animatedLoading.gone()
            layoutUser.visible()
            scrollView.visible()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}