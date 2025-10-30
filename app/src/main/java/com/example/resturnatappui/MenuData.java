package com.example.resturnatappui;

import java.util.ArrayList;
import java.util.List;

public class MenuData {

    // 🍽 Categories for grouping menu items
    public enum Category {
        SPECIAL, DEAL, MEAL, DRINK, DESSERT
    }

    // 🍔 Menu item model
    public static class MenuItem {
        public String name;
        public double price;
        public int imageResId;
        public Category category;

        public MenuItem(String name, double price, int imageResId, Category category) {
            this.name = name;
            this.price = price;
            this.imageResId = imageResId;
            this.category = category;
        }
    }

    // 📦 Shared in-memory menu list
    public static List<MenuItem> menuItems = new ArrayList<>();

    // 🧾 Some starter data
    static {
        menuItems.add(new MenuItem("Cheeseburger", 6.99, android.R.drawable.ic_menu_gallery, Category.MEAL));
        menuItems.add(new MenuItem("Caesar Salad", 5.49, android.R.drawable.ic_menu_gallery, Category.MEAL));
        menuItems.add(new MenuItem("House Special Pizza", 9.99, android.R.drawable.ic_menu_gallery, Category.SPECIAL));
        menuItems.add(new MenuItem("2-for-1 Mojitos", 7.00, android.R.drawable.ic_menu_gallery, Category.DEAL));
        menuItems.add(new MenuItem("Cola", 2.00, android.R.drawable.ic_menu_gallery, Category.DRINK));
        menuItems.add(new MenuItem("Chocolate Cake", 3.99, android.R.drawable.ic_menu_gallery, Category.DESSERT));
    }

    // 🔍 Helper: get only items in a given category
    public static List<MenuItem> getItemsByCategory(Category category) {
        List<MenuItem> filtered = new ArrayList<>();
        for (MenuItem item : menuItems) {
            if (item.category == category) filtered.add(item);
        }
        return filtered;
    }
}
