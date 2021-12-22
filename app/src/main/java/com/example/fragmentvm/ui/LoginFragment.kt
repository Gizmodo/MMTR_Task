package com.example.fragmentvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fragmentvm.databinding.LoginFragmentBinding
import com.example.fragmentvm.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import timber.log.Timber

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var binding: LoginFragmentBinding
    private lateinit var viewModel: LoginViewModel

    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtDescription: TextInputEditText

    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilDescription: TextInputLayout

    private lateinit var btnLogin: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        Timber.d("onCreateView")
        binding = LoginFragmentBinding.inflate(inflater, container, false)

        edtEmail = binding.edtEmail
        tilEmail = binding.tilEmail

        edtDescription = binding.edtDescription
        tilDescription = binding.tilDescription
        btnLogin = binding.btnLogin

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.lifecycleOwner = this
        binding.loginViewModel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel.description.observe(viewLifecycleOwner) {
            Timber.d("Description $it")
        }

        viewModel.email.observe(viewLifecycleOwner) {
            Timber.d("Email $it")
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doinits()
    }


    private fun doinits() = with(binding) {
        getTextWatcher()

    }

    private fun getTextWatcher() {
        edtDescription.doAfterTextChanged {
            viewModel.updateDescription(it.toString())
        }
        edtEmail.doAfterTextChanged {
            viewModel.updateEmail(it.toString())
        }
    }
}
