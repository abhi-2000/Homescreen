package com.example.cookbook;

import android.widget.ImageView;
import android.widget.TextView;

public class modleClass {
private int imageresource;
private String txt;

    public modleClass(int imageresource, String  txt) {
        this.imageresource = imageresource;
        this.txt = txt;
    }

    public int getImageresource() {
        return imageresource;
    }

    public String getTxt() {
        return txt;
    }
}
