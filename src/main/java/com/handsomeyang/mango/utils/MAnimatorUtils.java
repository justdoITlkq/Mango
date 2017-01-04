package com.handsomeyang.mango.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.handsomeyang.mango.R;

public final class MAnimatorUtils {
  /**
   * EditText will shake if edt is null
   */
  public static void shakeshake(Context context, View view) {
    Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
    view.startAnimation(shake);
  }
}
