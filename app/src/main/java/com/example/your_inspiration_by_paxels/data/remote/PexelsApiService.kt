package com.example.your_inspiration_by_paxels.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PexelsApiService {
    @GET("curated")
    suspend fun getCuratedPhotos(
        @Header("Authorization") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): PexelsResponse

    @GET("search")
    suspend fun searchPhotos(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): PexelsResponse
}

data class PexelsResponse(
    @SerializedName("photos") val photos: List<RemotePhoto>
)

data class RemotePhoto(
    @SerializedName("id") val id: Int,
    @SerializedName("src") val src: PhotoSrc,
    @SerializedName("photographer") val photographer: String,
    @SerializedName("alt") val alt: String
)

data class PhotoSrc(
    @SerializedName("large2x") val large2x: String,
    @SerializedName("medium") val medium: String
)
