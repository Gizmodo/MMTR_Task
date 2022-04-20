package com.example.fragmentvm.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.fragmentvm.R
import com.example.fragmentvm.core.utils.getViewModel
import com.example.fragmentvm.core.utils.viewBindingWithBinder
import com.example.fragmentvm.data.datasource.FavCatPagingSource
import com.example.fragmentvm.databinding.FavouriteFragmentBinding
import com.example.fragmentvm.ui.utils.StateMain
import com.example.fragmentvm.ui.viewmodels.FavouriteViewModel
import kotlinx.coroutines.flow.collectLatest

class FavouriteFragment : Fragment(R.layout.favourite_fragment) {
    private val binding by viewBindingWithBinder(FavouriteFragmentBinding::bind)
    private val viewModel: FavouriteViewModel by lazy {
        getViewModel { FavouriteViewModel() }
    }
private var favCatsAdapter = FavCatPagingAdapter(){

}
    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var rv: RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUIStates()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        swipe = binding.swipeLayout
        rv = binding.recyclerview
        with(rv) {
            val animator = itemAnimator
            if (animator is SimpleItemAnimator) {
                animator.supportsChangeAnimations = false
            }
            setHasFixedSize(true)
            adapter = favCatsAdapter
        }
        lifecycleScope.launchWhenCreated {
            favCatsAdapter.addLoadStateListener { loadState ->
                if (loadState.source.refresh is LoadState.Loading) {
                    viewModel.setState(StateMain.Loading)
                }
                if (loadState.source.refresh is LoadState.NotLoading) {
                    viewModel.setState(StateMain.Finished)
                }
            }
            viewModel.favCats().collectLatest {
                favCatsAdapter.submitData(it)
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}