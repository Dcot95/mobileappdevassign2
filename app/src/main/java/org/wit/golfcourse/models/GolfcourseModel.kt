package org.wit.golfcourse.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GolfcourseModel(var id: Long = 0,
                           var title: String = "",
                           var description: String = "",
                           var dateplayed: String = "",
                           var price: String = "",
                           var rating: Float = 0.0f,
                           var image: Uri = Uri.EMPTY,
                           var lat : Double = 0.0,
                           var lng: Double = 0.0,
                           var zoom: Float = 0f) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable