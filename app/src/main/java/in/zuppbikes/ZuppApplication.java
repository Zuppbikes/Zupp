package in.zuppbikes;

import android.app.Application;
import android.content.Context;

import com.zupp.component.AppHelper;

/**
 * Created by riteshdubey on 3/17/18.
 */

public class ZuppApplication extends Application {
    private static ZuppApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AppHelper.getInstance().initialize(this);
        AppHelper.getInstance().enableDebug(false);
        AppHelper.getInstance().setBuildType(AppHelper.BuildType.PRODUCTION);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static ZuppApplication getInstance() {
        return mInstance;
    }

}
