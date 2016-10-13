package com.handsomeyang.mango.base;

import android.app.Application;
import org.androidannotations.annotations.EApplication;

/**
 * Created by HandsomeYang on 2016/9/9.
 */
public abstract class BaseApplication extends Application {
  private static BaseApplication INSTANCE;

  public static BaseApplication getInstance() {
    if (INSTANCE == null) {
      synchronized (String.class) {
        if (INSTANCE == null) {
          return INSTANCE;
        }
      }
    }
    return null;
  }

  @Override public void onCreate() {
    super.onCreate();
    INSTANCE = this;
  }
}
