package com.monicatifanyz.digimagz.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LikeModel(
    @field:SerializedName("ID_NEWS") var idNews: String, @field:SerializedName(
        "EMAIL"
    ) var email: String
) : Serializable