package in.zuppbikes.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.zupp.component.httpclient.AppClient;
import com.zupp.component.models.LoginResponse;
import com.zupp.component.storage.AppUserSession;
import com.zupp.component.utils.Validation;

import in.zuppbikes.R;
import in.zuppbikes.activity.BaseActivity;
import in.zuppbikes.activity.dashboard.DashboardActivity;
import in.zuppbikes.utils.ErrorHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextInputLayout mTILUsername, mTILPassword;
    private AppCompatEditText mACEUsername, mACEPassword;
    private Button mBEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            final Intent aIntent = getIntent();
            final String aIntentAction = aIntent.getAction();
            if (aIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && aIntentAction != null && aIntentAction.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        if (AppUserSession.getInstance().getLoginStatus()) {
            showDashBoard();
        }
        setContentView(R.layout.activity_launcher);
        initView();
    }

    private void showDashBoard() {
        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
        finish();
    }

    private void initView() {
        mBEnter = findViewById(R.id.bEnter);
        mTILUsername = findViewById(R.id.tilUserName);
        mTILPassword = findViewById(R.id.tilPassword);
        mACEUsername = findViewById(R.id.etUserName);
        mACEPassword = findViewById(R.id.etPassword);
        setListeners();
    }

    private void setListeners() {
        mBEnter.setOnClickListener(this);
        mACEUsername.addTextChangedListener(mWatcher);
        mACEPassword.addTextChangedListener(mWatcher);
    }

    TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (getCurrentFocus() != null) {
                if (getCurrentFocus().getId() == R.id.etUserName) {
                    mTILUsername.setErrorEnabled(false);
                } else if (getCurrentFocus().getId() == R.id.etPassword) {
                    mTILPassword.setErrorEnabled(false);
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bEnter:
                if (validate()) {
                    login();
                }
                break;
        }
    }

    private boolean validate() {
        if (mACEUsername.getText().toString().trim().length() == 0) {
            mTILUsername.setErrorEnabled(true);
            mTILUsername.setError(getString(R.string.error_msg_empty_email));
            return false;
        } else if (!Validation.isValidEmail(mACEUsername.getText().toString().trim())) {
            mTILUsername.setErrorEnabled(true);
            mTILUsername.setError(getString(R.string.error_msg_valid_email));
            return false;
        } else if (mACEUsername.getText().toString().trim().length() == 0) {
            mTILPassword.setErrorEnabled(true);
            mTILPassword.setError(getString(R.string.error_msg_empty_password));
            return false;
        } else if (mACEUsername.getText().toString().trim().length() < 4) {
            mTILPassword.setErrorEnabled(true);
            mTILPassword.setError(getString(R.string.error_msg_valid_password_length));
            return false;
        }

        return true;
    }

    private void login() {
        showProgress();
        Call<LoginResponse> aCall = AppClient.get().login(mACEUsername.getText().toString().trim(), mACEPassword.getText().toString().trim());
        aCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                try {
                    hideProgress();
                    if (response.body() != null && response.body().status.equals("success")) {
                        AppUserSession.getInstance().setAccessToken(response.body().data);
                        AppUserSession.getInstance().setLoginStatus(true);
                        showDashBoard();
                    } else {
                        ErrorHandler.getErrorMessage(null, response.errorBody());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                try {
                    hideProgress();
                    ErrorHandler.getErrorMessage(t, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
