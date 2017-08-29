package io.github.iurimenin.upcomingmovies.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.github.iurimenin.upcomingmovies.R

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.activity_main, MovieDetailFragment())
                    .commit()
        }
    }
}
