package com.monicatifanyz.digimagz.view.activity

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.iid.FirebaseInstanceId
import com.monicatifanyz.digimagz.Constant
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.api.ApiClient
import com.monicatifanyz.digimagz.api.ApiInterface
import com.monicatifanyz.digimagz.api.NotifApi
import com.monicatifanyz.digimagz.model.DefaultStructureVideo
import com.monicatifanyz.digimagz.model.NotifValue
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SplashActivity : AppCompatActivity() {

    private var apiInterface: ApiInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        apiInterface = ApiClient().getRetrofit()?.create(ApiInterface::class.java)

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

        val PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )

        if (!hasPermissions(*PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 0)
        } else {
            goToMain()
        }
    }

    private fun hasPermissions(vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "Izin diperlukan untuk menggunakan aplikasi",
                        Toast.LENGTH_SHORT
                    ).show()
                    //finish();
                } else {
                    goToMain()
                }
            }
        }
    }

    private fun goToMain() {
        val handler = Handler()
        handler.postDelayed({
            apiInterface?.getVideo()?.enqueue(object : Callback<DefaultStructureVideo> {
                override fun onFailure(call: Call<DefaultStructureVideo>, t: Throwable) {
                    startActivity(
                        Intent(
                            applicationContext,
                            ErrorActivity::class.java
                        )
                    )
                    finish()
                }

                override fun onResponse(
                    call: Call<DefaultStructureVideo>,
                    response: Response<DefaultStructureVideo>
                ) {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }

            })
        }, 5000)
    }
}
