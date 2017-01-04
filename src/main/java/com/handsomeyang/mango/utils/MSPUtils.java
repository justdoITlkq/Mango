package com.handsomeyang.mango.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by HandsomeYang on 2016/9/20.
 * SharedPreferenceHelper created by zhy  don't use apply method is for compatible
 */

public class MSPUtils {
  /**
   * file name
   */
  public static final String FILE_NAME = "share_data";

  /**
   * save data
   */
  public static void put(Context context, String key, Object object) {

    SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();

    if (object instanceof String) {
      editor.putString(key, (String) object);
    } else if (object instanceof Integer) {
      editor.putInt(key, (Integer) object);
    } else if (object instanceof Boolean) {
      editor.putBoolean(key, (Boolean) object);
    } else if (object instanceof Float) {
      editor.putFloat(key, (Float) object);
    } else if (object instanceof Long) {
      editor.putLong(key, (Long) object);
    } else {
      editor.putString(key, object.toString());
    }

    SharedPreferencesCompat.apply(editor);
  }

  /**
   * get  data
   */
  public static Object get(Context context, String key, Object defaultObject) {
    SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

    if (defaultObject instanceof String) {
      return sp.getString(key, (String) defaultObject);
    } else if (defaultObject instanceof Integer) {
      return sp.getInt(key, (Integer) defaultObject);
    } else if (defaultObject instanceof Boolean) {
      return sp.getBoolean(key, (Boolean) defaultObject);
    } else if (defaultObject instanceof Float) {
      return sp.getFloat(key, (Float) defaultObject);
    } else if (defaultObject instanceof Long) {
      return sp.getLong(key, (Long) defaultObject);
    }

    return null;
  }

  /**
   * remove some key and value
   */
  public static void remove(Context context, String key) {
    SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    editor.remove(key);
    SharedPreferencesCompat.apply(editor);
  }

  /**
   * c;ear all data
   */
  public static void clear(Context context) {
    SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    editor.clear();
    SharedPreferencesCompat.apply(editor);
  }

  /**
   * judge key is exited or not
   */
  public static boolean contains(Context context, String key) {
    SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    return sp.contains(key);
  }

  /**
   * return all data
   */
  public static Map<String, ?> getAll(Context context) {
    SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    return sp.getAll();
  }

  /**
   * create SharedPreferencesCompat.apply for compatible
   *
   * @author zhy
   */
  private static class SharedPreferencesCompat {
    private static final Method sApplyMethod = findApplyMethod();

    /**
     * reflect to use apply method
     */
    @SuppressWarnings({ "unchecked", "rawtypes" }) private static Method findApplyMethod() {
      try {
        Class clz = SharedPreferences.Editor.class;
        return clz.getMethod("apply");
      } catch (NoSuchMethodException e) {
      }

      return null;
    }

    /**
     * if use apply  ,else use commit
     */
    public static void apply(SharedPreferences.Editor editor) {
      try {
        if (sApplyMethod != null) {
          sApplyMethod.invoke(editor);
          return;
        }
      } catch (IllegalArgumentException e) {
      } catch (IllegalAccessException e) {
      } catch (InvocationTargetException e) {
      }
      editor.commit();
    }
  }
}
