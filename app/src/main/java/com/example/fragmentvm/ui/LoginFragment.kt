package com.example.fragmentvm.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fragmentvm.databinding.LoginFragmentBinding
import com.example.fragmentvm.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private var subscriptions: CompositeDisposable = CompositeDisposable()
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
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUIs()
        createFieldsObservers()
        vmObservers()
    }

    private fun vmObservers() {
        viewModel.isValidForm.observe(viewLifecycleOwner) {
            btnLogin.isEnabled = it
        }

        viewModel.isValidEmail.observe(viewLifecycleOwner) {
            tilEmail.isErrorEnabled = !it
            if (it == true) {
                tilEmail.error = ""
            } else {
                tilEmail.error = "Ошибка"
            }
        }

        viewModel.isValidDescription.observe(viewLifecycleOwner) {
            tilDescription.isErrorEnabled = !it
            if (it == true) {
                tilDescription.error = ""
            } else {
                tilDescription.error = "Ошибка"
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

    private fun toObservable(editText: TextInputEditText): Observable<String> {
        val descriptionTextChangeObservable = Observable.create<String> { emitter ->
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    s?.toString()?.let { emitter.onNext(it) }
                }

                override fun afterTextChanged(p0: Editable?) {}
            }
            editText.addTextChangedListener(textWatcher)
            emitter.setCancellable {
                editText.removeTextChangedListener(textWatcher)
            }
        }

        return descriptionTextChangeObservable.debounce(50, TimeUnit.MILLISECONDS)
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

    override fun onPause() {
        subscriptions.clear()
        super.onPause()
    }
}
