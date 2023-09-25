package com.hennamehndi.mehndipatterns.bridalmehndi.designs.model;

import java.util.ArrayList;

public class CategoryModel {

    public String name = "";
    public String icon = "";
    public ArrayList<String> images = new ArrayList<>();

    public CategoryModel() {
    }

    public CategoryModel(String name, String icon, ArrayList<String> images) {
        this.name = name;
        this.icon = icon;
        this.images = images;
    }

}
