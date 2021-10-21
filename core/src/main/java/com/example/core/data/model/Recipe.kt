package com.example.core.data.model

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("id")
    var id: String,
    @SerializedName("calories")
    val calories: String,
    @SerializedName("carbos")
    val carbos: String,
    @SerializedName("proteins")
    val proteins: String,
    @SerializedName("fats")
    val fats: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("headline")
    val headline: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("thumb")
    val thumbUrl: String,
    @SerializedName("image")
    val fullImageUrl: String,
    @SerializedName("difficulty")
    val difficulty: Int,
    @SerializedName("time")
    val time: String
)