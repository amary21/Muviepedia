package com.ian.app.muviepedia.movie.movie.ui


import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.ian.app.helper.util.fullScreen
import com.ian.app.helper.util.gone
import com.ian.app.helper.util.loadWithGlide
import com.ian.app.helper.util.visible
import com.ian.app.muviepedia.data.BuildConfig.imageFormatter
import com.ian.app.muviepedia.data.data.ResultToConsume
import com.ian.app.muviepedia.data.data_source.movie.data.remote.DetailMovieData
import com.ian.app.muviepedia.data.data_source.movie.data.remote.toSaveDetail
import com.ian.app.muviepedia.data.data_source.movie.data.ui.MovieDataViewModel
import com.ian.app.muviepedia.data.util.MovieDetailConstant.movieAdapterCallback
import com.ian.app.muviepedia.movie.R
import com.ian.app.muviepedia.movie.databinding.FragmentMovieDetailBinding
import com.ian.recyclerviewhelper.helper.setUpVerticalListAdapter
import kotlinx.android.synthetic.main.item_similar.view.*
import org.koin.android.viewmodel.ext.android.viewModel


class MovieDetailFragment : Fragment() {
    private val vm: MovieDataViewModel by viewModel()
    private var idForDeleteItem: Int? = null
    private var isFavorite: Boolean = false
    private var menuItem: Menu? = null
    private lateinit var detailData: DetailMovieData
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val args = MovieDetailFragmentArgs.fromBundle(arguments!!)
        val binding: FragmentMovieDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_detail, container, false)
        binding.apply {
            consumeDetailData(args.movieID, this)
            consumeSimilarData(args.movieID, this)
            invalidateAll()

        }
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun consumeDetailData(movieId: Int, binding: FragmentMovieDetailBinding) {
        binding.apply {

            vm.observeDetailData(movieId).observe(viewLifecycleOwner, Observer { result ->
                when (result.status) {
                    ResultToConsume.Status.LOADING -> {
                        progressDetail.visible()
                    }
                    ResultToConsume.Status.SUCCESS -> {
                        progressDetail.gone()
                        result.data?.poster_path = imageFormatter + result.data?.poster_path
                        detailData = result.data
                        ivDetailMovieImages.setOnClickListener {
                            it.context.fullScreen(result.data?.poster_path)
                        }
                        consumeSaveDetailData(result.data)
                    }
                    ResultToConsume.Status.ERROR -> {
                        progressDetail.gone()
                        Snackbar.make(coordinatorParent, result.message!!, Snackbar.LENGTH_LONG).show()
                    }
                }
            })
        }


    }

    private fun consumeSimilarData(movieId: Int, binding: FragmentMovieDetailBinding) {
        binding.apply {
            vm.observeSimilarData(movieId).observe(viewLifecycleOwner, Observer { result ->
                when (result.status) {
                    ResultToConsume.Status.LOADING -> {
                        shimmerSimilar.startShimmer()
                    }
                    ResultToConsume.Status.SUCCESS -> {
                        if (!result.data?.results?.isNullOrEmpty()!!) {
                            rvSimilarMovie.setUpVerticalListAdapter(result.data!!.results, movieAdapterCallback, R.layout.item_similar, {
                                ivSimilarMovie.loadWithGlide(imageFormatter + it.poster_path)
                                tvSimilarMovieTittle.text = it.title
                                tvSimilarMovieReleaseDate.text = it.release_date
                            }, {
                                if (id != null) {
                                    consumeDetailData(id!!, this@apply)
                                    consumeSimilarData(id!!, this@apply)
                                }
                            })

                            if (shimmerSimilar.isShimmerStarted && shimmerSimilar.isShimmerVisible) {
                                shimmerSimilar.stopShimmer()
                                shimmerSimilar.hideShimmer()
                                shimmerSimilar.gone()
                            }
                        } else {
                            tvNoData.visible()
                            if (shimmerSimilar.isShimmerStarted && shimmerSimilar.isShimmerVisible) {
                                shimmerSimilar.stopShimmer()
                                shimmerSimilar.hideShimmer()
                                shimmerSimilar.gone()
                            }
                        }
                    }
                    ResultToConsume.Status.ERROR -> {
                        if (shimmerSimilar.isShimmerStarted && shimmerSimilar.isShimmerVisible) {
                            shimmerSimilar.stopShimmer()
                            shimmerSimilar.hideShimmer()
                            shimmerSimilar.gone()
                        }

                    }
                }
            })
        }
    }

    private fun consumeSaveDetailData(movieDetailData: DetailMovieData?) {
        vm.consumeSaveDetailMovie.observe(viewLifecycleOwner, Observer {
            if (movieDetailData != null) {
                detailData = movieDetailData
                it.forEach { result ->
                    if (result.id == movieDetailData.id) {
                        idForDeleteItem = result.id
                        isFavorite = true
                        setFavoriteState()
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_nav_menu, menu)
        menuItem = menu
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.add_to_favorite -> {
                if (isFavorite) {
                    if (idForDeleteItem != null) vm.deleteSelectedMovie(idForDeleteItem!!)
                    isFavorite = false
                    setFavoriteState()
                } else {
                    if (::detailData.isInitialized) vm.saveDetailMovieData(detailData.toSaveDetail())
                    isFavorite = true
                    setFavoriteState()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setFavoriteState() {
        if (isFavorite) {
            if (context != null) menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(context!!, R.drawable.ic_bookmark)
        } else {
            if (context != null) menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(context!!, R.drawable.ic_unbookmark)
        }

    }
    /* //handle backpressed
     override fun onActivityCreated(savedInstanceState: Bundle?) {
         super.onActivityCreated(savedInstanceState)
         requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
             override fun handleOnBackPressed() {
                 NavHostFragment.findNavController(this@MovieDetailFragment).navigateUp()
             }
         })
     }*/


}