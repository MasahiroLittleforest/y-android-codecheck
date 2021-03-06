/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.common.hideKeyboard
import jp.co.yumemi.android.code_check.databinding.FragmentSearchBinding
import jp.co.yumemi.android.code_check.model.GitHubRepository

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val searchViewModel: SearchViewModel by viewModels()

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentSearchBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        val adapter =
            GitHubRepositoryAdapter(object : GitHubRepositoryAdapter.OnItemClickListener {
                override fun itemClick(item: GitHubRepository) {
                    gotoRepositoryFragment(item)
                }
            })

        binding.apply {
            viewModel = searchViewModel
            lifecycleOwner = viewLifecycleOwner
            searchInputText.setOnEditorActionListener { editText, action, _ ->
                onQuerySubmit(editText, action)
            }
            reposRecyclerView.also {
                it.layoutManager = layoutManager
                it.addItemDecoration(dividerItemDecoration)
                it.adapter = adapter
            }
        }
    }

    private fun onQuerySubmit(editText: TextView, action: Any): Boolean {
        if (action == EditorInfo.IME_ACTION_SEARCH) {
            editText.text.toString().let {
                editText.hideKeyboard()
                searchViewModel.searchRepositories(it)
            }
            return true
        }
        return false
    }

    private fun gotoRepositoryFragment(gitHubRepository: GitHubRepository) {
        val action = SearchFragmentDirections
            .actionRepositoriesFragmentToRepositoryFragment(repository = gitHubRepository)
        findNavController().navigate(action)
    }
}
