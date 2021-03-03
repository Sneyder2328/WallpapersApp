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

@file:Suppress("unused")

package com.twismart.wallpapershd.ui.base

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.twismart.wallpapershd.WallpaperApplication
import com.twismart.wallpapershd.di.component.ActivityComponent
import com.twismart.wallpapershd.di.component.DaggerActivityComponent
import com.twismart.wallpapershd.di.module.ActivityModule
import com.twismart.wallpapershd.utils.*

abstract class BaseActivity : AppCompatActivity(), BaseContract.View {

    val mProgressDialog: ProgressDialog by lazy { progressDialog() }

    lateinit var activityComponent: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent = DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .applicationComponent((application as WallpaperApplication).component)
                .build()
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun showLoading() {
        hideLoading()
        mProgressDialog.show()

    }

    override fun hideLoading() {
        if (mProgressDialog.isShowing)
            mProgressDialog.cancel()
    }

    override fun showToast(resId: Int) = toast(resId)

    override fun showToast(message: String) = toast(message)

    override fun showSnackBar(message: String) = snackBar(message)

    override fun showSnackBar(resId: Int) = snackBar(resId)

    override fun isNetworkConnected() = applicationContext.isNetworkConnected()

    override fun hideKeyboard() {
        hideSoftInput()
    }

    protected abstract fun setUp()
}
