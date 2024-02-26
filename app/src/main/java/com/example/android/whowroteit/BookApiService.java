package com.example.android.whowroteit;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface BookApiService {

    @GET("volumes")
    Call<JsonElement> getBookInfo(
            @Query("q") String queryString,
            @Query("maxResults") int maxResults,
            @Query("printType") String printType
    );

}
