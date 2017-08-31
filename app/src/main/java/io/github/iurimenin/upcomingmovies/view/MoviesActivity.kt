package io.github.iurimenin.upcomingmovies.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import io.github.iurimenin.upcomingmovies.R

/**
 * Created by Iuri Menin on 26/08/17.
 */
class MoviesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.activityListMovies, MoviesFragment())
                    .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}
