package com.exzell.myfootball.utils

import android.content.Context
import com.exzell.myfootball.BuildConfig
import java.io.File

fun Context.getCacheDir(): File {
    return if(BuildConfig.DEBUG) cacheDir else externalCacheDir!!
}