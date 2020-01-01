package com.monicatifanyz.digimagz.api

import android.util.Log
import com.google.gson.JsonObject
import com.monicatifanyz.digimagz.model.*
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

    init {
        this.retrofit = ApiClient().getRetrofit()
        this.apiInterface = retrofit!!.create(ApiInterface::class.java)
    }

    fun getNewsFromApi() {
        apiInterface.getNews().enqueue(object : Callback<DefaultStructureNews> {
            override fun onResponse(
                call: Call<DefaultStructureNews>,
                response: Response<DefaultStructureNews>
            ) {
                val list = ArrayList<NewsModel>()
                if (response.code() == 200) {
                    assert(response.body() != null)
                    response.body()?.data?.let { list.addAll(it) }
                    Log.e("getNewsFromApi", list.size.toString())
                }
                onRetrofitSuccess.onSuccessGetData(list)
            }

            override fun onFailure(
                call: Call<DefaultStructureNews>,
                t: Throwable
            ) {
                Log.e(
                    "getNewsFromApi",
                    Objects.requireNonNull(t.message)
                )
            }
        })
    }

    fun getNewsFromApiWithParams(params: String) {
        apiInterface.getNewsSearch(params!!)
            .enqueue(object : Callback<DefaultStructureNews> {
                override fun onResponse(
                    call: Call<DefaultStructureNews>,
                    response: Response<DefaultStructureNews>
                ) {
                    val list = ArrayList<NewsModel>()
                    if (response.code() == 200) {
                        assert(response.body() != null)
                        response.body()?.data?.let { list.addAll(it) }
                        Log.e("getNewsWithParams", list.size.toString())
                    }
                    onRetrofitSuccess.onSuccessGetData(list)
                }

                override fun onFailure(
                    call: Call<DefaultStructureNews>,
                    t: Throwable
                ) {
                    Log.e(
                        "getNewsFromApi",
                        Objects.requireNonNull(t.message)
                    )
                }
            })
    }

    fun getNewsTrendingFromApi() {
        apiInterface.getNewsTrending("yes")
            .enqueue(object : Callback<DefaultStructureNews> {
                override fun onResponse(
                    call: Call<DefaultStructureNews>,
                    response: Response<DefaultStructureNews>
                ) {
                    val list = ArrayList<NewsModel>()
                    if (response.code() == 200) {
                        assert(response.body() != null)
                        response.body()?.data?.let { list.addAll(it) }
                        Log.e("getNewsTrendingFromApi", list.size.toString())
                    }
                    onRetrofitSuccess.onSuccessGetData(list)
                }

                override fun onFailure(
                    call: Call<DefaultStructureNews>,
                    t: Throwable
                ) {
                    Log.e(
                        "getNewsTrendingFromApi",
                        Objects.requireNonNull(t.message)
                    )
                }
            })
    }

    fun getNewsRelatedFromApi(idNews: String) {
        apiInterface.getNewsRelated(idNews!!)
            .enqueue(object : Callback<DefaultStructureNews> {
                override fun onResponse(
                    call: Call<DefaultStructureNews>,
                    response: Response<DefaultStructureNews>
                ) {
                    val list = ArrayList<NewsModel>()
                    if (response.code() == 200) {
                        assert(response.body() != null)
                        response.body()?.data?.let { list.addAll(it) }
                        Log.e("getNewsRelatedFromApi", list.size.toString())
                    }
                    onRetrofitSuccess.onSuccessGetData(list)
                }

                override fun onFailure(
                    call: Call<DefaultStructureNews>,
                    t: Throwable
                ) {
                }
            })
    }

    fun getCommentFromApi(idNews: String) {
        apiInterface.getComment(idNews!!)
            .enqueue(object : Callback<DefaultStructureComment> {
                override fun onResponse(
                    call: Call<DefaultStructureComment>,
                    response: Response<DefaultStructureComment>
                ) {
                    val list =
                        ArrayList<CommentModel>()
                    if (response.code() == 200) {
                        assert(response.body() != null)
                        response.body()?.data?.let { list.addAll(it) }
                        Log.e("getCommentFromApi", list.size.toString())
                    }
                    onRetrofitSuccess.onSuccessGetData(list)
                }

                override fun onFailure(
                    call: Call<DefaultStructureComment>,
                    t: Throwable
                ) {
                    Log.e(
                        "getCommentFromApi",
                        Objects.requireNonNull(t.message)
                    )
                }
            })
    }

    fun postCommentToApi(
        idNews: String,
        email: String,
        comment: String
    ) {
        val `object` = JsonObject()
        `object`.addProperty("id_news", idNews)
        `object`.addProperty("email", email)
        `object`.addProperty("comments", comment)
        apiInterface.postComment(`object`).enqueue(object : Callback<CommentModel> {
            override fun onResponse(
                call: Call<CommentModel>,
                response: Response<CommentModel>
            ) {
                Log.e("postCommentToApi", "Success")
            }

            override fun onFailure(
                call: Call<CommentModel>,
                t: Throwable
            ) {
                Log.e(
                    "postCommentToApi",
                    Objects.requireNonNull(t.message)
                )
            }
        })
    }

    fun postLikeToApi(idNews: String, email: String) {
        val `object` = JsonObject()
        `object`.addProperty("id_news", idNews)
        `object`.addProperty("email", email)
        apiInterface.postLike(`object`).enqueue(object : Callback<LikeModel> {
            override fun onResponse(
                call: Call<LikeModel>,
                response: Response<LikeModel>
            ) {
                Log.e("postLikeToApi", "Success")
            }

            override fun onFailure(call: Call<LikeModel>, t: Throwable) {
                Log.e(
                    "postLikeToApi",
                    Objects.requireNonNull(t.message)
                )
            }
        })
    }

    fun deleteLikeFromApi(idNews: String, email: String) {
        apiInterface.deleteLike(idNews!!, email!!).enqueue(object : Callback<LikeModel> {
            override fun onResponse(
                call: Call<LikeModel>,
                response: Response<LikeModel>
            ) {
                Log.e("deleteLikeFromApi", "Success")
            }

            override fun onFailure(call: Call<LikeModel>, t: Throwable) {
                Log.e(
                    "deleteLikeFromApi",
                    Objects.requireNonNull(t.message)
                )
            }
        })
    }

    fun getStoryFromApi() {
        apiInterface.getStory().enqueue(object : Callback<DefaultStructureStory> {
            override fun onResponse(
                call: Call<DefaultStructureStory>,
                response: Response<DefaultStructureStory>
            ) {
                val list = ArrayList<StoryModel>()
                if (response.code() == 200) {
                    assert(response.body() != null)
                    response.body()?.data?.let { list.addAll(it) }
                    Log.e("getStoryFromApi", list.size.toString())
                }
                onRetrofitSuccess.onSuccessGetData(list)
            }

            override fun onFailure(
                call: Call<DefaultStructureStory>,
                t: Throwable
            ) {
                Log.e(
                    "getStoryFromApi",
                    Objects.requireNonNull(t.message)
                )
            }
        })
    }

    fun getNewsCoverStoryFromApi(id: String) {
        apiInterface.getNewsCoverStory(id!!)
            .enqueue(object : Callback<DefaultStructureNewsCoverStory> {
                override fun onResponse(
                    call: Call<DefaultStructureNewsCoverStory>,
                    response: Response<DefaultStructureNewsCoverStory>
                ) {
                    val list =
                        ArrayList<NewsCoverStoryModel>()
                    if (response.code() == 200) {
                        assert(response.body() != null)
                        response.body()?.data?.let { list.addAll(it) }
                        Log.e("getNewsCoverStory", list.size.toString())
                    }
                    onRetrofitSuccess.onSuccessGetData(list)
                }

                override fun onFailure(
                    call: Call<DefaultStructureNewsCoverStory>,
                    t: Throwable
                ) {
                }
            })
    }

    fun getSliderFromApi() {
        apiInterface.getSlider().enqueue(object : Callback<DefaultStructureNews> {
            override fun onResponse(
                call: Call<DefaultStructureNews>,
                response: Response<DefaultStructureNews>
            ) {
                val list = ArrayList<NewsModel>()
                if (response.code() == 200) {
                    assert(response.body() != null)
                    response.body()?.data?.let { list.addAll(it) }
                    Log.e("getSliderFromApi", list.size.toString())
                }
                onRetrofitSuccess.onSuccessGetData(list)
            }

            override fun onFailure(
                call: Call<DefaultStructureNews>,
                t: Throwable
            ) {
                Log.e(
                    "getSliderFromApi",
                    Objects.requireNonNull(t.message)
                )
            }
        })
    }

    fun postUserToApi(
        email: String,
        name: String,
        urlPic: String
    ) {
        val `object` = JsonObject()
        `object`.addProperty("email", email)
        `object`.addProperty("name", name)
        `object`.addProperty("pic_url", urlPic)
        apiInterface.postUser(`object`).enqueue(object : Callback<UserModel> {
            override fun onResponse(
                call: Call<UserModel>,
                response: Response<UserModel>
            ) {
                Log.e("postUserToApi", "Success")
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Log.e(
                    "postUserToApi",
                    Objects.requireNonNull(t.message)
                )
            }
        })
    }

    fun putUserToApi(
        email: String,
        name: String,
        urlPic: String,
        dateBirth: String,
        gender: String
    ) {
        apiInterface.putUser(email!!, name!!, urlPic!!, dateBirth!!, gender!!)
            .enqueue(object : Callback<DefaultStructureUser> {
                override fun onResponse(
                    call: Call<DefaultStructureUser>,
                    response: Response<DefaultStructureUser>
                ) {
                    Log.e("putUserToApi", "Success")
                }

                override fun onFailure(
                    call: Call<DefaultStructureUser>,
                    t: Throwable
                ) {
                    Log.e(
                        "putUserToApi",
                        Objects.requireNonNull(t.message)
                    )
                }
            })
    }

    fun getLikeFromApi(idNews: String, email: String) {
        apiInterface.getLikes(idNews!!, email!!)
            .enqueue(object : Callback<DefaultStructureLike> {
                override fun onResponse(
                    call: Call<DefaultStructureLike>,
                    response: Response<DefaultStructureLike>
                ) {
                    val list =
                        ArrayList<String>()
                    if (response.code() == 200) {
                        assert(response.body() != null)
                        response.body()?.data?.let { list.add(it) }
                    }
                    onRetrofitSuccess.onSuccessGetData(list)
                }

                override fun onFailure(
                    call: Call<DefaultStructureLike>,
                    t: Throwable
                ) { //  Log.e("getLikeFromApi", t.getMessage());
                }
            })
    }

    fun getStatusCodeFromServer() {
        apiInterface.getNews().enqueue(object : Callback<DefaultStructureNews> {
            override fun onResponse(
                call: Call<DefaultStructureNews>,
                response: Response<DefaultStructureNews>
            ) {
                val list = ArrayList<Int>()
                list.add(response.code())
                onRetrofitSuccess.onSuccessGetData(list)
            }

            override fun onFailure(
                call: Call<DefaultStructureNews>,
                t: Throwable
            ) {
                val list = ArrayList<Int>()
                list.add(0)
                onRetrofitSuccess.onSuccessGetData(list)
                Log.e(
                    "serverDigi",
                    Objects.requireNonNull(t.message)
                )
                Log.e(
                    "serverDigi",
                    Objects.requireNonNull(t.javaClass.name)
                )
            }
        })
    }

    fun getVideoFromApi() {
        apiInterface.getVideo().enqueue(object : Callback<DefaultStructureVideo> {
            override fun onResponse(
                call: Call<DefaultStructureVideo>,
                response: Response<DefaultStructureVideo>
            ) {
                val list = ArrayList<VideoModel>()
                if (response.code() == 200) {
                    assert(response.body() != null)
                    response.body()?.data?.let { list.addAll(it) }
                    Log.e("getVideoFromApi", list.size.toString())
                }
                onRetrofitSuccess.onSuccessGetData(list)
            }

            override fun onFailure(
                call: Call<DefaultStructureVideo>,
                t: Throwable
            ) {
                Log.e(
                    "getVideoFromApi",
                    Objects.requireNonNull(t.message)
                )
            }
        })
    }

    fun postViewToApi(idNews: String, email: String) {
        val `object` = JsonObject()
        `object`.addProperty("id_news", idNews)
        `object`.addProperty("email", email)
        apiInterface.postView(`object`).enqueue(object : Callback<ViewModel> {
            override fun onResponse(
                call: Call<ViewModel>,
                response: Response<ViewModel>
            ) {
                Log.e("postViewToApi", "Sukses")
            }

            override fun onFailure(call: Call<ViewModel>, t: Throwable) {
                Log.e(
                    "postViewToApi",
                    Objects.requireNonNull(t.message)
                )
            }
        })
    }

    fun postShareToApi(idNews: String, email: String) {
        val `object` = JsonObject()
        `object`.addProperty("id_news", idNews)
        `object`.addProperty("email", email)
        apiInterface.postShare(`object`).enqueue(object : Callback<ViewModel> {
            override fun onResponse(
                call: Call<ViewModel>,
                response: Response<ViewModel>
            ) {
                Log.e("postShareToApi", "Sukses")
            }

            override fun onFailure(call: Call<ViewModel>, t: Throwable) {
                Log.e(
                    "postShareToApi",
                    Objects.requireNonNull(t.message)
                )
            }
        })
    }

    fun getEmagzFromApi() {
        apiInterface.getEmagz().enqueue(object : Callback<DefaultStructureEmagz> {
            override fun onResponse(
                call: Call<DefaultStructureEmagz>,
                response: Response<DefaultStructureEmagz>
            ) {
                val list = ArrayList<EmagzModel>()
                if (response.code() == 200) {
                    assert(response.body() != null)
                    response.body()?.data?.let { list.addAll(it) }
                    Log.e("getEmagzFromApi", list.size.toString())
                }
                onRetrofitSuccess.onSuccessGetData(list)
            }

            override fun onFailure(
                call: Call<DefaultStructureEmagz>,
                t: Throwable
            ) {
                Log.e(
                    "getEmagzFromApi",
                    Objects.requireNonNull(t.message)
                )
            }
        })
    }

    fun getUserFromApi(email: String) {
        apiInterface.getUser(email!!).enqueue(object : Callback<DefaultStructureUser> {
            override fun onResponse(
                call: Call<DefaultStructureUser>,
                response: Response<DefaultStructureUser>
            ) {
                val list = ArrayList<UserModel>()
                if (response.code() == 200) {
                    assert(response.body() != null)
                    response.body()?.data?.let { list.addAll(it) }
                    Log.e("getUserFromApi", list.size.toString())
                }
                onRetrofitSuccess.onSuccessGetData(list)
            }

            override fun onFailure(
                call: Call<DefaultStructureUser>,
                t: Throwable
            ) {
                Log.e(
                    "getUserFromApi",
                    Objects.requireNonNull(t.message)
                )
            }
        })
    }

    //Interface
    interface OnRetrofitSuccess {
        fun onSuccessGetData(arrayList: ArrayList<*>?)
    }

    fun setOnRetrofitSuccess(onRetrofitSuccess: OnRetrofitSuccess) {
        this.onRetrofitSuccess = onRetrofitSuccess
    }
}