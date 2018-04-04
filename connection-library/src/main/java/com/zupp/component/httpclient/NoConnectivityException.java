package com.zupp.component.httpclient;

import com.zupp.component.AppHelper;
import com.zupp.component.R;

import java.io.IOException;


public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return AppHelper.getInstance().getContext().getString(R.string.no_internet);
    }
}