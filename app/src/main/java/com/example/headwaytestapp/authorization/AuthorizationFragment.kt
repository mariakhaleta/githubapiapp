package com.example.headwaytestapp.authorization

import android.net.wifi.hotspot2.pps.Credential
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.headwaytestapp.BaseFragment
import com.example.headwaytestapp.R
import com.example.headwaytestapp.databinding.ViewAuthorizationBinding
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.FirebaseUser




@AndroidEntryPoint
class AuthorizationFragment : BaseFragment<ViewAuthorizationBinding>() {

    private val viewModel: AuthorizationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = Firebase.auth.currentUser
        if (user != null) {
            saveCredsOpenRepositoryList("")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        viewModel.authorization(binding?.tokenEditText?.text.toString())

    private fun handleLogIn(state: UiStateManager) {
        when (state) {
            is UiStateManager.SuccessLogIn -> {
                saveCredsOpenRepositoryList(state.token)
            }
            is UiStateManager.ErrorLogIn -> {
                showSignInError(state.message!!)
            }
            is UiStateManager.InProgress -> {
                //
            }
            is UiStateManager.OpenLogIn -> {
                openGitHubSignIn(state.provider)
            }
        }
    }

    private fun openGitHubSignIn(provider: OAuthProvider.Builder) {
        Firebase.auth.startActivityForSignInWithProvider(requireActivity(), provider.build())
            .addOnSuccessListener {
                saveCredsOpenRepositoryList(it.credential?.signInMethod!!)
            }
            .addOnFailureListener {
                showSignInError(it.message!!)
            }
    }

    private fun saveCredsOpenRepositoryList(credential: String) {
        findNavController().navigate(AuthorizationFragmentDirections.toShowRepositoryFragment())
        //save credential.credential?.signInMethod!!
    }

    private fun showSignInError(message: String) {
        //show error
    }
}