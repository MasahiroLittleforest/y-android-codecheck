package jp.co.yumemi.android.code_check

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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

    class GitHubRepositoryViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface OnItemClickListener {
        fun itemClick(item: GitHubRepository)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubRepositoryViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.github_repository_item, parent, false)
        return GitHubRepositoryViewHolder(layout)
    }

    override fun onBindViewHolder(holder: GitHubRepositoryViewHolder, position: Int) {
        val gitHubRepository = getItem(position)
        (holder.itemView.findViewById<View>(R.id.repositoryNameView) as TextView).text =
            gitHubRepository.name

        holder.itemView.setOnClickListener {
            itemClickListener.itemClick(gitHubRepository)
        }
    }
}