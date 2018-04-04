package com.zupp.component.httpclient;

public class EndPoint {
    // Constants for Endpoints

    public static final String LOGIN = "api/login/v";
    public static final String GET_VEHICLE_LIST = "api/vehicles";
    public static final String BOOKINGS = "api/booking";
    public static final String GENERATE_OTP = "api/booking/o/{mobile}";
    public static final String VERIFY_USER = "api/booking/v/{mobile}/{otp}";
    public static final String GET_BOOKING_PLANS = "api/bookingPlans";
    public static final String END_BOOKING = "api/booking/e/{bookingId}";
    public static final String GET_BILL_DETAILS = "api/booking/i/{bookingId}";
    public static final String FEEDBACK_RATE = "api/booking/r/{bookingId}";
}
