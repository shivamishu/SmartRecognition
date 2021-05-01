package com.sjsu.smartrecognition.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Instance {

    @SerializedName("BoundingBox")
    @Expose
    private BoundingBox boundingBox;
    @SerializedName("Confidence")
    @Expose
    private Double confidence;

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Instance.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("boundingBox");
        sb.append('=');
        sb.append(((this.boundingBox == null) ? "<null>" : this.boundingBox));
        sb.append(',');
        sb.append("confidence");
        sb.append('=');
        sb.append(((this.confidence == null) ? "<null>" : this.confidence));
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
        result = ((result * 31) + ((this.boundingBox == null) ? 0 : this.boundingBox.hashCode()));
        result = ((result * 31) + ((this.confidence == null) ? 0 : this.confidence.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Instance) == false) {
            return false;
        }
        Instance rhs = ((Instance) other);
        return (((this.boundingBox == rhs.boundingBox) || ((this.boundingBox != null) && this.boundingBox.equals(rhs.boundingBox))) && ((this.confidence == rhs.confidence) || ((this.confidence != null) && this.confidence.equals(rhs.confidence))));
    }

}
