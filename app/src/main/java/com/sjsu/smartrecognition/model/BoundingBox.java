package com.sjsu.smartrecognition.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class BoundingBox {

    @SerializedName("Width")
    @Expose
    private Double width;
    @SerializedName("Height")
    @Expose
    private Double height;
    @SerializedName("Left")
    @Expose
    private Double left;
    @SerializedName("Top")
    @Expose
    private Double top;

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getLeft() {
        return left;
    }

    public void setLeft(Double left) {
        this.left = left;
    }

    public Double getTop() {
        return top;
    }

    public void setTop(Double top) {
        this.top = top;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(BoundingBox.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("width");
        sb.append('=');
        sb.append(((this.width == null) ? "<null>" : this.width));
        sb.append(',');
        sb.append("height");
        sb.append('=');
        sb.append(((this.height == null) ? "<null>" : this.height));
        sb.append(',');
        sb.append("left");
        sb.append('=');
        sb.append(((this.left == null) ? "<null>" : this.left));
        sb.append(',');
        sb.append("top");
        sb.append('=');
        sb.append(((this.top == null) ? "<null>" : this.top));
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
        result = ((result * 31) + ((this.width == null) ? 0 : this.width.hashCode()));
        result = ((result * 31) + ((this.top == null) ? 0 : this.top.hashCode()));
        result = ((result * 31) + ((this.left == null) ? 0 : this.left.hashCode()));
        result = ((result * 31) + ((this.height == null) ? 0 : this.height.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BoundingBox) == false) {
            return false;
        }
        BoundingBox rhs = ((BoundingBox) other);
        return (((((this.width == rhs.width) || ((this.width != null) && this.width.equals(rhs.width))) && ((this.top == rhs.top) || ((this.top != null) && this.top.equals(rhs.top)))) && ((this.left == rhs.left) || ((this.left != null) && this.left.equals(rhs.left)))) && ((this.height == rhs.height) || ((this.height != null) && this.height.equals(rhs.height))));
    }

}
