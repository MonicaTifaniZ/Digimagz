package com.monicatifanyz.digimagz.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StoryModel(
    @field:SerializedName("ID_COVERSTORY") var idCoverStory: String,
    @field:SerializedName("TITLE_COVERSTORY") var titleCoverStory: String,
    @field:SerializedName("SUMMARY") var summary: String,
    @field:SerializedName("IMAGE_COVERSTORY") var imageCoverStory: String,
    @field:SerializedName("DATE_COVERSTORY") var dateCoverStory: String
) : Serializable