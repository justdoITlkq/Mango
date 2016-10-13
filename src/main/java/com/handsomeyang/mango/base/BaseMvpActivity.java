package com.handsomeyang.mango.base;

import android.os.Bundle;

public abstract class BaseMvpActivity<V, T extends BasePresenter<V>> extends BaseActivity {

  public T presenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter = initPresenter();
  }

  @Override protected void onResume() {
    super.onResume();
    presenter.attach((V) this);
  }

  @Override protected void onDestroy() {
    presenter.dettach();
    super.onDestroy();
  }

  public abstract T initPresenter();
}
