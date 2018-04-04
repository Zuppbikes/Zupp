package in.zuppbikes.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zupp.component.AppHelper;
import com.zupp.component.listener.SessionListener;

import in.zuppbikes.R;
import in.zuppbikes.activity.login.LoginActivity;

/**
 * Created by riteshdubey on 3/17/18.
 */

public class BaseActivity extends AppCompatActivity implements SessionListener {
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.please_wait));
        AppHelper.getInstance().setSessionListener(this);
    }

    public void showProgress(){
        mProgressDialog.show();
    }

    public void hideProgress(){
        mProgressDialog.dismiss();
    }

    @Override
    public void onSessionInvalidated() {
        Intent aIntent = new Intent(this, LoginActivity.class);
        aIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        aIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(aIntent);
    }


//    private void login() {
//        showProgress();
//        Call<LoginResponse> aCall = AppClient.get().login(mACEUsername.getText().toString().trim(), mACEPassword.getText().toString().trim());
//        aCall.enqueue(new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                try {
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LoginResponse> call, Throwable t) {
//                try {
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
}
