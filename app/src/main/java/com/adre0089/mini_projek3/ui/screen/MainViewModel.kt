package com.adre0089.mini_projek3.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adre0089.mini_projek3.model.Jaket
import com.adre0089.mini_projek3.network.ApiStatus
import com.adre0089.mini_projek3.network.JaketApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class MainViewModel: ViewModel() {
    var data = mutableStateOf(emptyList<Jaket>())
        private set
    var status = MutableStateFlow(ApiStatus.LOADING)
        private set
    var errorMessage = mutableStateOf<String?>(null)
        private set


    fun retrieveData(userId: String){
        viewModelScope.launch(Dispatchers.IO){
            status.value = ApiStatus.LOADING
            try {
                // Log the API call attempt
                Log.d("MainViewModel", "Attempting to retrieve data for userId: $userId")
                val response = JaketApi.service.getJaket(userId)
                data.value = response
                status.value = ApiStatus.SUCCESS
                Log.d("MainViewModel", "Data retrieved successfully. Item count: ${response.size}")
            }catch (e: Exception){
                Log.e("MainViewModel", "Failure retrieving data: ${e.message}", e)
                errorMessage.value = "Failed to load data: ${e.message}"
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(userId: String, nama:String, jenis: String, status: String, bitmap: Bitmap){
        viewModelScope.launch(Dispatchers.IO){
            try {
                Log.d("MainViewModel", "Attempting to save data for userId: $userId, nama: $nama , Status $status , gambar $bitmap")
                val result = JaketApi.service.postJaket(
                    userId,
                    nama.toRequestBody("text/plain".toMediaTypeOrNull()),
                    jenis.toRequestBody("text/plain".toMediaTypeOrNull()),
                    status.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )
                if (result.status == "success") {
                    Log.d("MainViewModel", "Data saved successfully. Status: ${result.status}")
                    retrieveData(userId) // Refresh data after successful save
                } else {
                    Log.e("MainViewModel", "Save data failed with message: ${result.message}")
                    throw Exception(result.message)
                }
            }catch (e: Exception){
                Log.e("MainViewModel", "Failure saving data: ${e.message}", e)
                errorMessage.value = "Error saving data: ${e.message}"
            }
        }
    }

    fun deleteData(userId: String, hewanId: String) {
        Log.d("MainViewModel", "Attempting to delete data: UserId= $userId, hewanId= $hewanId")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = JaketApi.service.deleteJaket(
                    userId = userId,
                    id = hewanId
                )

                if (result.status == "success") {
                    Log.d("MainViewModel", "Data deleted successfully. Status: ${result.status}")
                    retrieveData(userId) // Refresh data after successful delete
                } else {
                    Log.e("MainViewModel", "Delete data failed with message: ${result.message}")
                    throw Exception(result.message)
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Failure deleting data: ${e.message}", e)
                errorMessage.value = "Error deleting data: ${e.message}"
            }
        }
    }
    fun updateData(userId: String, id: String, nama: String, jenis: String, status: String, bitmap: Bitmap?){
        viewModelScope.launch(Dispatchers.IO){
            try {
                Log.d("MainViewModel", "Attempting to update data: JaketId= $id, userId: $userId, nama: $nama , Status $status , gambar $bitmap")
                val result = JaketApi.service.putJaket(
                    userId,
                    id,
                    nama.toRequestBody("text/plain".toMediaTypeOrNull()),
                    jenis.toRequestBody("text/plain".toMediaTypeOrNull()),
                    status.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap?.toMultipartBody() // Kirim gambar hanya jika ada bitmap baru
                )
                if (result.status == "success") {
                    Log.d("MainViewModel", "Data updated successfully. Status: ${result.status}")
                    retrieveData(userId) // Refresh data after successful update
                } else {
                    Log.e("MainViewModel", "Update data failed with message: ${result.message}")
                    throw Exception(result.message)
                }
            }catch (e: Exception){
                Log.e("MainViewModel", "Failure updating data: ${e.message}", e)
                errorMessage.value = "Error updating data: ${e.message}"
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream) // Adjust quality if needed
        val byteArray = stream.toByteArray()

        val requestBody = byteArray.toRequestBody(
            "jaket_images/jpeg".toMediaTypeOrNull(), 0,byteArray.size
        )

        return MultipartBody.Part.createFormData(
            "gambar", "jaket_images.jpg", requestBody // "image" should match the multipart key on your server
        )
    }

    fun clearMessage() { errorMessage.value = null }
}