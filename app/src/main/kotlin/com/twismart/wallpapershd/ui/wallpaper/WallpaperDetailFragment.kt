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

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.twismart.wallpapershd.R
import com.twismart.wallpapershd.data.model.Wallpaper
import com.twismart.wallpapershd.ui.base.BaseFragment
import com.twismart.wallpapershd.utils.*
import kotlinx.android.synthetic.main.fragment_wallpaper.*

class WallpaperDetailFragment : BaseFragment() {

    companion object {
        private val ARG_WALLPAPER = "wallpaper"
        private val ARG_POSITION_FRAGMENT = "positionFragment"
        fun newInstance(wallpaper: Wallpaper, positionFragment: Int): WallpaperDetailFragment {
            debug("WallpaperDetailFragment newInstance id ${wallpaper.id}")
            val fragment = WallpaperDetailFragment()
            val args = Bundle()
            args.putParcelable(ARG_WALLPAPER, wallpaper)
            args.putInt(ARG_POSITION_FRAGMENT, positionFragment)
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var mWallpaper: Wallpaper
    var positionFragment: Int = 0
    var mListener: OnWallpaperFragmentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mWallpaper = arguments.getParcelable(ARG_WALLPAPER)
        positionFragment = arguments.getInt(ARG_POSITION_FRAGMENT)
        debug("WallpaperDetailFragment onCreate id ${mWallpaper.id}")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activityComponent?.inject(this)
        debug("WallpaperDetailFragment onCreateView id ${mWallpaper.id}")
        return activity.inflate(R.layout.fragment_wallpaper, container)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        debug("WallpaperDetailFragment onActivityCreated id ${mWallpaper.id}")
        // It's necessary use post method because of other way this would cause error due to imageWallpaper.getWidth() or getHeight() == 0
        imageWallpaper.post {
            // Calculate the width and height in order to fit the image correctly
            val (widthEnd, heightEnd) = calculateDimensions()

            // Pick up whether the image comes from a url or a path
            debug("onActivityCreated: widthEnd $widthEnd heightEnd $heightEnd")
            val sourceImage = if (mWallpaper.urlImage.isEmpty()) mWallpaper.pathImage else mWallpaper.urlImage
            Glide.with(activity)
                    .load(sourceImage)
                    .asBitmap()
                    .override(widthEnd, heightEnd)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageWallpaper)
            imageWallpaper.setDimensions(widthEnd, heightEnd)
            frameWrapper.setDimensions(widthEnd, heightEnd)
        }

        imageSwitchFavorite.setOnClickListener {
            debug("clickFavorite id ${mWallpaper.id}")
            if (mWallpaper.isFavorite) {
                mListener?.onClickUnFavorite(mWallpaper.id, positionFragment)
            } else {
                mListener?.onClickFavorite(mWallpaper, positionFragment)
            }
        }
    }

    private fun calculateDimensions(): Pair<Int, Int> {
        var widthEnd: Float
        var heightEnd: Float

        val widthWallpaper = mWallpaper.width.toFloat()
        val heightWallpaper = mWallpaper.height.toFloat()

        val imageViewWidth = imageWallpaper.width.toFloat()
        val imageViewHeight = imageWallpaper.height.toFloat()

        if (widthWallpaper > heightWallpaper) {
            widthEnd = imageViewWidth
            heightEnd = (heightWallpaper / widthWallpaper) * widthEnd
        } else {
            heightEnd = imageViewHeight
            widthEnd = (widthWallpaper / heightWallpaper) * heightEnd
        }

        if (widthEnd > imageViewWidth) {
            widthEnd = imageViewWidth
            heightEnd = (heightWallpaper / widthWallpaper) * widthEnd
        } else if (heightEnd > imageViewHeight) {
            heightEnd = imageViewHeight
            widthEnd = (widthWallpaper / heightWallpaper) * heightEnd
        }
        return widthEnd.toInt() to heightEnd.toInt()
    }

    val animation: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.move_right) }

    fun loadingWallpaper() {
        indicator.visible()
        indicator.startAnimation(animation)
    }

    fun readyWallpaper() {
        indicator.clearAnimation()
        indicator.gone()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnWallpaperFragmentListener) {
            mListener = context
        }
    }

    @TargetApi(21)
    fun wallpaperIsInFavorites() {
        mWallpaper.isFavorite = true
        imageSwitchFavorite.setColorFilter(Color.RED)

        if (isLollipopOrLater()) {
            val cx = imageSwitchFavorite.width / 2
            val cy = imageSwitchFavorite.height / 2
            val finalRadius = Math.max(imageSwitchFavorite.width, imageSwitchFavorite.height) / 2
            val anim = ViewAnimationUtils.createCircularReveal(imageSwitchFavorite, cx, cy, 0f, finalRadius.toFloat())
            anim.start()
        }
    }

    fun wallpaperIsNotInFavorites() {
        imageSwitchFavorite.setColorFilter(Color.WHITE)
        mWallpaper.isFavorite = false
    }


    interface OnWallpaperFragmentListener {
        fun onClickFavorite(wallpaperToFavorites: Wallpaper, positionFragment: Int)
        fun onClickUnFavorite(id: String, positionFragment: Int)
    }

    override fun onResume() {
        debug("F onResume")
        super.onResume()
    }

    override fun onPause() {
        debug("F onPause")
        super.onPause()
    }

    override fun onStop() {
        debug("F onStop")
        super.onStop()
    }

    override fun onDestroyView() {
        debug("F onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        debug("F onDestroy")
        super.onDestroy()
    }
}