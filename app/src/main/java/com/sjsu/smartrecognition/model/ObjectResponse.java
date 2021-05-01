package com.sjsu.smartrecognition.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class ObjectResponse {

    @SerializedName("Labels")
    @Expose
    private ArrayList<Label> labels = null;
    @SerializedName("LabelModelVersion")
    @Expose
    private String labelModelVersion;

    public ArrayList<Label> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<Label> labels) {
        this.labels = labels;
    }

    public String getLabelModelVersion() {
        return labelModelVersion;
    }

    public void setLabelModelVersion(String labelModelVersion) {
        this.labelModelVersion = labelModelVersion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ObjectResponse.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("labels");
        sb.append('=');
        sb.append(((this.labels == null) ? "<null>" : this.labels));
        sb.append(',');
        sb.append("labelModelVersion");
        sb.append('=');
        sb.append(((this.labelModelVersion == null) ? "<null>" : this.labelModelVersion));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result * 31) + ((this.labelModelVersion == null) ? 0 : this.labelModelVersion.hashCode()));
        result = ((result * 31) + ((this.labels == null) ? 0 : this.labels.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ObjectResponse) == false) {
            return false;
        }
        ObjectResponse rhs = ((ObjectResponse) other);
        return (((this.labelModelVersion == rhs.labelModelVersion) || ((this.labelModelVersion != null) && this.labelModelVersion.equals(rhs.labelModelVersion))) && ((this.labels == rhs.labels) || ((this.labels != null) && this.labels.equals(rhs.labels))));
    }

}
