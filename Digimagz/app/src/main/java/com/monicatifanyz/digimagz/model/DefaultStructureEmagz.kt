package com.monicatifanyz.digimagz.model

import com.google.gson.annotations.SerializedName
import java.util.*

class DefaultStructureEmagz(
    @field:SerializedName("status") var isStatus: Boolean,
    @field:SerializedName("data") var data: ArrayList<EmagzModel>
)