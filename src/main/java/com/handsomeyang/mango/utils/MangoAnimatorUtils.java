package com.handsomeyang.mango.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.handsomeyang.mango.R;

public final class MangoAnimatorUtils {
  /**
   * EditText 没有填写的时候会左右循环抖动
   */
  public static void shakeshake(Context context, View view) {
    Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
    view.startAnimation(shake);
  }
}
