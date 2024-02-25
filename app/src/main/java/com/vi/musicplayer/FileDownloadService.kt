package com.vi.musicplayer

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


interface FileDownloadService {
    @GET
    fun downloadFileWithDynamicUrlSync(
        @Url url: String
    ): Call<ResponseBody>
}
