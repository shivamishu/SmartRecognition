
package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sjsu.smartrecognition.model.BoundingBox;

import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Geometry {

    @SerializedName("BoundingBox")
    @Expose
    private BoundingBox boundingBox;
    @SerializedName("Polygon")
    @Expose
    private List<model.Polygon> polygon = null;

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public List<model.Polygon> getPolygon() {
        return polygon;
    }

    public void setPolygon(List<model.Polygon> polygon) {
        this.polygon = polygon;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Geometry.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("boundingBox");
        sb.append('=');
        sb.append(((this.boundingBox == null)?"<null>":this.boundingBox));
        sb.append(',');
        sb.append("polygon");
        sb.append('=');
        sb.append(((this.polygon == null)?"<null>":this.polygon));
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
        result = ((result* 31)+((this.polygon == null)? 0 :this.polygon.hashCode()));
        result = ((result* 31)+((this.boundingBox == null)? 0 :this.boundingBox.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Geometry) == false) {
            return false;
        }
        Geometry rhs = ((Geometry) other);
        return (((this.polygon == rhs.polygon)||((this.polygon!= null)&&this.polygon.equals(rhs.polygon)))&&((this.boundingBox == rhs.boundingBox)||((this.boundingBox!= null)&&this.boundingBox.equals(rhs.boundingBox))));
    }

}
