package in.zuppbikes.activity.customer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zupp.component.httpclient.AppClient;
import com.zupp.component.httpclient.GsonHelper;
import com.zupp.component.httpclient.NoConnectivityException;
import com.zupp.component.models.CustomerData;
import com.zupp.component.models.InVoiceResponse;
import com.zupp.component.models.Invoice;
import com.zupp.component.models.SimpleResponse;
import com.zupp.component.models.Slots;
import com.zupp.component.storage.AppUserSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.zuppbikes.R;
import in.zuppbikes.activity.BaseFragment;
import in.zuppbikes.activity.dashboard.DashboardActivity;
import in.zuppbikes.adapters.InvoiceAdapter;
import in.zuppbikes.constants.Constants;
import in.zuppbikes.utils.AppMessage;
import in.zuppbikes.utils.CurrencyHelper;
import in.zuppbikes.utils.DateUtils;
import in.zuppbikes.utils.ErrorHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Displays Invoice data
 */
public class InvoiceFragment extends BaseFragment implements View.OnClickListener, InvoiceAdapter.OnItemClickListener {

    private TextView mTVInvoiceId, mTVCustomerName, mTVCustomerContact
            , mTVVehicleNo, mTVStartDate, mTVEndDate, mTVBillSubtotal
            , mTVBillSGST, mTVBillCGST, mTVBillDiscount, mTVBillTotal, mTVEndDateLabel;
    private Button mBConfirm, mBRetry;
    private LinearLayout mBillDetailsParent, mSlotsHeader;
    private ProgressBar mProgressBar;
    private RecyclerView mRVCharges;
    private InvoiceAdapter mAdapter;
    private ArrayList<Slots> mList = new ArrayList<>();
    private String mBookingId;
    private CoordinatorLayout mRootLayout;
    private boolean mDiscounted;
    private NestedScrollView mScroll;
    private boolean isLocal;
    private float mRating = 0;
    private Invoice mInvoice;

