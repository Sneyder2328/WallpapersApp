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

import android.os.Bundle
import android.support.design.widget.TabLayout
import com.twismart.wallpapershd.R
import com.twismart.wallpapershd.ui.adapters.ZoomOutPageTransformer
import com.twismart.wallpapershd.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityComponent.inject(this)
        setUp()
    }

    override fun setUp() {
        setSupportActionBar(toolbar)

        tabLayout.tabMode = TabLayout.MODE_FIXED

        mViewPager.adapter = MainPagerAdapter(fragmentManager, this@MainActivity)
        mViewPager.setPageTransformer(true, ZoomOutPageTransformer())
        tabLayout.setupWithViewPager(mViewPager)
    }
}