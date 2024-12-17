package com.prayatna.storyapp.helper.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.prayatna.storyapp.R
import com.prayatna.storyapp.data.pref.UserPreference
import com.prayatna.storyapp.data.pref.dataStore
import com.prayatna.storyapp.data.remote.response.ListStory
import com.prayatna.storyapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class StackRemoteViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {

    private val apiService = ApiConfig.getInstance()
    private val mWidgetItems = ArrayList<ListStory>()
    private val mWidgetBitmap = ArrayList<Bitmap>()

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        runBlocking {
            mWidgetItems.clear()
            mWidgetBitmap.clear()
            Log.d("WidgetStory", "onDataSetChanged called")

            val token = UserPreference.getInstance(context.dataStore).getSession().first().token
            Log.d("WidgetStory", "Token retrieved: $token")

            try {
                val response = apiService.getStories(token!!, 0, 1, 5)
                Log.d("WidgetStory", "API response: $response")

                response.listStory?.take(5)?.forEach { image ->
                    Log.d("WidgetStory", "Processing image: ${image?.photoUrl}")

                    val bitmap = downloadBitmap(image!!.photoUrl)
                    if (bitmap != null) {
                        Log.d("WidgetStory", "Bitmap downloaded for image: ${image.photoUrl}")
                        mWidgetItems.add(image)
                        mWidgetBitmap.add(bitmap)
                    } else {
                        Log.e("WidgetStory", "Bitmap download failed for: ${image.photoUrl}")
                    }
                }
            } catch (e: Exception) {
                Log.e("WidgetStory", "Error in onDataSetChanged", e)
            }
        }
    }

    private fun downloadBitmap(url: String?): Bitmap? {
         return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
             null
        }
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int {
        return mWidgetItems.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        val bitmap = downloadBitmap(mWidgetItems[position].photoUrl)
        rv.setImageViewBitmap(R.id.imageView, bitmap)
        val extras = bundleOf(
            ImageBannerWidget.EXTRA_ITEM to position
        )
        val fillIntent = Intent()
        fillIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return false
    }
}