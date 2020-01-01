package com.monicatifanyz.digimagz.model

import com.google.gson.annotations.SerializedName

class ShareModel(
    @field:SerializedName("id_news") var idNews: String,
    @field:SerializedName("email") var email: String
)