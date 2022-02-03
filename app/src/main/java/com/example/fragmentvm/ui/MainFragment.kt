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
import com.example.fragmentvm.R
import com.example.fragmentvm.adapter.CatAdapter
import com.example.fragmentvm.databinding.MainFragmentBinding
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.utils.CatUiState
import com.example.fragmentvm.utils.SharedViewModel
import com.example.fragmentvm.utils.UiState
import com.example.fragmentvm.utils.VotesEnum
import com.example.fragmentvm.viewmodel.MainViewModel
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.launch
import timber.log.Timber

class MainFragment : Fragment() {
    companion object {
        fun instance() = MainFragment()
    }

    val faker = Faker()
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                monitorList()
                monitorVote()
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

    private suspend fun monitorVote() {
        viewModel.uiVoteState.collect {
            Timber.d("monitorVote called")
            when (val state = it) {
                is UiState.BadResponse -> {
                    Timber.d("BadResponse")
                    // TODO: в функцию передать position
                    setVoteButtons(position,false)
                    showDialog(state.badResponse.message)
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
                    // TODO: в функцию передать position
                    setVoteButtons(position,true)
//                    showDialog(state.badResponse.message)
                }
            }
        }
    }

    private fun setVoteButtons(position : Int, state: Boolean) {
        adapter.setToggle(position,state)
        adapter.notifyItemChanged(position)
    }

    private suspend fun monitorList() {
        viewModel.uiState.collect {
            Timber.d("monitorList called")
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
                adapter = CatAdapter(cats, catClickListener, voteListener = voteClickListener,
                    groupListener = groupListener, onDotsListener = onDotsListener)
                it.adapter = adapter
            }
        }

        swipe = binding.swipeLayout
        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            viewModel.getCats()
        }

        val e = binding.navView

        e.setOnItemSelectedListener {
            Timber.d(it.itemId.toString())
            if (it.itemId == 2131296550) {
//                adapter.changeText()
            }
            true
        }
        return binding.root
    }

    private val  onDotsListener =object: CatAdapter.OnDotsListener {
        override fun onClick(view: View, cat: Cat, position: Int) {
            // TODO: добавить position в параметры
//            viewModel.vote()

        }

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

    private val groupListener = object : CatAdapter.OnButtonCheckedListener {
        override fun onButtonChecked(group: MaterialButtonToggleGroup, cat: Cat) {
            Timber.d("onButtonChecked ")
            when (group.checkedButtonId) {
                R.id.btnVoteUp -> {
                    Timber.d("onButtonChecked VoteUp")
                }
                R.id.btnVoteDown -> {
                    Timber.d("onButtonChecked VoteDown")
                }
            }
        }

    }

}