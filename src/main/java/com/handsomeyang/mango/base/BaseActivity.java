package com.handsomeyang.mango.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.design.widget.BottomNavigationView;
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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.handsomeyang.mango.base.BaseActivity.MRequestCode.Code.REQUEST_CODE_1;
import static com.handsomeyang.mango.base.BaseActivity.MRequestCode.Code.REQUEST_CODE_2;

/**
 * Created by HandsomeYang on 2016/9/9.
 */
public abstract class BaseActivity extends FragmentActivity {

  protected BaseActivity mContext;
  protected LayoutInflater mLayoutInflater;
  protected FragmentManager mFragmentManager;

  protected BaseActivityMaster mBaseActivityMaster;
  private View mTitleBar, mLeftTitle, mCenterTitle, mRightTitle, mCommonTitlebar;
  private TextView mTvTitleLeft, mTvTitleCenter, mTvTitleRight;
  private ImageView mImageView;
  //public toolbar
  public Toolbar mToolbar;

  private long mTimeBegin;
  private boolean mSafeExit;
  private String mSafeText;

  private boolean useBottombar = false;
  private boolean useTitlebar = false;
  private boolean useToolbar = false;
  private boolean useViewPager = false;

  protected BottomNavigationView mBottomBar;
  protected View mDecorView;

  /**
   * StartActivityForResult two  ResquestCode
   */
  public static class MRequestCode {
    @IntDef({ 1111, 2222 }) @Retention(RetentionPolicy.SOURCE) public @interface Code {
      int REQUEST_CODE_1 = 1111;
      int REQUEST_CODE_2 = 2222;
    }
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    init();
    super.onCreate(savedInstanceState);
  }

