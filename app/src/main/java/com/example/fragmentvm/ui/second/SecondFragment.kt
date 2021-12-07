package com.example.fragmentvm.ui.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fragmentvm.adapter.MainAdapter
import com.example.fragmentvm.databinding.SecondFragmentBinding
import com.example.fragmentvm.repository.MainRepository
import com.example.fragmentvm.utils.Common
import com.example.fragmentvm.utils.MyViewModelFactory
import timber.log.Timber

class SecondFragment : Fragment() {

    companion object {
        fun newInstance() = SecondFragment()
    }

    //    private lateinit var viewModel: SecondViewModel
    private lateinit var viewModel: SecondViewModel
    lateinit var binding: SecondFragmentBinding
    val adapter = MainAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SecondFragmentBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.second_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.recyclerview.adapter = adapter

        viewModel =
            ViewModelProvider(this, MyViewModelFactory(MainRepository(Common.retrofitService))).get(
                SecondViewModel::class.java
            )


//        viewModel = ViewModelProvider(this).get(MainViewModelTest::class.java)
        viewModel.catsList.observe(viewLifecycleOwner) {
            adapter.setCatsList(it)
            it?.forEach { item -> Timber.d(item.url) }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Timber.e(it)
        }

        viewModel.getFiveCats()
        /*
        viewModel = ViewModelProvider(this).get(SecondViewModel::class.java)
        viewModel.catList.observe(this, {
            it?.forEach { item -> Timber.d(item.url) }
        })

        viewModel.getCatsList()*/
    }

}