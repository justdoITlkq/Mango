package com.handsomeyang.mango.utils;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import static com.handsomeyang.mango.utils.ConstantUtils.*;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/8/2
 *     desc  : 正则相关工具类
 * </pre>
 */
public class RegularUtils {

  private RegularUtils() {
    throw new UnsupportedOperationException("u can't fuck me...");
  }

  /**
   * If u want more please visit http://toutiao.com/i6231678548520731137/
   */

  /**
   * 验证手机号（简单）
   *
   * @param string 待验证文本
   * @return {@code true}: 匹配<br>{@code false}: 不匹配
   */
  public static boolean isMobileSimple(String string) {
    return isMatch(REGEX_MOBILE_SIMPLE, string);
  }

  /**
   * 验证手机号（精确）
   *
   * @param string 待验证文本
   * @return {@code true}: 匹配<br>{@code false}: 不匹配
   */
  public static boolean isMobileExact(String string) {
    return isMatch(REGEX_MOBILE_EXACT, string);
  }

  /**
   * 验证电话号码
   *
   * @param string 待验证文本
   * @return {@code true}: 匹配<br>{@code false}: 不匹配
   */
  public static boolean isTel(String string) {
    return isMatch(REGEX_TEL, string);
  }

  /**
   * 验证身份证号码15位
   *
   * @param string 待验证文本
   * @return {@code true}: 匹配<br>{@code false}: 不匹配
   */
  public static boolean isIDCard15(String string) {
    return isMatch(REGEX_IDCARD15, string);
  }

  /**
   * 验证身份证号码18位
   *
   * @param string 待验证文本
   * @return {@code true}: 匹配<br>{@code false}: 不匹配
   */
  public static boolean isIDCard18(String string) {
    return isMatch(REGEX_IDCARD18, string);
  }

  /**
   * 不管15位还是18位身份证都能验证
   */
  public static boolean isIDCard18Or15(String string) {
    if (string.length() == 15) {
      return isMatch(REGEX_IDCARD15, string);
    } else if (string.length() == 18) {
      return isMatch(REGEX_IDCARD18, string);
    }
    return false;
  }

  /**
   * 方法名：parseGender
   * 详述：根据所传身份证号解析其性别
   * 开发人员：souvc
   * 创建时间：2015-9-7 下午1:55:44
   *
   * @return 说明返回值含义
   * @throw 说明发生此异常的条件
   */
  public static String parseGender(String cid) {
    String gender = null;
    char c = cid.charAt(cid.length() - 2);
    int sex = Integer.parseInt(String.valueOf(c));
    if (sex % 2 == 0) {
      gender = "女";
    } else {
      gender = "男";
    }
    return gender;
  }

  /**
   * 方法名：parseAge
   * 详述：根据身份证号码，返回年龄
   * 开发人员：souvc
   * 创建时间：2015-9-7 下午1:56:15
   *
   * @return 说明返回值含义
   * @throw 说明发生此异常的条件
   */
  public static int parseAge(String cid) {
    int age = 0;
    String birthDayStr = cid.substring(6, 14);
    Date birthDay = null;
    try {
      birthDay = new SimpleDateFormat("yyyyMMdd").parse(birthDayStr);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    Calendar cal = Calendar.getInstance();
    if (cal.before(birthDay)) {
      throw new IllegalArgumentException("您还没有出生么？");
    }
    int yearNow = cal.get(Calendar.YEAR);
    int monthNow = cal.get(Calendar.MONTH) + 1;
    int dayNow = cal.get(Calendar.DAY_OF_MONTH);
    cal.setTime(birthDay);
    int yearBirth = cal.get(Calendar.YEAR);
    int monthBirth = cal.get(Calendar.MONTH) + 1;
    int dayBirth = cal.get(Calendar.DAY_OF_MONTH);
    age = yearNow - yearBirth;
    if (monthNow <= monthBirth) {
      if (monthNow == monthBirth && dayNow < dayBirth) {
        age--;
      }
    } else {
      age--;
    }
    return age;
  }

  /**
   * 方法名：parseBirthday
   * 详述：根据身份证号截取出生日期
   * 开发人员：liuhf
   * 创建时间：2015-9-7 下午2:08:20
   *
   * @return 说明返回值含义
   * @throw 说明发生此异常的条件
   */
  public static String parseBirthday(String cid) {
    //通过身份证号来读取出生日期
    String birthday = "";
    //如果没有身份证，那么不进行字符串截取工作。
    if (isIDCard18Or15(cid)) {
      String year = cid.substring(6, 10);
      String month = cid.substring(10, 12);
      String day = cid.substring(12, 14);
      birthday = year + "-" + month + "-" + day;
    }
    return birthday;
  }

  /**
   * 验证邮箱
   *
   * @param string 待验证文本
   * @return {@code true}: 匹配<br>{@code false}: 不匹配
   */
  public static boolean isEmail(String string) {
    return isMatch(REGEX_EMAIL, string);
  }

  /**
   * 验证URL
   *
   * @param string 待验证文本
   * @return {@code true}: 匹配<br>{@code false}: 不匹配
   */
  public static boolean isURL(String string) {
    return isMatch(REGEX_URL, string);
  }

  /**
   * 验证汉字
   *
   * @param string 待验证文本
   * @return {@code true}: 匹配<br>{@code false}: 不匹配
   */
  public static boolean isChz(String string) {
    return isMatch(REGEX_CHZ, string);
  }

  /**
   * 验证用户名
   * <p>取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位</p>
   *
   * @param string 待验证文本
   * @return {@code true}: 匹配<br>{@code false}: 不匹配
   */
  public static boolean isUsername(String string) {
    return isMatch(REGEX_USERNAME, string);
  }

  /**
   * 验证yyyy-MM-dd格式的日期校验，已考虑平闰年
   *
   * @param string 待验证文本
   * @return {@code true}: 匹配<br>{@code false}: 不匹配
   */
  public static boolean isDate(String string) {
    return isMatch(REGEX_DATE, string);
  }

  /**
   * 验证IP地址
   *
   * @param string 待验证文本
   * @return {@code true}: 匹配<br>{@code false}: 不匹配
   */
  public static boolean isIP(String string) {
    return isMatch(REGEX_IP, string);
  }

  /**
   * string是否匹配regex
   *
   * @param regex 正则表达式字符串
   * @param string 要匹配的字符串
   * @return {@code true}: 匹配<br>{@code false}: 不匹配
   */
  public static boolean isMatch(String regex, String string) {
    return !StringUtils.isEmpty(string) && Pattern.matches(regex, string);
  }

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
