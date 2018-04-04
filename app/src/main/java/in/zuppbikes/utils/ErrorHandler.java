package in.zuppbikes.utils;

import android.text.TextUtils;

import com.zupp.component.AppHelper;
import com.zupp.component.httpclient.GsonHelper;
import com.zupp.component.httpclient.NoConnectivityException;
import com.zupp.component.httpclient.UnAuthorizedException;
import com.zupp.component.models.GenericResponse;

import in.zuppbikes.R;
import okhttp3.ResponseBody;

/**
 * Created by riteshdubey on 3/20/18.
 */

public class ErrorHandler {

    public static void getErrorMessage(Throwable t, ResponseBody response) {
        String aErrorMsg = AppHelper.getInstance().getContext().getString(R.string.error_msg_something_went_wrong);
        try {
            if (t != null && t instanceof NoConnectivityException) {
                aErrorMsg = t.getMessage();
            } else if (t != null && t instanceof UnAuthorizedException) {
                aErrorMsg = t.getMessage();
            } else if (response != null) {
                GenericResponse aResponse = (GenericResponse) GsonHelper.getGson(response.string(), GenericResponse.class);
                if (!TextUtils.isEmpty(aResponse.message)) {
                    aErrorMsg = aResponse.message;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        AppMessage.showToast(aErrorMsg);
    }

    public String getErrorMessageWithoutToast(Throwable t, ResponseBody response) {
        String aErrorMsg = AppHelper.getInstance().getContext().getString(R.string.error_msg_something_went_wrong);
        try {
            if (t != null && t instanceof NoConnectivityException) {
                aErrorMsg = t.getMessage();
            } else if (t != null && t instanceof UnAuthorizedException) {
                aErrorMsg = t.getMessage();
            } else if (response != null) {
                GenericResponse aResponse = (GenericResponse) GsonHelper.getGson(response.string(), GenericResponse.class);
                if (!TextUtils.isEmpty(aResponse.message)) {
                    aErrorMsg = aResponse.message;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aErrorMsg;
    }

//    public static String getErrorMessageWithoutToast(Throwable t, GenericResponse response) {
//        String aErrorMsg = AppHelper.getInstance().getContext().getString(R.string.error_msg_something_went_wrong);
//        try {
//            if (t != null && t instanceof NoConnectivityException) {
//                aErrorMsg = t.getMessage();
//            } else if (t != null && t instanceof UnAuthorizedException) {
//                aErrorMsg = t.getMessage();
//            } else if (response != null && !response.status.equals(Constants.SUCCESS)) {
//                if (!TextUtils.isEmpty(response.errorReason)) {
//                    aErrorMsg = response.errorReason;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return aErrorMsg;
//    }
}
