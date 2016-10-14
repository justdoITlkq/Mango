package com.handsomeyang.mango.base;

import android.app.Application;

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
  }
}
