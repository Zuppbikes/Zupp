package in.zuppbikes.activity.customer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zupp.component.httpclient.AppClient;
import com.zupp.component.listener.EndlessRecyclerOnScrollListener;
import com.zupp.component.models.CustomerData;
import com.zupp.component.models.Vehicle;
import com.zupp.component.models.VehicleListResponse;
import com.zupp.component.storage.AppUserSession;

import java.util.ArrayList;
import java.util.Locale;

import in.zuppbikes.R;
import in.zuppbikes.activity.dashboard.DashboardActivity;
import in.zuppbikes.adapters.VehicleListAdapter;
import in.zuppbikes.constants.Constants;
import in.zuppbikes.utils.ErrorHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Displays booking screen to select vehicle
 */
public class SelectVehicleFragment extends Fragment implements View.OnClickListener, VehicleListAdapter.OnZuppListener {

    private CustomerData mCustomerDetail;
    private TextView mTVCustomerName, mTVCustomerSVC, mTVCustomerMobile;
    private Button mBEditNumber;
    private ProgressBar mProgressBar;
    private RecyclerView mRVVehicles;
    private VehicleListAdapter mAdapter;
    private ArrayList<Vehicle> mList = new ArrayList<>();
    private int mPage = 1;

    public SelectVehicleFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(CustomerData iData) {
        SelectVehicleFragment aFragment = new SelectVehicleFragment();
        Bundle aBundle = new Bundle();
        aBundle.putParcelable(Constants.EXTRAS_CUSTOMER, iData);
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
        return inflater.inflate(R.layout.fragment_select_vehicle, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            mCustomerDetail = getArguments().getParcelable(Constants.EXTRAS_CUSTOMER);
        }
        initView(view);
        setCustomerData();
        getVehicles(false);
    }

    private void initView(View iView) {
        mProgressBar = iView.findViewById(R.id.progressBar);
        mTVCustomerName = iView.findViewById(R.id.tvCustomerName);
        mTVCustomerSVC = iView.findViewById(R.id.tvSVCNumber);
        mTVCustomerMobile = iView.findViewById(R.id.tvCustomerMobile);
        mBEditNumber = iView.findViewById(R.id.bEditNumber);
        mRVVehicles = iView.findViewById(R.id.rvVehicles);
        try {
            mRVVehicles.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAdapter = new VehicleListAdapter(getActivity(), R.layout.row_vehicle, mList);
            mRVVehicles.setAdapter(mAdapter);
            mRVVehicles.addOnScrollListener(mScrollListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setListeners();
    }

    private EndlessRecyclerOnScrollListener mScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadMore(int current_page) {
            mPage = current_page;
            getVehicles(true);
        }

        @Override
        public void onScroll() {

        }
    };

    private void setListeners() {
        mBEditNumber.setOnClickListener(this);
        mAdapter.setOnZuppItemClickListener(this);
    }

    private void setCustomerData() {
        if (mCustomerDetail != null) {
            mTVCustomerName.setText(mCustomerDetail.customer.name);
            mTVCustomerSVC.setText(String.format(Locale.getDefault(), getString(R.string.customer_svc_format), mCustomerDetail.svcToken));
            mTVCustomerMobile.setText(String.format(Locale.getDefault(), getString(R.string.customer_mobile_no_format), mCustomerDetail.customer.phoneNumber));
        }
    }

    private void getVehicles(final boolean toAppend) {
        Call<VehicleListResponse> aCall = AppClient.get().getVehicleList(mPage);
        aCall.enqueue(new Callback<VehicleListResponse>() {
            @Override
            public void onResponse(Call<VehicleListResponse> call, Response<VehicleListResponse> response) {
                try {
                    mProgressBar.setVisibility(View.GONE);
                    if (response.body() != null && response.body().status.equals("success")) {
                        if (response.body().data != null) {
                            if (!toAppend)
                                mList.clear();
                            mList.addAll(response.body().data);
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        ErrorHandler.getErrorMessage(null, response.errorBody());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<VehicleListResponse> call, Throwable t) {
                try {
                    mProgressBar.setVisibility(View.GONE);
                    mScrollListener.resetLoading();
                    ErrorHandler.getErrorMessage(t, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bEditNumber:
                AppUserSession.getInstance().clearCustomerData();
                ((DashboardActivity) getActivity()).replaceFragment(new CustomerLoginFragment(), true);
                break;
        }
    }


    @Override
    public void onZuppClick(Vehicle iData) {
        ((DashboardActivity) getActivity()).replaceFragment(ZuppConfirmFragment.newInstance(iData), false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).setToolBarTitle(getString(R.string.available_to_zupp));
    }
}
