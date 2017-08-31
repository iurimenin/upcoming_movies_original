package io.github.iurimenin.upcomingmovies.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.github.iurimenin.upcomingmovies.R
import io.github.iurimenin.upcomingmovies.model.MovieVO
import io.github.iurimenin.upcomingmovies.util.Utils


/**
 * Created by Iuri Menin on 26/08/17.
 */
class MovieAdapter(context: Context, private var movieVOs: ArrayList<MovieVO>) :
        ArrayAdapter<MovieVO>(context, 0, movieVOs) {

    private var mUtils = Utils()
    private var mInitialListFiscalizacoes: ArrayList<MovieVO> = ArrayList()
    private var mFilteredListFiscalizacoes: ArrayList<MovieVO> = ArrayList()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var convertView = convertView
        val movieVO = getItem(position)

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false)
        }

        val textViewName = convertView?.findViewById<TextView>(R.id.textViewName)
        val textViewGenre = convertView?.findViewById<TextView>(R.id.textViewGenre)
        val textViewReleaseDate = convertView?.findViewById<TextView>(R.id.textViewReleaseDate)
        val movieImage = convertView?.findViewById<ImageView>(R.id.movieImage)
        val genres = movieVO.getGenreString()

        textViewName?.text = movieVO.title

        if (genres.isEmpty())
            textViewGenre?.text = context.getString(R.string.no_genre)
        else
            textViewGenre?.text = movieVO?.getGenreString()

        if (movieVO.release_date?.isEmpty() == false)
            textViewReleaseDate?.text = mUtils.convertDate(context, movieVO.release_date!!)
        else
            textViewReleaseDate?.text = context.getString(R.string.no_release_date)

        var imagePath = ""
        if (movieVO.poster_path?.isEmpty() == false)
            imagePath = movieVO.poster_path!!
        else if (movieVO.backdrop_path?.isEmpty() == false)
            imagePath = movieVO.backdrop_path!!

        if (imagePath.isNotEmpty())
            Picasso.with(context)
                    .load(mUtils.getImageUrl185(imagePath))
                    .into(movieImage)
        else
            Picasso.with(context)
                    .load(R.drawable.no_image_available)
                    .into(movieImage)

        return convertView!!
    }

    fun flushFilter() {
        mInitialListFiscalizacoes.clear()
        mInitialListFiscalizacoes.addAll(this.movieVOs)
        mFilteredListFiscalizacoes.clear()
        notifyDataSetChanged()
    }

    fun setFilter(queryText: String) {
        mFilteredListFiscalizacoes.clear()
        if (!queryText.isEmpty()) {
            this.mInitialListFiscalizacoes
                    .filter { it.title?.toLowerCase()?.contains(queryText.toLowerCase()) == true }
                    .forEach { mFilteredListFiscalizacoes.add(it) }
        } else {
            mFilteredListFiscalizacoes.addAll(this.mInitialListFiscalizacoes)
        }

        movieVOs.clear()
        movieVOs.addAll(mFilteredListFiscalizacoes)
        notifyDataSetChanged()
    }
}