package io.github.iurimenin.upcomingmovies.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Iuri Menin on 28/08/17.
 */
class GenreVO : Parcelable {

    var id: Int? = null
    var name: String? = null

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(this.id!!)
        dest.writeString(this.name)
    }

    constructor(id: Int){
        this.id = id
    }

    protected constructor(`in`: Parcel) {
        this.id = `in`.readInt()
        this.name = `in`.readString()
    }

    companion object {

        val CREATOR: Parcelable.Creator<GenreVO> = object : Parcelable.Creator<GenreVO> {
            override fun createFromParcel(source: Parcel): GenreVO {
                return GenreVO(source)
            }

            override fun newArray(size: Int): Array<GenreVO?> {
                return arrayOfNulls(size)
            }
        }
        val  PARCELABLE_KEY: String = "genre"
    }

}