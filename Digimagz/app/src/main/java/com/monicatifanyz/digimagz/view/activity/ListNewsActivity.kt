package com.monicatifanyz.digimagz.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.monicatifanyz.digimagz.R

class ListNewsActivity : AppCompatActivity() {

    private val recyclerView : RecyclerView? = null
    private val frameLayoutEmpty : FrameLayout? = null
    private lateinit var shimmerFrameLayout: FrameLayout
    private var params : String = ""
    private val materialToolbar : MaterialToolbar? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_news)

        params = intent.getStringExtra("params")

    }
}
