package com.example.fragmentvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.fragmentvm.R
import com.example.fragmentvm.base.BaseAdapterCallback
import com.example.fragmentvm.base.Ca
import com.example.fragmentvm.databinding.MainFragmentBinding
import com.example.fragmentvm.model.BackendResponse
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.utils.SharedViewModel
import com.example.fragmentvm.utils.StateUIMain
import com.example.fragmentvm.utils.StateUIVote
import com.example.fragmentvm.utils.VotesEnum
import com.example.fragmentvm.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class MainFragment : Fragment() {
    companion object {
        fun instance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private val sharedModel: SharedViewModel by activityViewModels()
    private lateinit var nav: NavController
    private lateinit var swipe: SwipeRefreshLayout
    private var adapter: Ca = Ca()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nav = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUIStates()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.cats.observe(viewLifecycleOwner) { cats ->
            swipe.isRefreshing = false
            adapter.updateItems(cats)

            binding.recyclerview.also {
                val animator = it.itemAnimator
                if (animator is SimpleItemAnimator) {
                    animator.supportsChangeAnimations = false
                }
                it.setHasFixedSize(true)

                it.adapter = adapter
            }
            adapter.attachClick(clickHandler = object : Ca.ServiceClickHandler {
                override fun onPriceClick(model: Cat, view: View, position: Int) {
                    Timber.d("Cat ${model.id} clicked at position $position")
                }
            })
            adapter.attachClickBase(object : BaseAdapterCallback<Cat> {
                override fun onItemClick(model: Cat, view: View, position: Int) {
                    Timber.d("attachClickBase")
                }

            })
            adapter.attachCallback(object : BaseAdapterCallback<Cat> {
                override fun onItemClick(model: Cat, view: View, position: Int) {
                    Timber.d("Card clicked")
                    /*  sharedModel.select(model)
                      nav.navigate(
                          MainFragmentDirections.actionMainFragmentToDetailFragment()
                      )*/
                }
            })
        }

        swipe = binding.swipeLayout
        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            viewModel.getCats()
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
    }

    private fun handleVoteState(state: StateUIVote<BackendResponse>) {
        when (state) {
            is StateUIVote.BadResponse -> {
                showDialog(state.badResponse.message)
                adapter.notifyItemChanged(state.badResponse.position)
                viewModel.resetVoteState()
            }
            StateUIVote.Empty -> {
                Timber.d("Empty")
            }
            is StateUIVote.Error -> {
                Timber.d("Error")
            }
            StateUIVote.Finished -> {
                Timber.d("Finished")
            }
            StateUIVote.Loading -> {
                Timber.d("Loading")
            }
            is StateUIVote.Success -> {
                setVoteButton(state.item.position, state.item.vote)
                viewModel.resetVoteState()
            }
        }
    }

    private fun handleUIState(state: StateUIMain) {
        when (state) {
            StateUIMain.Empty -> {
                binding.progressBar.visibility = View.GONE
            }
            is StateUIMain.Error -> {
                Toast.makeText(context, state.t.message, Toast.LENGTH_LONG)
                    .show()
            }
            StateUIMain.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            StateUIMain.Finished -> {
                binding.progressBar.visibility = View.GONE
            }
        }
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

    private fun setVoteButton(position: Int, vote: VotesEnum) {
//        adapter.setToggle(position, vote)
    }
}