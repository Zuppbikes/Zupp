package in.zuppbikes.activity.customer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.zupp.component.httpclient.AppClient;
import com.zupp.component.logger.Logger;
import com.zupp.component.models.BookingSend;
import com.zupp.component.models.SimpleResponse;
import com.zupp.component.models.Vehicle;
import com.zupp.component.storage.AppUserSession;

import java.util.Calendar;
import java.util.Date;

import in.zuppbikes.R;
import in.zuppbikes.activity.BaseFragment;
import in.zuppbikes.activity.dashboard.DashboardActivity;
import in.zuppbikes.constants.Constants;
import in.zuppbikes.utils.AppMessage;
import in.zuppbikes.utils.DateUtils;
import in.zuppbikes.utils.ErrorHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Displays confirmation screen before booking is complete
 */
public class ZuppConfirmFragment extends BaseFragment implements View.OnClickListener {

    private TextView mTVLicensePlateNo, mTVVehicleBrand, mTVReturnDate;
    private LinearLayout mLinearReturnDate;
    private Button mBZuppIt, mBOK;
    private long mReturningDate = 0;
    private Vehicle mSelectedVehicle;

    public ZuppConfirmFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Vehicle iData) {
        ZuppConfirmFragment aFragment = new ZuppConfirmFragment();
        Bundle aBundle = new Bundle();
        aBundle.putParcelable(Constants.EXTRAS_VEHICLE, iData);
        aFragment.setArguments(aBundle);
        return aFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_vehicle, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setVehicleDetail();

    }

    private void initView(View iView) {

        mTVLicensePlateNo = iView.findViewById(R.id.tvLicensePlateNo);
        mTVVehicleBrand = iView.findViewById(R.id.tvVehicleBrand);
        mLinearReturnDate = iView.findViewById(R.id.linearReturnDate);
        mTVReturnDate = iView.findViewById(R.id.tvReturnDate);
        mBZuppIt = iView.findViewById(R.id.bZuppIt);
        mBOK = iView.findViewById(R.id.bOk);
        mBZuppIt.setVisibility(View.GONE);
        setListeners();
    }

    private void setListeners() {
        mLinearReturnDate.setOnClickListener(this);
        mBOK.setOnClickListener(this);
    }

    private void setVehicleDetail() {
        mSelectedVehicle = getArguments().getParcelable(Constants.EXTRAS_VEHICLE);
        if (mSelectedVehicle != null) {
            mTVLicensePlateNo.setText(mSelectedVehicle.registrationNumber);
            mTVVehicleBrand.setText(mSelectedVehicle.brand);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bOk:
                if (mReturningDate > 0) {
                    createBooking();
                } else {
                    AppMessage.showToast(getString(R.string.error_msg_select_return_date_time));
                }
                break;

            case R.id.linearReturnDate:
                showDatePickerDialog();
                break;
        }
    }

    public void showDatePickerDialog() {
        Calendar aDefault = Calendar.getInstance();
        if (mReturningDate > 0) {
            aDefault.setTimeInMillis(mReturningDate);
        }
        Calendar aMin = Calendar.getInstance();
        aMin.add(Calendar.DAY_OF_MONTH, 1);
        Calendar aMax = Calendar.getInstance();
        aMax.add(Calendar.DAY_OF_MONTH, 30);

        new SlideDateTimePicker.Builder(getActivity().getSupportFragmentManager())
                .setListener(listener)
                .setMinDate(aMin.getTime())
                .setInitialDate(aDefault.getTime())
                .setTheme(R.style.AlertDialogTheme)
                .setIs24HourTime(false)
                .setMaxDate(aMax.getTime())
                .setIndicatorColor(ContextCompat.getColor(getActivity(), R.color.white))
                .build()
                .show();
    }

    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            // Do something with the date. This Date object contains
            // the date and time that the user has selected.
            mReturningDate = date.getTime();
            mTVReturnDate.setText(DateUtils.format(date.getTime()));
            Logger.logInfo("Date Selected", DateUtils.formatSendServer(mReturningDate));
        }

        @Override
        public void onDateTimeCancel() {
            // Overriding onDateTimeCancel() is optional.
        }
    };

    private void createBooking() {
        showProgress();
        BookingSend aBooking = new BookingSend();
        aBooking.customerId = AppUserSession.getInstance().getCustomerDetail().customer.id;
        aBooking.endDate = DateUtils.formatSendServer(mReturningDate);
        aBooking.fuelExcluded = true;
        aBooking.vehicleId = mSelectedVehicle.id;
        Call<SimpleResponse> aCall = AppClient.get().createBooking(aBooking);
        aCall.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                try {
                    hideProgress();
                    if (response.body() != null && response.body().status.equals("success")) {
                        AppUserSession.getInstance().clearCustomerData();
                        showDialogMessage();
                    } else {
                        ErrorHandler.getErrorMessage(null, response.errorBody());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                try {
                    hideProgress();
                    ErrorHandler.getErrorMessage(t, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showDialogMessage() {
        AlertDialog aDialog = null;
        AlertDialog.Builder aDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DialogSlideAnim);

        LayoutInflater aInflater = getLayoutInflater();
        View aDialogView = aInflater.inflate(R.layout.layout_booking_confirmation, null);
        aDialogBuilder.setView(aDialogView).setCancelable(false);
        aDialog = aDialogBuilder.create();
        aDialog.setCanceledOnTouchOutside(false);
        aDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        final TextView aHeader = aDialogView.findViewById(R.id.tvTitle);
        aHeader.setText(R.string.ride_booked);
        final Button aDone = aDialogView.findViewById(R.id.bDone);
        final AlertDialog finalADialog = aDialog;
        aDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashboardActivity) getActivity()).replaceFragment(new CustomerLoginFragment(), true);
                finalADialog.dismiss();
            }
        });

        aDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).setToolBarTitle(getString(R.string.return_zupp));
    }
}
