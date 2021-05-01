
package com.sjsu.smartrecognition.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class PostObject {

    @SerializedName("FileName")
    @Expose
    private String fileName;
    @SerializedName("MimeType")
    @Expose
    private String mimeType;
    @SerializedName("Base64String")
    @Expose
    private String base64String;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getBase64String() {
        return base64String;
    }

    public void setBase64String(String base64String) {
        this.base64String = base64String;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PostObject.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("fileName");
        sb.append('=');
        sb.append(((this.fileName == null)?"<null>":this.fileName));
        sb.append(',');
        sb.append("mimeType");
        sb.append('=');
        sb.append(((this.mimeType == null)?"<null>":this.mimeType));
        sb.append(',');
        sb.append("base64String");
        sb.append('=');
        sb.append(((this.base64String == null)?"<null>":this.base64String));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.fileName == null)? 0 :this.fileName.hashCode()));
        result = ((result* 31)+((this.mimeType == null)? 0 :this.mimeType.hashCode()));
        result = ((result* 31)+((this.base64String == null)? 0 :this.base64String.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PostObject) == false) {
            return false;
        }
        PostObject rhs = ((PostObject) other);
        return ((((this.fileName == rhs.fileName)||((this.fileName!= null)&&this.fileName.equals(rhs.fileName)))&&((this.mimeType == rhs.mimeType)||((this.mimeType!= null)&&this.mimeType.equals(rhs.mimeType))))&&((this.base64String == rhs.base64String)||((this.base64String!= null)&&this.base64String.equals(rhs.base64String))));
    }

}
