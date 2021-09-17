package jp.co.yumemi.android.code_check

import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.ColorInt
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

@BindingAdapter("apiStatus")
fun bindStatusToProgressBar(progressBar: ProgressBar, status: ApiStatus) {
    when (status) {
        ApiStatus.LOADING -> {
            progressBar.visibility = View.VISIBLE
        }
        ApiStatus.ERROR -> {
            progressBar.visibility = View.VISIBLE
        }
        ApiStatus.DONE -> {
            progressBar.visibility = View.GONE
        }
    }
}

@BindingAdapter("apiStatus")
fun bindStatusToRecyclerView(recyclerView: RecyclerView, status: ApiStatus) {
    when (status) {
        ApiStatus.LOADING -> {
            recyclerView.visibility = View.INVISIBLE
        }
        ApiStatus.ERROR -> {
            recyclerView.visibility = View.INVISIBLE
        }
        ApiStatus.DONE -> {
            recyclerView.visibility = View.VISIBLE
        }
    }
}

@BindingAdapter("languageColor")
fun ImageView.setImageTint(@ColorInt color: Int) {
    setColorFilter(color, PorterDuff.Mode.SRC_IN)
}