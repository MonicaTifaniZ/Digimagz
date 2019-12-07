package com.monicatifanyz.digimagz.model

import com.google.gson.annotations.SerializedName

class DefaultStructureLike(
    @field:SerializedName("status") var isStatus: Boolean, @field:SerializedName(
        "data"
    ) var data: String
)