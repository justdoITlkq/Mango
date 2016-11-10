package com.handsomeyang.mango.output;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import com.handsomeyang.mango.R;
import com.handsomeyang.mango.thrid.iosdialog.AlertView;
import com.handsomeyang.mango.thrid.mddialog.MaterialDialog;

/**
 * Created by HandsomeYang on 2016/9/14.
 * dialog
 */

public class D {
  /**
   * API：
   * https://github.com/saiwu-bigkoo/Android-AlertView
   * 类似ios确认操作对话框
   *
   * @param msg 对话框内容
   * @param listener 确认点击监听
   */
  public static void ios(String msg, Context context, AlertView.OnItemClickListener listener) {
    new AlertView("确认操作", msg, "取消", new String[] { "确定" }, null, context, AlertView.Style.Alert,
        listener).setCancelable(true).show();
  }

  /**
   * @param title 标题
   * @param msg 消息
   */
  public static void ios(String title, String msg, Context context,
      AlertView.OnItemClickListener listener) {
    new AlertView(title, msg, "取消", new String[] { "确定" }, null, context, AlertView.Style.Alert,
        listener).setCancelable(true).show();
  }

  /**
   * API:
   * https://github.com/drakeet/MaterialDialog
   */
  public static void md(Context context, String title, String msg, View.OnClickListener listener) {
    final MaterialDialog mMaterialDialog = new MaterialDialog(context);
    mMaterialDialog.setTitle(title)
        .setMessage(msg)
        .setCanceledOnTouchOutside(false)
        .setPositiveButton("确认", listener)
        .setNegativeButton("取消", new View.OnClickListener() {
          @Override public void onClick(View view) {
            mMaterialDialog.dismiss();
          }
        });
    mMaterialDialog.show();
    mMaterialDialog.getPositiveButton()
        .setTextColor(context.getResources().getColor(R.color.lite_blue));
  }
}
