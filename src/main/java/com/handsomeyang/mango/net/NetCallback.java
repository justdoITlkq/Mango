package com.handsomeyang.mango.net;

import com.handsomeyang.mango.output.L;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HandsomeYang on 2016/9/12.
 */

public abstract class NetCallback<T extends BaseCallModel> implements Callback<T> {
  @Override public void onResponse(Call<T> call, Response<T> response) {
    if (response.raw().code() == 200) {  //200ok 是服务器合理响应
      //服务器json 返回 0 1 2 3 等相应方法
      if (response.body().result == 0) {

      } else if (response.body().result == 1) {

      } else if (response.body().result == 2) {

      } else { //非正常响应码,连接失败
        L.e("联网失败" + response.body().message);
      }
    } else { //失败响应
      onFailure(call, new RuntimeException("response error,detail = " + response.raw().toString()));
    }
  }

  @Override public void onFailure(Call<T> call, Throwable t) {
    if (t instanceof SocketTimeoutException) {
      L.e("网络连接超时！");
    } else if (t instanceof ConnectException) {
      L.e("连接异常！");
    } else if (t instanceof RuntimeException) {
      L.e("程序运行出现异常！");
    }
    L.e("网络连接失败 " + t.getMessage());
  }
}
