package in.zuppbikes.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import in.zuppbikes.R;


public class BaseFragment extends Fragment {
    private ProgressDialog mProgressDialog;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.please_wait));
    }

    public void showProgress(){
        mProgressDialog.show();
    }

    public void hideProgress(){
        mProgressDialog.dismiss();
    }

}
