package io.github.iurimenin.upcomingmovies.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*


/**
 * Created by Iuri Menin on 26/08/17.
 */
class MovieVO protected constructor(`in`: Parcel) : Parcelable {

    var vote_count: String? = ""
    var id: Int = 0
    var vote_average:  String? = ""
    var title:  String? = ""
    var popularity:  String? = ""
    var poster_path:  String? = ""
    var original_language:  String? = ""
    var original_title:  String? = ""
    var backdrop_path:  String? = ""
    var adult:  String? = ""
    var overview:  String? = ""
    var release_date:  String? = ""
    var genre_ids: ArrayList<Int> = ArrayList()
    var genres: ArrayList<GenreVO> = ArrayList()

    fun getGenreString() : String {

        val sb = StringBuilder()
        if (genres != null ) {
            for (genre in genres) {
                if (sb.toString().isNotEmpty())
                    sb.append(", ")

                sb.append(genre.name)
            }
        }

        return sb.toString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.vote_count)
        dest.writeInt(this.id)
        dest.writeString(this.vote_average)
        dest.writeString(this.title)
        dest.writeString(this.popularity)
        dest.writeString(this.poster_path)
        dest.writeString(this.original_language)
        dest.writeString(this.original_title)
        dest.writeString(this.backdrop_path)
        dest.writeString(this.adult)
        dest.writeString(this.overview)
        dest.writeString(this.release_date)
        dest.writeList(this.genre_ids)
        dest.writeTypedList(this.genres)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as MovieVO

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {

        @JvmField val CREATOR: Parcelable.Creator<MovieVO> = object : Parcelable.Creator<MovieVO> {
            override fun createFromParcel(source: Parcel): MovieVO {
                return MovieVO(source)
            }

            override fun newArray(size: Int): Array<MovieVO?> {
                return arrayOfNulls(size)
            }
        }
        val  PARCELABLE_KEY: String = "movie"
    }

    init {
        this.vote_count = `in`.readString()
        this.id = `in`.readInt()
        this.vote_average = `in`.readString()
        this.title = `in`.readString()
        this.popularity = `in`.readString()
        this.poster_path = `in`.readString()
        this.original_language = `in`.readString()
        this.original_title = `in`.readString()
        this.backdrop_path = `in`.readString()
        this.adult = `in`.readString()
        this.overview = `in`.readString()
        this.release_date = `in`.readString()
        this.genre_ids = ArrayList()
        `in`.readList(this.genre_ids, Int::class.java.classLoader)
        this.genres = ArrayList()
        `in`.readTypedList(this.genres, GenreVO.CREATOR)
    }


}