package com.example.fragmentvm.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fragmentvm.R
import com.example.fragmentvm.adapter.CatAdapter
import com.example.fragmentvm.databinding.MainFragmentBinding
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.ui.detail.DetailFragment
import com.example.fragmentvm.utils.SharedVM
import timber.log.Timber

class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainVM by viewModels()
    private lateinit var binding: MainFragmentBinding
    private val sharedModel: SharedVM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.cats.observe(viewLifecycleOwner) { cats ->
            binding.recyclerview.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)
                it.adapter = CatAdapter(cats, catClickListener)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.cats.value?.size.let {
            if ((it == 0) || (it == null)) {
                viewModel.getCats()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.errors.observe(viewLifecycleOwner) {
            Timber.e(it)
        }
    }

    private val catClickListener = object : CatAdapter.onRecyclerViewItemClick {
        override fun onRecyclerViewItemClick(view: View, cat: Cat) {
            sharedModel.select(cat)
            val transaction = requireActivity()
                .supportFragmentManager
                .beginTransaction()

            with(transaction) {
                replace(
                    R.id.container,
                    DetailFragment.instance()
                )
                addToBackStack(null)
                commit()
            }
        }
    }
}