  /**
   * init sth; if you are not supposed layout content extend to statusbar then, in xml
   * +fitSystemWindow=true
   */
  private void init() {
    //immersive titlebar   no statusbar   4.4+--------------------------------------
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0+
      mDecorView = getWindow().getDecorView();
      int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
      mDecorView.setSystemUiVisibility(option);
      getWindow().setStatusBarColor(Color.TRANSPARENT);
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4----5.0
      WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
      localLayoutParams.flags =
          (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
    }
    //init titlebar end-----------------------------------------------------------

    //init  SnackbarUtils context
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

  @Override public void setContentView(int layoutResID) {
    if (useTitlebar || useToolbar || useBottombar || useViewPager) {
      initLayoutConfig(layoutResID);
    } else {
      super.setContentView(layoutResID);
    }
  }

  /**
   * init templet
   *
   * @param layoutResID activity xml
   * @return rootView  contain activity xml
   */
  private void initLayoutConfig(int layoutResID) {
    //set  baseactivity rootview  contain layoutResId
    RelativeLayout mBaseActivityRootView = new RelativeLayout(this);
    //baserootviewResId setId，for example： add fragment
    mBaseActivityRootView.setId(R.id.root_relative_layout);
    //setContent  must be called before getlayoutparams
    setContentView(mBaseActivityRootView);

    //set baserootview with and height
    ViewGroup.LayoutParams rootLayoutParams = mBaseActivityRootView.getLayoutParams();
    rootLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
    rootLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
    mBaseActivityRootView.setLayoutParams(rootLayoutParams);


    //titlebar settings ------------------------------------------------------
    if (useTitlebar || useToolbar) {
      mTitleBar = mLayoutInflater.inflate(R.layout.titlebar, mBaseActivityRootView, false);
      RelativeLayout.LayoutParams mTitleBarLayoutParams =
          (RelativeLayout.LayoutParams) mTitleBar.getLayoutParams();
      mTitleBarLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
      mTitleBar.setLayoutParams(mTitleBarLayoutParams);
      //titlebar ResContent
      mCommonTitlebar = mTitleBar.findViewById(R.id.title_bar_default);

      //common titlebar  -----------------------------------------------------------
      if (useTitlebar) {

        try {
          mLeftTitle = mTitleBar.findViewById(R.id.title_left);
        } catch (Exception e) {
          L.e("left title doesn't exit");
        }

        try {
          mTvTitleLeft = (TextView) mTitleBar.findViewById(R.id.tv_title_left);
        } catch (Exception e) {
          L.e("left title textview doesn't exit");
        }

        try {
          mCenterTitle = mTitleBar.findViewById(R.id.title_center);
        } catch (Exception e) {
          L.e("center title  doesn't exit");
        }

        try {
          mTvTitleCenter = (TextView) mTitleBar.findViewById(R.id.tv_title_center);
        } catch (Exception e) {
          L.e("center title textview doesn't exit");
        }
        try {
          mRightTitle = mTitleBar.findViewById(R.id.title_right);
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

        //Toolbar  mode:hide common title ------------------------------------
        mCommonTitlebar.setVisibility(View.GONE);
      }
    }
    //activity xml layout-----------------------------------------------------
    View ResContent = mLayoutInflater.inflate(layoutResID, mBaseActivityRootView, false);
    RelativeLayout.LayoutParams ResContentLayoutParams =
        (RelativeLayout.LayoutParams) ResContent.getLayoutParams();

    ResContentLayoutParams.addRule(RelativeLayout.BELOW, R.id.titlbar);
    ResContentLayoutParams.addRule(RelativeLayout.ABOVE, R.id.base_bottom_navigation);

    mBaseActivityRootView.addView(mTitleBar);
    mBaseActivityRootView.addView(ResContent);

    //mToolbar evaluate -----------------------------------
    mToolbar = (Toolbar) mTitleBar;

    //use viewpager: activity xml should be a viewpager---------------------------
    if (useViewPager) {
      View vpcontainer = mLayoutInflater.inflate(layoutResID, mBaseActivityRootView, false);

      RelativeLayout.LayoutParams vpLayoutParams =
          (RelativeLayout.LayoutParams) vpcontainer.getLayoutParams();

      vpLayoutParams.addRule(RelativeLayout.BELOW, R.id.titlbar);
      vpLayoutParams.addRule(RelativeLayout.ABOVE, R.id.base_bottom_navigation);
      vpcontainer.setLayoutParams(vpLayoutParams);

      mBaseActivityRootView.addView(vpcontainer);
    }

    //bottom_navigation  sytle ----------------------------------------------------
    if (useBottombar) {
      mBottomBar = (BottomNavigationView) mLayoutInflater.inflate(R.layout.bottom_navigation,
          mBaseActivityRootView, false);

      RelativeLayout.LayoutParams bottombarLayoutParams =
          (RelativeLayout.LayoutParams) mBottomBar.getLayoutParams();
      bottombarLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      mBottomBar.setLayoutParams(bottombarLayoutParams);

      mBaseActivityRootView.addView(mBottomBar);
    }

    mTitleBar.requestFocus();
  }

  /**
   * set left titlebar   with  back arraw
   *
   * @param text title
   * @param onClickListener listener
   */
  public void setTitleLeft(String text, View.OnClickListener onClickListener) {
    if (!useTitlebar) {
      return;
    }
    if (mLeftTitle != null) {
      mLeftTitle.setVisibility(View.VISIBLE);
    }
    if (onClickListener != null && mLeftTitle != null) {
      mLeftTitle.setOnClickListener(onClickListener);
    }
    if (text != null && mTvTitleLeft != null) {
      mTvTitleLeft.setText(text);
      if (onClickListener != null && mLeftTitle == null) {
        mTvTitleLeft.setOnClickListener(onClickListener);
      }
    }
  }

  /**
   * set default left title  with left arraw
   * kill this page
   */
  public void setTitleLeft() {
    if (!useTitlebar) {
      return;
    }
    if (mLeftTitle != null) {
      mLeftTitle.setVisibility(View.VISIBLE);
      ImageView imgv_title_left = (ImageView) mTitleBar.findViewById(R.id.img_title_left);
      imgv_title_left.setImageResource(R.drawable.ic_arraw_white_left);
      mLeftTitle.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          BaseActivity.this.finish();
        }
      });
    }
  }

  /**
   * set left title
   *
   * @param imgBackRes imgRes
   * @param onClickListener listener
   */
  public void setTitleLeft(@DrawableRes int imgBackRes, View.OnClickListener onClickListener) {
    if (!useTitlebar) {
      return;
    }
    if (mLeftTitle != null) {
      mLeftTitle.setVisibility(View.VISIBLE);
    }
    if (onClickListener != null && mLeftTitle != null) {
      mLeftTitle.setOnClickListener(onClickListener);
    }
    if (imgBackRes != 0 && mTvTitleLeft != null) {
      ImageView imgeBackLeft = (ImageView) mTitleBar.findViewById(R.id.img_title_left);
      imgeBackLeft.setImageResource(imgBackRes);
      if (onClickListener != null && mLeftTitle == null) {
        mTvTitleLeft.setOnClickListener(onClickListener);
      }
    }
  }

  /**
   * set center title
   *
   * @param text title
   */
  public void setCenterTitle(String text) {
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
   * set right title
   *
   * @param text title
   * @param onClickListener listener
   */
  public void setTitleRight(String text, View.OnClickListener onClickListener) {
    if (!useTitlebar) {
      return;
    }
    if (mRightTitle != null) {
      mRightTitle.setVisibility(View.VISIBLE);
    }

    if (onClickListener != null && mRightTitle != null) {
      mRightTitle.setOnClickListener(onClickListener);
    }
    if (text != null && mTvTitleRight != null) {
      mTvTitleRight.setText(text);
      if (onClickListener != null && mRightTitle == null) {
        mTvTitleRight.setOnClickListener(onClickListener);
      }
    }
  }

  /**
   * set right title  with  image  and listener
   *
   * @param ImgID image Res id
   * @param onClickListener listener
   */
  public void setTitleRight(int ImgID, View.OnClickListener onClickListener) {
    if (!useTitlebar) {
      return;
    }
    if (mRightTitle != null) {
      mRightTitle.setVisibility(View.VISIBLE);
    }

    if (onClickListener != null && mRightTitle != null) {
      mRightTitle.setOnClickListener(onClickListener);
    }
    if (ImgID != 0 && mTvTitleRight != null) {
      mImageView.setImageResource(ImgID);
      if (onClickListener != null && mRightTitle == null) {
        mTvTitleRight.setOnClickListener(onClickListener);
      }
    }
  }

  //Activity   start ------------------------------------------------------------
  public void mStartActivity(Class actiivty) {
    Intent intent = new Intent(mContext, actiivty);
    startActivity(intent);
  }

  /**
   * if startActivity with data return true，else return false
   */
  public boolean mStartActivity(Class activity, Bundle bundle) {
    Intent mIntent = new Intent(mContext, activity);
    mIntent.putExtra("bundle", bundle);
    startActivity(mIntent);
    if (bundle == null) {
      return false;
    }
    return true;
  }

  /**
   * Request code :  acitivity skip   flag
   * Result code :   activity back    flag
   */
  public void mStartActivityForResult(Class activity, @MRequestCode.Code int resquestCodes) {
    Intent intent = new Intent(mContext, activity);
    startActivityForResult(intent, REQUEST_CODE_1);
  }

  /**
   * startActivityForResult  with bundle
   */
  public void mStartActivityForResult(Class activity, @MRequestCode.Code int resquestCodes,
      Bundle bundle) {
    Intent mIntent = new Intent(mContext, activity);
    mIntent.putExtra("bundle", bundle);
    startActivityForResult(mIntent, REQUEST_CODE_1);
  }

  /**
   * StartActivityForResult callbak :OnActiityResult
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
   * callback:  resquestcode ==RESQUEST_CODE1
   */
  protected void onActivityResult2(Intent data) {
  }

  /**
   * call back:  resquestcode ==RESQUEST_CODE2
   */
  protected void OnActivityResult1(Intent data) {

  }
  //Activity   end ----------------------------------------------------------

  //fragment   start    ----------------------------------------------------
  public void mAddFragment(int des, BaseFragment fragment) {
    mFragmentManager.beginTransaction()
        .add(des, fragment, fragment.getClass().getSimpleName())
        .addToBackStack(fragment.getClass().getSimpleName())
        .commit();
  }

  public void mReplaceFragment(int des, BaseFragment fragment) {
    mFragmentManager.beginTransaction()
        .replace(des, fragment, fragment.getClass().getSimpleName())
        .addToBackStack(fragment.getClass().getSimpleName())
        .commit();
  }

  public void mHideFragment(BaseFragment fragment) {
    mFragmentManager.beginTransaction()
        .hide(fragment)
        .addToBackStack(fragment.getClass().getSimpleName())
        .commit();
  }

  public void mShowFragment(BaseFragment fragment) {
    mFragmentManager.beginTransaction()
        .show(fragment)
        .addToBackStack(fragment.getClass().getSimpleName())
        .commit();
  }
  //framgent end ---------------------------------------------------

  //backpress  start -------------------------------------------------

  /**
   * safe quit activity
   */
  public void setSafeExit(boolean useSafe, String text) {
    mSafeExit = useSafe;
    mSafeText = text;
  }

  /**
   * wheather safe quit or not
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
   * if fragment backstack has no fragment then,acticity safe quit
   */
  @Override public void onBackPressed() {
    if (mFragmentManager.getBackStackEntryCount() <= 1) {
      //if use safe quit
      if (mSafeExit) {
        safeExit();
        return;
      }
      super.onBackPressed();
    } else {
      //if fragment back stack has fragment ,remove fragment
      mRemoveFragment();
    }
  }

  /**
   * if fragment stack counts > 1，remove top fragment
   */
  public void mRemoveFragment() {
    if (mFragmentManager.getBackStackEntryCount() > 1) {
      mFragmentManager.popBackStack();
    } else {
      finish();
    }
  }

  /**
   * destroy  activity
   */
  protected void mDestroy() {
    this.finish();
  }

  //backpress end -----------------------------------------------------

  @Override protected void onDestroy() {
    super.onDestroy();
    mBaseActivityMaster.removeFromMaster(this);
    L.e(this.getClass().getSimpleName() + "  OnDestroy  .....");
  }

  //API---------------------------------------------------------------------------

  /**
   * init page style ,for example:use bottom_navigation  or toolbar
   */
  protected void initConfig() {
  }

  public abstract int setRootView();

  public abstract void initViews();

  protected void setUseTitlebar() {
    useTitlebar = !useToolbar;
  }

  protected void setUseToolbar() {
    useToolbar = !useTitlebar;
  }

  protected void setUseBottombar() {
    useBottombar = true;
  }

  protected void setUseViewPager() {
    useViewPager = true;
  }
}
