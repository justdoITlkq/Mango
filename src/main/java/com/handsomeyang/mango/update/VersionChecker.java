package com.handsomeyang.mango.update;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.handsomeyang.mango.utils.AppUtils;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by HandsomeYang on 2016/9/20.
 * 1、应用当日首次启动就提示对话框版本更新，启动服务检测版本，如果小于服务器版本，则
 * 2、 rxjava+retrofit 实现文件下载，
 * 3、下载完成后校验md5或者sha1值，如果与服务器一致
 * 4、提示用户安装
 * 5、 安装成功后提示用户删除之前apk
 *
 * 检查版本：服务器返回json中包含：版本号，版本名，apk大小，apk icon，apk下载地址；
 *
 * retrofit 2.0: @GET和@POST
 * 可以在注解（）参数中制定查询参数
 *
 * @PATH("") 可以替换参数中被{}括起来的参数
 * 复杂查询参数可以使用Map进行组合
 * @BODY 指定一个对象作为http请求请求体
 * 该对象会被Retrofit实例指定的转换器转换，如果没有添加转换器，则只有RequestBody可用。
 * Form encoded   将发送Form-Encoded 数据，每个键值都要被@Field标注
 * multipart      将发送Multipart数据，parts都要使用@Part注解进行声明
 * multipart parts要使用Retrofit的众多转换器之一或者实现RequestBody来处理自己的序列化
 * @Header 来设置静态的请求头
 * header 不能覆盖，所有具有相同名字的header将会被包含到请求中
 * 可以使用@Header注解动态的更新一个请求的header，必须给@Header相应的参数，如果参数值为空，header将被忽略，
 * 否则就调用参数的toString放回并使用返回结果
 * 使用OkhttpClient拦截器可以指定需要的header 给每一个http请求
 * BaseUrl  总是以/结尾， Url不要以/开头
 * 也可以在Url中定义完整的URL  ，这种情况baseUrl会被忽略
 * 不管response 能不能被解析，onResponse方法总是会被调用，但是在结果不能被解析的情况下response.body()会返回null
 * 如果response 存在什么问题，如404，oresponse也会被调用，可以从response.body().toString()获取错误信息的主体
 *
 * rxjava：响应式编程
 * observable 和 observer subject是同时继承了observable 和observer
 * 操作符； subscribe订阅；观察者模式
 * 当obervable发送事件后就丢给了subscriber，用来处理事件，实现了oberver接口
 * 一个oberver发送三中数据1、 values；2、完成状态，没有其他数据了；3、error 发送出现错误
 * subject 是oberver的一个实现，多了些其他拓展功能也是实现了observable， 既可以接受事件也可以发送事件
 *
 * PublishSubject是最直接的一个subject，onext中发送数据，立即发送
 * Function类型 只有一个参数没有返回值的function叫做action
 *
 * ReplaySubject 可以缓存所有发送给他的数据，当有一个新的订阅者的时候，所有缓存的事件都会发送给该订阅者，
 * 每个订阅者都会接收到所有的数据；可以限制缓存和缓存时间，createWithSize 和 createWithTime  也可以同时限制时间和数量
 * BehaviorSubject  只保留最后一个值；
 * AsyncSubject  也缓存最后一个数据，区别是只有onCompleted调用才发射这最后一个数据。
 * rx隐含规则：onerror或者oncompleted调用后就不会再发送数据；没有要求这样，但是要注意遵守
 * 操作符：just：onNext
 * from ：它接受一个集合作为输入，然后每次输出一个元素给subscriber
 * map：变化数据流
 * flatmap  接受一个obserable 同时返回另外一个observable
 * filter：输入和参数相同的元素，过滤不满足条件的
 * take：最多想要几个结果
 * doOnnext：允许我们每次输出一个元素之前做一些额外的事
 * 可以自定义操作符，不过不太推荐
 * SubscribeOn 和ObservaleOn    变换线程
 * Observable.just会返回subscription对象，代表被观察者和订阅者之间的关系，可以解除订阅关系
 * rxAndroid：rxjava针对android 平台做的一些拓展，能够简化android开发
 * AndroidSchedulers提供了针对Android系统的调度器
 */

public class VersionChecker extends Service {
  public static final String CHECKVERSION = "";

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    return super.onStartCommand(intent, flags, startId);
  }

  public int getLatestVersionCode() {
    //联网获取版本号
    return 0;
  }

  /**
   * 检测本地版本和服务器版本号区别
   * 如果需要更新就返回ture，如果不需要更新就返回false
   */
  public boolean checkVersionCode() {
    AppUtils.AppInfo mAppInfo = AppUtils.getAppInfo(this);
    int localVersionCode = mAppInfo.getVersionCode();
    return false;
  }

}
