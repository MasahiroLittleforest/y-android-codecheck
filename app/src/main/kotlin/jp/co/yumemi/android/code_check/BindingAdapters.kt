package jp.co.yumemi.android.code_check

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import jp.co.yumemi.android.code_check.model.GitHubRepository

@BindingAdapter("ownerIconUrl")
fun bindOwnerIconImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri)
    }
}

@BindingAdapter("gitHubRepositories")
fun bindReposRecyclerView(recyclerView: RecyclerView, repos: List<GitHubRepository>?) {
    val adapter = recyclerView.adapter as GitHubRepositoryAdapter
    adapter.submitList(repos)
}