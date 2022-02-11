package com.example.fragmentvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.fragmentvm.R
import com.example.fragmentvm.databinding.ApiFragmentBinding
import com.example.fragmentvm.utils.Util.toObservable
import com.example.fragmentvm.viewmodel.ApiViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

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
    private lateinit var nav: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ApiFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ApiViewModel::class.java]
        nav = findNavController()
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
        viewModel.getIsValidApiKey().observe(viewLifecycleOwner) {
            btnNext.isEnabled = it
            tilApiKey.isErrorEnabled = !it
            when (it) {
                true -> tilApiKey.error = ""
                false -> tilApiKey.error = "Ошибка"
            }
        }

        viewModel.getErrorLiveData().observe(viewLifecycleOwner) {
            when (it.status) {
                401 -> showDialog(it.message)
            }
        }

        viewModel.getIsSuccessRequest().observe(viewLifecycleOwner) {
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

    override fun onDestroy() {
        subscriptions.clear()
        super.onDestroy()
    }

    private fun navigateBack() {
        nav.navigate(ApiFragmentDirections.actionApiFragmentToLoginFragment())
    }

    private fun navigateMainFragment() {
        nav.navigate(ApiFragmentDirections.actionApiFragmentToMainFragment())
    }
}