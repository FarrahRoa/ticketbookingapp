package com.example.ticketbooking.Models

import android.os.Parcel
import android.os.Parcelable

data class Film(
    var id: Int = 0,  // Add an id property
    var Title: String? = null,
    var Description: String? = null,
    var Poster: String? = null,
    var Time: String? = null,
    var Trailer: String? = null,
    var Imdb: Int = 0,
    var Year: Int = 0,
    var Price: Int = 0,
    var Genre: ArrayList<String> = ArrayList(),
    var Casts: ArrayList<Cast> = ArrayList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),  // Read id from parcel
        Title = parcel.readString(),
        Description = parcel.readString(),
        Poster = parcel.readString(),
        Time = parcel.readString(),
        Trailer = parcel.readString(),
        Imdb = parcel.readInt(),
        Year = parcel.readInt(),
        Price = parcel.readInt(),
        Genre = parcel.createStringArrayList() ?: ArrayList(),
        Casts = parcel.createTypedArrayList(Cast.CREATOR) ?: ArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)  // Write id to parcel
        parcel.writeString(Title)
        parcel.writeString(Description)
        parcel.writeString(Poster)
        parcel.writeString(Time)
        parcel.writeString(Trailer)
        parcel.writeInt(Imdb)
        parcel.writeInt(Year)
        parcel.writeInt(Price)
        parcel.writeStringList(Genre)
        parcel.writeTypedList(Casts)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Film> {
        override fun createFromParcel(parcel: Parcel): Film {
            return Film(parcel)
        }

        override fun newArray(size: Int): Array<Film?> {
            return arrayOfNulls(size)
        }
    }
}
