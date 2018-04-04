package in.zuppbikes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.zuppbikes.R;
import in.zuppbikes.models.Navigation;


public class NavigationOptionsAdapter extends RecyclerView.Adapter<NavigationOptionsAdapter.OptionHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Navigation> mList;
    private OnItemClickListener mListener;
    private int mViewResourceId;

    public NavigationOptionsAdapter(Context iContext, int iViewResourceId, ArrayList<Navigation> iList) {
        mContext = iContext;
        mInflater = LayoutInflater.from(iContext);
        mList = iList;
        mViewResourceId = iViewResourceId;
    }

    @Override
    public OptionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OptionHolder(mInflater.inflate(mViewResourceId, parent, false));
    }

    @Override
    public void onBindViewHolder(OptionHolder holder, int position) {
        holder.mTVOption.setText(mList.get(position).name);
        holder.mTVOption.setCompoundDrawablesWithIntrinsicBounds(mList.get(position).drawable, 0, 0, 0);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class OptionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTVOption;


        public OptionHolder(View itemView) {
            super(itemView);
            mTVOption = itemView.findViewById(R.id.tvOption);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(mList.get(getAdapterPosition()));
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Navigation iItem);
    }

    public void setOnItemClickListener(OnItemClickListener iListener) {
        this.mListener = iListener;
    }
}
