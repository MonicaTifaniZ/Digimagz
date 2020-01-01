package com.monicatifanyz.digimagz.model

import com.google.gson.annotations.SerializedName

class EmagzModel(
    @field:SerializedName("ID_EMAGZ") var idEmagz: String,
    @field:SerializedName("THUMBNAIL") var thumbnail: String,
    @field:SerializedName("FILE") var file: String,
    @field:SerializedName("DATE_UPLOADED") var dateUploaded: String,
    @field:SerializedName("NAME") var name: String
)