package in.zuppbikes.activity.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zupp.component.httpclient.AppClient;
import com.zupp.component.models.SimpleResponse;

import in.zuppbikes.R;
import in.zuppbikes.activity.BaseFragment;
import in.zuppbikes.activity.dashboard.DashboardActivity;
import in.zuppbikes.utils.ErrorHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Displays customer login screen
 */
public class CustomerLoginFragment extends BaseFragment implements View.OnClickListener {

    private TextInputLayout mTILMobile;
    private AppCompatEditText mACEMobile;
    private Button mBEnter;

    public CustomerLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

    }

    /**
     * Initializes views
     *
     * @param iView
     */
    private void initView(View iView) {
        mBEnter = iView.findViewById(R.id.bEnter);
        mTILMobile = iView.findViewById(R.id.tilMobile);
        mACEMobile = iView.findViewById(R.id.etMobile);
        setListeners();
    }

    /**
     * Sets required listeners
     */
    private void setListeners() {
        mBEnter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bEnter:
                if (validate()) {
                    generateOTP();
                }
                break;
        }
    }

    /**
     * Validates form fields
     *
     * @return
     */
    private boolean validate() {
        if (mACEMobile.getText().toString().trim().length() == 0) {
            mTILMobile.setErrorEnabled(true);
            mTILMobile.setError(getString(R.string.error_msg_empty_mobile_number));
            return false;
        } else if (mACEMobile.getText().toString().trim().length() < 10) {
            mTILMobile.setErrorEnabled(true);
            mTILMobile.setError(getString(R.string.error_msg_valid_mobile_number));
            return false;
        }
        return true;
    }

    /**
     * API call to generate OTP for the phone number entered
     */
    private void generateOTP() {
        showProgress();
        Call<SimpleResponse> aCall = AppClient.get().generateOTP(mACEMobile.getText().toString().trim());
        aCall.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                try {
                    hideProgress();
                    if (response.body() != null && response.body().status.equals("success")) {
                        if (response.body().data == null) {
                            showError();
                        } else {
                            showOTPEntry();
                        }
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
     * Shows OTP entry screen
     */
    private void showOTPEntry() {
        if (getActivity() instanceof DashboardActivity) {
            ((DashboardActivity) getActivity()).replaceFragment(CustomerVerifyFragment.newInstance(mACEMobile.getText().toString().trim()), false);
        }
    }

    /**
     * Shows error screen if number is not found
     */
    private void showError() {
        startActivity(new Intent(getActivity(), NotFoundActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).setToolBarTitle(getString(R.string.customer_login));
    }
}
