package com.example.fragmentvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fragmentvm.R
import com.example.fragmentvm.adapter.CatAdapter
import com.example.fragmentvm.databinding.MainFragmentBinding
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.utils.SharedViewModel
import com.example.fragmentvm.viewmodel.MainViewModel

class MainFragment : Fragment() {
    companion object {
        fun instance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private val sharedModel: SharedViewModel by activityViewModels()
    private lateinit var btnBack: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.cats.observe(viewLifecycleOwner) { cats ->
            binding.recyclerview.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)
                it.adapter = CatAdapter(cats, catClickListener)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUIs()
    }

    private fun initUIs() {
        btnBack = binding.btnBack
        btnBack.setOnClickListener {
            navigateBack()
        }
    }

    private fun navigateBack() {
        val transaction = requireActivity()
            .supportFragmentManager
            .beginTransaction()

        with(transaction) {
            replace(
                R.id.container,
                ApiFragment.instance()
            )
            commit()
        }
    }

    private val catClickListener = object : CatAdapter.OnRecyclerViewItemClick {
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