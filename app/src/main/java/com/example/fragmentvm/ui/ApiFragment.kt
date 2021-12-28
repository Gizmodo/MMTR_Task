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
import com.example.fragmentvm.R
import com.example.fragmentvm.databinding.ApiFragmentBinding
import com.example.fragmentvm.viewmodel.ApiViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ApiFragment : Fragment() {

    companion object {
        fun instance() = ApiFragment()
    }

    private var subscriptions: CompositeDisposable = CompositeDisposable()
    private lateinit var binding: ApiFragmentBinding
    private lateinit var btnNext: Button
    private lateinit var btnBack: Button
    private lateinit var edtApiKey: TextInputEditText
    private lateinit var viewModel: ApiViewModel
    private lateinit var tilApiKey: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ApiFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ApiViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUIs()
        initFieldsObservers()
        initViewModelObservers()
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

    private fun initViewModelObservers() {
        viewModel.isValidApiKey.observe(viewLifecycleOwner) {
            btnNext.isEnabled = it
            tilApiKey.isErrorEnabled = !it
            when (it) {
                true -> tilApiKey.error = ""
                false -> tilApiKey.error = "Ошибка"
            }
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                401 -> showDialog(it.message)
            }
        }

        viewModel.isSuccessRequest.observe(viewLifecycleOwner) {
            if (it) navigateMainFragment()
        }
    }

    private fun initUIs() {
        btnNext = binding.btnNext
        edtApiKey = binding.edtApikey
        btnBack = binding.btnBack
        tilApiKey = binding.tilApikey

        btnNext.setOnClickListener {
            viewModel.sendRequest()
        }

        btnBack.setOnClickListener {
            navigateBack()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[ApiViewModel::class.java]
    }

    private fun initFieldsObservers() {
        val subscribeEdtApiKey = toObservable(edtApiKey)
            .toFlowable(BackpressureStrategy.DROP)
            .observeOn(Schedulers.io())
            .subscribe({
                viewModel.updateApiKey(it)
            }, { Timber.e(it) })
        subscriptions.add(subscribeEdtApiKey)
    }

    private fun toObservable(editText: TextInputEditText): Observable<String> {
        val observable = Observable.create<String> { emitter ->
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

        return observable.debounce(50, TimeUnit.MILLISECONDS)
    }

    override fun onPause() {
        subscriptions.clear()
        super.onPause()
    }

    private fun navigateBack() {
        val transaction = requireActivity()
            .supportFragmentManager
            .beginTransaction()

        with(transaction) {
            replace(
                R.id.container,
                LoginFragment.instance()
            )
            commit()
        }
    }

    private fun navigateMainFragment() {
        val transaction = requireActivity()
            .supportFragmentManager
            .beginTransaction()

        with(transaction) {
            replace(
                R.id.container,
                MainFragment.instance()
            )
            addToBackStack(null)
            commit()
        }
    }
}