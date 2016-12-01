package com.handsomeyang.mango.utils;

import java.util.Date;

/**
 * Created by HandsomeYang on 2016/11/29.
 */

public class MangoTimeUtils {
  /**
   * 比较两个日期是否一样，一样返回true，不一样返回false
   * @param d1
   * @param d2
   * @return
   */
  public static boolean isSame(Date d1, Date d2) {
    return d1.compareTo(d2) == 0;
  }
}
