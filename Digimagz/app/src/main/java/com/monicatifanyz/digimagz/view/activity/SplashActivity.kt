package com.monicatifanyz.digimagz.view.activity

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.monicatifanyz.digimagz.Constant
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.api.InitRetrofit
import com.monicatifanyz.digimagz.api.NotifApi
import com.monicatifanyz.digimagz.model.NotifValue
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import com.monicatifanyz.digimagz.view.activity.MainActivity as MainActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var initRetrofit:InitRetrofit
    private var list:ArrayList<Int> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initRetrofit = InitRetrofit()
        initRetrofit.getStatusCodeFromServer()
        
        if(intent.extras!=null){
            var id:Int = intent.getIntExtra("id", 0)
            var notificationManager:NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(id)
            
        }
        
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(object :OnSuccessListener<InstanceIdResult>{
            override fun onSuccess(p0: InstanceIdResult?) {
                var retrofit:Retrofit = Retrofit.Builder()
                    .baseUrl(Constant().URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()



            }

        })


        initRetrofit.setOnRetrofitSuccess(object:InitRetrofit.OnRetrofitSuccess)
        override fun

    }
}
