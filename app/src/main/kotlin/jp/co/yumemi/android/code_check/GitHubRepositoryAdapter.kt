package jp.co.yumemi.android.code_check

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.code_check.databinding.GithubRepositoryItemBinding
import jp.co.yumemi.android.code_check.model.GitHubRepository

class GitHubRepositoryAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<GitHubRepository, GitHubRepositoryAdapter.GitHubRepositoryViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<GitHubRepository>() {
        override fun areItemsTheSame(
            oldItem: GitHubRepository,
            newItem: GitHubRepository
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: GitHubRepository,
            newItem: GitHubRepository
        ): Boolean {
            return oldItem == newItem
        }
    }

    class GitHubRepositoryViewHolder(private var binding: GithubRepositoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(repo: GitHubRepository) {
            binding.gitHubRepository = repo
            binding.executePendingBindings()
        }
    }

    interface OnItemClickListener {
        fun itemClick(item: GitHubRepository)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubRepositoryViewHolder {
        return GitHubRepositoryViewHolder(
            GithubRepositoryItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: GitHubRepositoryViewHolder, position: Int) {
        val gitHubRepository = getItem(position)
        holder.bind(gitHubRepository)

        holder.itemView.setOnClickListener {
            itemClickListener.itemClick(gitHubRepository)
        }
    }
}