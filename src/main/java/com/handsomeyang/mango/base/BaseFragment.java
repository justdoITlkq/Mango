package com.handsomeyang.mango.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.handsomeyang.mango.R;
import com.handsomeyang.mango.output.L;

/**
 * Created by HandsomeYang on 2016/9/9.
 */
public abstract class BaseFragment extends Fragment {

  protected BaseActivity mActivity;                      //作为上下文使用
  protected LayoutInflater mLayoutInflater;             //渲染器
  protected FragmentManager mFragmentManager;

  //about titlebar
  protected View mRootView;                            //Fragment 根布局
  private View mTitleBar, mTitleLeft, mTitleCenter, mTitleRight, mTitleRootView;
  private TextView mTvTitleLeft, mTvTitleCenter, mTvTitleRight;
  private ImageView mImageView;
  protected Toolbar mToolbar;

  private boolean useBottombar = false;
  private boolean useTitlebar = false;
  private boolean useToolbar = false;

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    mActivity = (BaseActivity) activity;
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mLayoutInflater= mActivity.getLayoutInflater();

    if (useTitlebar || useToolbar || useBottombar) {
      mRootView = initTileBar(setRootView());
    } else {
      mRootView = mLayoutInflater.inflate(setRootView(), container, false);
      //为了不让app内容占据status bar 和 navbar
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        //mActivity.getWindow().getDecorView().getRootView().setPaddingRelative(0, 60, 0, 100);
      }
    }
    initConfig();
    init();
    initViews();
    return mRootView;
  }

  protected void initConfig() {
  }

  private void init() {
    //沉浸式标题栏   去掉状态栏  可以兼容4.4以上系统
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
      View decorView = mActivity.getWindow().getDecorView();
      int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
      decorView.setSystemUiVisibility(option);
      mActivity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
      WindowManager.LayoutParams localLayoutParams = mActivity.getWindow().getAttributes();
      localLayoutParams.flags =
          (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
    }

    L.e(this.getClass().getSimpleName() + "  Oncreate  .....");
    //初始化
    mFragmentManager = getFragmentManager();
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }

  /**
   * 初始化标题栏
   *
   * @param layoutResID 根布局
   * @return 添加标题栏后的根布局
   */

  private View initTileBar(int layoutResID) {
    RelativeLayout mRelativeLayout = new RelativeLayout(mActivity);
    ViewGroup.LayoutParams layoutParams =
        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
    mRelativeLayout.setLayoutParams(layoutParams);

    mTitleBar = mLayoutInflater.inflate(R.layout.titlebar, mRelativeLayout, false);

    RelativeLayout.LayoutParams mTitleBarLayoutParams =
        (RelativeLayout.LayoutParams) mTitleBar.getLayoutParams();
    mTitleBarLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    mTitleBar.setLayoutParams(mTitleBarLayoutParams);

    mTitleBar.setVisibility(View.GONE);
    //titlebar rootView
    mTitleRootView = mTitleBar.findViewById(R.id.title_bar_default);
    //普通标题栏
    if (useTitlebar) {
      try {
        mTitleLeft = mTitleBar.findViewById(R.id.title_left);
      } catch (Exception e) {
        L.e("left title doesn't exit");
      }

      try {
        mTvTitleLeft = (TextView) mTitleBar.findViewById(R.id.tv_title_left);
      } catch (Exception e) {
        L.e("left title textview doesn't exit");
      }

      try {
        mTitleCenter = mTitleBar.findViewById(R.id.title_center);
      } catch (Exception e) {
        L.e("center title  doesn't exit");
      }

      try {
        mTvTitleCenter = (TextView) mTitleBar.findViewById(R.id.tv_title_center);
      } catch (Exception e) {
        L.e("center title textview doesn't exit");
      }
      try {
        mTitleRight = mTitleBar.findViewById(R.id.title_right);
      } catch (Exception e) {
        L.e("right title  doesn't exit");
      }
      try {
        mTvTitleRight = (TextView) mTitleBar.findViewById(R.id.tv_title_right);
      } catch (Exception e) {
        L.e("right title textview doesn't exit");
      }
      try {
        mImageView = (ImageView) mTitleBar.findViewById(R.id.img_title_right);
      } catch (Exception e) {
        L.e("title right img  doesn't exit");
      }
    } else if (useToolbar) {
      //普通toolbar
      L.e("执行到toolbar 模式了");
      //默认都是显示toolbar的，隐藏的话需要手动调用hideToolbar的方法
      mTitleBar.setVisibility(View.VISIBLE);
      mTitleRootView.setVisibility(View.GONE);
      mToolbar = (Toolbar) mTitleBar;
    }

    View rootView = mLayoutInflater.inflate(layoutResID, mRelativeLayout, false);

    RelativeLayout.LayoutParams rootViewLayoutParams =
        (RelativeLayout.LayoutParams) rootView.getLayoutParams();
    rootViewLayoutParams.addRule(RelativeLayout.BELOW, R.id.titlbar);
    rootViewLayoutParams.addRule(RelativeLayout.ABOVE, R.id.bottombar);
    rootView.setLayoutParams(rootViewLayoutParams);

    mRelativeLayout.addView(mTitleBar);
    mRelativeLayout.addView(rootView);

    if (useBottombar) {
      View bottombar = mLayoutInflater.inflate(R.layout.bottombar, mRelativeLayout, false);
      RelativeLayout.LayoutParams bottombarLayoutParams =
          (RelativeLayout.LayoutParams) bottombar.getLayoutParams();
      bottombarLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      bottombar.setLayoutParams(bottombarLayoutParams);
      mRelativeLayout.addView(bottombar);
    }

    mTitleBar.requestFocus();
    //fragment 根布局
    return mRelativeLayout;
  }

  /**
   * 设置左边部分标题栏
   *
   * @param text 标题文字
   * @param onClickListener 监听
   */
  public void setTitleLeft(String text, View.OnClickListener onClickListener) {
    if (!useTitlebar) {
      return;
    }
    if (mTitleLeft != null) {
      mTitleLeft.setVisibility(View.VISIBLE);
    }
    if (onClickListener != null && mTitleLeft != null) {
      mTitleLeft.setOnClickListener(onClickListener);
    }
    if (text != null && mTvTitleLeft != null) {
      mTvTitleLeft.setText(text);
      if (onClickListener != null && mTitleLeft == null) {
        mTvTitleLeft.setOnClickListener(onClickListener);
      }
    }
  }

  /**
   * 设置默认左侧标题栏
   * 默认监听销毁自己
   * 默认image 是 返回箭头：  <
   */
  public void setTitleLeft() {
    if (!useTitlebar) {
      return;
    }
    if (mTitleLeft != null) {
      mTitleLeft.setVisibility(View.VISIBLE);
      mTitleLeft.setOnClickListener(view -> BaseFragment.this.killSelf());
    }
  }

  /**
   * 中间标题栏部分
   *
   * @param text 标题文字
   */
  public void setTitleCenter(String text) {
    if (!useTitlebar) {
      return;
    }
    if (mTitleBar != null) {
      mTitleBar.setVisibility(View.VISIBLE);
    }
    if (text != null && mTvTitleCenter != null) {
      mTvTitleCenter.setText(text);
    }
  }

  /**
   * 右侧部分标题栏
   *
   * @param text 标题文字
   * @param onClickListener 监听
   */
  public void setTitleRight(String text, View.OnClickListener onClickListener) {
    if (!useTitlebar) {
      return;
    }
    if (mTitleRight != null) {
      mTitleRight.setVisibility(View.VISIBLE);
    }

    if (onClickListener != null && mTitleRight != null) {
      mTitleRight.setOnClickListener(onClickListener);
    }
    if (text != null && mTvTitleRight != null) {
      mTvTitleRight.setText(text);
      if (onClickListener != null && mTitleRight == null) {
        mTvTitleRight.setOnClickListener(onClickListener);
      }
    }
  }

  /**
   * 设置右边标题栏image和监听
   *
   * @param ImgID image 资源id
   * @param onClickListener 监听
   */
  public void setTitleRight(int ImgID, View.OnClickListener onClickListener) {
    if (!useTitlebar) {
      return;
    }
    if (mTitleRight != null) {
      mTitleRight.setVisibility(View.VISIBLE);
    }

    if (onClickListener != null && mTitleRight != null) {
      mTitleRight.setOnClickListener(onClickListener);
    }
    if (ImgID != 0 && mTvTitleRight != null) {
      mImageView.setImageResource(ImgID);
      if (onClickListener != null && mTitleRight == null) {
        mTvTitleRight.setOnClickListener(onClickListener);
      }
    }
  }

  protected void setUseTitlebar() {
    useTitlebar = !useToolbar;
  }

  protected void setUseToolbar() {
    useToolbar = !useTitlebar;
  }

  protected void setUseBottombar() {
    useBottombar = true;
  }

  /**
   * 如果startActivity有数据就返回true，如果没数据就返回false
   */
  public boolean start_Activity(Class activity, Bundle bundle) {
    Intent mIntent = new Intent(mActivity, activity);
    mIntent.putExtra("bundle", bundle);
    startActivity(mIntent);
    if (bundle == null) {
      return false;
    }
    return true;
  }
  //--------------------------------------------------

  /**
   * 封装添加fragment,如果没有add就add上来，如果已经add就替换原有的fragment
   *
   * @param targetFragment 目标fragment
   * @param ResID 要添加到activity的位置
   */
  public void addOrReplace_Fragment(BaseFragment targetFragment, int ResID) {
    if (targetFragment == null) {
      mFragmentManager.beginTransaction()
          .add(ResID, targetFragment)
          .addToBackStack(targetFragment.getClass().getSimpleName())
          .commitAllowingStateLoss();
    } else {
      mFragmentManager.beginTransaction()
          .replace(ResID, targetFragment, targetFragment.getClass().getSimpleName())
          .addToBackStack(targetFragment.getClass().getSimpleName())
          .commitAllowingStateLoss();
    }
  }

  /**
   * 如果fragment 栈中数量大于1，就移除最顶层fragment
   */
  public void remove_Fragment() {
    if (mFragmentManager.getBackStackEntryCount() > 1) {
      mFragmentManager.popBackStack();
    } else {
      killSelf();
    }
  }

  /**
   * showframgent
   */
  public void show_Fragment(BaseFragment targetFragment) {
    mFragmentManager.beginTransaction()
        .show(targetFragment)
        .addToBackStack(targetFragment.getClass().getSimpleName())
        .commitAllowingStateLoss();
  }

  /**
   * 销毁自己  就是移除
   */
  public void killSelf() {
    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
    fragmentTransaction.remove(this);
    fragmentTransaction.commit();
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }

  //容器id
  public abstract int setRootView();

  protected abstract void initViews();
}
