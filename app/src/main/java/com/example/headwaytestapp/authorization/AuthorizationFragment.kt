package com.example.headwaytestapp.authorization

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.headwaytestapp.BaseFragment
import com.example.headwaytestapp.R
import com.example.headwaytestapp.databinding.ViewAuthorizationBinding
import com.example.headwaytestapp.network.NetworkUtils
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AuthorizationFragment : BaseFragment<ViewAuthorizationBinding>() {

    private val viewModel: AuthorizationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = Firebase.auth.currentUser
        if (user != null) {
            saveCredsOpenRepositoryList()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNetworkChangesListener()

        binding?.loginButton?.setOnClickListener {
            onLoginButtonClicked()
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.authorizationUiState.collect(this@AuthorizationFragment::handleLogIn)
                }
            }
        }
    }

    override fun layoutId(): Int {
        return R.layout.view_authorization
    }

    private fun onLoginButtonClicked() =
        if (binding?.tokenEditText?.text.toString().isEmpty()) {
            Toast.makeText(context, "Empty search key", Toast.LENGTH_LONG).show()
        } else {
            viewModel.authorization(binding?.tokenEditText?.text.toString())
        }

    private fun handleLogIn(state: UiStateManager) {
        when (state) {
            is UiStateManager.SuccessLogIn -> {
                saveCredsOpenRepositoryList()
            }
            is UiStateManager.ErrorLogIn -> {
                showSignInError(state.message!!)
            }
            is UiStateManager.InProgress -> {
                //start login
            }
            is UiStateManager.OpenLogIn -> {
                openGitHubSignIn(state.provider)
            }
        }
    }

    private fun openGitHubSignIn(provider: OAuthProvider.Builder) {
        Firebase.auth.startActivityForSignInWithProvider(requireActivity(), provider.build())
            .addOnSuccessListener {
                saveCredsOpenRepositoryList()
            }
            .addOnFailureListener {
                showSignInError(it.message!!)
            }
    }

    private fun saveCredsOpenRepositoryList() {
        findNavController().navigate(AuthorizationFragmentDirections.toShowRepositoryFragment())
    }

    private fun showSignInError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun initNetworkChangesListener() {
        NetworkUtils.getNetworkLiveData(requireContext())
            .observe(viewLifecycleOwner, Observer { isConnected ->
                if (!isConnected) {
                    Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show()
                    binding?.loginButton?.isEnabled = false
                } else {
                    binding?.loginButton?.isEnabled = true
                }
            })
    }
}