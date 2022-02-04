package com.example.fragmentvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.fragmentvm.R
import com.example.fragmentvm.adapter.CatAdapter
import com.example.fragmentvm.databinding.MainFragmentBinding
import com.example.fragmentvm.model.BackendResponse
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
    private lateinit var adapter: CatAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nav = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUIStates()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.cats.observe(viewLifecycleOwner) { cats ->
            swipe.isRefreshing = false
            binding.recyclerview.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)
                adapter = CatAdapter(
                    cats,
                    { cat, position, vote ->
                        Timber.d("Cat url: ${cat.url}  Cat state: ${cat.state} Vote to set $vote")
                        viewModel.vote(cat, vote, position)
                    }, { cat ->
                        sharedModel.select(cat)
                        nav.navigate(
                            MainFragmentDirections.actionMainFragmentToDetailFragment()
                        )
                    }
                )
                it.adapter = adapter
            }
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
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state -> handleUIState(state) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.getStateUIVote()
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state -> handleVoteState(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleVoteState(state: StateUIVote<BackendResponse>) {
        Timber.d("handleVoteState")
        when (state) {
            is StateUIVote.BadResponse -> {
                Timber.d("BadResponse")
                // TODO: в функцию передать position
                setVoteButtons(state.badResponse.position, state.badResponse.vote)
                showDialog(state.badResponse.message)
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
                Timber.d("Success")
                // TODO: в функцию передать position
                setVoteButtons(state.item.position, state.item.vote)
//                    showDialog(state.badResponse.message)
            }
        }
    }

    private fun handleUIState(state: StateUIMain) {
        Timber.d("handleUIState")
        when (state) {
            StateUIMain.Empty -> {
                Timber.d("Empty")
                binding.progressBar.visibility = View.GONE
            }
            is StateUIMain.Error -> {
                Timber.d("Error")
                Toast.makeText(context, state.t.message, Toast.LENGTH_LONG)
                    .show()
            }
            is StateUIMain.Loaded -> {
                Timber.d("Loaded")
                binding.progressBar.visibility = View.GONE
            }
            StateUIMain.Loading -> {
                Timber.d("Loading")
                binding.progressBar.visibility = View.VISIBLE
            }
            StateUIMain.Finished -> {
                Timber.d("Finished")
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

    private fun setVoteButtons(position: Int, vote: VotesEnum) {
        adapter.setToggle(position, vote)
//        adapter.notifyItemChanged(position)
    }
}