package com.example.fragmentvm.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
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
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
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
        Timber.d("onCreateView")
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
//        binding.lifecycleOwner = this
//        binding.loginViewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated")
        doinits()
    }

    override fun onPause() {
        subscriptions.clear()
        super.onPause()
    }

   /* private fun validateForm() {
        val emailObservable = binding.edtEmail.textchangeEve.editText.textChangeEvents()
            .skipInitialValue()
            .map { isValidEmail(it.text) || isValidPhoneNumber(it.text) }
            .doOnDispose {
                Log.i("disposed", "emailObservable")
            }


        val passwordObservable = viewBinding.detPassword.editText.textChangeEvents()
            .skipInitialValue()
            .map { !TextUtils.isEmpty(it.text) }
            .doOnDispose {
                Log.i("disposed", "passwordObservable")
            }

        val disposable = Observable.combineLatest(emailObservable, passwordObservable,
            BiFunction<Boolean, Boolean, Boolean> { t1, t2 -> t1 && t2 })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                viewBinding.bLogin.isEnabled = it
            }
        subscriptions.add(emailObservable, passwordObservable, disposable)
    }*/

    private fun doinits() {
        edtEmail = binding.edtEmail
        tilEmail = binding.tilEmail
        edtDescription = binding.edtDescription
        tilDescription = binding.tilDescription
        btnLogin = binding.btnLogin

        getTextWatcher()
    }

    // 1
    private fun createTextChangeObservable(): Observable<String> {
        // 2
        val descriptionTextChangeObservable = Observable.create<String> { emitter ->
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    s?.toString()?.let { emitter.onNext(it) }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            }
            edtDescription.addTextChangedListener(textWatcher)
            emitter.setCancellable {
                edtDescription.removeTextChangedListener(textWatcher)
            }
        }

        // 7
        return descriptionTextChangeObservable
            .filter { it.isNotEmpty() }
            .debounce(1000, TimeUnit.MILLISECONDS)

    }

    private fun getTextWatcher() {
        val doAfterTextChanged: TextWatcher = edtDescription.doAfterTextChanged {
            viewModel.updateDescription(it.toString())
        }
        val observable = PublishSubject.create<String>()
        observable
            .toFlowable(BackpressureStrategy.DROP)
            .observeOn(Schedulers.computation())
            .subscribe({
                Timber.d("Observable $it")

            }, { t ->
                Timber.e(t)
            })

        val descObservable = createTextChangeObservable()
        val subscribe: Disposable = descObservable.toFlowable(BackpressureStrategy.DROP)
            .observeOn(Schedulers.computation())
            .subscribe({
                Timber.d("descObservable $it")
            }, { t ->
                Timber.e(t)
            })
        subscriptions.add(subscribe)
        /* descObservable.subscribe {
             Timber.d(it)
         }*/
        edtEmail.doAfterTextChanged {
//            observable.onNext(it.toString())
            viewModel.updateEmail(it.toString())
        }
    }
}
