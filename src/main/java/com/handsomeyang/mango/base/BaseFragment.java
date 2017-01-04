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
import com.handsomeyang.mango.output.S;

/**
 * Created by HandsomeYang on 2016/9/9.
 */
public abstract class BaseFragment extends Fragment {

  protected BaseActivity mActivity;                      //context
  protected LayoutInflater mLayoutInflater;             //inflate
  protected FragmentManager mFragmentManager;

  //about titlebar
  protected View mRootView;                            //Fragment rootView
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
    mLayoutInflater = mActivity.getLayoutInflater();

    //must be called before initlayoutConfig
    initConfig();
    if (useTitlebar || useToolbar || useBottombar) {
      mRootView = initLayoutConfig(setRootView());
    } else {
      mRootView = mLayoutInflater.inflate(setRootView(), container, false);
    }

    init();
    initViews();

    return mRootView;
  }

  private void init() {
    //immersive titlebar no statusbar 4.4+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0+
      View decorView = mActivity.getWindow().getDecorView();
      int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
      decorView.setSystemUiVisibility(option);
      mActivity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4----5.0
      WindowManager.LayoutParams localLayoutParams = mActivity.getWindow().getAttributes();
      localLayoutParams.flags =
          (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
    }
    //immersive titlebar end----------------------------------------------------------

    //init SnackBarUtils    content
    S.init(mActivity);

    mFragmentManager = getFragmentManager();
    L.e(this.getClass().getSimpleName() + "  OnCreate  .....");
  }

  /**
   * init titlebar
   *
   * @param layoutResID rootView
   * @return rootView after added titlebar
   */

  private View initLayoutConfig(int layoutResID) {

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

    //common titlebar ------------------------------------------------------------
    if (useTitlebar) {
      L.e(this.getClass().getSimpleName()+"  is common titlebar mode ");
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
      L.e(this.getClass().getSimpleName()+"  is toolbar mode ");
      //Toolbar style-------------------------------------------------------
      mTitleBar.setVisibility(View.VISIBLE);
      mTitleRootView.setVisibility(View.GONE);
    }

    //Activity rootView
    View rootView = mLayoutInflater.inflate(layoutResID, mRelativeLayout, false);

    RelativeLayout.LayoutParams rootViewLayoutParams =
        (RelativeLayout.LayoutParams) rootView.getLayoutParams();
    rootViewLayoutParams.addRule(RelativeLayout.BELOW, R.id.titlbar);
    rootViewLayoutParams.addRule(RelativeLayout.ABOVE, R.id.bottombar);
    rootView.setLayoutParams(rootViewLayoutParams);

    mRelativeLayout.addView(mTitleBar);
    mRelativeLayout.addView(rootView);

    //only after addView can findviewbyid
    mToolbar = (Toolbar) mTitleBar;

    if (useBottombar) {
      View bottombar = mLayoutInflater.inflate(R.layout.bottombar, mRelativeLayout, false);
      RelativeLayout.LayoutParams bottombarLayoutParams =
          (RelativeLayout.LayoutParams) bottombar.getLayoutParams();
      bottombarLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      bottombar.setLayoutParams(bottombarLayoutParams);
      mRelativeLayout.addView(bottombar);
    }

    mTitleBar.requestFocus();
    return mRelativeLayout;
  }

  /**
   * set left titlebar
   *
   * @param text title text
   * @param onClickListener listener
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
   * set defualt left titlebar
   * listener :  destroy  self with left arraw
   */
  public void setTitleLeft() {
    if (!useTitlebar) {
      return;
    }
    if (mTitleLeft != null) {
      mTitleLeft.setVisibility(View.VISIBLE);
      mTitleLeft.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          BaseFragment.this.mDestroy();
        }
      });
    }
  }

  /**
   * set center title
   *
   * @param text title textss
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
   * set right title
   *
   * @param text title text
   * @param onClickListener listener
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
   * set right titlebar; image and listener
   *
   * @param ImgID image  ResId
   * @param onClickListener listener
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

  // Activiy start -----------------------------------------------------------------------

  /**
   * if startActivity with data return true，else return false
   */
  public boolean mStartActivity(Class activity, Bundle bundle) {
    Intent mIntent = new Intent(mActivity, activity);
    mIntent.putExtra("bundle", bundle);
    mActivity.startActivity(mIntent);
    if (bundle == null) {
      return false;
    }
    return true;
  }

  /**
   * start Activity with no data
   */
  public boolean mStartActivity(Class activity) {
    Intent mIntent = new Intent(mActivity, activity);
    mActivity.startActivity(mIntent);
    return false;
  }

  //Activity end --------------------------------------------------------------------

  //Fragment start -----------------------------------------------------------------------------

  /**
   * if fragment not crateed , add ,if added ,replace
   *
   * @param targetFragment target fragment
   * @param ResID fragmetn location on activity
   */
  public void mAddOrReplaceFragment(BaseFragment targetFragment, int ResID) {
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

  /**
   * if fragment stack count >1，remove the top fragment
   */
  public void mRemoveFragment() {
    if (mFragmentManager.getBackStackEntryCount() > 1) {
      mFragmentManager.popBackStack();
    } else {
      mDestroy();
    }
  }

  //framgent end--------------------------------------------------------------------

  /**
   * destroy self
   */
  public void mDestroy() {
    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
    fragmentTransaction.remove(this);
    fragmentTransaction.commit();
  }

  @Override public void onDestroy() {
    super.onDestroy();
    L.e(this.getClass().getSimpleName() + "  OnDestroy  .....");
  }

  //API----------------------------------------------------------------------------
  protected void initConfig() {
  }

  public abstract int setRootView();

  protected abstract void initViews();

  protected void setUseTitlebar() {
    useTitlebar = !useToolbar;
  }

  protected void setUseToolbar() {
    useToolbar = !useTitlebar;
  }

  protected void setUseBottombar() {
    useBottombar = true;
  }
}
