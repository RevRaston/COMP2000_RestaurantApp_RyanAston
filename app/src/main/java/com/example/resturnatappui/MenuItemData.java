package com.example.resturnatappui;

import java.util.ArrayList;

public class MenuItemData {
    public static ArrayList<MenuItem> menuItems = new ArrayList<>();

    public static class MenuItem {
        public String name;
        public String price;
        public String description;
        public int imageResId;

        public MenuItem(String name, String price, String description, int imageResId) {
            this.name = name;
            this.price = price;
            this.description = description;
            this.imageResId = imageResId;
        }
    }
}
