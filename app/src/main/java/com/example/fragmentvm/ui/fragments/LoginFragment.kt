package com.example.fragmentvm.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fragmentvm.R
import com.example.fragmentvm.core.utils.StatefulData
import com.example.fragmentvm.core.utils.Util.toObservable
import com.example.fragmentvm.core.utils.fancyException
import com.example.fragmentvm.databinding.LoginFragmentBinding
import com.example.fragmentvm.domain.model.BackendResponseDomain
import com.example.fragmentvm.ui.viewmodels.LoginViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class LoginFragment : Fragment() {

    companion object {
        fun instance() = LoginFragment()
    }

    private var subscriptions: CompositeDisposable = CompositeDisposable()
    private lateinit var binding: LoginFragmentBinding
    private lateinit var viewModel: LoginViewModel

    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtDescription: TextInputEditText

    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilDescription: TextInputLayout

    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUIs()
        createFieldsObservers()
        initViewModelObservers()
        initListeners()
    }

    private fun initListeners() {
        btnLogin.setOnClickListener {
            viewModel.postRequest()
        }
    }

    private fun navigateNextFragment() {
        this.findNavController()
            .navigate(LoginFragmentDirections.actionLoginFragmentToApiFragment())
    }

    private fun showDialog(message: String) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(resources.getString(R.string.title))
                .setMessage(message)
                .setPositiveButton(resources.getString(R.string.accept)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun handleSignUp(state: StatefulData<BackendResponseDomain>) {
        when (state) {
            is StatefulData.Error -> {}
            is StatefulData.ErrorUiText -> {
                showDialog(state.message.toString())
            }
            StatefulData.Loading -> {}
            is StatefulData.Success -> {
                navigateNextFragment()
            }
        }
    }

    private fun initViewModelObservers() {
        viewModel.signupStateFlow
            .flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
            .onEach {
                handleSignUp(it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.exceptionMessage.observe(viewLifecycleOwner) {
            Timber.e(it)
            fancyException { it }
        }

        viewModel.isValidForm.observe(viewLifecycleOwner) {
            btnLogin.isEnabled = it
        }

        viewModel.isValidEmail.observe(viewLifecycleOwner) {
            tilEmail.isErrorEnabled = !it
            if (it == true) {
                tilEmail.error = String()
            } else {
                tilEmail.error = resources.getString(R.string.error)
            }
        }

        viewModel.isValidDescription.observe(viewLifecycleOwner) {
            tilDescription.isErrorEnabled = !it
            if (it == true) {
                tilDescription.error = String()
            } else {
                tilDescription.error = resources.getString(R.string.error)
            }
        }
    }

    private fun initUIs() {
        edtEmail = binding.edtEmail
        tilEmail = binding.tilEmail
        edtDescription = binding.edtDescription
        tilDescription = binding.tilDescription
        btnLogin = binding.btnLogin
    }

    private fun createFieldsObservers() {
        val subscribeEdtDescription = toObservable(edtDescription)
            .toFlowable(BackpressureStrategy.DROP)
            .observeOn(Schedulers.io())
            .subscribe({
                viewModel.updateDescription(it)
            }, { t -> Timber.e(t) })

        subscriptions.add(subscribeEdtDescription)

        val subscribeEdtEmail = toObservable(edtEmail)
            .toFlowable(BackpressureStrategy.DROP)
            .observeOn(Schedulers.io())
            .subscribe({
                viewModel.updateEmail(it)
            }, { Timber.e(it) })

        subscriptions.add(subscribeEdtEmail)
    }

    override fun onDestroy() {
        subscriptions.clear()
        super.onDestroy()
    }
}
