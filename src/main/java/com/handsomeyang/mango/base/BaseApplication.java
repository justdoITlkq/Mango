package com.handsomeyang.mango.base;

import android.app.Application;
import android.util.Log;
import com.blankj.utilcode.utils.Utils;
import com.elvishew.xlog.BuildConfig;
import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.XLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.handsomeyang.mango.output.T;

/**
 * Created by HandsomeYang on 2016/9/9.
 */
public abstract class BaseApplication extends Application {

  private static BaseApplication INSTANCE;

  public static BaseApplication getInstance() {
    return INSTANCE;
  }

  @Override public void onCreate() {
    super.onCreate();

    INSTANCE = this;
    //Fresco  init  + internet permission
    Fresco.initialize(this);
    //output Toast Utils init
    T.init(this);
    //BlankJ  utils  context  https://github.com/Blankj/AndroidUtilCode
    Utils.init(this);
    // init xLog
    initXLog();
  }

  /**
   * init xLog    https://github.com/elvishew/XLog/blob/master/README_ZH.md
   */
  private void initXLog() {

    LogConfiguration configuration = new LogConfiguration.Builder()
        .b()
        //.logLevel(BuildConfig.DEBUG ? LogLevel.ALL: LogLevel.NONE)
        .tag(this.getClass().getSimpleName())
        .t()
        .st(4)
        .build();
    XLog.init(configuration);

    Log.e(this.getClass().getSimpleName(), "android.support.compat.BuildConfig.BUILD_TYPE---> "
        + android.support.compat.BuildConfig.BUILD_TYPE);
    Log.e(this.getClass().getSimpleName(),
        "android.support.compat.BuildConfig.DEBUG---> " + android.support.compat.BuildConfig.DEBUG);

    Log.e(this.getClass().getSimpleName(),
        "com.elvishew.xlog.BuildConfig.BUILD_TYPE-----> " + BuildConfig.BUILD_TYPE);
    Log.e(this.getClass().getSimpleName(),
        "com.elvishew.xlog.BuildConfig.DEBUG ---> " + BuildConfig.DEBUG);
  }
}
