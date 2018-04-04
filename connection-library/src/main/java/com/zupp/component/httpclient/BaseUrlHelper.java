package com.zupp.component.httpclient;

import com.zupp.component.AppHelper;
import com.zupp.component.config.Config;

/**
 * Created by riteshdubey on 3/7/17.
 */

public final class BaseUrlHelper {

    public static String getBaseUrl() {
        if (AppHelper.getInstance().getBuildType() == AppHelper.BuildType.STAGING) {
            return Config.BASE_URL_STAGING;
        } else {
            return Config.BASE_URL_PROD;
        }
    }
}
