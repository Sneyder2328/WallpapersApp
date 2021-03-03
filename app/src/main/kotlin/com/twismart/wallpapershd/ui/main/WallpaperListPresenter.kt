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

package com.twismart.wallpapershd.ui.main

import com.twismart.wallpapershd.data.IDataManager
import com.twismart.wallpapershd.data.model.Wallpaper
import com.twismart.wallpapershd.data.remote.WallpaperService
import com.twismart.wallpapershd.ui.base.BasePresenter
import com.twismart.wallpapershd.utils.debug
import com.twismart.wallpapershd.utils.error
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class WallpaperListPresenter<V : WallpaperListContract.View>
@Inject constructor(dataManager: IDataManager, compositeDisposable: CompositeDisposable) : BasePresenter<V>(dataManager, compositeDisposable), WallpaperListContract.Presenter<V> {

    override fun loadWallpapersList() {
        getListOfWallpapers(dataManager.loadWallpapersList(WallpaperService.GET_WALLPAPERS))
    }

    override fun loadMostPopularWallpapers() {
        getListOfWallpapers(dataManager.loadWallpapersList(WallpaperService.GET_MOST_POPULAR_WALLPAPERS))
    }

    override fun loadFavoriteWallpapers() {
        getListOfWallpapers(dataManager.loadFavoriteWallpapers())
    }

    private fun getListOfWallpapers(func: Observable<ArrayList<Wallpaper>>) {
        //use the same request scheme for the others methods because all of them return an Observable with an ArrayList of Wallpaper
        compositeDisposable.add(func
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<ArrayList<Wallpaper>>() {
                    override fun onComplete() {}
                    override fun onNext(wallpapers: ArrayList<Wallpaper>) {
                        debug("onNext2 $wallpapers", "getListOfWallpapers")
                        baseView?.setWallpaperList(wallpapers)
                    }

                    override fun onError(e: Throwable) {
                        error("onError2 ${e.message}", "getListOfWallpapers")
                    }
                })
        )
    }
}