package com.handsomeyang.mango.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import android.widget.Toast;
import com.handsomeyang.mango.R;
import com.handsomeyang.mango.output.L;

/**
 * Created by HandsomeYang on 2016/9/9.
 * 封装标题栏，安全退出，上下文等常用属性赋值，snakebar，floatButton，toolbar
 * 转场动画，跳转，携带数据
 * 沉浸式标题栏，兼容4.4
 * 继承appcompatActivity是为了使Toolbar兼容的
 * 开发要注意向md风格靠近
 */
public abstract class BaseActivity extends FragmentActivity {
  protected BaseActivity mContext;
  protected LayoutInflater mLayoutInflater;
  protected FragmentManager mFragmentManager;

  protected BaseActivityMaster mBaseActivityMaster;
  private View mTitleBar, mTitleLeft, mTitleCenter, mTitleRight, mTitleRootView;
  private TextView mTvTitleLeft, mTvTitleCenter, mTvTitleRight;
  private ImageView mImageView;
  protected Toolbar mToolbar;                         //开放toolbar出去

  private long mTimeBegin;
  private boolean mSafeExit;
  private String mSafeText;

  private boolean useBottombar = false;
  private boolean useTitlebar = false;
  private boolean useToolbar = false;

  private View mDecorView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    init();
    super.onCreate(savedInstanceState);
  }

  /**
   * 一些初始化赋值操作  如果不希望布局中的内容拉到状态栏上的话就在xml最外层+fitSystemWindow=true
   */
  private void init() {
    //沉浸式标题栏   去掉状态栏  可以兼容4.4以上系统
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
      mDecorView = getWindow().getDecorView();
      int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
          //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
          //| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
      mDecorView.setSystemUiVisibility(option);

      getWindow().setStatusBarColor(Color.TRANSPARENT);
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
      WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
      localLayoutParams.flags =
          (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
    }

    //----------------------------------------------------------------------------------------------
    mContext = this;
    mLayoutInflater = this.getLayoutInflater();
    mFragmentManager = this.getSupportFragmentManager();
    mBaseActivityMaster = BaseActivityMaster.getInstance();
    mBaseActivityMaster.addToMaster(this);
    initConfig();
    setContentView(setRootView());
    initViews();

    L.e(this.getClass().getSimpleName() + "   Oncreate  ......");
  }

  public abstract int setRootView();

  public abstract void initViews();

  /**
   * 相当于onResume  当窗口回到焦点的时候调用
   */
  @Override public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    //当app 从后台回来的时候会清除flag  所以要重置flag
    if (hasFocus) {
      mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
          //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
          //| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
  }

  /**
   * 初始化一些配置，比如使用标题栏或者使用bottombar
   */
  protected void initConfig() {
    //useBottombar=true;
  }

  @Override public void setContentView(int layoutResID) {
    if (useTitlebar || useToolbar || useBottombar) {
      initTileBar(layoutResID);
    } else {
      super.setContentView(layoutResID);
    }
  }

  /**
   * 初始化标题栏
   *
   * @param layoutResID 根布局
   * @return 添加标题栏后的根布局
   */
  private void initTileBar(int layoutResID) {
    //rootview
    RelativeLayout mRelativeLayout = new RelativeLayout(this);
    //mRelativeLayout.setOrientation(LinearLayout.VERTICAL);
    //要先设置布局才能用布局参数
    setContentView(mRelativeLayout);

    //设置宽高
    ViewGroup.LayoutParams layoutParams = mRelativeLayout.getLayoutParams();
    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    mRelativeLayout.setLayoutParams(layoutParams);
    //----------------------------------------------------------------------------------
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
  }

  /**
   * 设置左边部分标题栏  会带有返回箭头图标
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
      mTitleLeft.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          BaseActivity.this.finish();
        }
      });
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

  //对Activity操作---------------------------------------------------------------------------------
  public void mangoStartActivity(Class actiivty) {
    Intent intent = new Intent(mContext, actiivty);
    startActivity(intent);
  }

  /**
   * 如果startActivity有数据就返回true，如果没数据就返回false
   */
  public boolean mangoStartActivity(Class activity, Bundle bundle) {
    Intent mIntent = new Intent(mContext, activity);
    mIntent.putExtra("bundle", bundle);
    startActivity(mIntent);
    if (bundle == null) {
      return false;
    }
    return true;
  }

  //对于Activity操作结束------------------------------------------------------------------------

  //对于fragment操作--------------------------------------------------------------------------
  public void mangoAddFragment(int des, BaseFragment fragment) {
    mFragmentManager.beginTransaction()
        .add(des, fragment, fragment.getClass().getSimpleName())
        .addToBackStack(fragment.getClass().getSimpleName())
        .commit();
  }

  public void mangoReplaceFragment(int des, BaseFragment fragment) {
    mFragmentManager.beginTransaction()
        .replace(des, fragment, fragment.getClass().getSimpleName())
        .addToBackStack(fragment.getClass().getSimpleName())
        .commit();
  }

  public void mangoHideFragment(BaseFragment fragment) {
    mFragmentManager.beginTransaction()
        .hide(fragment)
        .addToBackStack(fragment.getClass().getSimpleName())
        .commit();
  }

  public void mangoShowFragment(BaseFragment fragment) {
    mFragmentManager.beginTransaction()
        .show(fragment)
        .addToBackStack(fragment.getClass().getSimpleName())
        .commit();
  }
  //对于framgent 操作结束-----------------------------------------------------------------

  //backpress 相关----------------------------------------------------------------------

  /**
   * 是否安全退出
   */
  private void safeExit() {
    long timeNow = System.currentTimeMillis();
    long cha = timeNow - mTimeBegin;
    if (cha >= 2000) {
      mTimeBegin = timeNow;
      Toast.makeText(mContext, mSafeText, Toast.LENGTH_SHORT).show();
      return;
    }
    mBaseActivityMaster.finishAll();
  }

  /**
   * 对安全退出开放方法
   */
  public void setSafeExit(boolean useSafe, String text) {
    mSafeExit = useSafe;
    mSafeText = text;
  }

  /**
   * 如果fragment 栈中数量大于1，就移除最顶层fragment
   */
  public void mangoRemoveFragment() {
    if (mFragmentManager.getBackStackEntryCount() > 1) {
      mFragmentManager.popBackStack();
    } else {
      finish();
    }
  }

  /**
   * 只有回退栈中没有事务的时候才会触发acticity安全退出
   */
  @Override public void onBackPressed() {
    if (mFragmentManager.getBackStackEntryCount() == 1) {
      //如果设置了安全退出就安全退出
      if (mSafeExit) {
        safeExit();
        return;
      }
      super.onBackPressed();
    } else {
      //如果回退栈中有事务，就要先退出fragment
      mangoRemoveFragment();
    }
  }

  //backpress 相关----------------------------------------------------------------------
  @Override protected void onDestroy() {
    super.onDestroy();
    mBaseActivityMaster.removeFromMaster(this);
  }
}
