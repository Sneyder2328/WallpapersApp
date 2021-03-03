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

package com.twismart.wallpapershd.utils

object Constants {
    const val API_KEY = "6ce7cedf1a1f41cea28628fce0467bc1"
    const val WALLPAPERS_LIST = "listWallpapers"
    const val WALLPAPER_TO_SHOW = "wallpaperToShow"

    enum class TypeListWallpapers(var value: String) {
        ALL("all"), MY_FAVORITES("myFavorites"), MOST_POPULAR("mostPopular")
    }
}
