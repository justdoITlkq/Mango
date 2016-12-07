package com.handsomeyang.mango.base;

import android.app.Application;
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

    //Fresco  init  + 联网权限
    Fresco.initialize(this);

    //output Toast Utils init
    T.init(this);
  }
}
