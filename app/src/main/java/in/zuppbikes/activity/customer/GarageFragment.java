package in.zuppbikes.activity.customer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.zupp.component.httpclient.AppClient;
import com.zupp.component.listener.EndlessRecyclerOnScrollListener;
import com.zupp.component.models.Vehicle;
import com.zupp.component.models.VehicleListResponse;

import java.util.ArrayList;

import in.zuppbikes.R;
import in.zuppbikes.activity.dashboard.DashboardActivity;
import in.zuppbikes.adapters.VehicleListAdapter;
import in.zuppbikes.utils.ErrorHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Displays vehicles list
 */
public class GarageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ProgressBar mProgressBar;
    private RecyclerView mRVVehicles;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private VehicleListAdapter mAdapter;
    private ArrayList<Vehicle> mList = new ArrayList<>();
    private int mPage = 1;

    public GarageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicle, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        getVehicles(false);
    }

    /**
     * Initializes views
     *
     * @param iView
     */
    private void initView(View iView) {
        mSwipeRefreshLayout = iView.findViewById(R.id.swipeRefreshLayout);
        mProgressBar = iView.findViewById(R.id.progressBar);
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

    /**
     * Sets required listeners
     */
    private void setListeners() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * Scroll listener to handle pagination
     */
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

    /**
     * API call to fetch vehicle list
     *
     * @param toAppend
     */
    private void getVehicles(final boolean toAppend) {
        mSwipeRefreshLayout.setRefreshing(true);
        Call<VehicleListResponse> aCall = AppClient.get().getVehicleList(mPage);
        aCall.enqueue(new Callback<VehicleListResponse>() {
            @Override
            public void onResponse(Call<VehicleListResponse> call, Response<VehicleListResponse> response) {
                try {
                    mSwipeRefreshLayout.setRefreshing(false);
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
                    mSwipeRefreshLayout.setRefreshing(false);
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
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).setToolBarTitle(getString(R.string.garage));
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        mScrollListener.reset();
        getVehicles(false);
    }
}
