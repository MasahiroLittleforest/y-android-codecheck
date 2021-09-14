/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.util.Log
import androidx.lifecycle.ViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import jp.co.yumemi.android.code_check.MainActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.model.GitHubRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.lang.Exception
import java.util.*

const val API_URL = "https://api.github.com"

class SearchViewModel : ViewModel() {
    private val httpClient = HttpClient(Android)

    // 検索結果
    fun searchResults(inputText: String): List<GitHubRepository> = runBlocking {
        return@runBlocking GlobalScope.async {
            try {
                val response: HttpResponse =
                    httpClient.get("${API_URL}/search/repositories") {
                        header("Accept", "application/vnd.github.v3+json")
                        parameter("q", inputText)
                    }

                val jsonBody = JSONObject(response.receive<String>())
                val jsonItems = jsonBody.optJSONArray("items")

                val items = mutableListOf<GitHubRepository>()

                /**
                 * アイテムの個数分ループする
                 */
                for (i in 0 until jsonItems.length()) {
                    val jsonItem = jsonItems.optJSONObject(i)!!

                    val gitHubRepository = GitHubRepository(
                        name = jsonItem.optString("full_name"),
                        ownerIconUrl = jsonItem.optJSONObject("owner")!!.optString("avatar_url"),
                        language = jsonItem.optString("language"),
                        stargazersCount = jsonItem.optLong("stargazers_count"),
                        watchersCount = jsonItem.optLong("watchers_count"),
                        forksCount = jsonItem.optLong("forks_count"),
                        openIssuesCount = jsonItem.optLong("open_issues_count")
                    )

                    items.add(gitHubRepository)
                }

                lastSearchDate = Date()

                return@async items.toList()
            } catch (e: Exception) {
                Log.d("SearchViewModel", "${e.message}")
                return@async listOf()
            }
        }.await()
    }
}

