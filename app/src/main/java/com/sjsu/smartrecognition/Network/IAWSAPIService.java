package com.sjsu.smartrecognition.Network;

import com.sjsu.smartrecognition.model.ObjectResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IAWSAPIService {
    @GET("/recognize")
    Call<ObjectResponse> recognize(@Query("fileName") String fileName, @Query("mode") String mode);
}
