package com.monicatifanyz.digimagz.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class NewsModel(
    @field:SerializedName("ID_NEWS") var idNews: String,
    @field:SerializedName("NAME_CATEGORY") var nameCategory: String,
    @field:SerializedName("TITLE_NEWS") var titleNews: String,
    @field:SerializedName("CONTENT_NEWS") var contentNews: String,
    @field:SerializedName("VIEWS_COUNT") var viewsCount: Int,
    @field:SerializedName("SHARES_COUNT") var sharesCount: Int,
    @field:SerializedName("DATE_NEWS") var dateNews: String,
    @field:SerializedName("NEWS_IMAGE") var newsImage: String,
    @field:SerializedName("LIKES") var likes: Int,
    @field:SerializedName("COMMENTS") var comments: Int,
    @field:SerializedName("EDITOR") var editor: String,
    @field:SerializedName("VERIFICATOR") var verificator: String,
    @field:SerializedName("STATUS") var status: String,
    var checkLike: Int
) : Serializable