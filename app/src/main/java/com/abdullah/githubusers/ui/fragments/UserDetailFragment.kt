package com.abdullah.githubusers.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.abdullah.githubusers.R
import com.abdullah.githubusers.databinding.FragmentUserDetailBinding
import com.abdullah.githubusers.extenstion.visible
import com.abdullah.githubusers.ui.adapter.UserFollowPagerAdapter
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserDetailFragment : Fragment() {
    private var binding: FragmentUserDetailBinding? = null
    private var pagerAdapter: UserFollowPagerAdapter? = null
    private val githubUser get() = args.githubUser

    private val args: UserDetailFragmentArgs by navArgs()

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
        initViewPager()
        loadDataToView()
    }

    private fun initToolbar() {
        binding?.apply {
            toolbar.title = githubUser.name
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
            shareIntent.type = "text/plain"
            var shareMessage = getString(R.string.greetings_share)
            shareMessage = "${shareMessage}${githubUser.htmlUrl}"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, getString(R.string.choose_one)))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadDataToView() {
        binding?.apply {
            Glide.with(root.context)
                .load(githubUser.avatarUrl)
                .dontAnimate()
                .into(imgUser)
            tvFullName.text = githubUser.name
            val userName = "@${githubUser.login}"
            tvUserName.text = userName

            githubUser.location?.let {
                tvLocation.visible()
                tvLocation.text = it
            }
            githubUser.company?.let {
                tvCompany.visible()
                tvCompany.text = it
            }
            githubUser.bio?.let {
                tvBio.visible()
                tvBio.text = it
            }
        }
    }

    private fun initViewPager() {
        pagerAdapter = UserFollowPagerAdapter(this)
        binding?.apply {
            viewPager.adapter = pagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
                when (pos) {
                    0 -> tab.text = getString(R.string.followers)
                    1 -> tab.text = getString(R.string.following)
                }
            }.attach()
        }
    }
}