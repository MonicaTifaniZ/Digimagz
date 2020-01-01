package com.monicatifanyz.digimagz.model

import com.google.gson.annotations.SerializedName

class AvatarModel(
    @field:SerializedName("status") var status: String,
    @field:SerializedName("message") var message: String
)