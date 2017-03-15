package com.cylee.androidlib.base;

import android.app.Application;
import android.content.Context;

import com.cylee.androidlib.net.Net;
import com.cylee.androidlib.util.DirectoryManager;
import com.cylee.androidlib.util.PreferenceUtils;

/**
 * Created by cylee on 2016/6/13.
 */
public class BaseApplication extends Application {
    private static Context mContext;

    public static void init(Context context) {
        mContext = context.getApplicationContext();
        DirectoryManager.init();
        Net.init(mContext);
    }
    public static Context getApplication() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init(getApplicationContext());
    }
}
