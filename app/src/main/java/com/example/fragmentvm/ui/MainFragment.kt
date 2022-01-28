package com.example.fragmentvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.fragmentvm.adapter.CatAdapter
import com.example.fragmentvm.databinding.MainFragmentBinding
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.utils.SharedViewModel
import com.example.fragmentvm.viewmodel.MainViewModel
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
                it.adapter = CatAdapter(cats, catClickListener)
            }
        }

        swipe = binding.swipeLayout
        swipe.setOnRefreshListener {
            swipe.isRefreshing = true
            viewModel.getCats()
        }
viewModel.uiState.observe
        when (val state = viewModel.uiState.value) {
            MainViewModel.CatUiState.Empty -> {
                Timber.d("Empty")
                binding.progressBar.visibility = View.GONE
            }
            is MainViewModel.CatUiState.Error -> {
                Timber.d("Error")
                Toast.makeText(context, state.t.message, Toast.LENGTH_LONG)
                    .show()
            }
            is MainViewModel.CatUiState.Loaded -> {
                Timber.d("Loaded")
                binding.progressBar.visibility = View.GONE
            }
            MainViewModel.CatUiState.Loading -> {
                Timber.d("Loading")
                binding.progressBar.visibility = View.VISIBLE
            }
            MainViewModel.CatUiState.Finished -> {
                Timber.d("Finished")
                binding.progressBar.visibility = View.GONE
            }
        }
        return binding.root
    }

    private val catClickListener = object : CatAdapter.OnRecyclerViewItemClick {
        override fun onRecyclerViewItemClick(view: View, cat: Cat) {
            sharedModel.select(cat)
            nav.navigate(MainFragmentDirections.actionMainFragmentToDetailFragment())
        }
    }
}