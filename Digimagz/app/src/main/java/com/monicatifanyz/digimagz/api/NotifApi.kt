package com.monicatifanyz.digimagz.api

import com.monicatifanyz.digimagz.model.NotifValue
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface NotifApi {

    @FormUrlEncoded
    @POST("/firebase_notif/register")
    fun sendToken(@Field("token") token: String?): Call<NotifValue?>?

}