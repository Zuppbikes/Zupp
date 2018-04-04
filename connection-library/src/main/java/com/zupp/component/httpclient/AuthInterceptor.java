package com.zupp.component.httpclient;

import android.text.TextUtils;

import com.zupp.component.AppHelper;
import com.zupp.component.constants.Constants;
import com.zupp.component.session.AppSession;
import com.zupp.component.utils.NetworkUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!NetworkUtil.isOnline()) {
            throw new NoConnectivityException();
        }
//        Request original = chain.request();
        // Request customization: add request headers
        Request.Builder requestBuilder = chain.request().newBuilder().header("Accept", "application/json")
                .method(chain.request().method(), chain.request().body());
        String aAccessToken = AppSession.getInstance().getAccessToken();
        if (!TextUtils.isEmpty(aAccessToken) && chain.request().url().toString().contains(BaseUrlHelper.getBaseUrl()) && !chain.request().url().toString().contains(EndPoint.LOGIN)) {
            requestBuilder.addHeader(Constants.AUTHORIZATION_HEADER_NAME, Constants.AUTHORIZATION_TOKEN_PREFIX + aAccessToken);
        }
        Response response = chain.proceed(requestBuilder.build());
        boolean unAuthorized = ((response.code() == 401 || response.code() == 417) && !chain.request().url().toString().contains(EndPoint.LOGIN));
        if (unAuthorized) {
            try {
                clearCredentials();
                if (AppHelper.getInstance().getSessionListener() != null) {
                    AppHelper.getInstance().getSessionListener().onSessionInvalidated();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return response;
    }

    private static void clearCredentials() {
        AppSession.getInstance().clearSession();
    }
}