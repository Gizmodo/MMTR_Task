package com.example.fragmentvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.fragmentvm.adapter.CatAdapter
import com.example.fragmentvm.databinding.MainFragmentBinding
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.utils.CatUiState
import com.example.fragmentvm.utils.SharedViewModel
import com.example.fragmentvm.utils.UiState
import com.example.fragmentvm.utils.VotesEnum
import com.example.fragmentvm.viewmodel.MainViewModel
import kotlinx.coroutines.launch
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nav = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                monitor1()
                monitor2()
            }
        }
    }

    private suspend fun monitor1() {
        viewModel.uiVoteState.collect {
            when (val state = it) {
                is UiState.BadResponse -> {
                    Timber.d("BadResponse")
                }
                UiState.Empty -> {
                    Timber.d("Empty")
                }
                is UiState.Error -> {
                    Timber.d("Error")
                }
                UiState.Finished -> {
                    Timber.d("Finished")
                }
                UiState.Loading -> {
                    Timber.d("Loading")
                }
                is UiState.Success -> {
                    Timber.d("Success")
                }
            }
        }
    }

    private suspend fun monitor2() {
        viewModel.uiState.collect {
            when (val state = it) {
                CatUiState.Empty -> {
                    Timber.d("Empty")
                    binding.progressBar.visibility = View.GONE
                }
                is CatUiState.Error -> {
                    Timber.d("Error")
                    Toast.makeText(context, state.t.message, Toast.LENGTH_LONG)
                        .show()
                }
                is CatUiState.Loaded -> {
                    Timber.d("Loaded")
                    binding.progressBar.visibility = View.GONE
                }
                CatUiState.Loading -> {
                    Timber.d("Loading")
                    binding.progressBar.visibility = View.VISIBLE
                }
                CatUiState.Finished -> {
                    Timber.d("Finished")
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
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

                it.adapter = CatAdapter(cats, catClickListener, voteListener = voteClickListener)
            }
        }

        swipe = binding.swipeLayout
        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            viewModel.getCats()
        }
        return binding.root
    }

    private val voteClickListener = object : CatAdapter.OnVoteClickListener {
        override fun onVoteClickListener(view: View, cat: Cat, vote: VotesEnum) {
            viewModel.vote(cat, vote)
        }

    }
    private val catClickListener = object : CatAdapter.OnRecyclerViewItemClick {
        override fun onRecyclerViewItemClick(view: View, cat: Cat) {
            sharedModel.select(cat)
            nav.navigate(MainFragmentDirections.actionMainFragmentToDetailFragment())
        }
    }

}