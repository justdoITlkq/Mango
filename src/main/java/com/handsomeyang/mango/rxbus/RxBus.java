package com.handsomeyang.mango.rxbus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by HandsomeYang on 2016/9/12.
 * rxbus并不是一个库，而是一种模式，它的思想是使用rxjava来实现eventbus
 * 1、数据rxjava用法
 * 2、减少程序引用三方库
 * 注意在oncreate中注册，在ondestroy中解注册
 */

public class RxBus {
  //volatile的作用是： 作为指令关键字，确保本条指令不会因编译器的优化而省略，且要求每次直接读值.
  private static volatile RxBus sRxBus;
  private Subject<Object, Object> bus;

  //publishSubject 只会把订阅发生时间点之后来自原始Observable的数据发射给观察者
  public RxBus() {
    bus = new SerializedSubject<>(PublishSubject.create());
  }

  //单实例
  public static RxBus getDefault() {
    if (sRxBus == null) {
      synchronized (String.class) {
        if (sRxBus == null) {
          sRxBus = new RxBus();
        }
      }
    }
    return sRxBus;
  }

  //发送一个事件
  public void post(Object object) {
    bus.onNext(object);
  }

  //根据传递的eventType类型返回特定类型eventType的  被观察者
  public <T> Observable<T> toObservable(Class<T> eventType) {
    return bus.ofType(eventType);
  }
}
