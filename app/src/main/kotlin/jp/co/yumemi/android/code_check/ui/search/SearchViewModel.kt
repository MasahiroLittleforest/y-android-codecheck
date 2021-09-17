/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.yumemi.android.code_check.MainActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.model.GitHubRepository
import jp.co.yumemi.android.code_check.network.GitHubApi
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

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
        Log.d("ViewModel", "search")
        viewModelScope.launch {
            _apiStatus.value = ApiStatus.LOADING

            try {
                _gitHubRepositories.value = GitHubApi.retrofitService.searchRepos(inputText).items

                lastSearchDate = Date()

                _apiStatus.value = ApiStatus.DONE
            } catch (e: Exception) {
                _apiStatus.value = ApiStatus.ERROR
                Log.d("SearchViewModel", "${e.message}")
            }
        }
    }
}

