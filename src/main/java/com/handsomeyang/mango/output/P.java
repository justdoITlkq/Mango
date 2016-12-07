package com.handsomeyang.mango.output;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.blankj.utilcode.utils.ConvertUtils;
import com.handsomeyang.mango.R;
import com.handsomeyang.mango.thrid.jumpingbeans.JumpingBeans;
import com.handsomeyang.mango.thrid.smoothprogressbar.circular.CircularProgressDrawable;
import com.handsomeyang.mango.thrid.smoothprogressbar.horizontal.SmoothProgressDrawable;
import com.handsomeyang.mango.utils.MangoRandomColorUtils;

/**
 * Created by HandsomeYang on 2016/9/14.
 * md loading progressbar   circular/horizontal_Wave
 * 这个封装的并不是很好，还有很大优化空间，暂时先如此
 */

public class P {
  private static Dialog mDialog_C, mDialog_H;

  /**
   * smoothprogressbar circular dialog
   * 弹出的时候面板上的控件是点不了的，因为焦点在dialog上
   */
  public static void circular_Normal(Context context) {
    //progressbar 放在dialog上
    mDialog_C = new Dialog(context);
    //去掉标题
    mDialog_C.requestWindowFeature(Window.FEATURE_NO_TITLE);
    //让dialog 背景消失
    mDialog_C.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    //设置周围暗色指数    1为全黑
    mDialog_C.getWindow().setDimAmount(0f);
    //设置进入动画    淡入淡出
    //mDialog_C.getWindow().setWindowAnimations(R.style.ProgressBarDialogWindowAnim_fade);
    //点击外部不能取消
    mDialog_C.setCanceledOnTouchOutside(false);
    mDialog_C.setContentView(R.layout.dialog_smooth_progressbar_circular);
    mDialog_C.show();
  }

  public static void circular_Interpolator(Context context) {
    //progressbar 放在dialog上
    mDialog_C = new Dialog(context);
    //去掉标题
    mDialog_C.requestWindowFeature(Window.FEATURE_NO_TITLE);
    //让dialog 背景消失
    mDialog_C.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    //设置周围暗色指数    1为全黑
    mDialog_C.getWindow().setDimAmount(0f);
    //设置进入动画    淡入淡出
    //mDialog_C.getWindow().setWindowAnimations(R.style.ProgressBarDialogWindowAnim_fade);
    //点击外部不能取消
    mDialog_C.setCanceledOnTouchOutside(false);
    //View mInflate =
    //    LayoutInflater.from(context).inflate(R.layout.dialog_smooth_progressbar_circular, null);
    ProgressBar mProgressBar = new ProgressBar(context);
    CircularProgressDrawable mCircularProgressDrawable =
        new CircularProgressDrawable.Builder(context).angleInterpolator(
            new FastOutLinearInInterpolator())
            //设置旋转插值器
            .sweepInterpolator(new OvershootInterpolator())
            //设置style
            .style(CircularProgressDrawable.STYLE_ROUNDED)
            //环形半径宽度
            .strokeWidth(5.0f)
            //设置颜色
            .colors(context.getResources().getIntArray(R.array.mycolors))
            //弧形旋转速度
            .sweepSpeed(1.5f)
            //旋转速度
            .rotationSpeed(1.5f).build();
    mProgressBar.setIndeterminateDrawable(mCircularProgressDrawable);
    mDialog_C.setContentView(mProgressBar);
    mDialog_C.show();
  }

  public static void cricular_Interpolator_RandomColor(Context context) {
    //progressbar 放在dialog上
    mDialog_C = new Dialog(context);
    //去掉标题
    mDialog_C.requestWindowFeature(Window.FEATURE_NO_TITLE);
    //让dialog 背景消失
    mDialog_C.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    //设置周围暗色指数    1为全黑
    mDialog_C.getWindow().setDimAmount(0f);
    //设置进入动画    淡入淡出
    //mDialog_C.getWindow().setWindowAnimations(R.style.ProgressBarDialogWindowAnim_fade);
    //点击外部不能取消
    mDialog_C.setCanceledOnTouchOutside(false);
    //View mInflate =
    //    LayoutInflater.from(context).inflate(R.layout.dialog_smooth_progressbar_circular, null);
    ProgressBar mProgressBar = new ProgressBar(context);
    CircularProgressDrawable mCircularProgressDrawable =
        new CircularProgressDrawable.Builder(context).angleInterpolator(
            new FastOutLinearInInterpolator())
            //设置旋转插值器
            .sweepInterpolator(new OvershootInterpolator())
            //设置style
            .style(CircularProgressDrawable.STYLE_ROUNDED)
            //环形半径宽度
            .strokeWidth(20.0f)
            //设置颜色
            .colors(MangoRandomColorUtils.randomColor(10))
            //弧形旋转速度
            .sweepSpeed(1.2f)
            //旋转速度
            .rotationSpeed(1.5f).build();
    mProgressBar.setIndeterminateDrawable(mCircularProgressDrawable);
    mDialog_C.setContentView(mProgressBar);
    mDialog_C.show();
  }

