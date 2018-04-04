package in.zuppbikes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zupp.component.models.Invoice;

import java.util.ArrayList;

import in.zuppbikes.R;
import in.zuppbikes.constants.Constants;
import in.zuppbikes.utils.DateUtils;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.SlotHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Invoice> mList;
    private OnItemClickListener mListener;
    private int mViewResourceId;

    public BillAdapter(Context iContext, int iViewResourceId, ArrayList<Invoice> iList) {
        mContext = iContext;
        mInflater = LayoutInflater.from(iContext);
        mList = iList;
        mViewResourceId = iViewResourceId;
    }

    @Override
    public SlotHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SlotHolder(mInflater.inflate(mViewResourceId, parent, false));
    }

    @Override
    public void onBindViewHolder(SlotHolder holder, int position) {
        holder.mTVName.setText((mList.get(position).customerId != null ? (mList.get(position).customerId.name + "(" + mList.get(position).customerId.phoneNumber + ")") : ""));
        holder.mTVVehicle.setText(mList.get(position).vehicleId != null ? mList.get(position).vehicleId.registrationNumber : "");
        holder.mTVStartDate.setText(DateUtils.formatFromServer(mList.get(position).timeOfRent));
        holder.mTVEndDate.setText(mList.get(position).status.equals(Constants.STATUS_COMPLETED) ? DateUtils.formatFromServer(mList.get(position).actualTimeOfReturn) : DateUtils.formatFromServer(mList.get(position).expectedTimeOfReturn));
        holder.mTVEndDateLabel.setText(mList.get(position).status.equals(Constants.STATUS_COMPLETED) ? mContext.getString(R.string.end_date_adapter) : mContext.getString(R.string.expected_end_date_adapter));
        holder.mTVStatus.setText(mList.get(position).status.equals(Constants.STATUS_ACTIVE) ? mContext.getString(R.string.zupping) : (Character.toUpperCase(mList.get(position).status.charAt(0)) + mList.get(position).status.substring(1)));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class SlotHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTVName, mTVVehicle, mTVStartDate, mTVEndDate, mTVStatus, mTVEndDateLabel;


        public SlotHolder(View itemView) {
            super(itemView);
            mTVName = itemView.findViewById(R.id.tvName);
            mTVVehicle = itemView.findViewById(R.id.tvVehicle);
            mTVStartDate = itemView.findViewById(R.id.tvStartDate);
            mTVEndDate = itemView.findViewById(R.id.tvEndDate);
            mTVEndDateLabel = itemView.findViewById(R.id.tvEndDateLabel);
            mTVStatus = itemView.findViewById(R.id.tvStatus);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(mList.get(getAdapterPosition()), getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Invoice iItem, int position);
    }

    public void setOnItemClickListener(OnItemClickListener iListener) {
        this.mListener = iListener;
    }
}
