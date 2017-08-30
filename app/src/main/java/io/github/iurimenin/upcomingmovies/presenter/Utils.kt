package io.github.iurimenin.upcomingmovies.presenter

import android.content.Context
import android.net.ConnectivityManager
import android.text.format.DateUtils
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat


/**
 * Created by Iuri Menin on 26/08/17.
 */
class Utils {

    private val TAG = "Utils"
    private val IMAGES_URL_500 = "/w500"
    private val IMAGES_URL_185 = "/w185"
    private val IMAGES_URL = "https://image.tmdb.org/t/p/"

    fun getImageUrl780(poster_path: String): String {
        return IMAGES_URL + IMAGES_URL_500 + poster_path
    }

    fun getImageUrl185(poster_path: String): String {
        return IMAGES_URL + IMAGES_URL_185 + poster_path
    }

    fun convertDate(context: Context, releaseDate: String): String {

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        try {
            val date = simpleDateFormat.parse(releaseDate)
            return DateUtils.formatDateTime(context, date.time,
                    DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
        } catch (e: ParseException) {
            Log.e(TAG, "Erro in method convertDate", e)
        }

        return releaseDate
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}