    public InvoiceFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Invoice iInvoice) {
        InvoiceFragment aFragment = new InvoiceFragment();
        Bundle aBundle = new Bundle();
        aBundle.putParcelable(Constants.EXTRAS_INVOICE, iInvoice);
        aFragment.setArguments(aBundle);
        return aFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        CustomerData aData = AppUserSession.getInstance().getCustomerDetail();
        mBookingId = aData != null && aData.booking != null && !TextUtils.isEmpty(aData.booking.id) ? aData.booking.id : "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_invoice, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        if (getArguments() != null) {
            Invoice aInVoice = getArguments().getParcelable(Constants.EXTRAS_INVOICE);
            if (aInVoice != null) {
                isLocal = true;
                mProgressBar.setVisibility(View.GONE);
                mBConfirm.setVisibility(View.GONE);
                refreshData(aInVoice);
            }
        } else {
            getBillDetails();
        }

    }

    /**
     * Initializes views
     *
     * @param iView
     */
    private void initView(View iView) {
        mTVEndDateLabel = iView.findViewById(R.id.tvEndDateLabel);
        mSlotsHeader = iView.findViewById(R.id.slotsHeader);
        mBillDetailsParent = iView.findViewById(R.id.billDetailsParent);
        mTVBillSubtotal = iView.findViewById(R.id.tvBillSubtotal);
        mTVBillSGST = iView.findViewById(R.id.tvBillSGst);
        mTVBillCGST = iView.findViewById(R.id.tvBillCGst);
        mTVBillDiscount = iView.findViewById(R.id.tvBillDiscount);
        mTVBillTotal = iView.findViewById(R.id.tvBillTotal);

        mTVCustomerName = iView.findViewById(R.id.tvCustomerName);
        mTVCustomerContact = iView.findViewById(R.id.tvCustomerContact);
        mTVVehicleNo = iView.findViewById(R.id.tvVehicleNo);
        mTVStartDate = iView.findViewById(R.id.tvStartDate);
        mTVEndDate = iView.findViewById(R.id.tvEndDate);


        mScroll = iView.findViewById(R.id.nestedScrollView);
//        mSwipeRefreshLayout = iView.findViewById(R.id.swipeRefreshLayout);
        mRootLayout = iView.findViewById(R.id.invoiceRoot);
        mProgressBar = iView.findViewById(R.id.progressBar);
        mTVInvoiceId = iView.findViewById(R.id.tvInVoiceId);
        mBConfirm = iView.findViewById(R.id.bConfirm);
        mBRetry = iView.findViewById(R.id.bRetry);
        mRVCharges = iView.findViewById(R.id.rvCharges);

        try {
            mRVCharges.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRVCharges.setNestedScrollingEnabled(false);
            mRVCharges.setHasFixedSize(true);
            mAdapter = new InvoiceAdapter(getActivity(), R.layout.row_charges, mList);
            mRVCharges.setAdapter(mAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
        setListeners();
    }

    /**
     * Sets required listeners
     */
    private void setListeners() {
        mBRetry.setOnClickListener(this);
        mBConfirm.setOnClickListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    /**
     * Updates UI and refreshed adapter data
     *
     * @param iData
     */
    private void refreshData(Invoice iData) {
        mInvoice = iData;
        mDiscounted = isLocal;
        mAdapter.setDiscounted(mDiscounted);

        //Customer Details
        setCustomerDetails(iData);

        // Invoice Data List
        mRootLayout.setVisibility(View.VISIBLE);
        setSlotsData(iData);

        //Bill Details
        setBillDetails(iData);
    }

    /**
     * Sets the slots data list section in Invoice
     *
     * @param iData
     */
    private void setSlotsData(Invoice iData) {
        mSlotsHeader.setVisibility(View.GONE);
        if (iData.slots != null && iData.slots.size() > 0) {
            mSlotsHeader.setVisibility(View.VISIBLE);
            mList.clear();
            mList.addAll(iData.slots);
            mAdapter.notifyDataSetChanged();
            mScroll.post(new Runnable() {
                @Override
                public void run() {
                    mScroll.requestLayout();
                }
            });
        }
    }

    /**
     * Sets customer details in Invoice section
     *
     * @param iData
     */
    private void setCustomerDetails(Invoice iData) {
        if (iData != null) {
            boolean hasEnded  = !TextUtils.isEmpty(iData.status) && iData.status.equals(Constants.STATUS_COMPLETED);
            mTVInvoiceId.setText(!TextUtils.isEmpty(iData.invoiceId) ? iData.invoiceId : "");
            mTVCustomerName.setText(!isLocal ? (!TextUtils.isEmpty(iData.customerName) ? iData.customerName : "") : (iData.customerId != null ? iData.customerId.name : ""));
            mTVCustomerContact.setText(!TextUtils.isEmpty(iData.customerPhoneNumber) ? iData.customerPhoneNumber : "");
            mTVVehicleNo.setText(iData.vehicleId != null && !TextUtils.isEmpty(iData.vehicleId.registrationNumber) ? iData.vehicleId.registrationNumber : "");
            mTVStartDate.setText(DateUtils.formatFromServer(iData.timeOfRent));
            mTVEndDateLabel.setText(hasEnded ? getString(R.string.end_date) : getString(R.string.expected_end_date));
            mTVEndDate.setText(hasEnded ? DateUtils.formatFromServer(iData.actualTimeOfReturn) : DateUtils.formatFromServer(iData.expectedTimeOfReturn));

        }
    }

    /**
     * Sets the bill details in Invoice section
     *
     * @param iData
     */
    private void setBillDetails(Invoice iData) {
        boolean isZupping  = isLocal && !iData.status.equals(Constants.STATUS_COMPLETED);
        if (isZupping) {
            mBillDetailsParent.setVisibility(View.GONE);
        } else {
            mBillDetailsParent.setVisibility(View.VISIBLE);
            mTVBillSubtotal.setText(CurrencyHelper.getFormattedPrice(iData.total));
            mTVBillSGST.setText(CurrencyHelper.getFormattedPrice(iData.sgst));
            mTVBillCGST.setText(CurrencyHelper.getFormattedPrice(iData.cgst));
            mTVBillDiscount.setText(CurrencyHelper.getFormattedPrice(iData.discount));
            mTVBillTotal.setText(CurrencyHelper.getFormattedPrice(iData.finalBill));
        }
    }

    /**
     * API call to get bill details
     */
    private void getBillDetails() {
        mRootLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        Call<InVoiceResponse> aCall = AppClient.get().getBillDetails(mBookingId);
        aCall.enqueue(new Callback<InVoiceResponse>() {
            @Override
            public void onResponse(Call<InVoiceResponse> call, Response<InVoiceResponse> response) {
                try {
                    mProgressBar.setVisibility(View.GONE);
                    if (response.body() != null) {
                        if (response.body().status.equals("success") && response.body().data != null) {
                            mBRetry.setVisibility(View.GONE);
                            refreshData(response.body().data);
                        } else {
                            mBRetry.setVisibility(View.VISIBLE);
                            AppMessage.showToast(response.body().message);
                        }
                    } else {
                        mBRetry.setVisibility(View.VISIBLE);
                        ErrorHandler.getErrorMessage(null, response.errorBody());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<InVoiceResponse> call, Throwable t) {
                try {
                    mBRetry.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    ErrorHandler.getErrorMessage(t, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * API call to end Booking
     */
    private void endBooking() {
        showProgress();
        Map<String, Boolean> aParams = new HashMap<>();
        aParams.put(Constants.KEY_DISCOUNTED, mDiscounted);
        Call<SimpleResponse> aCall = AppClient.get().endBooking(mBookingId, aParams);
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

    /**
     * API call to rate service at the end of the booking
     *
     * @param iDialog
     */
    private void rateService(final AlertDialog iDialog) {
        showProgress();
        Map<String, Integer> aParams = new HashMap<>();
        aParams.put(Constants.KEY_RATING, (int) mRating);
        Call<SimpleResponse> aCall = AppClient.get().rateService(mBookingId, aParams);
        aCall.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                try {
                    hideProgress();
                    if (response.body() != null && response.body().status.equals("success")) {
                        ((DashboardActivity) getActivity()).replaceFragment(new CustomerLoginFragment(), true);
                        iDialog.dismiss();
                    } else {
                        if (response.errorBody() != null) {
                            SimpleResponse aResponse = (SimpleResponse) GsonHelper.getGson(response.errorBody().string(), SimpleResponse.class);
                            AppMessage.showToast(aResponse.message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                try {
                    hideProgress();
                    AppMessage.showToast(t instanceof NoConnectivityException ? t.getMessage() : getString(R.string.error_msg_something_went_wrong));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Shows End booking confirmation dialog and takes user rating
     */
    private void showDialogMessage() {
        mRating = 0;
        AlertDialog aDialog = null;
        AlertDialog.Builder aDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DialogSlideAnim);

        LayoutInflater aInflater = getLayoutInflater();
        View aDialogView = aInflater.inflate(R.layout.layout_booking_ended, null);
        aDialogBuilder.setView(aDialogView).setCancelable(false);
        aDialog = aDialogBuilder.create();
        aDialog.setCanceledOnTouchOutside(false);
        aDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        final TextView aHeader = aDialogView.findViewById(R.id.tvTitle);
        aHeader.setText(R.string.thank_you);
        AppCompatRatingBar aRBService = aDialogView.findViewById(R.id.rbService);

        aRBService.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mRating = (int) rating;
            }
        });
        final Button aDone = aDialogView.findViewById(R.id.bDone);
        final AlertDialog finalADialog = aDialog;
        aDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateRating()) {
                    rateService(finalADialog);
                }
            }
        });

        aDialog.show();
    }

    /**
     * Validates if rating is done
     *
     * @return
     */
    private boolean validateRating() {
        if (mRating == 0) {
            AppMessage.showToast(getString(R.string.error_msg_empty_rating));
            return false;
        }
        return true;
    }

    @Override
    public void onItemClick(Slots iItem, int position) {
        if (iItem != null) {
            mList.remove(mList.size() - 1);
            mAdapter.notifyDataSetChanged();
            mDiscounted = true;
            if ((mInvoice != null && mInvoice.finalBill > 0)) {
                mTVBillDiscount.setText(CurrencyHelper.getFormattedPrice(mInvoice.discount + iItem.amount));
                mTVBillTotal.setText(CurrencyHelper.getFormattedPrice(mInvoice.finalBill - iItem.amount));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bConfirm:
                endBooking();
                break;

            case R.id.bRetry:
                getBillDetails();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.refresh, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            getBillDetails();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).setToolBarTitle(getString(R.string.invoice));
    }
}
