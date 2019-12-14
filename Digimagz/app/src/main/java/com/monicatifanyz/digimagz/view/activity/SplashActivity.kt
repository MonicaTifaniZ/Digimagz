package com.monicatifanyz.digimagz.view.activity

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.monicatifanyz.digimagz.Constant
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.api.InitRetrofit
import com.monicatifanyz.digimagz.api.NotifApi
import com.monicatifanyz.digimagz.model.NotifValue
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SplashActivity : AppCompatActivity() {

    private lateinit var initRetrofit:InitRetrofit
    private var list:ArrayList<Int> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initRetrofit = InitRetrofit()
        initRetrofit.getStatusCodeFromServer()
        
        if(intent.extras!=null){
            val id:Int = intent.getIntExtra("id", 0)
            val notificationManager:NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(id)
            
        }

        FirebaseInstanceId.getInstance().instanceId
            .addOnSuccessListener { instanceIdResult ->
                val retrofit: Retrofit = Retrofit.Builder().baseUrl(Constant().URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val notifApi = retrofit.create(NotifApi::class.java)
                val call = notifApi.sendToken(instanceIdResult.token)
                call!!.enqueue(object : Callback<NotifValue?> {
                    override fun onFailure(call: Call<NotifValue?>, t: Throwable) {
                        Log.e("ERROR", "Network error!")
                    }

                    override fun onResponse(
                        call: Call<NotifValue?>,
                        response: Response<NotifValue?>
                    ) {
                        val value: String = response.body()!!.value.toString()
                        val message: String = response.body()!!.message.toString()
                        Log.d("val", value)
                        Log.d("mes", message)
                    }

                })
            }
        initRetrofit.setOnRetrofitSuccess(object :InitRetrofit.OnRetrofitSuccess{
            override fun onSuccessGetData(arrayList: java.util.ArrayList<*>?) {
                if (arrayList != null) {
                    list = arrayList.clone() as ArrayList<Int>
                }
            }
        })

        val handler = Handler()
        handler.postDelayed({
            if (list.isNotEmpty()) {
                if (list[0] != 0) {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(applicationContext, ErrorActivity::class.java))
                    finish()
                }
            } else {
                startActivity(Intent(applicationContext, ErrorActivity::class.java))
                finish()
            }
        }, 2500)
    }
}
