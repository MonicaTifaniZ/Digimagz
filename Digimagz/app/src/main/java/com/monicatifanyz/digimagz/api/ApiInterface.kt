package com.monicatifanyz.digimagz.api

import com.google.gson.JsonObject
import com.monicatifanyz.digimagz.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @GET("dummy/index_get")
    fun getNews(): Call<DefaultStructureNews>

    @GET("dummy/index_get")
    fun getNewsSearch(@Query("q") params: String): Call<DefaultStructureNews>

    @GET("dummy/index_get")
    fun getNewsTrending(@Query("trend") params: String): Call<DefaultStructureNews>

    @GET("related/index_get")
    fun getNewsRelated(@Query("id") idNews: String): Call<DefaultStructureNews>

    @GET("slider/index_get")
    fun getSlider(): Call<DefaultStructureNews>

    @GET("story/index_get")
    fun getStory(): Call<DefaultStructureStory>

    @GET("emagz/index_get")
    fun getEmagz(): Call<DefaultStructureEmagz>

    @GET("newscover/index_get")
    fun getNewsCoverStory(@Query("id") id: String): Call<DefaultStructureNewsCoverStory>

    @GET("comments/index_get")
    fun getComment(@Query("id_news") idNews: String): Call<DefaultStructureComment>

    @GET("user/index_get")
    fun getUser(@Query("email") email: String): Call<DefaultStructureUser>

    @GET("likes/index_get")
    fun getLikes(
        @Query("id_news") idNews: String, @Query(
            "email"
        ) email: String
    ): Call<DefaultStructureLike>

    @GET("video/index_get")
    fun getVideo(): Call<DefaultStructureVideo>

    @POST("comments/index_post")
    fun postComment(@Body jsonObject: JsonObject): Call<CommentModel>

    @POST("likes/index_post")
    fun postLike(@Body jsonObject: JsonObject): Call<LikeModel>

    @POST("user/index_post")
    fun postUser(@Body jsonObject: JsonObject): Call<UserModel>

    @POST("dummy/click")
    fun postView(@Body jsonObject: JsonObject): Call<ViewModel>

    @POST("dummy/share")
    fun postShare(@Body jsonObject: JsonObject): Call<ViewModel>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "likes/index_delete", hasBody = true)
    fun deleteLike(
        @Field("id_news") idNews: String, @Field(
            "email"
        ) email: String
    ): Call<LikeModel>

    @FormUrlEncoded
    @HTTP(method = "PUT", path = "user/index_put", hasBody = true)
    fun putUserName(
        @Field("email") email: String, @Field(
            "name"
        ) name: String
    ): Call<DefaultStructureUser>

    @FormUrlEncoded
    @HTTP(method = "PUT", path = "user/index_put", hasBody = true)
    fun putUser(
        @Field("email") email: String, @Field("name") name: String, @Field(
            "pic_url"
        ) pic_url: String, @Field("date_birth") date_birth: String, @Field(
            "gender"
        ) gender: String
    ): Call<DefaultStructureUser>

    @FormUrlEncoded
    @HTTP(method = "PUT", path = "user/index_put", hasBody = true)
    fun putUserPhoto(
        @Field("email") email: String, @Field(
            "pic_url"
        ) pic_url: String
    ): Call<DefaultStructureUser>

    @Multipart
    @POST("user/avatar")
    fun postAvatar(@Part("email") email: RequestBody, @Part picture: MultipartBody.Part): Call<AvatarModel>

}