package com.sjsu.smartrecognition.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class ObjectResult {
    @SerializedName("response")
    @Expose
    private List<ObjectResponse> response = new ArrayList<ObjectResponse>();

    /**
     * @return The todos
     */
    public List<ObjectResponse> getResponse() {
        return response;
    }

    /**
     * @param response The todos
     */
    public void setResponse(List<ObjectResponse> response) {
        this.response = response;
    }
}
