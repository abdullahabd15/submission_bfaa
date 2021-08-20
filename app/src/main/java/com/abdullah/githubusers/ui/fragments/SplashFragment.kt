package com.abdullah.githubusers.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navGraphViewModels
import com.abdullah.githubusers.R
import com.abdullah.githubusers.databinding.FragmentSplashBinding
import com.abdullah.githubusers.state.SplashState
import com.abdullah.githubusers.viewmodels.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private var binding: FragmentSplashBinding? = null
    private val splashViewModel: SplashViewModel by navGraphViewModels(R.id.main_navigation) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        splashViewModel.splashState.observe(viewLifecycleOwner, {
            when (it) {
                is SplashState.Initial -> splashViewModel.splashScreenDelay(4000)
                is SplashState.Done -> navigateToUsersFragment()
                else -> Unit
            }
        })
    }

    private fun navigateToUsersFragment() {
        val navController = NavHostFragment.findNavController(this)
        val direction = SplashFragmentDirections.actionSplashFragmentToUsersFragment()
        navController.navigate(direction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}