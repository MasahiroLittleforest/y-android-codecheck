/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import jp.co.yumemi.android.code_check.MainActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.databinding.FragmentRepositoryDetailBinding
import org.json.JSONObject

class RepositoryDetailFragment : Fragment(R.layout.fragment_repository_detail) {
    private val args: RepositoryDetailFragmentArgs by navArgs()

    private var _binding: FragmentRepositoryDetailBinding? = null
    private val binding get() = _binding!!

    var languageColor = Color.parseColor("#FF666666")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentRepositoryDetailBinding.inflate(inflater, container, false)
        _binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("検索した日時", lastSearchDate.toString())

        binding.apply {
            gitHubRepository = args.repository
            repositoryDetailFragment = this@RepositoryDetailFragment
        }

        args.repository.language?.let {
            languageColor = getLanguageColor(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getLanguageColor(languageName: String): Int {
        val file = "colors.json"
        val json = requireContext().assets.open(file).bufferedReader().use { it.readText() }
        val jsonBody = JSONObject(json)
        val jsonLanguage = jsonBody.optJSONObject(languageName)
        val color = jsonLanguage.optString("color")

        return Color.parseColor(makeOpaque(color))
    }

    private fun makeOpaque(colorString: String): String {
        val color = colorString.split("#")[1]
        return "#FF$color"
    }
}