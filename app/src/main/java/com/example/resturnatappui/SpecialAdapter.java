package com.example.resturnatappui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SpecialAdapter extends RecyclerView.Adapter<SpecialAdapter.SpecialViewHolder> {

    private final List<String> specials;

    public SpecialAdapter(List<String> specials) {
        this.specials = specials;
    }

    @NonNull
    @Override
    public SpecialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_special, parent, false);
        return new SpecialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialViewHolder holder, int position) {
        holder.title.setText(specials.get(position));

        // Optionally set images later
        holder.image.setBackgroundResource(android.R.drawable.ic_menu_gallery);
    }

    @Override
    public int getItemCount() {
        return specials.size();
    }

    static class SpecialViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;

        SpecialViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvSpecialTitle);
            image = itemView.findViewById(R.id.imgSpecial);
        }
    }
}
