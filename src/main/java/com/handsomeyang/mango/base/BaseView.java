package com.handsomeyang.mango.base;

public interface BaseView {

  //未加载出来展示的进度条
  void showLoading();

  //加载出来页面后隐藏进度条
  void hideLoading();
}
