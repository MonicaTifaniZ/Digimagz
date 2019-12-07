package com.monicatifanyz.digimagz.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CommentModel(
    @field:SerializedName("ID_COMMENT") var idNComment: String,
    @field:SerializedName("ID_NEWS") var idNews: String,
    @field:SerializedName("EMAIL") var email: String,
    @field:SerializedName("COMMENT_TEXT") var commentText: String,
    @field:SerializedName("IS_APPROVED") var isApproved: String,
    @field:SerializedName("DATE_COMMENT") var dateComment: String,
    @field:SerializedName("USER_NAME") var userName: String,
    @field:SerializedName("PROFILEPIC_URL") var profilpicUrl: String
) : Serializable