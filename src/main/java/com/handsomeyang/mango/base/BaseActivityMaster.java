package com.handsomeyang.mango.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by HandsomeYang on 2016/9/10.
 */

public class BaseActivityMaster {

  private static List<BaseActivity> mBaseActivities = new ArrayList<>();
  private static BaseActivityMaster sMBaseActivityMaster;

  private BaseActivityMaster() {
  }

  public static BaseActivityMaster getInstance() {
    if (sMBaseActivityMaster == null) {
      synchronized (BaseActivityMaster.class) {
        if (sMBaseActivityMaster == null) {
          sMBaseActivityMaster = new BaseActivityMaster();
        }
      }
    }
    return sMBaseActivityMaster;
  }

  public void addToMaster(BaseActivity mBaseActivity) {
    mBaseActivities.add(mBaseActivity);
  }

  public void removeFromMaster(BaseActivity mBaseActivity) {
    mBaseActivities.remove(mBaseActivity);
  }

  public void finishAll() {
    Iterator<BaseActivity> it = mBaseActivities.iterator();
    while (it.hasNext()) {
      it.next().finish();
    }
  }

  public void finishBaseActicity(Class baseActivityClass) {
    Iterator<BaseActivity> iterator = mBaseActivities.iterator();
    while (iterator.hasNext()) {
      BaseActivity baseActivity = iterator.next();
      if (baseActivity.getClass() == baseActivityClass) {
        baseActivity.finish();
      }
    }
  }

  public BaseActivity getActivity(Class baseActivityClass) {
    Iterator<BaseActivity> iterator = mBaseActivities.iterator();
    while (iterator.hasNext()) {
      BaseActivity baseActivity = iterator.next();
      if (baseActivity.getClass() == baseActivityClass) {
        return baseActivity;
      }
    }
    throw new RuntimeException("NO Activiy found!!");
  }
}
