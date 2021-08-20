package com.abdullah.githubusers.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdullah.githubusers.R
import com.abdullah.githubusers.const.Const
import com.abdullah.githubusers.databinding.FragmentUsersBinding
import com.abdullah.githubusers.extenstion.gone
import com.abdullah.githubusers.extenstion.showErrorMessage
import com.abdullah.githubusers.extenstion.visible
import com.abdullah.githubusers.models.SearchUserData
import com.abdullah.githubusers.models.UserData
import com.abdullah.githubusers.state.RequestState
import com.abdullah.githubusers.ui.adapter.GithubUserAdapter
import com.abdullah.githubusers.viewmodels.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.ceil

@AndroidEntryPoint
class UsersFragment : Fragment() {
    private var binding: FragmentUsersBinding? = null
    private var adapter: GithubUserAdapter? = null
    private var searchUserData: SearchUserData? = null
    private var isOnSearching = false
    private var isOnLoadingSearch = false
    private var currentPage = 1
    private val users: MutableList<UserData> = mutableListOf()
    private val usersViewModel: UsersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)?.setSupportActionBar(binding?.toolbar)

        initRecyclerView()
        observeGithubUsers()
        observeSearchUser()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val searchView = menu.findItem(R.id.menu_search).actionView as SearchView?
        searchView?.queryHint = getString(R.string.search_github_user)
        usersViewModel.searchQuery?.let {
            isOnSearching = true
            searchView?.setQuery(it, false)
            searchView?.setIconifiedByDefault(false)
        }
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    isOnSearching = true
                    users.clear()
                    currentPage = 1
                    usersViewModel.searchGithubUser(it, 1)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                usersViewModel.searchQuery = newText
                return false
            }
        })
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = GithubUserAdapter(users) {
            navigateToUserDetail(it)
        }
        binding?.rvGithubUsers?.apply {
            this.layoutManager = layoutManager
            this.adapter = this@UsersFragment.adapter
            this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (isOnSearching) {
                        onRecyclerViewScrolled(recyclerView, dy)
                    }
                }
            })
        }
    }

    private fun observeGithubUsers() {
        usersViewModel.findUsersState.observe(viewLifecycleOwner, {
            when (it) {
                is RequestState.Progress -> showLoading()
                is RequestState.RequestSucceed -> {
                    hideLoading()
                    updateRecyclerViewItems(it.data)
                }
                is RequestState.RequestFailed -> {
                    hideLoading()
                }
                else -> Unit
            }
        })
    }

    private fun observeSearchUser() {
        usersViewModel.searchUsersState.observe(viewLifecycleOwner, {
            when (it) {
                is RequestState.Init -> {
                    users.clear()
                    usersViewModel.findUsers()
                }
                is RequestState.Progress -> {
                    if (currentPage > 1) showLoadingPaging() else showLoading()
                }
                is RequestState.RequestSucceed -> {
                    if (currentPage > 1) hideLoadingPaging() else {
                        binding?.rvGithubUsers?.smoothScrollToPosition(0)
                        hideLoading()
                    }
                    searchUserData = it.data
                    updateRecyclerViewItems(it.data?.items)
                }
                is RequestState.RequestFailed -> {
                    if (currentPage > 1) hideLoadingPaging() else hideLoading()
                    showErrorMessage(it.message)
                }
                else -> Unit
            }
        })
    }

    private fun updateRecyclerViewItems(users: List<UserData>?) {
        if (!users.isNullOrEmpty()) {
            this.users.addAll(users)
            adapter?.notifyAllItemChanged()
        } else {
            showErrorMessage(getString(R.string.not_found))
        }
    }

    private fun showLoading() {
        isOnLoadingSearch = true
        binding?.apply {
            animatedLoading.visible()
            rvGithubUsers.gone()
        }
    }

    private fun hideLoading() {
        isOnLoadingSearch = false
        binding?.apply {
            animatedLoading.gone()
            rvGithubUsers.visible()
        }
    }

    private fun showLoadingPaging() {
        binding?.animatedLoadingPaging?.visible()
    }

    private fun hideLoadingPaging() {
        binding?.animatedLoadingPaging?.gone()
    }

    private fun navigateToUserDetail(userData: UserData) {
        val direction = UsersFragmentDirections.actionUsersFragmentToUserDetailFragment(userData)
        findNavController().navigate(direction)
    }

    private fun onRecyclerViewScrolled(recyclerView: RecyclerView, dy: Int) {
        if (dy > 0) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val itemCount = layoutManager.itemCount
            val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()
            val isLastVisible = itemCount.minus(1) == lastVisible
            searchUserData?.totalCount?.let { total ->
                val totalPage = ceil(total.toDouble() / Const.DEFAULT_REQUEST_PER_PAGE).toInt()
                if (isLastVisible && (currentPage < totalPage) && !isOnLoadingSearch) {
                    currentPage++
                    usersViewModel.searchQuery?.let {
                        usersViewModel.searchGithubUser(it, currentPage)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}