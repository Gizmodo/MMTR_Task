package com.example.fragmentvm.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.fragmentvm.R
import com.example.fragmentvm.adapter.CatAdapter
import com.example.fragmentvm.databinding.SecondFragmentBinding
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.ui.detail.DetailFragment
import com.example.fragmentvm.utils.SharedVM
import timber.log.Timber

class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainVM by viewModels()
    private lateinit var binding: SecondFragmentBinding
    private var adapter: CatAdapter? = null
    private val model: SharedVM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SecondFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = CatAdapter(onItemClick)
        binding.recyclerview.adapter = adapter

        viewModel.catsList.observe(viewLifecycleOwner) {
            adapter!!.setCatsList(it)
            it?.forEach { item -> Timber.d(item.url) }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Timber.e(it)
        }

        viewModel.getFiveCats()
    }

    private val onItemClick = object : CatAdapter.OnItemClickListener {
        override fun onClick(item: Cat) {
            model.select(item)
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, DetailFragment.newInstance())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }
}