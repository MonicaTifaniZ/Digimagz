package com.monicatifanyz.digimagz.model

import com.google.gson.annotations.SerializedName

class UserModel(
    @field:SerializedName("EMAIL") var email: String,
    @field:SerializedName("USER_NAME") var userName: String,
    @field:SerializedName("PROFILEPIC_URL") var urlPic: String,
    @field:SerializedName("LAST_LOGIN") var lastLogin: String,
    @field:SerializedName("DATE_BIRTH") var dateBirth: String,
    @field:SerializedName("GENDER") var gender: String,
    @field:SerializedName("USER_TYPE") var userType: String,
    @field:SerializedName("PASSWORD") var password: String
)