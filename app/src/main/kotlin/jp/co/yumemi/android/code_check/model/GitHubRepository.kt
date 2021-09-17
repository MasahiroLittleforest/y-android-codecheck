package jp.co.yumemi.android.code_check.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

data class ReposResponse(
    val items: List<GitHubRepository>
)

@Parcelize
data class GitHubRepository(
    val name: String,
    val owner: RepositoryOwner,
    val language: String?,
    @Json(name = "stargazers_count") val stargazersCount: Long,
    @Json(name = "watchers_count") val watchersCount: Long,
    @Json(name = "forks_count") val forksCount: Long,
    @Json(name = "open_issues_count") val openIssuesCount: Long
) : Parcelable {
    @Parcelize
    data class RepositoryOwner(
        @Json(name = "avatar_url") val iconUrl: String
    ) : Parcelable
}
