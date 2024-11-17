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
import kotlinx.android.synthetic.main.fragment_register.btnGoToLogin
import kotlinx.android.synthetic.main.fragment_register.btnRegister
import kotlinx.android.synthetic.main.fragment_register.etEmail
import kotlinx.android.synthetic.main.fragment_register.etName
import kotlinx.android.synthetic.main.fragment_register.etPassword
import kotlinx.android.synthetic.main.fragment_register.registerProgressBar

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var viewModel: AuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        subscribeToObservers()

        btnRegister.setOnClickListener {
            viewModel.register(
                etEmail.text.toString().trim(),
                etName.text.toString().trim(),
                etPassword.text.toString().trim()
            )
        }

        btnGoToLogin.setOnClickListener {
            if (findNavController().previousBackStackEntry != null) {
                findNavController().popBackStack()
            } else {
                findNavController().navigate(
                    RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                )
            }
        }
    }

    private fun subscribeToObservers() {
        viewModel.registerStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                registerProgressBar.isVisible = false
                btnRegister.isEnabled = true
                snackBar(it)
            },
            onLoading = {
                registerProgressBar.isVisible = true
                btnRegister.isEnabled = false
            }
        ) {
            registerProgressBar.isVisible = false
            btnRegister.isEnabled = true
            Intent(requireContext(), PlaceMarkListActivity::class.java).also {
                startActivity(it)
                requireActivity().finish()
            }
        })
    }
}