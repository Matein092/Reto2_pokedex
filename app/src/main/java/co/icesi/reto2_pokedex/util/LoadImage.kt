package co.icesi.reto2_pokedex.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import java.io.IOException

class LoadImage(val imageView: ImageView) : AsyncTask<String, Void, Bitmap>() {


    override fun doInBackground(vararg p: String?): Bitmap {
        var linkUrl = p[0]
        var map: Bitmap? = null

        try {
            var inputStream = java.net.URL(linkUrl).openStream()
            map = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return map!!
    }
    override fun onPostExecute(result: Bitmap?) {
        imageView.setImageBitmap(result)
    }
}