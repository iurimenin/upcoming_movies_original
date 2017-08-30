package io.github.iurimenin.upcomingmovies.presenter

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.ProgressBar
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
import kotlin.collections.HashMap


/**
 * Created by Iuri Menin on 26/08/17.
 */
class ListMoviesTask(private val mCallback: AsyncTaskCallback,
                     private val mContext: Context,
                     private val mMoviesProgressBar : ProgressBar?) :
        AsyncTask<String, Void, ArrayList<MovieVO>>() {

    private val GENRES = "genres"
    private val RESULT = "results"
    private val TAG = "ListMoviesTask"
    private val TOTAL_PAGES = "total_pages"

    private var mBufferedReader: BufferedReader? = null
    private var mUrlConnection: HttpURLConnection? = null
    private val mGenreList = HashMap<Int?, GenreVO>()

    override fun onPreExecute() {
        mMoviesProgressBar?.visibility = View.VISIBLE
        loadGenres()
    }

    override fun doInBackground(vararg params: String): ArrayList<MovieVO> {
        return getMoviesFromApi()
    }

    override fun onPostExecute(result: ArrayList<MovieVO>) {
        mMoviesProgressBar?.visibility = View.INVISIBLE
        super.onPostExecute(result)
        mCallback.processFinish(result)
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

                mUrlConnection = url.openConnection() as HttpURLConnection
                mUrlConnection?.requestMethod = "GET"
                mUrlConnection?.connect()

                val inputStream = mUrlConnection?.inputStream
                val buffer = StringBuffer()
                if (inputStream != null) {
                    mBufferedReader = BufferedReader(InputStreamReader(inputStream))

                    var line = mBufferedReader?.readLine()
                    while (line != null) {
                        buffer.append(line + "\n")
                        line = mBufferedReader?.readLine()
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
            Log.e(TAG, "Error on getMoviesFromApi", e)
            return ArrayList()
        } finally {
            mUrlConnection?.disconnect()
            mBufferedReader?.close()
        }
        return listMovies
    }

    private fun getPhoneLanguage(): String {
        return Locale.getDefault().toString().replace("_", "-")
    }

    private fun loadGenres() {

        val gson = Gson()
        val typeGenre = object : TypeToken<List<GenreVO>>() {}.type

        val sharedPref = mContext.getSharedPreferences("UPCOMINGMOVIES", Context.MODE_PRIVATE)
        val jsonStored = sharedPref?.getString(GenreVO.TAG, "")

        if (!jsonStored.isNullOrEmpty()) {
            val listStored : ArrayList<GenreVO> = gson.fromJson(jsonStored, typeGenre)
            for (genreVO in listStored) {
                mGenreList.put(genreVO.id, genreVO)
            }
        } else {
            try {
                val uri = Uri.parse(BuildConfig.THEMOVIEDB_API_URL).buildUpon()
                        .appendPath("genre")
                        .appendPath("movie")
                        .appendPath("list")
                        .appendQueryParameter("api_key", BuildConfig.API_KEY)
                        .appendQueryParameter("language", getPhoneLanguage())
                        .build()

                val url = URL(uri.toString())

                mUrlConnection = url.openConnection() as HttpURLConnection
                mUrlConnection?.requestMethod = "GET"
                mUrlConnection?.connect()

                val inputStream = mUrlConnection?.inputStream
                val buffer = StringBuffer()
                if (inputStream != null) {
                    mBufferedReader = BufferedReader(InputStreamReader(inputStream))

                    var line = mBufferedReader?.readLine()
                    while (line != null) {
                        buffer.append(line + "\n")
                        line = mBufferedReader?.readLine()
                    }

                    if (buffer.isNotEmpty()) {
                        val json = buffer.toString()
                        val genreJson = JSONObject(json)

                        val genreList: ArrayList<GenreVO> =
                                gson.fromJson(genreJson.get(GENRES).toString(), typeGenre)

                        val editor = sharedPref.edit()
                        editor.putString(GenreVO.TAG, genreJson.get(GENRES).toString())
                        editor.apply()
                        for (genreVO in genreList) {
                            mGenreList.put(genreVO.id, genreVO)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error on loadGenres", e)
            } finally {
                mUrlConnection?.disconnect()
                mBufferedReader?.close()
            }
        }
    }

    private fun getMoviesDataFromJson(moviesJson: JSONObject): ArrayList<MovieVO> {

        val gson = Gson()
        val moviesArray = moviesJson.getJSONArray(RESULT)

        val typeMovies = object : TypeToken<List<MovieVO>>() {}.type

        val movies : ArrayList<MovieVO> = gson.fromJson(moviesArray.toString(), typeMovies)

        for (movie in movies) {
            movie.genres = ArrayList()
            for (genre_id in movie.genre_ids) {
                mGenreList[genre_id]?.let { movie.genres.add(it) }
            }
        }
        return movies
    }
}