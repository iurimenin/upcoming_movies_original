package io.github.iurimenin.upcomingmovies.view

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ProgressBar
import io.github.iurimenin.upcomingmovies.R
import io.github.iurimenin.upcomingmovies.presenter.MoviesPresenter

/**
 * Created by Iuri Menin on 26/08/17.
 */
class MoviesFragment : Fragment(),
        SearchView.OnQueryTextListener,
        SearchView.OnCloseListener {

    private var mMovieAdapter: MovieAdapter? = null
    private var mProgressBar: ProgressBar? = null
    private var mMoviesPresenter: MoviesPresenter? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (mMoviesPresenter == null)
            mMoviesPresenter = MoviesPresenter()

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?,
                              @Nullable savedInstanceState: Bundle?): View? {

        val rootView = inflater?.inflate(R.layout.fragment_movies, container, false)
        val gridViewMovies = rootView?.findViewById<GridView>(R.id.gridViewMovies)
        mProgressBar = rootView?.findViewById(R.id.moviesProgressBar)

        mMovieAdapter = MovieAdapter(activity, ArrayList())
        gridViewMovies?.adapter = mMovieAdapter
        gridViewMovies?.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
           mMoviesPresenter?.itemClick(i)
        }

        rootView?.let {
            mMoviesPresenter?.bindView(this, mMovieAdapter, mProgressBar, it)
        }

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.movies_fragment, menu)
        val searchItem = menu?.findItem(R.id.actionSearchMovie)?.actionView as SearchView?
        searchItem?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        mMovieAdapter?.setFilter(query)
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        mMovieAdapter?.setFilter(newText)
        return false
    }

    override fun onClose(): Boolean {
        mMovieAdapter?.flushFilter()
        return false
    }

    override fun onStart() {
        super.onStart()
        mMoviesPresenter?.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMoviesPresenter?.unBindView()
        mMoviesPresenter = null
    }
}