package com.example.fragmentvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.fragmentvm.databinding.LoginFragmentBinding
import com.example.fragmentvm.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var binding: LoginFragmentBinding
    private lateinit var viewModel: LoginViewModel

    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtDescription: TextInputEditText
    private lateinit var btnLogin: Button

    private val email = MutableStateFlow("")
    private val description = MutableStateFlow("")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = LoginFragmentBinding.inflate(inflater, container, false)

        edtEmail = binding.edtEmail
        edtDescription = binding.edtDescription
        btnLogin = binding.btnLogin
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            edtEmail.doOnTextChanged { text, _, _, _ ->
                email.value = text.toString()
            }
            edtDescription.doOnTextChanged { text, _, _, _ ->
                description.value = text.toString()
            }
        }
        lifecycleScope.launch {
            formIsValid.collect {
                btnLogin.apply {
                    isClickable = it
                }
            }
        }
        btnLogin.setOnClickListener { Timber.d("Click") }
    }

    fun checkValidation(): Boolean {

        var isValid: Boolean = true;

        if (edtEmail.text.toString().isNullOrEmpty()) {
            binding.tilEmail.error = "Адрес пустой"
            binding.tilEmail.isErrorEnabled = true
            isValid = false
        } else {
            binding.tilEmail.error = ""
            binding.tilEmail.isErrorEnabled = false
        }

        return isValid
    }

    private val formIsValid = combine(
        email,
        description
    ) { email, description ->
        binding.tilEmail.error = ""

        val emailIsValid = email.length > 6
        val errorMessageEmail = when {
            email.isNotEmpty() -> "email is empty"
            !emailIsValid -> "email not valid"
            else -> null
        }

        errorMessageEmail?.let {
            binding.tilEmail.error = it
        }

        val descriptionIsValid = description.length > 6
        val errorMessageDescription = when {
            description.isNotEmpty() -> "description is empty"
            !descriptionIsValid -> "description not valid"
            else -> null
        }

        errorMessageDescription?.let {
            binding.tilDescription.error = it
        }

        return@combine (emailIsValid and descriptionIsValid)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

}