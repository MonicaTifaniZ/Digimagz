package com.monicatifanyz.digimagz.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.InputDevice
import android.view.MotionEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.monicatifanyz.digimagz.R
import kotlinx.android.synthetic.main.activity_detail_video.*

class DetailVideoActivity : AppCompatActivity() {

    private lateinit var youtubeId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_video)

        val intent:Intent = getIntent()
        youtubeId = intent.getStringExtra("youtubeId")

        val url:String = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + youtubeId + "?autoplay=1\" frameborder=\"0\" allow=\"accelerometer;  autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"

        webViewVideo.settings.javaScriptEnabled = true
        webViewVideo.settings.javaScriptCanOpenWindowsAutomatically = true
        webViewVideo.settings.pluginState = WebSettings.PluginState.ON

        webViewVideo.loadData(url, "text/html", "utf-8")
        webViewVideo.webViewClient = AutoPlayVideoWebViewClient(this)

    }

    class AutoPlayVideoWebViewClient(var context: Context) : WebViewClient() {

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            val delta:Long = 100
            val downTime:Long = SystemClock.uptimeMillis()

            val x : Float = (view!!.left + (view.width/2)).toFloat()
            val y : Float = (view!!.top + (view.height/2)).toFloat()

            val tapDownEvent: MotionEvent = MotionEvent.obtain(downTime, downTime + delta, MotionEvent.ACTION_DOWN, x,y, 0)
            tapDownEvent.source = InputDevice.SOURCE_CLASS_POINTER
            val tapUpEvent:MotionEvent = MotionEvent.obtain(downTime, downTime + delta + 2, MotionEvent.ACTION_UP,x,y,0)
            tapUpEvent.source = InputDevice.SOURCE_CLASS_POINTER

            if (view != null) {
                view.dispatchTouchEvent(tapDownEvent)
            }
            if (view != null) {
                view.dispatchTouchEvent(tapUpEvent)
            }

            //ShowHide
            (context as DetailVideoActivity).progressBarVideo.visibility = View.GONE
        }
    }
}
