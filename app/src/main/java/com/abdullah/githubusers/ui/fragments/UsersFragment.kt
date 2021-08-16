package com.abdullah.githubusers.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdullah.githubusers.databinding.FragmentUsersBinding
import com.abdullah.githubusers.extenstion.gone
import com.abdullah.githubusers.extenstion.visible
import com.abdullah.githubusers.models.GithubUser
import com.abdullah.githubusers.state.RequestState
import com.abdullah.githubusers.ui.adapter.GithubUserAdapter
import com.abdullah.githubusers.viewmodels.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersFragment : Fragment() {
    private var binding: FragmentUsersBinding? = null
    private var adapter: GithubUserAdapter? = null
    private val githubUsers: MutableList<GithubUser> = mutableListOf()
    private val usersViewModel: UsersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        observeGithubUsers()
        usersViewModel.findGithubUsers()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = GithubUserAdapter(githubUsers) {
            navigateToUserDetail(it)
        }
        binding?.rvGithubUsers?.apply {
            this.layoutManager = layoutManager
            this.adapter = this@UsersFragment.adapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeGithubUsers() {
        usersViewModel.githubUsersState.observe(viewLifecycleOwner, {
            when (it) {
                is RequestState.Progress -> showLoading()
                is RequestState.RequestSucceed -> {
                    hideLoading()
                    githubUsers.clear()
                    githubUsers.addAll(it.data ?: mutableListOf())
                    adapter?.notifyDataSetChanged()
                }
                is RequestState.RequestFailed -> {
                    hideLoading()
                }
            }
        })
    }

    private fun showLoading() {
        binding?.apply {
            progressBar.visible()
            rvGithubUsers.gone()
        }
    }

    private fun hideLoading() {
        binding?.apply {
            progressBar.gone()
            rvGithubUsers.visible()
        }
    }

    private fun navigateToUserDetail(user: GithubUser) {
        val direction = UsersFragmentDirections.actionUsersFragmentToUserDetailFragment(user)
        findNavController().navigate(direction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}