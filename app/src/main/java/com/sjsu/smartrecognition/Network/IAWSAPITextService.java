package com.sjsu.smartrecognition.Network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IAWSAPITextService {
    @GET("/recognize")
    Call<model.ResponseText> recognize(@Query("fileName") String fileName, @Query("mode") String mode);
}
