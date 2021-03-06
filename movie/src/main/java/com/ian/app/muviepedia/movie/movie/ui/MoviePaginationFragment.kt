package com.ian.app.muviepedia.movie.movie.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ian.app.helper.util.gone
import com.ian.app.helper.util.loadResizeWithGlide
import com.ian.app.muviepedia.movie.MovieViewModel
import com.ian.app.muviepedia.movie.R
import com.ian.app.muviepedia.movie.databinding.FragmentMoviePagingBinding
import com.ian.app.muvipedia.presentation.util.MovieDetailConstant.localMoviePopularPaginationAdapterCallback
import com.ian.app.muvipedia.presentation.util.MovieDetailConstant.localMovieUpComingPaginationAdapterCallback
import com.ian.app.muvipedia.presentation.util.MovieDetailConstant.popularMovie
import com.ian.app.muvipedia.presentation.util.MovieDetailConstant.upcomingMovie
import com.ian.recyclerviewhelper.helper.setUpPagingWithGrid
import kotlinx.android.synthetic.main.item_paging.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class MoviePaginationFragment : Fragment() {
    private val vm: MovieViewModel by viewModel()
    private lateinit var binding: FragmentMoviePagingBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_paging, container, false)
        val args = MoviePaginationFragmentArgs.fromBundle(arguments!!)
        initFetchNetworkData(args.movieType, binding)
        return binding.root
    }

    private fun initFetchNetworkData(state: String, binding: FragmentMoviePagingBinding) {
        binding.apply {
                when (state) {
                    popularMovie -> {
                        vm.observePopularPagination().observe(viewLifecycleOwner, Observer { data ->
                            rvDiscoverMovie.setUpPagingWithGrid(data, R.layout.item_paging, 2, {
                                ivDiscoverMovie.loadResizeWithGlide(it.poster_path)
                                tvDiscoverMovieDescription.text = it.title
                            }, localMoviePopularPaginationAdapterCallback, {
                                this@apply.root.findNavController().navigate(MoviePaginationFragmentDirections.actionPaginationFragmentToHomeFragment(id!!))
                            })

                            if (shimmerGridListContainer.isShimmerStarted && shimmerGridListContainer.isShimmerVisible) {
                                shimmerGridListContainer.stopShimmer()
                                shimmerGridListContainer.hideShimmer()
                                shimmerGridListContainer.gone()
                            }
                        })
                    }

                    upcomingMovie -> {
                        vm.observeUpComingPagination().observe(viewLifecycleOwner, Observer { data ->
                            rvDiscoverMovie.setUpPagingWithGrid(data, R.layout.item_paging, 2, {
                                ivDiscoverMovie.loadResizeWithGlide(it.poster_path)
                                tvDiscoverMovieDescription.text = it.title
                            }, localMovieUpComingPaginationAdapterCallback, {
                                this@apply.root.findNavController().navigate(MoviePaginationFragmentDirections.actionPaginationFragmentToHomeFragment(id!!))
                            })

                            if (shimmerGridListContainer.isShimmerStarted && shimmerGridListContainer.isShimmerVisible) {
                                shimmerGridListContainer.stopShimmer()
                                shimmerGridListContainer.hideShimmer()
                                shimmerGridListContainer.gone()
                            }
                        })
                    }
                }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.shimmerGridListContainer.stopShimmer()
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerGridListContainer.startShimmer()
    }
}
