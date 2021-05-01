package com.sjsu.smartrecognition.model;

import android.net.Uri;

import java.io.Serializable;
import java.util.Objects;

public class ImageURI implements Serializable {
    private Uri imageUri;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageURI imageURI = (ImageURI) o;
        return Objects.equals(imageUri, imageURI.imageUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageUri);
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public String toString() {
        return "ImageURI{" +
                "imageUri=" + imageUri +
                '}';
    }
}
