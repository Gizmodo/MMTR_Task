package com.example.fragmentvm.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.fragmentvm.R
import com.example.fragmentvm.core.utils.StatefulData
import com.example.fragmentvm.core.utils.getViewModel
import com.example.fragmentvm.core.utils.viewBindingWithBinder
import com.example.fragmentvm.databinding.FavouriteFragmentBinding
import com.example.fragmentvm.domain.model.favourite.FavouriteResponseDomain
import com.example.fragmentvm.ui.adapter.CatFavouritePagingAdapter
import com.example.fragmentvm.ui.utils.StateMain
import com.example.fragmentvm.ui.viewmodels.FavouriteViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class FavouriteFragment : Fragment(R.layout.favourite_fragment) {
    private val binding by viewBindingWithBinder(FavouriteFragmentBinding::bind)
    private val viewModel: FavouriteViewModel by lazy {
        getViewModel { FavouriteViewModel() }
    }
    private var favCatsAdapter = CatFavouritePagingAdapter { favCat, position ->
        viewModel.onFavClicked(favCat,
            position)
    }
    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var progressBar: CircularProgressIndicator
    private lateinit var rv: RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeUIStates()
    }

    private fun initUI() {
        progressBar = binding.progressBar
        swipe = binding.swipeLayout
        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            favCatsAdapter.refresh()
        }
        rv = binding.recyclerview
        with(rv) {
            val animator = itemAnimator
            if (animator is SimpleItemAnimator) {
                animator.supportsChangeAnimations = false
            }
            setHasFixedSize(true)
            adapter = favCatsAdapter
        }

        lifecycleScope.launchWhenCreated {
            favCatsAdapter.addLoadStateListener { loadState ->
                if (loadState.source.refresh is LoadState.Loading) {
                    viewModel.setState(StateMain.Loading)
                }
                if (loadState.source.refresh is LoadState.NotLoading) {
                    viewModel.setState(StateMain.Finished)
                }
            }
            viewModel.catsFavFlow.collectLatest {
                favCatsAdapter.submitData(it)
            }
        }
    }

    private fun observeUIStates() {
        viewModel.getStateUIMain()
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { handleUIState(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.favState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { handleFavStateChange(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleFavStateChange(state: StatefulData<FavouriteResponseDomain>) {
        when (state) {
            is StatefulData.Error -> {
                Timber.d(state.message)
            }
            StatefulData.Loading -> {
                Timber.d("Loading")
            }
            is StatefulData.ErrorUiText -> {
                showDialog(state.message.asString(requireContext()))
                Timber.d("ErrorUiText " + state.message.asString(requireContext()))
            }
            is StatefulData.Success -> {
                favCatsAdapter.refresh()
            }
        }
    }

    private fun handleUIState(state: StateMain) {
        when (state) {
            StateMain.Empty -> {
                progressBar.visibility = View.GONE
                swipe.isEnabled = true
            }
            is StateMain.Error -> {
                Toast.makeText(context, state.t.message, Toast.LENGTH_LONG)
                    .show()
            }
            StateMain.Loading -> {
                progressBar.visibility = View.VISIBLE
                swipe.isEnabled = false
            }
            StateMain.Finished -> {
                progressBar.visibility = View.GONE
                swipe.isEnabled = true
            }
        }
    }

    private fun showDialog(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.title))
            .setMessage(message)
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}