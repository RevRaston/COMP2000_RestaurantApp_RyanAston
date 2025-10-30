package com.example.resturnatappui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StaffMenuFragment extends Fragment {

    private LinearLayout menuContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_menu, container, false);

        menuContainer = view.findViewById(R.id.menuContainer);
        MaterialButton btnAddItem = view.findViewById(R.id.btnAddItem);
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddMenuItem);

        // Add new item buttons (both do same thing)
        View.OnClickListener addListener = v -> showAddEditDialog(null);
        btnAddItem.setOnClickListener(addListener);
        fabAdd.setOnClickListener(addListener);

        refreshMenuList();

        return view;
    }

    // 🧾 Dialog for adding/editing menu items
    private void showAddEditDialog(@Nullable MenuData.MenuItem itemToEdit) {
        boolean isEditing = (itemToEdit != null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(isEditing ? "Edit Menu Item" : "Add New Menu Item");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        EditText nameInput = new EditText(requireContext());
        nameInput.setHint("Item name");
        if (isEditing) nameInput.setText(itemToEdit.name);
        layout.addView(nameInput);

        EditText priceInput = new EditText(requireContext());
        priceInput.setHint("Price");
        priceInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if (isEditing) priceInput.setText(String.valueOf(itemToEdit.price));
        layout.addView(priceInput);

        Spinner categorySpinner = new Spinner(requireContext());
        ArrayAdapter<MenuData.Category> categoryAdapter =
                new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, MenuData.Category.values());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        if (isEditing) {
            categorySpinner.setSelection(itemToEdit.category.ordinal());
        }

        layout.addView(categorySpinner);

        builder.setView(layout);

        builder.setPositiveButton(isEditing ? "Save" : "Add", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String priceStr = priceInput.getText().toString().trim();
            MenuData.Category category = (MenuData.Category) categorySpinner.getSelectedItem();

            if (name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(requireContext(), "All fields required", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);

            if (isEditing) {
                itemToEdit.name = name;
                itemToEdit.price = price;
                itemToEdit.category = category;
            } else {
                MenuData.menuItems.add(new MenuData.MenuItem(name, price, android.R.drawable.ic_menu_gallery, category));
            }

            refreshMenuList();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    // 🔁 Refresh all menu items in the layout
    private void refreshMenuList() {
        menuContainer.removeAllViews();

        for (MenuData.MenuItem item : MenuData.menuItems) {
            View itemView = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, menuContainer, false);

            TextView text1 = itemView.findViewById(android.R.id.text1);
            TextView text2 = itemView.findViewById(android.R.id.text2);

            text1.setText(item.name + "  (£" + String.format("%.2f", item.price) + ")");
            text2.setText(item.category.toString());

            // Edit on tap
            itemView.setOnClickListener(v -> showAddEditDialog(item));

            // Delete on long press
            itemView.setOnLongClickListener(v -> {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Delete Menu Item?")
                        .setMessage("Are you sure you want to remove " + item.name + "?")
                        .setPositiveButton("Delete", (d, w) -> {
                            MenuData.menuItems.remove(item);
                            refreshMenuList();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            });

            menuContainer.addView(itemView);
        }

        if (MenuData.menuItems.isEmpty()) {
            TextView emptyText = new TextView(requireContext());
            emptyText.setText("No menu items yet. Tap + to add one!");
            emptyText.setPadding(0, 50, 0, 0);
            emptyText.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
            menuContainer.addView(emptyText);
        }
    }
}
