package com.example.fragmentvm.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.fragmentvm.R
import com.example.fragmentvm.core.utils.StatefulData
import com.example.fragmentvm.databinding.MainFragmentBinding
import com.example.fragmentvm.domain.model.favourite.FavouriteResponseDomain
import com.example.fragmentvm.domain.model.vote.VoteResponseDomain
import com.example.fragmentvm.ui.adapter.CatPagingAdapter
import com.example.fragmentvm.ui.utils.StateMain
import com.example.fragmentvm.ui.utils.StateVote
import com.example.fragmentvm.ui.utils.VotesEnum
import com.example.fragmentvm.ui.viewmodels.CatViewModel
import com.example.fragmentvm.ui.viewmodels.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class MainFragment : Fragment() {
    companion object {
        fun instance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var catViewModel: CatViewModel
    private lateinit var binding: MainFragmentBinding
    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var rv: RecyclerView

    private var catAdapter = CatPagingAdapter(
        { cat, position, vote ->
            viewModel.vote(cat, vote, position)
        }, { cat ->
            catViewModel.setCat(cat)
        }, { cat, position ->
            catViewModel.setFavourite(cat, position)
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUIStates()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        catViewModel = ViewModelProvider(this)[CatViewModel::class.java]
        binding = MainFragmentBinding.inflate(inflater, container, false)
        swipe = binding.swipeLayout!!
        rv = binding.recyclerview!!
//        this.findNavController()            .navigate(MainFragmentDirections.actionMainFragmentToFavouriteFragment())
        with(rv) {
            val animator = itemAnimator
            if (animator is SimpleItemAnimator) {
                animator.supportsChangeAnimations = false
            }
            setHasFixedSize(true)
            adapter = catAdapter
        }

        lifecycleScope.launchWhenCreated {
            catAdapter.addLoadStateListener { loadState ->
                if (loadState.source.refresh is LoadState.Loading) {
                    viewModel.setState(StateMain.Loading)
                }
                if (loadState.source.refresh is LoadState.NotLoading) {
                    viewModel.setState(StateMain.Finished)
                }
            }
            viewModel.catsFlow.collectLatest {
                catAdapter.submitData(it)
            }
        }

        catViewModel.getCat().observe(viewLifecycleOwner) { cat ->
            val bottomSheet = DetailBottomSheet.instance(cat.url)
            bottomSheet.show(parentFragmentManager, bottomSheet.toString())
        }

        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            catAdapter.refresh()
        }

        return binding.root
    }

    private fun observeUIStates() {
        viewModel.getStateUIMain()
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { handleUIState(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.getStateUIVote()
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { handleVoteState(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        catViewModel.favouriteState
            .flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
            .onEach { handleFavouriteState(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun handleFavouriteState(it: StatefulData<FavouriteResponseDomain>) {
        when (it) {
            is StatefulData.Error -> {
                Timber.d(it.message)
            }
            StatefulData.Loading -> {
                Timber.d("Loading")
            }
            is StatefulData.Success -> {
                Timber.d("Success")
                with(it.result) {
                    setFavouriteId(adapterPosition, id)
                    catViewModel.resetFavouriteState()
                }
            }
            is StatefulData.ErrorUiText -> {
                showDialog(it.message.asString(requireContext()))
                Timber.d(it.message.asString(requireContext()))
            }
        }
    }

    private fun handleVoteState(state: StateVote<VoteResponseDomain>) {
        when (state) {
            StateVote.Empty -> Timber.d("Empty")
            is StateVote.Error -> Timber.d("Error")
            StateVote.Finished -> Timber.d("Finished")
            StateVote.Loading -> Timber.d("Loading")
            is StateVote.Success -> {
                setVoteButton(state.data.position, state.data.vote)
                viewModel.resetVoteState()
            }
            is StateVote.UnSuccess -> {
                showDialog(state.data.message)
                catAdapter.notifyItemChanged(state.data.position)
                viewModel.resetVoteState()
            }
        }
    }

    private fun handleUIState(state: StateMain) {
        when (state) {
            StateMain.Empty -> {
                binding.progressBar!!.visibility = View.GONE
                binding.swipeLayout?.isEnabled = true
            }
            is StateMain.Error -> {
                Toast.makeText(context, state.t.message, Toast.LENGTH_LONG)
                    .show()
            }
            StateMain.Loading -> {
                binding.progressBar!!.visibility = View.VISIBLE
                binding.swipeLayout?.isEnabled = false
            }
            StateMain.Finished -> {
                binding.progressBar!!.visibility = View.GONE
                binding.swipeLayout?.isEnabled = true
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

    private fun setFavouriteId(position: Int?, favouriteId: Int?) {
        catAdapter.setFavouriteId(position, favouriteId)
    }

    private fun setVoteButton(position: Int, vote: VotesEnum) {
        catAdapter.setToggle(position, vote)
    }
}