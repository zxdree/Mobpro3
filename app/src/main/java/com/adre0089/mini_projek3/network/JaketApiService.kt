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
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

// Pastikan URL ngrok Anda selalu terbaru
// Ini adalah URL dasar untuk API (yang ada '/api/')
private const val BASE_API_URL = "https://0d1d-36-69-194-228.ngrok-free.app/api/"
// Ini adalah URL dasar untuk aset (gambar, tanpa '/api/')
private const val BASE_ASSET_URL = "https://0d1d-36-69-194-228.ngrok-free.app/"


private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory()) // Pastikan KotlinJsonAdapterFactory selalu terakhir
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_API_URL) // Gunakan BASE_API_URL untuk Retrofit
    .build()

interface JaketApiService {
    @GET("jakets")
    suspend fun getJaket(
         @Header("Authorization") userId: String // Jika UID tidak lagi relevan, header ini mungkin tidak diperlukan
        // atau jika digunakan untuk otentikasi umum, maka bisa dipertahankan.
        // Asumsi dari percakapan sebelumnya, UID sudah dihapus dari logika server.
    ) : List<Jaket>

    @Multipart
    @POST("jakets")
    suspend fun postJaket( // Ganti nama fungsi menjadi postJaket (lebih konsisten)
        @Header("Authorization") userId:String, // Jika UID tidak lagi relevan, header ini mungkin tidak diperlukan
        @Part("nama") nama: RequestBody,
        @Part("jenis") jenis: RequestBody,
        @Part("status") status: RequestBody, // Ganti 'satus' menjadi 'status'
        @Part gambar: MultipartBody.Part // Gambar tanpa nama Part karena itu adalah file
    ): JaketSatus // Sesuaikan nama model respons

    @DELETE("jakets/{id}")
    suspend fun deleteJaket( // Ganti nama fungsi menjadi deleteJaket (lebih konsisten)
        @Header("Authorization") userId: String, // Jika UID tidak lagi relevan, header ini mungkin tidak diperlukan
        @Path("id") id: String // Ganti @Query menjadi @Path karena id ada di URL path
    ): JaketSatus // Sesuaikan nama model respons

    @Multipart // Tambahkan ini
    @POST("jakets/{id}") // Tambahkan ini untuk operasi update
    suspend fun putJaket(
        @Header("Authorization") userId: String,
        @Path("id") id: String, // ID jaket yang akan diupdate
        @Part("nama") nama: RequestBody,
        @Part("jenis") jenis: RequestBody,
        @Part("status") status: RequestBody,
        @Part gambar: MultipartBody.Part? // Gambar bisa null jika tidak diupdate
    ): JaketSatus
}

object JaketApi {
    val service: JaketApiService by lazy {
        retrofit.create(JaketApiService::class.java)
    }

    // Fungsi untuk mendapatkan URL gambar
    // Parameter diubah menjadi 'gambar' karena di model Jaket Anda itu 'gambar'
    fun getJaketImageUrl(gambarFileName: String): String {
        return "${BASE_ASSET_URL}storage/jaket_images/$gambarFileName"
    }
}

enum class ApiStatus {LOADING, SUCCESS, FAILED}