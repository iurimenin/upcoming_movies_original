package io.github.iurimenin.upcomingmovies.presenter

/**
 * Created by Iuri Menin on 26/08/17.
 */
interface AsyncTaskDelegate {
    fun processFinish(output: Any)
}