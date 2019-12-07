package com.monicatifanyz.digimagz.model

import com.google.gson.annotations.SerializedName

class VideoModel(
    @field:SerializedName("ID_VIDEO") var idVideo: String,
    @field:SerializedName("TITLE") var title: String,
    @field:SerializedName("DESCRIPTION") var description: String,
    @field:SerializedName("DATE_PUBLISHED") var datePublished: String,
    @field:SerializedName("URL_DEFAULT_THUMBNAIL") var urlDefaultThumbnail: String,
    @field:SerializedName("URL_MEDIUM_THUMBNAIL") var urlMediumThumbnail: String,
    @field:SerializedName("URL_HIGH_THUMBNAIL") var urlLargeThumbnail: String,
    @field:SerializedName("STATUS_PUBLISHED") var statusPublished: String
)