  /**
   * 销毁dialog
   */
  public static void dismiss() {
    if (mDialog_C != null) {
      mDialog_C.dismiss();
    }
    if (mDialog_H != null) {
      mDialog_H.dismiss();
    }
  }

  /**
   * 自定义横向 progressdialog
   * smoothprogressbar horizontal_Wave dialog
   */
  public static void horizontal_Wave(Context context) {
    mDialog_H = new Dialog(context);
    mDialog_H.requestWindowFeature(Window.FEATURE_NO_TITLE);
    View rootView =
        LayoutInflater.from(context).inflate(R.layout.dialog_smooth_progressbar_horizontal, null);
    //inerpolator------------------------------------------------------
    ProgressBar mSmoothProgressBar =
        (ProgressBar) rootView.findViewById(R.id.smooth_progressbar_horizontal);

    SmoothProgressDrawable mSmoothProgressDrawable =
        new SmoothProgressDrawable.Builder(context).sectionsCount(3)
            .separatorLength(10)
            .strokeWidth(25f)
            .gradients()
            .colors(context.getResources().getIntArray(R.array.mycolors))
            .generateBackgroundUsingColors()
            .interpolator(new FastOutLinearInInterpolator())
            .progressiveStart(true)
            .progressiveStartSpeed(2.0f)
            .progressiveStopSpeed(1.0f)
            .speed(2f)
            .build();
    mSmoothProgressBar.setIndeterminateDrawable(mSmoothProgressDrawable);
    //------------------------------------------------------
    TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title_dialog);
    tvTitle.setVisibility(View.GONE);
    mDialog_H.setContentView(rootView);
    mDialog_H.show();
  }

  /**
   * 横向进度条，没有dialog背景暗色
   */
  public static void horizontal_NoDigBg(Context context) {
    mDialog_H = new Dialog(context);
    mDialog_H.requestWindowFeature(Window.FEATURE_NO_TITLE);
    View rootView =
        LayoutInflater.from(context).inflate(R.layout.dialog_smooth_progressbar_horizontal, null);
    //inerpolator------------------------------------------------------
    ProgressBar mSmoothProgressBar =
        (ProgressBar) rootView.findViewById(R.id.smooth_progressbar_horizontal);

    SmoothProgressDrawable mSmoothProgressDrawable =
        new SmoothProgressDrawable.Builder(context).sectionsCount(4)
            .separatorLength(25)
            .strokeWidth(35f)
            .colors(context.getResources().getIntArray(R.array.mycolors))
            .interpolator(new AccelerateDecelerateInterpolator())
            .progressiveStart(true)
            .progressiveStartSpeed(1.5f)
            .progressiveStopSpeed(0.7f)
            .speed(1.5f)
            .build();
    mSmoothProgressBar.setIndeterminateDrawable(mSmoothProgressDrawable);

    //让进度条变长
    ViewGroup.LayoutParams layoutParamsPro = mSmoothProgressBar.getLayoutParams();
    layoutParamsPro.width = ConvertUtils.dp2px(context, 250);
    mSmoothProgressBar.setLayoutParams(layoutParamsPro);

    //------------------------------------------------------
    TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title_dialog);
    tvTitle.setVisibility(View.GONE);

    //让dialog 背景消失
    //mDialog_H.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    //设置周围暗色指数    1为全黑   0为全透明
    mDialog_H.getWindow().setDimAmount(0f);

    WindowManager.LayoutParams layoutParams = mDialog_H.getWindow().getAttributes();
    layoutParams.y = 100;
    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    mDialog_H.getWindow().setAttributes(layoutParams);

    mDialog_H.setCancelable(false);
    mDialog_H.setContentView(rootView);
    mDialog_H.show();
  }

  /**
   * 横向进度条，没有dialog背景暗色
   */
  public static void horizontal_RandomColor(Context context) {
    mDialog_H = new Dialog(context);
    mDialog_H.requestWindowFeature(Window.FEATURE_NO_TITLE);
    View rootView =
        LayoutInflater.from(context).inflate(R.layout.dialog_smooth_progressbar_horizontal, null);
    //inerpolator------------------------------------------------------
    ProgressBar mSmoothProgressBar =
        (ProgressBar) rootView.findViewById(R.id.smooth_progressbar_horizontal);

    SmoothProgressDrawable mSmoothProgressDrawable =
        new SmoothProgressDrawable.Builder(context).sectionsCount(4)
            .separatorLength(25)
            .strokeWidth(35f)
            .colors(MangoRandomColorUtils.randomColor(20))
            .interpolator(new AccelerateDecelerateInterpolator())
            .progressiveStart(true)
            .progressiveStartSpeed(1.5f)
            .progressiveStopSpeed(0.7f)
            .speed(1.5f)
            .build();
    mSmoothProgressBar.setIndeterminateDrawable(mSmoothProgressDrawable);

    //让进度条变长
    ViewGroup.LayoutParams layoutParamsPro = mSmoothProgressBar.getLayoutParams();
    layoutParamsPro.width = ConvertUtils.dp2px(context, 250);
    mSmoothProgressBar.setLayoutParams(layoutParamsPro);

    //------------------------------------------------------
    TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title_dialog);
    tvTitle.setVisibility(View.GONE);

    //让dialog 背景消失
    //mDialog_H.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    //设置周围暗色指数    1为全黑   0为全透明
    mDialog_H.getWindow().setDimAmount(0f);

    WindowManager.LayoutParams layoutParams = mDialog_H.getWindow().getAttributes();
    layoutParams.y = 100;
    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    mDialog_H.getWindow().setAttributes(layoutParams);

    mDialog_H.setCancelable(false);
    mDialog_H.setContentView(rootView);
    mDialog_H.show();
  }

  /**
   * 小尾巴能抖动，同时文字也可以上下跳跃
   */
  public static void horizontal_Wave(Context context, String title, int startPos, int endPos) {
    Dialog mDialog_H = new Dialog(context);
    mDialog_H.requestWindowFeature(Window.FEATURE_NO_TITLE);
    View rootView =
        LayoutInflater.from(context).inflate(R.layout.dialog_smooth_progressbar_horizontal, null);
    //inerpolator------------------------------------------------------
    ProgressBar mSmoothProgressBar =
        (ProgressBar) rootView.findViewById(R.id.smooth_progressbar_horizontal);

    SmoothProgressDrawable mSmoothProgressDrawable =
        new SmoothProgressDrawable.Builder(context).sectionsCount(3)
            .separatorLength(10)
            .strokeWidth(25f)
            .gradients()
            .colors(context.getResources().getIntArray(R.array.mycolors))
            .generateBackgroundUsingColors()
            .interpolator(new FastOutLinearInInterpolator())
            .progressiveStart(true)
            .progressiveStartSpeed(2.0f)
            .progressiveStopSpeed(1.0f)
            .speed(2f)
            .build();
    mSmoothProgressBar.setIndeterminateDrawable(mSmoothProgressDrawable);
    //------------------------------------------------------
    TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title_dialog);
    tvTitle.setText(title);
    //小尾巴不停抖动
    JumpingBeans.with(tvTitle).appendJumpingDots().build();
    //文字跳跃
    JumpingBeans.with(tvTitle).makeTextJump(startPos, endPos).
        setIsWave(true).
        setLoopDuration(1000).
        build();
    mDialog_H.setContentView(rootView);
    mDialog_H.show();
  }

  /**
   * 小尾巴在抖动
   */
  public static void horizontal_Wave(Context context, String title) {
    Dialog mDialog_H = new Dialog(context);
    mDialog_H.requestWindowFeature(Window.FEATURE_NO_TITLE);
    View rootView =
        LayoutInflater.from(context).inflate(R.layout.dialog_smooth_progressbar_horizontal, null);
    //inerpolator------------------------------------------------------
    ProgressBar mSmoothProgressBar =
        (ProgressBar) rootView.findViewById(R.id.smooth_progressbar_horizontal);

    SmoothProgressDrawable mSmoothProgressDrawable =
        new SmoothProgressDrawable.Builder(context).sectionsCount(3)
            .separatorLength(10)
            .strokeWidth(25f)
            .gradients()
            .colors(context.getResources().getIntArray(R.array.mycolors))
            .generateBackgroundUsingColors()
            .interpolator(new FastOutLinearInInterpolator())
            .progressiveStart(true)
            .progressiveStartSpeed(2.0f)
            .progressiveStopSpeed(1.0f)
            .speed(2f)
            .build();
    mSmoothProgressBar.setIndeterminateDrawable(mSmoothProgressDrawable);
    //------------------------------------------------------
    TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title_dialog);
    tvTitle.setText(title);
    //小尾巴不停抖动
    JumpingBeans.with(tvTitle).appendJumpingDots().build();
    mDialog_H.setContentView(rootView);
    mDialog_H.show();
  }
}
