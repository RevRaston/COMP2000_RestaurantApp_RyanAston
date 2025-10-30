package com.example.resturnatappui;

public class NotificationData {

    // Staff notifications
    public static boolean staffHasNotification = false;
    public static int staffNewReservationCount = 0;

    // Customer notifications
    public static boolean customerHasNotification = false;
    public static String customerMessage = "";

    // Reset functions
    public static void resetStaffNotifications() {
        staffHasNotification = false;
        staffNewReservationCount = 0;
    }

    public static void resetCustomerNotifications() {
        customerHasNotification = false;
        customerMessage = "";
    }
}
