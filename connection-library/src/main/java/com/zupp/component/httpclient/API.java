package com.zupp.component.httpclient;


import com.zupp.component.models.BookingDetailsResponse;
import com.zupp.component.models.BookingSend;
import com.zupp.component.models.CustomerResponse;
import com.zupp.component.models.SimpleResponse;
import com.zupp.component.models.InVoiceResponse;
import com.zupp.component.models.LoginResponse;
import com.zupp.component.models.VehicleListResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {
    // Define interface for API endpoints in retrofit style

    @FormUrlEncoded
    @POST(EndPoint.LOGIN)
    Call<LoginResponse> login(@Field("email") String userName, @Field("password") String password);

    @GET(EndPoint.GENERATE_OTP)
    Call<SimpleResponse> generateOTP(@Path("mobile") String mobile);

    @GET(EndPoint.VERIFY_USER)
    Call<CustomerResponse> verifyCustomer(@Path("mobile") String mobile, @Path("otp") String otp);

    @GET(EndPoint.GET_VEHICLE_LIST)
    Call<VehicleListResponse> getVehicleList(@Query("page") int page);

    @POST(EndPoint.BOOKINGS)
    Call<SimpleResponse> createBooking(@Body BookingSend iBooking);

    @PUT(EndPoint.END_BOOKING)
    Call<SimpleResponse> endBooking(@Path("bookingId") String bookingId, @Body Map<String, Boolean> iParams);

    @GET(EndPoint.BOOKINGS)
    Call<BookingDetailsResponse> getBookings();

    @GET(EndPoint.GET_BILL_DETAILS)
    Call<InVoiceResponse> getBillDetails(@Path("bookingId")  String bookingId);

    @PUT(EndPoint.FEEDBACK_RATE)
    Call<SimpleResponse> rateService(@Path("bookingId") String bookingId, @Body Map<String, Integer> iParams);


}