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
import io.github.iurimenin.upcomingmovies.util.Utils


/**
 * Created by Iuri Menin on 29/08/17.
 */
class MovieDetailFragment : Fragment() {

    private val mUtils = Utils()
    private var movieVO: MovieVO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?,
                              @Nullable savedInstanceState: Bundle?): View? {

        val rootView = inflater?.inflate(R.layout.fragment_movie_datail,
                container,
                false)

        movieVO = activity.intent.extras.getParcelable(MovieVO.PARCELABLE_KEY)

        val textViewName = rootView?.findViewById<TextView>(R.id.textViewName)
        val textViewReleaseDate = rootView?.findViewById<TextView>(R.id.textViewReleaseDate)
        val textViewGenre = rootView?.findViewById<TextView>(R.id.textViewGenre)
        val textViewOverview = rootView?.findViewById<TextView>(R.id.textViewOverview)
        val imageViewMoviePoster = rootView?.findViewById<ImageView>(R.id.imageViewMoviePoster)
        val genres = movieVO?.getGenreString()

        textViewName?.text = movieVO?.title

        if (movieVO?.release_date?.isEmpty() == false)
            textViewReleaseDate?.text = mUtils.convertDate(context, movieVO?.release_date!!)
        else
            textViewReleaseDate?.text = context.getString(R.string.no_release_date)

        if (genres.isNullOrEmpty())
            textViewGenre?.text = context.getString(R.string.no_genre)
        else
            textViewGenre?.text = movieVO?.getGenreString()

        if (movieVO?.overview?.isEmpty() == true)
            textViewOverview?.text = getString(R.string.no_overview)
        else
            textViewOverview?.text = movieVO?.overview

        if (movieVO?.poster_path?.isEmpty() == false) {
            Picasso.with(context)
                    .load(mUtils.getImageUrl780(movieVO?.poster_path!!))
                    .into(imageViewMoviePoster)
        } else {
            Picasso.with(context)
                    .load(R.drawable.no_image_available)
                    .into(imageViewMoviePoster)
        }
        return rootView
    }
}