
package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class ResponseText {

    @SerializedName("TextDetections")
    @Expose
    private ArrayList<model.TextDetection> textDetections = null;
    @SerializedName("TextModelVersion")
    @Expose
    private String textModelVersion;

    public ArrayList<model.TextDetection> getTextDetections() {
        return textDetections;
    }

    public void setTextDetections(ArrayList<model.TextDetection> textDetections) {
        this.textDetections = textDetections;
    }

    public String getTextModelVersion() {
        return textModelVersion;
    }

    public void setTextModelVersion(String textModelVersion) {
        this.textModelVersion = textModelVersion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ResponseText.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("textDetections");
        sb.append('=');
        sb.append(((this.textDetections == null)?"<null>":this.textDetections));
        sb.append(',');
        sb.append("textModelVersion");
        sb.append('=');
        sb.append(((this.textModelVersion == null)?"<null>":this.textModelVersion));
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
        result = ((result* 31)+((this.textDetections == null)? 0 :this.textDetections.hashCode()));
        result = ((result* 31)+((this.textModelVersion == null)? 0 :this.textModelVersion.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ResponseText) == false) {
            return false;
        }
        ResponseText rhs = ((ResponseText) other);
        return (((this.textDetections == rhs.textDetections)||((this.textDetections!= null)&&this.textDetections.equals(rhs.textDetections)))&&((this.textModelVersion == rhs.textModelVersion)||((this.textModelVersion!= null)&&this.textModelVersion.equals(rhs.textModelVersion))));
    }

}
