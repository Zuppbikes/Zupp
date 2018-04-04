package in.zuppbikes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zupp.component.models.Slots;

import java.util.ArrayList;
import java.util.Locale;

import in.zuppbikes.R;
import in.zuppbikes.utils.CurrencyHelper;
import in.zuppbikes.utils.DateUtils;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.SlotHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Slots> mList;
    private OnItemClickListener mListener;
    private int mViewResourceId;
    private boolean mDiscounted;

    public InvoiceAdapter(Context iContext, int iViewResourceId, ArrayList<Slots> iImpacts) {
        mContext = iContext;
        mInflater = LayoutInflater.from(iContext);
        mList = iImpacts;
        mViewResourceId = iViewResourceId;
    }

    @Override
    public SlotHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SlotHolder(mInflater.inflate(mViewResourceId, parent, false));
    }

    @Override
    public void onBindViewHolder(SlotHolder holder, int position) {
        holder.mTVDate.setText(DateUtils.formatSlotDate(mList.get(position).date));
        holder.mTVType.setText(Character.toUpperCase(mList.get(position).type.charAt(0)) + mList.get(position).type.substring(1));
        holder.mTVAmount.setText(mList.get(position).amount > 0 ? String.format(Locale.getDefault(), mContext.getString(R.string.charged_amount), CurrencyHelper.getFormattedPrice(mList.get(position).amount)) : "NIL");
        holder.mClose.setVisibility((!mDiscounted && position == (mList.size() -1)) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class SlotHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTVDate, mTVType, mTVAmount;
        private ImageButton mClose;


        public SlotHolder(View itemView) {
            super(itemView);
            mClose = itemView.findViewById(R.id.ibClose);
            mTVDate = itemView.findViewById(R.id.tvDate);
            mTVType = itemView.findViewById(R.id.tvType);
            mTVAmount = itemView.findViewById(R.id.tvAmount);
            mClose.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mDiscounted = true;
            if (mListener != null) {
                mListener.onItemClick(mList.get(getAdapterPosition()), getAdapterPosition());
            }
        }
    }

    public void setDiscounted(boolean iDiscounted){
        mDiscounted = iDiscounted;
    }

    public interface OnItemClickListener {
        void onItemClick(Slots iItem, int position);
    }

    public void setOnItemClickListener(OnItemClickListener iListener) {
        this.mListener = iListener;
    }
}
