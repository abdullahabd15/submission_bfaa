package com.abdullah.githubusers.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abdullah.githubusers.databinding.ItemGithubUserBinding
import com.abdullah.githubusers.extenstion.loadImageByUrl
import com.abdullah.githubusers.models.UserData

class GithubUserAdapter(
    private val users: List<UserData>,
    private val onItemClicked: (UserData) -> Unit
): RecyclerView.Adapter<GithubUserAdapter.GithubUserViewHolder>() {

    inner class GithubUserViewHolder(
        private val binding: ItemGithubUserBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(userData: UserData) {
            binding.apply {
                imgUser.loadImageByUrl(userData.avatarUrl)
                val userName = "@${userData.login}"
                tvUserName.text = userName
                tvType.text = userData.type
                root.setOnClickListener {
                    onItemClicked.invoke(userData)
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
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    @SuppressLint("NotifyDataSetChanged")
    fun notifyAllItemChanged() {
        try {
            notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}