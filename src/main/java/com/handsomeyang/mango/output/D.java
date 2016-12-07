package com.handsomeyang.mango.output;

import android.content.Context;
import android.view.View;
import com.handsomeyang.mango.R;
import com.handsomeyang.mango.thrid.mddialog.MaterialDialog;

/**
 * Created by HandsomeYang on 2016/9/14.
 * dialog
 */

public class D {

  /**
   * Darkeet 的md dialog 库
   * API:
   * https://github.com/drakeet/MaterialDialog
   * 已经标记废弃了
   * 想封装一个木有时间
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
