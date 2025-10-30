package com.example.resturnatappui;

import java.util.ArrayList;
import java.util.List;

// A simple shared data class to hold user reservations and account info
public class ReservationData {

    // Global account name (used across screens)
    public static String accountName = "NULL";

    // Store all reservations added by user
    public static List<Reservation> reservations = new ArrayList<>();

    // Simple model for a reservation
    public static class Reservation {
        public String date;
        public String time;
        public int guests;

        public Reservation(String date, String time, int guests) {
            this.date = date;
            this.time = time;
            this.guests = guests;
        }
    }
}

