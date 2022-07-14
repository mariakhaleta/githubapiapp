package com.example.headwaytestapp.show_repos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
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
import com.example.headwaytestapp.network.NetworkUtils


@AndroidEntryPoint
class ShowRepositoryFragment : BaseFragment<ViewShowRepositoryBinding>() {

    private val viewModel: RepositoryListViewModel by viewModels()
    private val adapter = RepositoryAdapter(this::onItemClicked)
    private var currentPage = 1
    private var currentSearchKey = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPostsRecyclerView()
        initNetworkChangesListener()

        binding?.search?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.searchRepos(v?.text.toString(), currentPage)
                    currentSearchKey = v?.text.toString()
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
        }) // Pagination

        binding?.searchLatestRepos?.setOnClickListener {
            findNavController().navigate(ShowRepositoryFragmentDirections.toLatestSearchReposFragment())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.repoListUiState.collect(
                    this@ShowRepositoryFragment::handleSearchRepositories
                )
            }
        }
    }

    private fun latestVisibleItem(): Int {
        val layoutManager = binding?.recyclerview?.layoutManager as LinearLayoutManager
        return layoutManager.findLastCompletelyVisibleItemPosition()
    }

    private fun handleSearchRepositories(state: UiStateManager) {
        when (state) {
            is UiStateManager.Loading -> {
                binding?.progressBar?.visibility = VISIBLE
            }
            is UiStateManager.Success -> {
                binding?.progressBar?.visibility = GONE
                adapter.submitList(state.data)
            }
            is UiStateManager.Empty -> {
                Toast.makeText(context, "Your searched key is incorrect", Toast.LENGTH_LONG).show()
            }
            is UiStateManager.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
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

    private fun initNetworkChangesListener() {
        NetworkUtils.getNetworkLiveData(requireContext()).observe(viewLifecycleOwner, Observer { isConnected ->
            if (!isConnected) {
                Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show()
                binding?.search?.isEnabled = false
            } else {
                binding?.search?.isEnabled = true
            }
        })
    }
}