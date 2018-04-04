package in.zuppbikes.activity.customer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zupp.component.logger.Logger;
import com.zupp.component.models.Booking;
import com.zupp.component.storage.AppUserSession;

import in.zuppbikes.R;
import in.zuppbikes.activity.BaseFragment;
import in.zuppbikes.activity.dashboard.DashboardActivity;
import in.zuppbikes.constants.Constants;
import in.zuppbikes.utils.DateUtils;


/**
 * Displays return confirmation screen befor booking ends
 */
public class ZuppReturnFragment extends BaseFragment implements View.OnClickListener {

    private TextView mTVLicensePlateNo, mTVVehicleBrand, mTVStartedZupping;
    private Button mBZuppIt, mBReturn;
    private Booking mBookingDetails;

    public ZuppReturnFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Booking iData) {
        ZuppReturnFragment aFragment = new ZuppReturnFragment();
        Bundle aBundle = new Bundle();
        aBundle.putParcelable(Constants.EXTRAS_BOOKING, iData);
        aFragment.setArguments(aBundle);
        return aFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.logInfo("Data", AppUserSession.getInstance().getAccessToken());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_return_vehicle, container, false);
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
        mTVStartedZupping = iView.findViewById(R.id.tvStartedZuppingOn);
        mBZuppIt = iView.findViewById(R.id.bZuppIt);
        mBReturn = iView.findViewById(R.id.bReturn);
        mBZuppIt.setVisibility(View.GONE);
        setListeners();
    }

    private void setListeners() {
        mTVStartedZupping.setOnClickListener(this);
        mBReturn.setOnClickListener(this);
    }

    private void setVehicleDetail() {
        mBookingDetails = getArguments().getParcelable(Constants.EXTRAS_BOOKING);
        if (mBookingDetails != null) {
            mTVLicensePlateNo.setText(mBookingDetails.vehicle.registrationNumber);
            mTVVehicleBrand.setText(mBookingDetails.vehicle.brand);
            mTVStartedZupping.setText(DateUtils.formatFromServer(mBookingDetails.timeOfRent));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bReturn:
                ((DashboardActivity) getActivity()).replaceFragment(new InvoiceFragment(), false);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).setToolBarTitle(getString(R.string.return_zupp));
    }
}
