package com.sjsu.smartrecognition.Network;

import com.sjsu.smartrecognition.model.PostObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IAWSAPIService {
    @POST("/detectObject")
    @FormUrlEncoded
    Call<PostObject> sendPhoto(@Field("Base64String") String base64String,
                               @Field("FileName") String fileName,
                               @Field("MimeType") String mimeType);
}
