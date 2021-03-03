/*
 * Copyright (C) 2017 Sneyder Angulo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twismart.wallpapershd.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Wallpaper(
        @SerializedName("id") @Expose var id: String,
        @SerializedName("image_url") @Expose var urlImage: String,
        @SerializedName("image_path") @Expose var pathImage: String,
        @SerializedName("width") @Expose var width: String,
        @SerializedName("height") @Expose var height: String,
        @SerializedName("license") @Expose var license: String,
        @SerializedName("rating") @Expose var rating: String = "",
        var isFavorite: Boolean = false) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Wallpaper> = object : Parcelable.Creator<Wallpaper> {
            override fun createFromParcel(source: Parcel): Wallpaper = Wallpaper(source)
            override fun newArray(size: Int): Array<Wallpaper?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readString().orEmpty(),
            source.readString().orEmpty(),
            source.readString().orEmpty(),
            source.readString().orEmpty(),
            source.readString().orEmpty(),
            source.readString().orEmpty(),
            source.readString().orEmpty(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(urlImage)
        dest.writeString(pathImage)
        dest.writeString(width)
        dest.writeString(height)
        dest.writeString(license)
        dest.writeString(rating)
        dest.writeInt((if (isFavorite) 1 else 0))
    }
}