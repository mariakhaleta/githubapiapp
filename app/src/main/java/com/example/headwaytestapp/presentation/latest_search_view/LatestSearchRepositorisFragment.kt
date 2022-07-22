package com.example.headwaytestapp.presentation.latest_search_view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.headwaytestapp.presentation.BaseFragment
import com.example.headwaytestapp.R
import com.example.headwaytestapp.data.dao.Repository
import com.example.headwaytestapp.databinding.ViewLatestSearchListBinding
import com.example.headwaytestapp.presentation.show_repos_view.RepositoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LatestSearchReposFragment : BaseFragment<ViewLatestSearchListBinding>() {

    private val viewModel: LatestSearchReposViewModel by viewModels()
    private val adapter = RepositoryAdapter(this::onItemClicked)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPostsRecyclerView()

        viewModel.showLatestSearch()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.repoListState.collect { showList(it) }
            }
        }
    }

    private fun showList(list: List<Repository>) {
        if (list.isNullOrEmpty()) {
            binding?.noSavedReposText?.visibility = View.VISIBLE
        } else {
            binding?.noSavedReposText?.visibility = View.GONE
            adapter.submitList(list)
        }
    }


    private fun initPostsRecyclerView() {
        binding?.apply {
            recyclerview.adapter = adapter
            recyclerview.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )
        }
    }

    private fun onItemClicked(repositoryItem: Repository) {
        val defaultBrowser =
            Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER)
        defaultBrowser.data = Uri.parse(repositoryItem.htmlUrl)
        startActivity(defaultBrowser)
    }

    override fun layoutId(): Int {
        return R.layout.view_latest_search_list
    }
}