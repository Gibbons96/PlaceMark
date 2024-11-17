package com.example.placemark.ui.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.placemark.R
import com.example.placemark.others.EventObserver
import com.example.placemark.others.snackBar
import com.example.placemark.ui.auth.AuthViewModel
import com.example.placemark.ui.placemarklist.PlaceMarkListActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.btnGoToRegister
import kotlinx.android.synthetic.main.fragment_login.btnLogin
import kotlinx.android.synthetic.main.fragment_login.etEmail
import kotlinx.android.synthetic.main.fragment_login.etPassword
import kotlinx.android.synthetic.main.fragment_login.loginProgressBar

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var viewModel: AuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        subscribeToObservers()

        btnLogin.setOnClickListener {
            viewModel.login(
                etEmail.text.toString().trim(),
                etPassword.text.toString().trim()
            )
        }

        btnGoToRegister.setOnClickListener {
            if (findNavController().previousBackStackEntry != null) {
                findNavController().popBackStack()
            } else {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                )
            }
        }
    }

    private fun subscribeToObservers() {
        viewModel.loginStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                loginProgressBar.isVisible = false
                btnLogin.isEnabled = true
                snackBar(it)
            },
            onLoading = {
                loginProgressBar.isVisible = true
                btnLogin.isEnabled = false
            }
        ) {
            loginProgressBar.isVisible = false
            btnLogin.isEnabled = true
            Intent(requireContext(), PlaceMarkListActivity::class.java).also {
                startActivity(it)
                requireActivity().finish()
            }
        })
    }
}