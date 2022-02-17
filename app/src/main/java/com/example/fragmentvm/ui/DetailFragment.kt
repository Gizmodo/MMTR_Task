package com.example.fragmentvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.fragmentvm.databinding.DetailFragmentBinding

class DetailFragment : Fragment() {

    companion object {
        fun instance() = DetailFragment()
    }

    private val args: DetailFragmentArgs by navArgs()
    private lateinit var binding: DetailFragmentBinding
    private lateinit var imgDetail: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imgDetail = binding.imgDetail
        Glide.with(imgDetail).load(args.catUrl).into(imgDetail)
    }
}