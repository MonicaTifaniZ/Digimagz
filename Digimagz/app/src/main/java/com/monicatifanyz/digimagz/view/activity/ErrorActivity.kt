package com.monicatifanyz.digimagz.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.monicatifanyz.digimagz.R
import kotlinx.android.synthetic.main.activity_error.*

class ErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        materialButtonCobaLagi.setOnClickListener {
            startActivity(Intent(it.context, MainActivity::class.java))
            finish()
        }
    }
}