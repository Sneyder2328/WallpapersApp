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

package com.twismart.wallpapershd.data.local.database

import com.twismart.wallpapershd.data.model.Wallpaper
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class MyDatabaseHelper
@Inject constructor(private val myDatabase: MyDataBase)
    : IMyDatabaseHelper {

    override fun addWallpaperToFavorites(wallpaper: Wallpaper): Observable<Unit> {
        return Observable.create { subscriber ->
            myDatabase.writableDatabase.use { SQLiteDatabase ->
                if (SQLiteDatabase.insert(MyDataBase.TABLE_NAME, null, wallpaper.toContentValues()).toInt() != -1) {
                    subscriber.onNext(Unit)
                    subscriber.onComplete()
                } else {
                    subscriber.onError(Throwable())
                    subscriber.onComplete()
                }
            }
        }
    }

    override fun deleteWallpaperFromFavorites(id: String): Observable<Unit> {
        return Observable.create { subscriber ->
            myDatabase.writableDatabase.use { SQLiteDatabase ->
                if (SQLiteDatabase.delete(MyDataBase.TABLE_NAME, "id=?", arrayOf(id)) != 0) {
                    subscriber.onNext(Unit)
                    subscriber.onComplete()
                } else {
                    subscriber.onError(Throwable())
                    subscriber.onComplete()
                }
            }
        }
    }


    override fun loadFavoriteWallpapers(): Observable<ArrayList<Wallpaper>> {
        return Observable.create { subscriber ->
            myDatabase.readableDatabase.use { SQLiteDatabase ->
                SQLiteDatabase.rawQuery("SELECT * FROM ${MyDataBase.TABLE_NAME}", null).use { cursor ->
                    val listWallpapers: ArrayList<Wallpaper> = ArrayList()
                    while (cursor.moveToNext()) {
                        listWallpapers.add(cursor.toWallpaper())
                    }
                    subscriber.onNext(listWallpapers)
                    subscriber.onComplete()
                }
            }
        }
    }

    override fun loadWallpaperFromFavorites(id: String): Observable<Wallpaper?> {
        return Observable.create { subscriber ->
            myDatabase.readableDatabase.use { SQLiteDatabase ->
                SQLiteDatabase.rawQuery("SELECT * FROM ${MyDataBase.TABLE_NAME} WHERE id=$id", null).use { cursor ->
                    if (cursor.moveToFirst()) {
                        val wallpaper = cursor.toWallpaper()
                        subscriber.onNext(wallpaper)
                    } else {
                        subscriber.onError(Throwable())
                    }
                    subscriber.onComplete()
                }
            }
        }
    }
}