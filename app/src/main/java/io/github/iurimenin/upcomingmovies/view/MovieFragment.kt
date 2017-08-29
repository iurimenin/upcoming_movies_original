package io.github.iurimenin.upcomingmovies.view

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ProgressBar
import io.github.iurimenin.upcomingmovies.R
import io.github.iurimenin.upcomingmovies.model.MovieVO
import io.github.iurimenin.upcomingmovies.presenter.AsyncTaskCallback
import io.github.iurimenin.upcomingmovies.presenter.ListMoviesTask
import io.github.iurimenin.upcomingmovies.presenter.Utils
import kotlinx.android.synthetic.main.activity_list_movies.*


/**
 * Created by Iuri Menin on 26/08/17.
 */
class MovieFragment : Fragment(), AsyncTaskCallback {

    private var mMovieAdapter: MovieAdapter? = null
    private var mMoviesProgressBar : ProgressBar? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?,
                              @Nullable savedInstanceState: Bundle?): View? {

        val rootView = inflater?.inflate(R.layout.fragment_list_movies, container, false)
        val gridViewMovies = rootView?.findViewById<GridView>(R.id.gridViewMovies)

        mMoviesProgressBar = rootView?.findViewById<ProgressBar>(R.id.moviesProgressBar)
        mMovieAdapter = MovieAdapter(activity, ArrayList<MovieVO>())
        gridViewMovies?.adapter = mMovieAdapter
        gridViewMovies?.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            val movieVO = mMovieAdapter?.getItem(i)
            val intent = Intent(this.context, DetailActivity::class.java)
            intent.putExtra(MovieVO.PARCELABLE_KEY, movieVO)
            startActivity(intent)
        }

        return rootView
    }

    override fun onStart() {
        super.onStart()
        updateMovies()
    }

    override fun processFinish(output: Any) {

        mMovieAdapter?.clear()
        mMovieAdapter?.addAll(output as ArrayList<MovieVO>)
        mMovieAdapter?.notifyDataSetChanged()
    }

    private fun updateMovies() {

        val utils = Utils()
        if (utils.isNetworkConnected(this.context)) {
            val fetchWeatherTask = ListMoviesTask(this, context, mMoviesProgressBar)
            fetchWeatherTask.execute()
        } else {
            val snackbar = Snackbar.make(activityListMovies,
                    getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
            snackbar.setAction(getString(R.string.retry)) { updateMovies() }
            snackbar.show()
        }
    }
}