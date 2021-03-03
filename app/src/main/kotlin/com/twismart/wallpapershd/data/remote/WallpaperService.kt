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

package com.twismart.wallpapershd.data.remote

import com.twismart.wallpapershd.data.model.Wallpaper
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url
import java.util.*

interface WallpaperService {

    companion object {
        val END_POINT = "http://sneyder.net/wallpapers/"
        //val GET_WALLPAPERS = "getWallpapers.php"
        val GET_WALLPAPERS = "index.php"
        val GET_MOST_POPULAR_WALLPAPERS = GET_WALLPAPERS
        //val GET_MOST_POPULAR_WALLPAPERS = "getMostPopularWallpapers.php"
        val INCREASE_RATING = "increaseRatingWallpaper.php"
    }

    @GET
    fun loadWallpapersList(
            @Url url: String)
            : Observable<ArrayList<Wallpaper>>

    @POST
    fun increaseRating(
            @Url url: String,
            @Query("extraRating")extraRating: Int,
            @Query("idWallpaper")idWallpaper: Int)
            : Observable<Unit>
}