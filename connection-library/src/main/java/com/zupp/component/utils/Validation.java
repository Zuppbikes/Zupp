package com.zupp.component.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by riteshdubey on 1/24/18.
 */

public class Validation {

    private static final String mCoDomain = "headstart.in";
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + Pattern.quote(mCoDomain) + "$";

    public static boolean isValidEmail(CharSequence iTarget) {
        return !TextUtils.isEmpty(iTarget) && android.util.Patterns.EMAIL_ADDRESS.matcher(iTarget).matches();
    }

    public static boolean isValidDomain(CharSequence iTarget) {
        return !TextUtils.isEmpty(iTarget) && Pattern.matches(EMAIL_PATTERN, iTarget);
    }
}
