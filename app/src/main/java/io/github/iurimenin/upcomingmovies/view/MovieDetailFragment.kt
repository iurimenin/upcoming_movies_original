package io.github.iurimenin.upcomingmovies.view

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.github.iurimenin.upcomingmovies.R
import io.github.iurimenin.upcomingmovies.model.MovieVO
import io.github.iurimenin.upcomingmovies.presenter.Utils


/**
 * Created by Iuri Menin on 29/08/17.
 */
class MovieDetailFragment : Fragment() {

    private var movieVO: MovieVO? = null
    val mUtils = Utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {

        val rootView = inflater?.inflate(R.layout.fragment_movie_datail, container, false)

        movieVO = activity.intent.extras.getParcelable(MovieVO.PARCELABLE_KEY)

        rootView?.findViewById<TextView>(R.id.textViewName)?.text = movieVO?.title
        rootView?.findViewById<TextView>(R.id.textViewReleaseDate)?.text = mUtils.convertDate(context, movieVO?.release_date!!)
        rootView?.findViewById<TextView>(R.id.textViewGenre)?.text = movieVO?.getGenreString()
        rootView?.findViewById<TextView>(R.id.textViewOverview)?.text = movieVO?.overview

        Picasso.with(context)
                .load(mUtils.getImageUrl780(movieVO?.poster_path!!))
                .into(rootView?.findViewById<ImageView>(R.id.imageViewMoviePoster))

        return rootView
    }
}