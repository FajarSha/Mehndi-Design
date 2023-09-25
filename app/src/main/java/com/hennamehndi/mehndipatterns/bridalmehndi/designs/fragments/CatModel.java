package com.hennamehndi.mehndipatterns.bridalmehndi.designs.fragments;

import android.graphics.drawable.Drawable;

public class CatModel {
    public String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Drawable icon;

    public CatModel(String name, Drawable icon) {
        this.name = name;
        this.icon = icon;
    }
}
