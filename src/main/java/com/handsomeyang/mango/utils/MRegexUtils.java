package com.handsomeyang.mango.utils;

import static com.blankj.utilcode.utils.RegexUtils.isMatch;
import static com.handsomeyang.mango.MConstant.REGEX_PASSWORD;

/**
 * Created by HandsomeYang on 2016/11/17.
 * 使用三方工具类中的isMatch方法
 */

public class MRegexUtils {

  /**
   * 验证密码，大小写字母，数字
   *
   * @param pwd 验证的密码
   * @return 是否可用
   */
  public static boolean isPassword(String pwd) {
    return isMatch(REGEX_PASSWORD, pwd);
  }
}
