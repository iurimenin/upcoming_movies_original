package io.github.iurimenin.upcomingmovies.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Iuri Menin on 28/08/17.
 */
class GenreVO protected constructor(`in`: Parcel) : Parcelable {

    var id: Int? = null
    var name: String? = null

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(this.id!!)
        dest.writeString(this.name!!)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GenreVO

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id ?: 0
    }

    companion object {

        @JvmField val CREATOR: Parcelable.Creator<GenreVO> = object : Parcelable.Creator<GenreVO> {
            override fun createFromParcel(source: Parcel): GenreVO {
                return GenreVO(source)
            }

            override fun newArray(size: Int): Array<GenreVO?> {
                return arrayOfNulls(size)
            }
        }
        val TAG: String = "GenreVO"
        val  PARCELABLE_KEY: String = "genre"
    }

    init {
        this.id = `in`.readInt()
        this.name = `in`.readString()
    }

}