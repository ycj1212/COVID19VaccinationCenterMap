package com.ycj1212.covid19vaccinationcentermap.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class CenterResponse(
    @SerializedName("currentCount")
    val currentCount: Int,
    @SerializedName("data")
    val data: List<Center>,
    @SerializedName("matchCount")
    val matchCount: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("perPage")
    val perPage: Int,
    @SerializedName("totalCount")
    val totalCount: Int
)

@Entity
data class Center(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("address")
    val address: String,
    @SerializedName("centerName")
    val centerName: String,
    @SerializedName("centerType")
    val centerType: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("facilityName")
    val facilityName: String,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lng")
    val lng: String,
    @SerializedName("org")
    val org: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("sido")
    val sido: String,
    @SerializedName("sigungu")
    val sigungu: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("zipCode")
    val zipCode: String
)
