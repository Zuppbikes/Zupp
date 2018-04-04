package in.zuppbikes.activity.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.zupp.component.httpclient.AppClient;
import com.zupp.component.models.CustomerData;
import com.zupp.component.models.CustomerResponse;
import com.zupp.component.models.SimpleResponse;
import com.zupp.component.storage.AppUserSession;

import in.zuppbikes.R;
import in.zuppbikes.activity.BaseFragment;
import in.zuppbikes.activity.dashboard.DashboardActivity;
import in.zuppbikes.constants.Constants;
import in.zuppbikes.custom.PinEntryEditText;
import in.zuppbikes.utils.AppMessage;
import in.zuppbikes.utils.ErrorHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Displays OTP verification screen
 */
public class CustomerVerifyFragment extends BaseFragment implements View.OnClickListener, PinEntryEditText.OnPinEnteredListener {

    private PinEntryEditText mPETOtp;
    private Button mBZuppNow, mBResendOTP;
    private TextView mTVTitle;
    private String mCustomerNumber;

    public CustomerVerifyFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(String iNumber) {
        CustomerVerifyFragment aBarChartFrag = new CustomerVerifyFragment();
        Bundle aBundle = new Bundle();
        aBundle.putString(Constants.EXTRAS_NUMBER, iNumber);
        aBarChartFrag.setArguments(aBundle);
        return aBarChartFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verify_otp, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mCustomerNumber = getArguments().getString(Constants.EXTRAS_NUMBER);

    }

    /**
     * Initializes views
     *
     * @param iView
     */
    private void initView(View iView) {
        mTVTitle = iView.findViewById(R.id.tvTitle);
        mBZuppNow = iView.findViewById(R.id.bZuppNow);
        mBResendOTP = iView.findViewById(R.id.bResentOTP);

        mPETOtp = iView.findViewById(R.id.etVerificationCode);
        mTVTitle.setText(R.string.verification);
        setListeners();
    }

    /**
     * Sets required listeners
     */
    private void setListeners() {
        mBZuppNow.setOnClickListener(this);
        mBResendOTP.setOnClickListener(this);
        mPETOtp.setOnPinEnteredListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bZuppNow:
                if (validate()) {
                    verifyCustomer();
                }
                break;

            case R.id.bResentOTP:
                generateOTP();
                break;
        }
    }

    @Override
    public void onPinEntered(CharSequence iPin) {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validate() {
        if (mPETOtp.getText().toString().trim().length() < 6) {
            AppMessage.showToast(getString(R.string.error_msg_valid_otp));
            return false;
        }
        return true;
    }


    /**
     * API call to generate OTP for the entered phone number
     */
    private void generateOTP() {
        showProgress();
        Call<SimpleResponse> aCall = AppClient.get().generateOTP(mCustomerNumber);
        aCall.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                try {
                    hideProgress();
                    if (response.body() != null && response.body().status.equals("success")) {

                    } else {
                        ErrorHandler.getErrorMessage(null, response.errorBody());
                        showError();
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

    /**
     * Shows error screen when number not found
     */
    private void showError() {
        startActivity(new Intent(getActivity(), NotFoundActivity.class));
    }

    /**
     * API call to verify OTP and customer
     */
    private void verifyCustomer() {
        showProgress();
        Call<CustomerResponse> aCall = AppClient.get().verifyCustomer(mCustomerNumber, mPETOtp.getText().toString().trim());
        aCall.enqueue(new Callback<CustomerResponse>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                try {
                    hideProgress();
                    if (response.body() != null && response.body().status.equals("success")) {
                        handleCustomer(response.body().data);
                    } else {
                        ErrorHandler.getErrorMessage(null, response.errorBody());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                try {
                    hideProgress();
                    ErrorHandler.getErrorMessage(t, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Handles customer response
     *
     * @param iData
     */
    private void handleCustomer(CustomerData iData) {
        AppUserSession.getInstance().setCustomerDetail(iData);
        if (!iData.hasBooking) {
            ((DashboardActivity) getActivity()).replaceFragment(SelectVehicleFragment.newInstance(iData), true);
        } else {
            ((DashboardActivity) getActivity()).replaceFragment(ZuppReturnFragment.newInstance(iData.booking), true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).setToolBarTitle(getString(R.string.verification));
    }
}
