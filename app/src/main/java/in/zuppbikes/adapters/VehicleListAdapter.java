package in.zuppbikes.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zupp.component.models.Vehicle;

import java.util.ArrayList;

import in.zuppbikes.R;

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.VehicleHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Vehicle> mList;
    private OnZuppListener mListener;
    private int mViewResourceId;

    public VehicleListAdapter(Context iContext, int iViewResourceId, ArrayList<Vehicle> iImpacts) {
        mContext = iContext;
        mInflater = LayoutInflater.from(iContext);
        mList = iImpacts;
        mViewResourceId = iViewResourceId;
    }

    @Override
    public VehicleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VehicleHolder(mInflater.inflate(mViewResourceId, parent, false));
    }

    @Override
    public void onBindViewHolder(VehicleHolder holder, int position) {
        boolean isAvailable = mList.get(position).status.equals("available");
        holder.mTVLicensePlateNo.setText(mList.get(position).registrationNumber);
        holder.mTVVehicleBrand.setText(mList.get(position).brand);

        holder.mTVVehicleBrand.setTextColor(isAvailable ? ContextCompat.getColor(mContext, R.color.black) : ContextCompat.getColor(mContext, R.color.grey_1));
        holder.mTVLicensePlateNo.setTextColor(isAvailable ? ContextCompat.getColor(mContext, R.color.black) : ContextCompat.getColor(mContext, R.color.grey_1));
        holder.mZuppIt.setEnabled(isAvailable);
        holder.mZuppIt.setText(isAvailable ? mContext.getString(R.string.zupp_it) : mContext.getString(R.string.zupping));
        holder.mTVVehicleBrand.setCompoundDrawablesWithIntrinsicBounds(isAvailable ? R.drawable.dot_1 : R.drawable.dot_2, 0, 0, 0);
        holder.mIVVehicle.setBackgroundResource(isAvailable ? R.drawable.circle_yellow_border : R.drawable.circle_grey_border);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class VehicleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTVLicensePlateNo, mTVVehicleBrand;
        private Button mZuppIt;
        private ImageView mIVVehicle;


        public VehicleHolder(View itemView) {
            super(itemView);
            mIVVehicle = itemView.findViewById(R.id.ivVehicle);
            mZuppIt = itemView.findViewById(R.id.bZuppIt);
            mTVLicensePlateNo = itemView.findViewById(R.id.tvLicensePlateNo);
            mTVVehicleBrand = itemView.findViewById(R.id.tvVehicleBrand);
            mZuppIt.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onZuppClick(mList.get(getAdapterPosition()));
            }
        }
    }

    public interface OnZuppListener {
        void onZuppClick(Vehicle iImpact);
    }

    public void setOnZuppItemClickListener(OnZuppListener iListener) {
        this.mListener = iListener;
    }
}
