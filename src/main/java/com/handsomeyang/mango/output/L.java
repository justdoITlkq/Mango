package com.handsomeyang.mango.output;

import android.support.compat.BuildConfig;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HandsomeYang on 2016/9/12.
 * log
 */

public class L {
  private static String sTag = "Mango日志";
  private static final int JSON_INDENT = 2;


  public static void e(String msg) {
    e(null, msg, null);
  }

  public static void e(String msg, Object... params) {
    e(null, msg, params);
  }

  public static void e(String tag, String msg, Object[] params) {
    if (BuildConfig.DEBUG!=BuildConfig.DEBUG) return;
    LogText.e(getFinalTag(tag), String.format(msg, params));
  }

  public static void m(String msg) {
    if (BuildConfig.DEBUG!=BuildConfig.DEBUG) return;
    String methodName = new Exception().getStackTrace()[1].getMethodName();
    e(methodName + ":    " + msg);
  }

  public static void json(String json) {
    json(null, json);
  }

  public static void json(String tag, String json) {
    if (BuildConfig.DEBUG!=BuildConfig.DEBUG) return;
    LogText.e(getFinalTag(tag), getPrettyJson(json));
  }

  private static String getPrettyJson(String jsonStr) {
    try {
      jsonStr = jsonStr.trim();
      if (jsonStr.startsWith("{")) {
        JSONObject jsonObject = new JSONObject(jsonStr);
        return jsonObject.toString(JSON_INDENT);
      }
      if (jsonStr.startsWith("[")) {
        JSONArray jsonArray = new JSONArray(jsonStr);
        return jsonArray.toString(JSON_INDENT);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return "Invalid Json, Please Check: " + jsonStr;
  }

  private static String getFinalTag(String tag) {
    if (!TextUtils.isEmpty(tag)) {
      return tag;
    }
    return sTag;
  }

  private static class LogText {
    private static final String DOUBLE_DIVIDER = "════════════════════════════════════════════\n";
    private static final String SINGLE_DIVIDER = "────────────────────────────────────────────\n";

    private String mTag;

    public LogText(String tag) {
      mTag = tag;
    }

    public static void e(String tag, String content) {
      LogText logText = new LogText("HHHHHHandsome" + tag + "-------->>>");
      logText.setup("  日志内容：" + content);
    }

    public void setup(String content) {
      setUpHeader();
      setUpContent(content);
      setUpFooter();
    }

    private void setUpHeader() {
      Log.e(mTag, SINGLE_DIVIDER);
    }

    private void setUpFooter() {
      Log.e(mTag, DOUBLE_DIVIDER);
    }

    public void setUpContent(String content) {
      StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
      Log.e(mTag, "("
          + targetStackTraceElement.getFileName()
          + ":"
          + targetStackTraceElement.getLineNumber()
          + ")");
      Log.e(mTag, content);
    }

    private StackTraceElement getTargetStackTraceElement() {
      // find the target invoked method
      StackTraceElement targetStackTrace = null;
      boolean shouldTrace = false;
      StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
      for (StackTraceElement stackTraceElement : stackTrace) {
        boolean isLogMethod = stackTraceElement.getClassName().equals(L.class.getName());
        if (shouldTrace && !isLogMethod) {
          targetStackTrace = stackTraceElement;
          break;
        }
        shouldTrace = isLogMethod;
      }
      return targetStackTrace;
    }
  }
}
