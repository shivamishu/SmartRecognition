package com.sjsu.smartrecognition.Network;

public class AWSTextAPI {
    private AWSTextAPI() {}

    public static final String BASE_URL = "https://87hbqh1ghb.execute-api.us-west-1.amazonaws.com/";

    public static IAWSAPITextService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(IAWSAPITextService.class);
    }
}
