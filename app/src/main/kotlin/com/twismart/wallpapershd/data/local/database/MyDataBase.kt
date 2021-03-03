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

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.twismart.wallpapershd.data.model.Wallpaper

class MyDataBase(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int)
    : SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        val DATABASE_NAME = "database"
        val TABLE_NAME = "favoriteWallpapers"
        val COLUMN_NAME_ID = "id"
        val COLUMN_NAME_IMAGE_URL = "imageUrl"
        val COLUMN_NAME_IMAGE_PATH = "imagePath"
        val COLUMN_NAME_WIDTH = "width"
        val COLUMN_NAME_HEIGHT = "height"
        val COLUMN_NAME_LICENSE = "license"
        val COLUMN_NAME_RATING = "rating"
        val VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_NAME(" +
                "$COLUMN_NAME_ID text primary key, " +
                "$COLUMN_NAME_IMAGE_URL text, " +
                "$COLUMN_NAME_IMAGE_PATH text, " +
                "$COLUMN_NAME_WIDTH text, " +
                "$COLUMN_NAME_HEIGHT text, " +
                "$COLUMN_NAME_LICENSE text, " +
                "$COLUMN_NAME_RATING text)")
    }

    override fun onUpgrade(db: SQLiteDatabase, versionAnte: Int, versionNue: Int) {
    }
}

fun Cursor.toWallpaper() = Wallpaper(getString(0), getString(1), getString(2), getString(3), getString(4), getString(5), getString(6))

fun Wallpaper.toContentValues(): ContentValues {
    val values = ContentValues()
    values.put(MyDataBase.COLUMN_NAME_ID, id)
    values.put(MyDataBase.COLUMN_NAME_IMAGE_URL, urlImage)
    values.put(MyDataBase.COLUMN_NAME_IMAGE_PATH, pathImage)
    values.put(MyDataBase.COLUMN_NAME_WIDTH, width)
    values.put(MyDataBase.COLUMN_NAME_HEIGHT, height)
    values.put(MyDataBase.COLUMN_NAME_LICENSE, license)
    values.put(MyDataBase.COLUMN_NAME_RATING, rating)
    return values
}