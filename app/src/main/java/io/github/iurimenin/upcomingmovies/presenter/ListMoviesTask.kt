package io.github.iurimenin.upcomingmovies.presenter

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.iurimenin.upcomingmovies.BuildConfig
import io.github.iurimenin.upcomingmovies.model.GenreVO
import io.github.iurimenin.upcomingmovies.model.MovieVO
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Iuri Menin on 26/08/17.
 */
class ListMoviesTask(private val delegate: AsyncTaskDelegate?, private val context: Context) : AsyncTask<String, Void, ArrayList<MovieVO>>() {

    private val RESULT = "results"
    private val TOTAL_PAGES = "total_pages"
    private val TAG = "ListMoviesTask"

    private var reader: BufferedReader? = null
    private var urlConnection: HttpURLConnection? = null

    override fun doInBackground(vararg params: String): ArrayList<MovieVO> {
        return getMoviesFromApi()
    }

    private fun getMoviesFromApi(): ArrayList<MovieVO> {
        val listMovies = ArrayList<MovieVO>()

        var page = 1
        var totalPages = 1
        try {

            while(page <= totalPages) {
                val uri = Uri.parse(BuildConfig.THEMOVIEDB_API_URL).buildUpon()
                        .appendPath("movie")
                        .appendPath("upcoming")
                        .appendQueryParameter("api_key", BuildConfig.API_KEY)
                        .appendQueryParameter("language", getPhoneLanguage())
                        .appendQueryParameter("page", page.toString())
                        .build()

                val url = URL(uri.toString())

                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection?.setRequestMethod("GET")
                urlConnection?.connect()

                val inputStream = urlConnection?.getInputStream()
                val buffer = StringBuffer()
                if (inputStream != null) {
                    reader = BufferedReader(InputStreamReader(inputStream))

                    var line = reader?.readLine()
                    while (line != null) {
                        buffer.append(line + "\n")
                        line = reader?.readLine()
                    }

                    if (buffer.isEmpty()) {
                        return ArrayList()
                    }
                    val json = buffer.toString()
                    val moviesJson = JSONObject(json)
                    listMovies.addAll(getMoviesDataFromJson(moviesJson))
                    totalPages = moviesJson.getInt(TOTAL_PAGES)
                    page++
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error ", e)
            return ArrayList()
        } finally {
            urlConnection?.disconnect()
            reader?.close()
        }
        return listMovies
    }

    private fun  getPhoneLanguage(): String {
        return Locale.getDefault().toString().replace("_", "-")
    }

    private fun getGenreDataFromJson(): ArrayList<GenreVO> {

        var fileName = ""
        if (Locale.getDefault().language == "pt")
            fileName = "genre-pt.json"
        else
            fileName = "genre.json"

        reader = BufferedReader(InputStreamReader(context.assets.open(fileName)))
        var line = reader?.readLine()
        val buffer = StringBuffer()
        while (line != null) {
            buffer.append(line + "\n")
            line = reader?.readLine()
        }
        val json = buffer.toString()
        val gson = Gson()
        val typeGenre = object : TypeToken<List<GenreVO>>() {}.type

        val genreList : ArrayList<GenreVO> = gson.fromJson(json, typeGenre)
        return genreList
    }

    private fun getMoviesDataFromJson(moviesJson: JSONObject): ArrayList<MovieVO> {

        val gson = Gson()
        val moviesArray = moviesJson.getJSONArray(RESULT)

        val typeMovies = object : TypeToken<List<MovieVO>>() {}.type

        val movies : ArrayList<MovieVO> = gson.fromJson(moviesArray.toString(), typeMovies)

        val moviesWithPoster = ArrayList<MovieVO>()
        val genreList = getGenreDataFromJson()
        for (movie in movies) {
            if(movie.poster_path != null) {
                for (genreId in movie.genre_ids) {
                    val genre = genreList.filter { vo -> vo.id == genreId }.single()
                    if (movie.genres == null)
                        movie.genres = ArrayList<GenreVO>()
                    movie.genres.add(genre)
                }
                moviesWithPoster.add(movie)
            }
        }
        return moviesWithPoster
    }

    override fun onPostExecute(result: ArrayList<MovieVO>) {

        super.onPostExecute(result)
        delegate?.processFinish(result)
    }
}