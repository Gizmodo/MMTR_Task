package com.example.fragmentvm.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.fragmentvm.R
import com.example.fragmentvm.core.utils.getViewModel
import com.example.fragmentvm.core.utils.viewBindingWithBinder
import com.example.fragmentvm.databinding.FavouriteFragmentBinding
import com.example.fragmentvm.ui.viewmodels.FavouriteViewModel

class FavouriteFragment : Fragment(R.layout.favourite_fragment) {
    private val binding by viewBindingWithBinder(FavouriteFragmentBinding::bind)
    private val viewModel: FavouriteViewModel by lazy {
        getViewModel { FavouriteViewModel() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}