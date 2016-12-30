package com.handsomeyang.mango.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
import com.handsomeyang.mango.output.S;
import com.handsomeyang.mango.thrid.bottombar.BottomBar;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.handsomeyang.mango.base.BaseActivity.MangoRequestCode.RequestCode.REQUEST_CODE_1;
import static com.handsomeyang.mango.base.BaseActivity.MangoRequestCode.RequestCode.REQUEST_CODE_2;

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
  public Toolbar mToolbar;                         //开放toolbar出去

  private long mTimeBegin;
  private boolean mSafeExit;
  private String mSafeText;

  private boolean useBottombar = false;
  private boolean useTitlebar = false;
  private boolean useToolbar = false;

  protected BottomBar mBottomBar;
  protected View mDecorView;

  //protected static int REQUEST_CODE_1 = 1111;
  //protected static int REQUEST_CODE_2 = 2222;

  @Override protected void onCreate(Bundle savedInstanceState) {
    init();
    super.onCreate(savedInstanceState);
  }

  /**
   * 一些初始化赋值操作  如果不希望布局中的内容拉到状态栏上的话就在xml最外层+fitSystemWindow=true
   */
  private void init() {
    //沉浸式标题栏   去掉状态栏  可以兼容4.4以上系统-----------------------------------------------
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
      mDecorView = getWindow().getDecorView();
      int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
      mDecorView.setSystemUiVisibility(option);

      getWindow().setStatusBarColor(Color.TRANSPARENT);
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
      WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
      localLayoutParams.flags =
          (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
    }

    //标题栏设置结束------------------------------------------------------------------------------------
    //初始化SnackbarUtils 工具类
    S.init(this);

    mDecorView = getWindow().getDecorView();
    mContext = this;
    mLayoutInflater = this.getLayoutInflater();
    mFragmentManager = this.getSupportFragmentManager();
    mBaseActivityMaster = BaseActivityMaster.getInstance();
    mBaseActivityMaster.addToMaster(this);
    initConfig();
    setContentView(setRootView());
    initViews();

    L.e(this.getClass().getSimpleName() + "  OnCreate  .....");
  }

  /**
   * 初始化一些配置，比如使用标题栏或者使用bottombar
   */
  protected void initConfig() {
  }

  public abstract int setRootView();

  public abstract void initViews();

  @Override public void setContentView(int layoutResID) {
    if (useTitlebar || useToolbar || useBottombar) {
      initConfig(layoutResID);
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
  private void initConfig(int layoutResID) {
    //rootview
    RelativeLayout mRootRelativeLayout = new RelativeLayout(this);
    //为根布局 添加id，方便以后add fragment 的时候用
    mRootRelativeLayout.setId(R.id.root_relative_layout);

    //要先设置布局才能用布局参数
    setContentView(mRootRelativeLayout);

    //设置宽高
    ViewGroup.LayoutParams rootLayoutParams = mRootRelativeLayout.getLayoutParams();
    rootLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
    rootLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
    mRootRelativeLayout.setLayoutParams(rootLayoutParams);

    //----------------------------------------------------------------------------------
    mTitleBar = mLayoutInflater.inflate(R.layout.titlebar, mRootRelativeLayout, false);
    RelativeLayout.LayoutParams mTitleBarLayoutParams =
        (RelativeLayout.LayoutParams) mTitleBar.getLayoutParams();
    mTitleBarLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    mTitleBar.setLayoutParams(mTitleBarLayoutParams);

    mTitleBar.setVisibility(View.GONE);
    //titlebar rootView
    mTitleRootView = mTitleBar.findViewById(R.id.title_bar_default);
    //普通标题栏-------------------------------------------------------------------------------
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
      //使用Toolbar-------------------------------------------------------------------------
      mTitleBar.setVisibility(View.VISIBLE);
      mTitleRootView.setVisibility(View.GONE);
    }

    View rootView = mLayoutInflater.inflate(layoutResID, mRootRelativeLayout, false);
    RelativeLayout.LayoutParams rootViewLayoutParams =
        (RelativeLayout.LayoutParams) rootView.getLayoutParams();

    rootViewLayoutParams.addRule(RelativeLayout.BELOW, R.id.titlbar);
    rootViewLayoutParams.addRule(RelativeLayout.ABOVE, R.id.bottombar);

    mRootRelativeLayout.addView(mTitleBar);
    mRootRelativeLayout.addView(rootView);

    //只有添加到布局上之后才能findviewbyid
    mToolbar = (Toolbar) findViewById(R.id.titlbar);

    //bottombar-------------------------------------------------------------------------
    if (useBottombar) {
      View bottombar = mLayoutInflater.inflate(R.layout.bottombar, mRootRelativeLayout, false);
      RelativeLayout.LayoutParams bottombarLayoutParams =
          (RelativeLayout.LayoutParams) bottombar.getLayoutParams();
      bottombarLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      bottombar.setLayoutParams(bottombarLayoutParams);

      mRootRelativeLayout.addView(bottombar);
      //add 之后才能findviewbyid
      mBottomBar = (BottomBar) findViewById(R.id.bottombar);
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
      ImageView imgv_title_left = (ImageView) mTitleBar.findViewById(R.id.img_title_left);
      imgv_title_left.setImageResource(R.drawable.ic_arraw_white_left);
      mTitleLeft.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          BaseActivity.this.finish();
        }
      });
    }
  }

  public void setTitleLeft(@DrawableRes int imgBackRes, View.OnClickListener onClickListener) {
    if (!useTitlebar) {
      return;
    }
    if (mTitleLeft != null) {
      mTitleLeft.setVisibility(View.VISIBLE);
    }
    if (onClickListener != null && mTitleLeft != null) {
      mTitleLeft.setOnClickListener(onClickListener);
    }
    if (imgBackRes != 0 && mTvTitleLeft != null) {
      ImageView imgeBackLeft = (ImageView) mTitleBar.findViewById(R.id.img_title_left);
      imgeBackLeft.setImageResource(imgBackRes);
      if (onClickListener != null && mTitleLeft == null) {
        mTvTitleLeft.setOnClickListener(onClickListener);
      }
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

  /**
   * Request code 用于标识向哪个acitivity 跳转
   * Result code 用于标识是哪个activity 返回来的值
   */
  public void mangoStartActivityForResult(Class activity,
      @MangoRequestCode.RequestCode int resquestCodes) {
    Intent intent = new Intent(mContext, activity);
    startActivityForResult(intent, REQUEST_CODE_1);
  }

  /**
   * Request code 用于标识向哪个acitivity 跳转
   * Result code 用于标识是哪个activity 返回来的值
   */
  public void mangoStartActivityForResult(Class activity,
      @MangoRequestCode.RequestCode int resquestCodes, Bundle bundle) {
    Intent mIntent = new Intent(mContext, activity);
    mIntent.putExtra("bundle", bundle);
    startActivityForResult(mIntent, REQUEST_CODE_1);
  }

  /**
   * StartActivityForResult 回调的方法OnActiityResult
   */
  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      if (requestCode == REQUEST_CODE_1) {
      }
      OnActivityResult1(data);
    } else if (requestCode == REQUEST_CODE_2) {
      onActivityResult2(data);
    }
  }

  /**
   * 当resquestcode ==RESQUEST_CODE1 时候
   */
  protected void onActivityResult2(Intent data) {
  }

  /**
   * 当resquestcode ==RESQUEST_CODE2 时候
   */
  protected void OnActivityResult1(Intent data) {

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
   * 对安全退出开放方法
   */
  public void setSafeExit(boolean useSafe, String text) {
    mSafeExit = useSafe;
    mSafeText = text;
  }

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
   * 只有回退栈中没有事务的时候才会触发acticity安全退出
   */
  @Override public void onBackPressed() {
    if (mFragmentManager.getBackStackEntryCount() <= 1) {
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
   * 销毁当前activity
   */
  protected void suicide() {
    this.finish();
  }

  //backpress 相关结束----------------------------------------------------------------------

  @Override protected void onDestroy() {
    super.onDestroy();
    mBaseActivityMaster.removeFromMaster(this);
    L.e(this.getClass().getSimpleName() + "  OnDestroy  .....");
  }

  /**
   * 封装了StartActivityForResult 中的两个ResquestCode
   */
  public static class MangoRequestCode {
    @IntDef({ 1111, 2222 }) @Retention(RetentionPolicy.SOURCE) public @interface RequestCode {
      public static int REQUEST_CODE_1 = 1111;
      public static int REQUEST_CODE_2 = 2222;
    }
  }
}
