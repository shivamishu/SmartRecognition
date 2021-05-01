package com.sjsu.smartrecognition.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")

public class Label {

    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Confidence")
    @Expose
    private Double confidence;
    @SerializedName("Instances")
    @Expose
    private List<Instance> instances = null;
    @SerializedName("Parents")
    @Expose
    private List<Parent> parents = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public List<Instance> getInstances() {
        return instances;
    }

    public void setInstances(List<Instance> instances) {
        this.instances = instances;
    }

    public List<Parent> getParents() {
        return parents;
    }

    public void setParents(List<Parent> parents) {
        this.parents = parents;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Label.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null) ? "<null>" : this.name));
        sb.append(',');
        sb.append("confidence");
        sb.append('=');
        sb.append(((this.confidence == null) ? "<null>" : this.confidence));
        sb.append(',');
        sb.append("instances");
        sb.append('=');
        sb.append(((this.instances == null) ? "<null>" : this.instances));
        sb.append(',');
        sb.append("parents");
        sb.append('=');
        sb.append(((this.parents == null) ? "<null>" : this.parents));
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
        result = ((result * 31) + ((this.name == null) ? 0 : this.name.hashCode()));
        result = ((result * 31) + ((this.instances == null) ? 0 : this.instances.hashCode()));
        result = ((result * 31) + ((this.confidence == null) ? 0 : this.confidence.hashCode()));
        result = ((result * 31) + ((this.parents == null) ? 0 : this.parents.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Label) == false) {
            return false;
        }
        Label rhs = ((Label) other);
        return (((((this.name == rhs.name) || ((this.name != null) && this.name.equals(rhs.name))) && ((this.instances == rhs.instances) || ((this.instances != null) && this.instances.equals(rhs.instances)))) && ((this.confidence == rhs.confidence) || ((this.confidence != null) && this.confidence.equals(rhs.confidence)))) && ((this.parents == rhs.parents) || ((this.parents != null) && this.parents.equals(rhs.parents))));
    }

}
