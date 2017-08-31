package io.github.iurimenin.upcomingmovies.presenter

import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ProgressBar
import io.github.iurimenin.upcomingmovies.R
import io.github.iurimenin.upcomingmovies.model.MovieVO
import io.github.iurimenin.upcomingmovies.util.Utils
import io.github.iurimenin.upcomingmovies.view.MovieAdapter
import io.github.iurimenin.upcomingmovies.view.MovieDetailActivity

/**
 * Created by Iuri Menin on 26/08/17.
 */
class MoviesPresenter : AsyncTaskCallback {

    private var mContext: Context? = null
    private var mMovieAdapter: MovieAdapter? = null
    private var mMoviesProgressBar : ProgressBar? = null
    private var mView: View? = null

    fun  bindView(fragment: Fragment,
                  adapter: MovieAdapter?,
                  mProgressBar: ProgressBar?,
                  view : View) {

        this.mView = view
        this.mMovieAdapter = adapter
        this.mContext = fragment.context
        this.mMoviesProgressBar = mProgressBar
    }

    fun unBindView() {
        this.mContext = null
        this.mMovieAdapter = null
        this.mMoviesProgressBar = null
        this.mView = null
    }

    fun onStart() {
        val utils = Utils()
        this.mContext?.let {
            if (utils.isNetworkConnected(it)) {
                mMoviesProgressBar?.visibility = View.VISIBLE
                ListMoviesTask(this, it).execute()
            } else {
                mView?.let { it1 ->
                    val snackbar = Snackbar.make(it1,
                            it.getString(R.string.no_internet_connection),
                            Snackbar.LENGTH_LONG)
                    snackbar.setAction(it.getString(R.string.retry)) { onStart() }
                    snackbar.show()
                }
            }
        }
    }

    override fun processFinish(output: Any) {
        mMovieAdapter?.clear()
        mMovieAdapter?.addAll(output as ArrayList<MovieVO>)
        mMovieAdapter?.notifyDataSetChanged()
        mMovieAdapter?.flushFilter()
        mMoviesProgressBar?.visibility = View.INVISIBLE
    }

    fun itemClick(item: Int) {
        val movieVO = mMovieAdapter?.getItem(item)
        val intent = Intent(this.mContext, MovieDetailActivity::class.java)
        intent.putExtra(MovieVO.PARCELABLE_KEY, movieVO)
        this.mContext?.startActivity(intent)
    }
}