package com.example.q4_photogallery;

// model class to hold image file information
public class ImageItem {
    private String name;
    private String path;
    private long size;
    private long dateModified;

    public ImageItem(String name, String path, long size, long dateModified) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.dateModified = dateModified;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public long getDateModified() {
        return dateModified;
    }
}