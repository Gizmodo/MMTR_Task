package com.example.fragmentvm.ui.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fragmentvm.adapter.MainAdapter
import com.example.fragmentvm.databinding.SecondFragmentBinding
import com.example.fragmentvm.model.Cat
import timber.log.Timber

class SecondFragment : Fragment() {
    companion object {
        fun newInstance() = SecondFragment()
    }

    private lateinit var viewModel: SecondViewModel
    private lateinit var binding: SecondFragmentBinding
    private var adapter: MainAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SecondFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = MainAdapter(onItemClick)
        binding.recyclerview.adapter = adapter

        viewModel = ViewModelProvider(this)[SecondViewModel::class.java]

        viewModel.catsList.observe(viewLifecycleOwner) {
            adapter!!.setCatsList(it)
            it?.forEach { item -> Timber.d(item.url) }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Timber.e(it)
        }

        viewModel.getFiveCats()
    }

    private val onItemClick = object : MainAdapter.OnItemClickListener {
        override fun onClick(item: Cat) {
            Timber.d("Click performed on cat with url ${item.url}")
        }
    }

}