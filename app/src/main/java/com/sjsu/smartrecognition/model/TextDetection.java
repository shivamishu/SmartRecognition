
package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class TextDetection {

    @SerializedName("DetectedText")
    @Expose
    private String detectedText;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Confidence")
    @Expose
    private Double confidence;
    @SerializedName("Geometry")
    @Expose
    private model.Geometry geometry;
    @SerializedName("ParentId")
    @Expose
    private Integer parentId;

    public String getDetectedText() {
        return detectedText;
    }

    public void setDetectedText(String detectedText) {
        this.detectedText = detectedText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public model.Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(model.Geometry geometry) {
        this.geometry = geometry;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TextDetection.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("detectedText");
        sb.append('=');
        sb.append(((this.detectedText == null)?"<null>":this.detectedText));
        sb.append(',');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
        sb.append(',');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("confidence");
        sb.append('=');
        sb.append(((this.confidence == null)?"<null>":this.confidence));
        sb.append(',');
        sb.append("geometry");
        sb.append('=');
        sb.append(((this.geometry == null)?"<null>":this.geometry));
        sb.append(',');
        sb.append("parentId");
        sb.append('=');
        sb.append(((this.parentId == null)?"<null>":this.parentId));
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
        result = ((result* 31)+((this.detectedText == null)? 0 :this.detectedText.hashCode()));
        result = ((result* 31)+((this.confidence == null)? 0 :this.confidence.hashCode()));
        result = ((result* 31)+((this.geometry == null)? 0 :this.geometry.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.type == null)? 0 :this.type.hashCode()));
        result = ((result* 31)+((this.parentId == null)? 0 :this.parentId.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TextDetection) == false) {
            return false;
        }
        TextDetection rhs = ((TextDetection) other);
        return (((((((this.detectedText == rhs.detectedText)||((this.detectedText!= null)&&this.detectedText.equals(rhs.detectedText)))&&((this.confidence == rhs.confidence)||((this.confidence!= null)&&this.confidence.equals(rhs.confidence))))&&((this.geometry == rhs.geometry)||((this.geometry!= null)&&this.geometry.equals(rhs.geometry))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type))))&&((this.parentId == rhs.parentId)||((this.parentId!= null)&&this.parentId.equals(rhs.parentId))));
    }

}
