package com.monicatifanyz.digimagz.api

import android.util.Log
import com.google.gson.JsonObject
import com.monicatifanyz.digimagz.model.DefaultStructureNews
import com.monicatifanyz.digimagz.model.NewsModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*

class InitRetrofit {

    //API
    private var retrofit: Retrofit?
    private var apiInterface: ApiInterface

    //Interface
    private lateinit var onRetrofitSuccess: OnRetrofitSuccess

    constructor() {
        this.retrofit = ApiClient().getRetrofit()
        this.apiInterface = retrofit!!.create(ApiInterface::class.java)
    }

    fun getNewsFromApi() {
        apiInterface.getNews().enqueue(object :Callback<DefaultStructureNews> {
            override fun onFailure(call: Call<DefaultStructureNews>, t: Throwable) {
                Log.e("getNewsFromApi", t.message)
            }

            override fun onResponse(
                call: Call<DefaultStructureNews>,
                response: Response<DefaultStructureNews>
            ) {
                val list: ArrayList<NewsModel> = ArrayList<NewsModel>()
                if (response.code() == 200) {
                    response.body()?.data?.let { list.addAll(it) }
                    Log.e("getNewsFromApi", list.size.toString())
                }
                onRetrofitSuccess.onSuccessGetData(list)
            }

        })
    }

//    fun getNewsFromApiWithParams(params: String?) {
//        apiInterface!!.getNewsSearch(params)
//            .enqueue(object : Callback<DefaultStructureNews> {
//                override fun onResponse(
//                    call: Call<DefaultStructureNews>,
//                    response: Response<DefaultStructureNews>
//                ) {
//                    val list: ArrayList<NewsModel> = ArrayList<NewsModel>()
//                    if (response.code() == 200) {
//                        list.addAll(response.body().getData())
//                        Log.e("getNewsWithParams", "Empty List")
//                    }
//                    onRetrofitSuccess!!.onSuccessGetData(list)
//                }
//
//                override fun onFailure(
//                    call: Call<DefaultStructureNews>,
//                    t: Throwable
//                ) {
//                    Log.e("getNewsFromApi", t.message)
//                }
//            })
//    }
//
//    fun getNewsTrendingFromApi() {
//        apiInterface!!.getNewsTrending("yes")
//            .enqueue(object : Callback<DefaultStructureNews> {
//                override fun onResponse(
//                    call: Call<DefaultStructureNews>,
//                    response: Response<DefaultStructureNews>
//                ) {
//                    val list: ArrayList<NewsModel> = ArrayList<NewsModel>()
//                    if (response.code() == 200) {
//                        list.addAll(response.body().getData())
//                        Log.e("getNewsTrendingFromApi", "Empty List")
//                    }
//                    onRetrofitSuccess!!.onSuccessGetData(list)
//                }
//
//                override fun onFailure(
//                    call: Call<DefaultStructureNews>,
//                    t: Throwable
//                ) {
//                    Log.e("getNewsTrendingFromApi", t.message)
//                }
//            })
//    }
//
//    fun getNewsRelatedFromApi(idNews: String?) {
//        apiInterface!!.getNewsRelated(idNews)
//            .enqueue(object : Callback<DefaultStructureNews> {
//                override fun onResponse(
//                    call: Call<DefaultStructureNews>,
//                    response: Response<DefaultStructureNews>
//                ) {
//                    val list: ArrayList<NewsModel> = ArrayList<NewsModel>()
//                    if (response.code() == 200) {
//                        list.addAll(response.body().getData())
//                        Log.e("getNewsRelatedFromApi", "Empty List")
//                    }
//                    onRetrofitSuccess!!.onSuccessGetData(list)
//                }
//
//                override fun onFailure(
//                    call: Call<DefaultStructureNews>,
//                    t: Throwable
//                ) {
//                }
//            })
//    }
//
//    fun getCommentFromApi(idNews: String?) {
//        apiInterface!!.getComment(idNews)
//            .enqueue(object : Callback<DefaultStructureComment> {
//                override fun onResponse(
//                    call: Call<DefaultStructureComment>,
//                    response: Response<DefaultStructureComment>
//                ) {
//                    val list: ArrayList<CommentModel> =
//                        ArrayList<CommentModel>()
//                    if (response.code() == 200) {
//                        list.addAll(response.body().getData())
//                        Log.e("getCommentFromApi", "Empty List")
//                    }
//                    onRetrofitSuccess!!.onSuccessGetData(list)
//                }
//
//                override fun onFailure(
//                    call: Call<DefaultStructureComment>,
//                    t: Throwable
//                ) {
//                    Log.e("getCommentFromApi", t.message)
//                }
//            })
//    }
//
//    fun postCommentToApi(
//        idNews: String?,
//        email: String?,
//        comment: String?
//    ) {
//        val `object` = JsonObject()
//        `object`.addProperty("id_news", idNews)
//        `object`.addProperty("email", email)
//        `object`.addProperty("comments", comment)
//        apiInterface!!.postComment(`object`)!!.enqueue(object : Callback<CommentModel?> {
//            override fun onResponse(
//                call: Call<CommentModel?>,
//                response: Response<CommentModel?>
//            ) {
//                Log.e("postCommentToApi", "Success")
//            }
//
//            override fun onFailure(
//                call: Call<CommentModel?>,
//                t: Throwable
//            ) {
//                Log.e("postCommentToApi", t.message)
//            }
//        })
//    }
//
//    fun postLikeToApi(idNews: String?, email: String?) {
//        val `object` = JsonObject()
//        `object`.addProperty("id_news", idNews)
//        `object`.addProperty("email", email)
//        apiInterface!!.postLike(`object`)!!.enqueue(object : Callback<LikeModel?> {
//            override fun onResponse(
//                call: Call<LikeModel?>,
//                response: Response<LikeModel?>
//            ) {
//                Log.e("postLikeToApi", "Success")
//            }
//
//            override fun onFailure(call: Call<LikeModel?>, t: Throwable) {
//                Log.e("postLikeToApi", t.message)
//            }
//        })
//    }
//
//    fun deleteLikeFromApi(idNews: String?, email: String?) {
//        apiInterface!!.deleteLike(idNews, email)!!.enqueue(object : Callback<LikeModel?> {
//            override fun onResponse(
//                call: Call<LikeModel?>,
//                response: Response<LikeModel?>
//            ) {
//                Log.e("deleteLikeFromApi", "Success")
//            }
//
//            override fun onFailure(call: Call<LikeModel?>, t: Throwable) {
//                Log.e("deleteLikeFromApi", t.message)
//            }
//        })
//    }
//
//    fun getStoryFromApi() {
//        apiInterface!!.getStory()!!.enqueue(object : Callback<DefaultStructureStory> {
//            override fun onResponse(
//                call: Call<DefaultStructureStory>,
//                response: Response<DefaultStructureStory>
//            ) {
//                val list: ArrayList<StoryModel> = ArrayList<StoryModel>()
//                if (response.code() == 200) {
//                    list.addAll(response.body().getData())
//                }
//                onRetrofitSuccess!!.onSuccessGetData(list)
//            }
//
//            override fun onFailure(
//                call: Call<DefaultStructureStory>,
//                t: Throwable
//            ) {
//                Log.e("getStoryFromApi", t.message)
//            }
//        })
//    }
//
//    fun getNewsCoverStoryFromApi(id: String?) {
//        apiInterface!!.getNewsCoverStory(id)
//            .enqueue(object : Callback<DefaultStructureNewsCoverStory> {
//                override fun onResponse(
//                    call: Call<DefaultStructureNewsCoverStory>,
//                    response: Response<DefaultStructureNewsCoverStory>
//                ) {
//                    val list: ArrayList<NewsCoverStoryModel> =
//                        ArrayList<NewsCoverStoryModel>()
//                    if (response.code() == 200) {
//                        list.addAll(response.body().getData())
//                    }
//                    onRetrofitSuccess!!.onSuccessGetData(list)
//                }
//
//                override fun onFailure(
//                    call: Call<DefaultStructureNewsCoverStory>,
//                    t: Throwable
//                ) {
//                }
//            })
//    }
//
//    fun getSliderFromApi() {
//        apiInterface!!.getSlider()!!.enqueue(object : Callback<DefaultStructureNews> {
//            override fun onResponse(
//                call: Call<DefaultStructureNews>,
//                response: Response<DefaultStructureNews>
//            ) {
//                val list: ArrayList<NewsModel> = ArrayList<NewsModel>()
//                if (response.code() == 200) {
//                    list.addAll(response.body().getData())
//                    onRetrofitSuccess!!.onSuccessGetData(list)
//                }
//            }
//
//            override fun onFailure(
//                call: Call<DefaultStructureNews>,
//                t: Throwable
//            ) {
//                Log.e("getSliderFromApi", t.message)
//            }
//        })
//    }
//
//    fun postUserToApi(
//        email: String?,
//        name: String?,
//        urlPic: String?
//    ) {
//        val `object` = JsonObject()
//        `object`.addProperty("email", email)
//        `object`.addProperty("name", name)
//        `object`.addProperty("pic_url", urlPic)
//        apiInterface!!.postUser(`object`)!!.enqueue(object : Callback<UserModel?> {
//            override fun onResponse(
//                call: Call<UserModel?>,
//                response: Response<UserModel?>
//            ) {
//                Log.e("postUserToApi", "Success")
//            }
//
//            override fun onFailure(call: Call<UserModel?>, t: Throwable) {
//                Log.e("postUserToApi", t.message)
//            }
//        })
//    }
//
//
//    fun getLikeFromApi(idNews: String?, email: String?) {
//        apiInterface!!.getLikes(idNews, email)
//            .enqueue(object : Callback<DefaultStructureLike> {
//                override fun onResponse(
//                    call: Call<DefaultStructureLike>,
//                    response: Response<DefaultStructureLike>
//                ) {
//                    val list =
//                        ArrayList<String>()
//                    if (response.code() == 200) {
//                        list.add(response.body().getData())
//                    }
//                    onRetrofitSuccess!!.onSuccessGetData(list)
//                }
//
//                override fun onFailure(
//                    call: Call<DefaultStructureLike>,
//                    t: Throwable
//                ) {
//                    Log.e("getLikeFromApi", t.message)
//                }
//            })
//    }
//
//    fun getStatusCodeFromServer() {
//        apiInterface!!.getNews()!!.enqueue(object : Callback<DefaultStructureNews?> {
//            override fun onResponse(
//                call: Call<DefaultStructureNews?>,
//                response: Response<DefaultStructureNews?>
//            ) {
//                val list = ArrayList<Int>()
//                list.add(response.code())
//                onRetrofitSuccess!!.onSuccessGetData(list)
//            }
//
//            override fun onFailure(
//                call: Call<DefaultStructureNews?>,
//                t: Throwable
//            ) {
//                val list = ArrayList<Int>()
//                list.add(0)
//                onRetrofitSuccess!!.onSuccessGetData(list)
//                Log.e("serverDigi", t.message)
//            }
//        })
//    }
//
//    fun getVideoFromApi() {
//        apiInterface!!.getVideo()!!.enqueue(object : Callback<DefaultStructureVideo> {
//            override fun onResponse(
//                call: Call<DefaultStructureVideo>,
//                response: Response<DefaultStructureVideo>
//            ) {
//                val list: ArrayList<VideoModel> = ArrayList<VideoModel>()
//                if (response.code() == 200) {
//                    list.addAll(response.body().getData())
//                    onRetrofitSuccess!!.onSuccessGetData(list)
//                }
//            }
//
//            override fun onFailure(
//                call: Call<DefaultStructureVideo>,
//                t: Throwable
//            ) {
//                Log.e("getVideoFromApi", t.message)
//            }
//        })
//    }

    //Interface
    interface OnRetrofitSuccess {
        fun onSuccessGetData(arrayList: ArrayList<*>?)
    }

    fun setOnRetrofitSuccess(onRetrofitSuccess: OnRetrofitSuccess) {
        this.onRetrofitSuccess = onRetrofitSuccess
    }
}