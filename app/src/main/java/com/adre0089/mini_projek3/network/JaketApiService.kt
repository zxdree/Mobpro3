package com.adre0089.mini_projek3.network

import com.adre0089.mini_projek3.model.Jaket
import com.adre0089.mini_projek3.model.JaketSatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

private const val BASE_API_URL = "https://apinyaadre.sendiko.my.id/api/"

private const val BASE_ASSET_URL = "https://apinyaadre.sendiko.my.id/"


private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_API_URL)
    .build()

interface JaketApiService {
    @GET("jakets")
    suspend fun getJaket(
         @Header("Authorization") userId: String
    ) : List<Jaket>

    @Multipart
    @POST("jakets")
    suspend fun postJaket(
        @Header("Authorization") userId:String,
        @Part("nama") nama: RequestBody,
        @Part("jenis") jenis: RequestBody,
        @Part("status") status: RequestBody,
        @Part gambar: MultipartBody.Part
    ): JaketSatus

    @DELETE("jakets/{id}")
    suspend fun deleteJaket(
        @Header("Authorization") userId: String,
        @Path("id") id: String
    ): JaketSatus

    @Multipart
    @POST("jakets/{id}")
    suspend fun putJaket(
        @Header("Authorization") userId: String,
        @Path("id") id: String,
        @Part("nama") nama: RequestBody,
        @Part("jenis") jenis: RequestBody,
        @Part("status") status: RequestBody,
        @Part gambar: MultipartBody.Part?
    ): JaketSatus
}

object JaketApi {
    val service: JaketApiService by lazy {
        retrofit.create(JaketApiService::class.java)
    }

    fun getJaketImageUrl(gambarFileName: String): String {
        return "${BASE_ASSET_URL}/storage/jaket_images/$gambarFileName"
    }
}

enum class ApiStatus {LOADING, SUCCESS, FAILED}