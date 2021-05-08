package com.sjsu.smartrecognition.Network;

public class AWSObjectAPI {
    private AWSObjectAPI() {}

    public static final String BASE_URL = "https://87hbqh1ghb.execute-api.us-west-1.amazonaws.com/";

    public static IAWSAPIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(IAWSAPIService.class);
    }
}
