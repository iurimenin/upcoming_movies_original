package io.github.iurimenin.upcomingmovies.presenter

/**
 * Created by Iuri Menin on 26/08/17.
 */
interface AsyncTaskCallback {
    fun processFinish(output: Any)
}