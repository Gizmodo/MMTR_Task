package com.example.fragmentvm.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.fragmentvm.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailBottomSheet : BottomSheetDialogFragment() {
    companion object {
        private const val ARG_URL_KEY = "urlKey"
        fun instance(urlKey: String) = DetailBottomSheet().apply {
            arguments = bundleOf(ARG_URL_KEY to urlKey)
        }
    }
    private lateinit var binding: BottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = BottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = requireArguments().getString(ARG_URL_KEY)
        binding.apply {
            Glide.with(imgDetail).load(url).centerCrop().into(imgDetail)
        }
    }
}