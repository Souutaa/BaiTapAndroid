package com.example.appplaymusic

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface Download_Service {
    @GET
    fun downloadFileWithFixedUrl(
        @Url url: String
//        @Query("id") id: String,
//        @Query("export") export: Boolean = true,
    ): Call<ResponseBody>
}