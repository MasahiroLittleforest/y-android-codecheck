/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import jp.co.yumemi.android.code_check.MainActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.model.GitHubRepository
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception
import java.util.*

const val API_URL = "https://api.github.com"

enum class ApiStatus {
    LOADING,
    ERROR,
    DONE
}

class SearchViewModel : ViewModel() {
    private val _gitHubRepositories = MutableLiveData<List<GitHubRepository>>(listOf())

    val gitHubRepositories: LiveData<List<GitHubRepository>>
        get() = _gitHubRepositories

    private val _apiStatus = MutableLiveData<ApiStatus>(ApiStatus.DONE)

    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus


    fun searchRepositories(inputText: String) {
        viewModelScope.launch {
            _apiStatus.value = ApiStatus.LOADING

            try {
                val httpClient = HttpClient(Android)
                val response: HttpResponse =
                    httpClient.get("${API_URL}/search/repositories") {
                        header("Accept", "application/vnd.github.v3+json")
                        parameter("q", inputText)
                    }

                httpClient.close()

                val jsonBody = JSONObject(response.receive<String>())
                val jsonItems = jsonBody.optJSONArray("items")

                val items = mutableListOf<GitHubRepository>()

                for (i in 0 until jsonItems.length()) {
                    val jsonItem = jsonItems.optJSONObject(i) ?: return@launch

                    val gitHubRepository = GitHubRepository(
                        name = jsonItem.optString("full_name"),
                        ownerIconUrl = (jsonItem.optJSONObject("owner") ?: return@launch).optString(
                            "avatar_url"
                        ),
                        language = jsonItem.optString("language"),
                        stargazersCount = jsonItem.optLong("stargazers_count"),
                        watchersCount = jsonItem.optLong("watchers_count"),
                        forksCount = jsonItem.optLong("forks_count"),
                        openIssuesCount = jsonItem.optLong("open_issues_count")
                    )

                    items.add(gitHubRepository)
                }

                _gitHubRepositories.value = items.toList()

                lastSearchDate = Date()

                _apiStatus.value = ApiStatus.DONE
            } catch (e: Exception) {
                _apiStatus.value = ApiStatus.ERROR
                Log.d("SearchViewModel", "${e.message}")
            }
        }
    }
}

