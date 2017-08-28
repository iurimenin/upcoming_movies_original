package io.github.iurimenin.upcomingmovies.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso
import io.github.iurimenin.upcomingmovies.R
import io.github.iurimenin.upcomingmovies.model.MovieVO
import io.github.iurimenin.upcomingmovies.presenter.Utils


/**
 * Created by Iuri Menin on 26/08/17.
 */
class MovieAdapter(context: Context, movieVOs: List<MovieVO>) :
        ArrayAdapter<MovieVO>(context, 0, movieVOs) {

    var mUtils = Utils()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var convertView = convertView
        val movieVO = getItem(position)

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false)
        }

        Picasso.with(context)
                .load(mUtils.getImageUrl185(movieVO.poster_path))
                .into(convertView?.findViewById<ImageView>(R.id.movieImage))

        return convertView!!
    }
}