package com.example.fragmentvm.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.example.fragmentvm.domain.model.vote.VoteResponseDomain
import com.example.fragmentvm.ui.adapter.CatFavouritePagingAdapter
import com.example.fragmentvm.ui.utils.StateMain
import com.example.fragmentvm.ui.utils.VotesEnum
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
    private var favCatsAdapter = CatFavouritePagingAdapter(
        { favCat, position, vote -> viewModel.vote(favCat, vote, position) },
        { favCat -> viewModel.setCat(favCat) },
        { favCat, position -> viewModel.onFavClicked(favCat, position) },
    )
    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var progressBar: CircularProgressIndicator
    private lateinit var layoutEmpty: ConstraintLayout
    private lateinit var rv: RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeUIStates()
    }

    private fun initUI() {
        progressBar = binding.progressBar
        layoutEmpty = binding.layoutEmpty
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
                when (loadState.source.refresh) {
                    is LoadState.Loading -> {
                        viewModel.setState(StateMain.Loading)
                    }
                    is LoadState.NotLoading -> {
                        viewModel.setState(StateMain.Finished)
                    }
                    is LoadState.Error -> {
                        viewModel.setState(StateMain.Finished)
                    }
                }

                when {
                    loadState.append is LoadState.NotLoading && loadState.append.endOfPaginationReached -> {
                        when {
                            favCatsAdapter.itemCount < 1 -> viewModel.setState(StateMain.Empty)
                            else -> viewModel.setState(StateMain.Finished)
                        }
                    }
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

        viewModel.voteState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { handleVoteStateChange(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        lifecycleScope.launchWhenStarted {
            viewModel.evenFlow.collect {
                when (it) {
                    is StatefulData.Error -> TODO()
                    is StatefulData.ErrorUiText -> {
                        showDialog(it.message.asString(requireContext()))
                    }
                    StatefulData.Loading -> {

                    }
                    is StatefulData.Success -> {
                        it.result.image_url?.let { url ->
                            val bottomSheet = DetailBottomSheet.instance(url)
                            bottomSheet.show(parentFragmentManager, bottomSheet.toString())
                        }
                    }
                }
            }
        }
    }

    private fun handleVoteStateChange(state: StatefulData<VoteResponseDomain>) {
        when (state) {
            is StatefulData.Error -> Timber.d("Error")
            is StatefulData.ErrorUiText -> {
                showDialog(state.message.asString(requireContext()))
                viewModel.resetState()
            }
            StatefulData.Loading -> Timber.d("Loading")
            is StatefulData.Success -> {
                setVoteButtonState(state.result.position, state.result.vote)
                viewModel.resetState()
            }
        }
    }

    private fun setVoteButtonState(position: Int, vote: VotesEnum) {
        favCatsAdapter.setToggle(position, vote)
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
                layoutEmpty.visibility = View.VISIBLE
                swipe.isEnabled = true
            }
            is StateMain.Error -> {
                Toast.makeText(context, state.t.message, Toast.LENGTH_LONG)
                    .show()
            }
            StateMain.Loading -> {
                progressBar.visibility = View.VISIBLE
                layoutEmpty.visibility = View.GONE
                swipe.isEnabled = false
            }
            StateMain.Finished -> {
                progressBar.visibility = View.GONE
                layoutEmpty.visibility = View.GONE
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