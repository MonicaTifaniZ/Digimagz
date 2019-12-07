package com.monicatifanyz.digimagz.view.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.api.InitRetrofit
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var initRetrofit: InitRetrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRetrofit = InitRetrofit()
        initRetrofit.getNewsFromApi()
        initRetrofit.setOnRetrofitSuccess(object : InitRetrofit.OnRetrofitSuccess {
            override fun onSuccessGetData(arrayList: ArrayList<*>?) {
                if (arrayList != null) {
                    if (!arrayList.isEmpty()) {
                        Log.i("Size", arrayList.size.toString())
                        //showRecyclerListViewNews(arrayList)
                    } else {
                        Log.i("Size", arrayList.size.toString())
                    }
                }
            }
        })
    }
}
