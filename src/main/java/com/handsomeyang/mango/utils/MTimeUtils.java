package com.handsomeyang.mango.utils;

import java.util.Date;

/**
 * Created by HandsomeYang on 2016/11/29.
 */

public class MTimeUtils {
  /**
   * compare two dates,same return true,else return alse
   */
  public static boolean isSame(Date d1, Date d2) {
    return d1.compareTo(d2) == 0;
  }
}
