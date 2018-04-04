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
import com.zupp.component.models.BookingDetailsResponse;
import com.zupp.component.models.Invoice;

import java.util.ArrayList;

import in.zuppbikes.R;
import in.zuppbikes.activity.dashboard.DashboardActivity;
import in.zuppbikes.adapters.BillAdapter;
import in.zuppbikes.utils.ErrorHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Displays booking list screen
 */
public class BookingsFragment extends Fragment implements BillAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ProgressBar mProgressBar;
    private RecyclerView mRVVehicles;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BillAdapter mAdapter;
    private ArrayList<Invoice> mList = new ArrayList<>();

    public BookingsFragment() {
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
            mAdapter = new BillAdapter(getActivity(), R.layout.row_booking, mList);
            mRVVehicles.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setListeners();
    }

    /**
     * Sets required listener
     */
    private void setListeners() {
        mAdapter.setOnItemClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * API call to get Vehicles list
     *
     * @param toAppend
     */
    private void getVehicles(final boolean toAppend) {
        mSwipeRefreshLayout.setRefreshing(true);
        Call<BookingDetailsResponse> aCall = AppClient.get().getBookings();
        aCall.enqueue(new Callback<BookingDetailsResponse>() {
            @Override
            public void onResponse(Call<BookingDetailsResponse> call, Response<BookingDetailsResponse> response) {
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
            public void onFailure(Call<BookingDetailsResponse> call, Throwable t) {
                try {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mProgressBar.setVisibility(View.GONE);
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
        ((DashboardActivity) getActivity()).setToolBarTitle(getString(R.string.bookings));
    }

    @Override
    public void onItemClick(Invoice iItem, int position) {
        ((DashboardActivity) getActivity()).replaceFragment(InvoiceFragment.newInstance(iItem), false);
    }

    @Override
    public void onRefresh() {
        getVehicles(false);
    }

}
