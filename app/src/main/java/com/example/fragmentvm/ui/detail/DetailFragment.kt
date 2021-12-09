package com.example.fragmentvm.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.fragmentvm.databinding.DetailFragmentBinding
import com.example.fragmentvm.utils.SharedViewModel

class DetailFragment : Fragment() {

    companion object {
        fun newInstance() = DetailFragment()
    }

    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: DetailFragmentBinding
    private lateinit var imgDetail: ImageView
    private lateinit var txtDetailHeader: TextView

    private val model: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imgDetail = binding.imgDetail
        txtDetailHeader = binding.txtDetailHeader
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        model.selected.observe(viewLifecycleOwner) {
            Glide.with(imgDetail).load(it.url).into(imgDetail)
            txtDetailHeader.text = it.url
        }
    }

}