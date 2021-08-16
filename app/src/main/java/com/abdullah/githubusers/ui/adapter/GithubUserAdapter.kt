package com.abdullah.githubusers.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abdullah.githubusers.databinding.ItemGithubUserBinding
import com.abdullah.githubusers.models.GithubUser
import com.bumptech.glide.Glide

class GithubUserAdapter(
    private val githubUsers: List<GithubUser>,
    private val onItemClicked: (GithubUser) -> Unit
): RecyclerView.Adapter<GithubUserAdapter.GithubUserViewHolder>() {

    inner class GithubUserViewHolder(
        private val binding: ItemGithubUserBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: GithubUser) {
            binding.apply {
                Glide.with(root)
                    .load(user.avatarUrl)
                    .dontAnimate()
                    .into(imgUser)
                val userName = "@${user.login}"
                tvUserName.text = userName

                root.setOnClickListener {
                    onItemClicked.invoke(user)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUserViewHolder {
        val binding = ItemGithubUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GithubUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GithubUserViewHolder, position: Int) {
        holder.bind(githubUsers[position])
    }

    override fun getItemCount(): Int = githubUsers.size
}