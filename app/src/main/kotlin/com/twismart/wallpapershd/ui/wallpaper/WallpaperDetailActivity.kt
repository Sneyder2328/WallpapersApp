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

package com.twismart.wallpapershd.ui.wallpaper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.MenuItem
import com.amazon.device.ads.*
import com.twismart.wallpapershd.R
import com.twismart.wallpapershd.data.model.Wallpaper
import com.twismart.wallpapershd.ui.adapters.DepthPageTransformer
import com.twismart.wallpapershd.ui.base.BaseActivity
import com.twismart.wallpapershd.utils.*
import kotlinx.android.synthetic.main.activity_wallpaper.*

class WallpaperDetailActivity : BaseActivity(), WallpaperDetailContract.View, WallpaperDetailFragment.OnWallpaperFragmentListener {

    companion object {
        fun newIntent(context: Context, wallpaperList: List<Wallpaper>, wallpaperToShow: Int): Intent {
            val newIntent = android.content.Intent(context, WallpaperDetailActivity::class.java)
            newIntent.putParcelableArrayListExtra(Constants.WALLPAPERS_LIST, ArrayList(wallpaperList))
            newIntent.putExtra(com.twismart.wallpapershd.utils.Constants.WALLPAPER_TO_SHOW, wallpaperToShow)
            return newIntent
        }
    }

    var wallpapersList: ArrayList<Wallpaper>? = null

    @javax.inject.Inject lateinit var mPresenter: WallpaperDetailContract.Presenter<WallpaperDetailContract.View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.amazon.device.ads.AdRegistration.setAppKey(Constants.API_KEY)
        setContentView(R.layout.activity_wallpaper)
        activityComponent.inject(this)
        mPresenter.attachView(this)
        setUp()
    }


    override fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        wallpapersList = intent?.getParcelableArrayListExtra<Wallpaper>(com.twismart.wallpapershd.utils.Constants.WALLPAPERS_LIST)
        mViewPager.adapter = WallpaperDetailPagerAdapter(fragmentManager, applicationContext, wallpapersList)
        mViewPager.setPageTransformer(true, DepthPageTransformer())
        var currentItem = intent.getIntExtra(com.twismart.wallpapershd.utils.Constants.WALLPAPER_TO_SHOW, 0)
        //set and show the wallpaper selected
        mViewPager.currentItem = currentItem
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
            override fun onPageSelected(position: Int) {
                currentItem = position
                val wallpaperFragment = mViewPager.adapter?.instantiateItem(mViewPager, position) as WallpaperDetailFragment
                mPresenter.checkIfWallpaperIsFavorite(wallpaperFragment.mWallpaper.id, position)
            }
        })
        //the checking should be after the post once the fragment is instanced and ready to be used
        mViewPager.post {
            mPresenter.checkIfWallpaperIsFavorite(intent.getParcelableArrayListExtra<Wallpaper>(Constants.WALLPAPERS_LIST)[currentItem].id, currentItem)
        }

        //amazon ads
        val mAdLayout: AdLayout = findViewById(R.id.adLayout)
        mAdLayout.setListener(object : AdListener {
            override fun onAdLoaded(ad: Ad?, p1: AdProperties?) {
                debug("onAdLoaded")
            }
            override fun onAdFailedToLoad(ad: Ad?, error: AdError?) {
                error("onAdFailedToLoad ${error?.message}")
                mAdLayout.gone()
            }
            override fun onAdExpanded(p0: Ad?) {}
            override fun onAdDismissed(p0: Ad?) {}
            override fun onAdCollapsed(p0: Ad?) {}
        })
        mAdLayout.loadAd()
    }


    // implementation of WallpaperDetailContract.View
    override fun wallpaperIsInFavorites(positionFragment: Int) {
        val wallpaperFragment = mViewPager.adapter?.instantiateItem(mViewPager, positionFragment) as WallpaperDetailFragment
        wallpaperFragment.wallpaperIsInFavorites()
    }

    override fun wallpaperIsNotInFavorites(positionFragment: Int) {
        val wallpaperFragment = mViewPager.adapter?.instantiateItem(mViewPager, positionFragment) as WallpaperDetailFragment
        wallpaperFragment.wallpaperIsNotInFavorites()
    }

    override fun loadingWallpaper(positionFragment: Int) {
        if ((mViewPager.currentItem - positionFragment).toAbsoluteValue() <= 1) {
            val wallpaperFragment = mViewPager.adapter?.instantiateItem(mViewPager, positionFragment) as WallpaperDetailFragment
            wallpaperFragment.loadingWallpaper()
        }
    }

    override fun readyWallpaper(positionFragment: Int) {
        if ((mViewPager.currentItem - positionFragment).toAbsoluteValue() <= 1) {
            val wallpaperFragment = mViewPager.adapter?.instantiateItem(mViewPager, positionFragment) as WallpaperDetailFragment
            wallpaperFragment.readyWallpaper()
        }
    }

    // WallpaperDetailFragment Listener
    override fun onClickFavorite(wallpaperToFavorites: Wallpaper, positionFragment: Int) {
        mPresenter.addWallpaperToFavorites(wallpaperToFavorites, positionFragment)
    }

    override fun onClickUnFavorite(id: String, positionFragment: Int) {
        mPresenter.deleteWallpaperFromFavorites(id, positionFragment)
    }


    //Menu methods
    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menuInflater.inflate(R.menu.menu_wallpapers, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.menu_set_wallpaper -> {
                val url = wallpapersList!![mViewPager.currentItem].urlImage
                mPresenter.setWallpaperFromUrl(url, mViewPager!!.currentItem)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}
