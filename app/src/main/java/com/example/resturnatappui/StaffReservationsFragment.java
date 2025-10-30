package com.example.resturnatappui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class StaffReservationsFragment extends Fragment {

    private LinearLayout staffReservationsContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_reservations, container, false);
        staffReservationsContainer = view.findViewById(R.id.staffReservationsContainer);
        loadReservations();
        return view;
    }

    private void loadReservations() {
        staffReservationsContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (ReservationData.reservations.isEmpty()) {
            TextView empty = new TextView(getContext());
            empty.setText("No current reservations.");
            empty.setTextSize(16f);
            empty.setTextColor(getResources().getColor(android.R.color.darker_gray));
            staffReservationsContainer.addView(empty);
            return;
        }

        for (int i = 0; i < ReservationData.reservations.size(); i++) {
            ReservationData.Reservation res = ReservationData.reservations.get(i);

            // Create a card-like layout
            View item = inflater.inflate(R.layout.item_reservation, staffReservationsContainer, false);

            TextView info = item.findViewById(R.id.tvReservationInfo);
            MaterialButton btnEdit = item.findViewById(R.id.btnEdit);
            MaterialButton btnDelete = item.findViewById(R.id.btnDelete);

            btnEdit.setVisibility(View.GONE); // Staff doesn’t edit guest bookings
            btnDelete.setText("Cancel");

            String name = ReservationData.accountName.equals("NULL") ? "(Guest)" : ReservationData.accountName;
            info.setText(name + "\n" + res.date + "  " + res.time + "\nParty Size: " + res.guests);

            int index = i;
            btnDelete.setOnClickListener(v -> {
                ReservationData.reservations.remove(index);
                loadReservations();
            });

            staffReservationsContainer.addView(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadReservations();
    }
}
