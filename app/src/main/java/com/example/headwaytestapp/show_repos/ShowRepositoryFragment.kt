package com.example.headwaytestapp.show_repos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.headwaytestapp.BaseFragment
import com.example.headwaytestapp.R
import com.example.headwaytestapp.databinding.ViewShowRepositoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.RecyclerView
import com.example.headwaytestapp.dao.Repository


@AndroidEntryPoint
class ShowRepositoryFragment : BaseFragment<ViewShowRepositoryBinding>() {

    private val viewModel: RepositoryListViewModel by viewModels()
    private val adapter = RepositoryAdapter(this::onItemClicked)
    private var currentPage = 1
    private var currentSearchKey = ""
    private var loading = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPostsRecyclerView()

        binding?.search?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.searchRepos(v?.text.toString(), currentPage)
                    currentSearchKey = v?.text.toString()

                    viewLifecycleOwner.lifecycleScope.launch {
                        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                            viewModel.repoListUiState.collect(
                                this@ShowRepositoryFragment::handleSearchRepositories
                            )
                        }
                    }
                    return true
                }
                return false
            }
        })

        binding?.recyclerview?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                {
                    val totalItemCount: Int? = recyclerView.layoutManager?.itemCount
                    if (totalItemCount != null) {
                        if (latestVisibleItem() == totalItemCount - 1) {
                            currentPage += 2
                            viewModel.searchRepos(currentSearchKey, currentPage)
                        }
                    }
                }
            }
        })

        binding?.searchLatestRepos?.setOnClickListener {
            findNavController().navigate(ShowRepositoryFragmentDirections.toLatestSearchReposFragment())
        }
    }

    private fun latestVisibleItem(): Int {
        val layoutManager = binding?.recyclerview?.layoutManager as LinearLayoutManager
        return layoutManager.findLastCompletelyVisibleItemPosition()
    }

    private fun handleSearchRepositories(state: UiStateManager) {
        when (state) {
            is UiStateManager.Loading -> {
            }
            is UiStateManager.Success -> {
                adapter.submitList(state.data)
            }
            is UiStateManager.Empty -> {
                //
            }
            is UiStateManager.Error -> {
            }
        }
    }

    private fun onItemClicked(repositoryItem: Repository) {
        val defaultBrowser = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER)
        defaultBrowser.data = Uri.parse(repositoryItem.htmlUrl)
        startActivity(defaultBrowser)
    }

    private fun initPostsRecyclerView() {
        binding?.apply {
            recyclerview.adapter = adapter
            recyclerview.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL ,false)
        }
    }

    override fun layoutId(): Int {
        return R.layout.view_show_repository
    }
}