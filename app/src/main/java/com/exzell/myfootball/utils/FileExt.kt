package com.exzell.myfootball.utils

import android.content.Context
import android.net.Uri
import java.io.File

fun File.copyFrom(uri: Uri, context: Context){

    context.contentResolver.openInputStream(uri)?.apply {
        if(!exists()) createNewFile()

        val array = ByteArray(available()).apply { read(this) }
        outputStream().write(array)
    }
}