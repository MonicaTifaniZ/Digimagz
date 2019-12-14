package com.monicatifanyz.digimagz

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

class Constant {
    val URL_IMAGE_NEWS = "http://digimon.kristomoyo.com/images/news/"
    val URL_IMAGE_STORY = "http://digimon.kristomoyo.com/images/coverstory/"
    val URL = "http://digimagz.kristomoyo.com/"

    fun getOrientationFromURI(
        context: Context,
        contentUri: Uri
    ): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getOrientationForV19AndUp(context, contentUri)
        } else {
            getOrientationForPreV19(context, contentUri)
        }
    }

    fun getOrientationForPreV19(
        context: Context,
        contentUri: Uri
    ): Int {
        var orient = 0
        val proj = arrayOf(MediaStore.Images.Media.ORIENTATION)
        val uri = contentUri.toString().split("/").toTypedArray()
        val id = uri[uri.size - 1]
        val sel = MediaStore.Images.Media._ID + "=?"
        val cursor = context.contentResolver.query(contentUri, proj, sel, arrayOf(id), null)
        if(cursor != null && cursor.moveToFirst()){
            var column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION)
            orient= cursor.getInt(column_index)
        }
        if(cursor != null){
            cursor.close()

        }
        return orient

    }

    fun getOrientationForV19AndUp(context: Context, contentUri: Uri):Int{
        var orient:Int=0

        val proj = arrayOf(MediaStore.Images.Media.ORIENTATION)
        val uri = contentUri.toString().split("/")
        val id = uri[uri.size-1]
        val sel = MediaStore.Images.Media._ID+"=?"
        val cursor = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,proj, sel, arrayOf(id), null)
        if(cursor != null && cursor.moveToFirst()){
            var column_index:Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION)
            orient = cursor.getInt(column_index)
        }
        if(cursor !=null){
            cursor.close()
        }

        return orient
    }
}