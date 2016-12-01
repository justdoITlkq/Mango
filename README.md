# Mango

---
Faster develop Android Apps~

### usage
- 让Activity或者Fragment 继承 BaseActivity或BaseFragment即可；
- setRootView方法返回值为页面根布局:R.layout.xxx
- initView在Oncreate中执行
- initConfig为protected 方法，如果想让页面带有标题栏（沉浸式），或者BottomBar只需在这个方法中setUseTitlebar（普通标题栏）；setUseToolbar或者setUseBottombar；
- 配置BottomBar可以自己写一个xml，集成的：https://github.com/roughike/BottomBar
- 安全退出，按两次退出应用，在onBackPress中setSafeExit(boolean 是否启动安全退出模式，"显示土司的文本")
- 简单封装了Activity跳转和Fragment操作，MangoStart...或者MangoShow... ；StartActivityForResult 覆写OnActivityForResult1和2表示用RESULT_OK作为setResult的Result_code回执方法
- build.gradle中compile了许多，rxjava和retrofit，fresco，smartTablayout，还有BlankJ的工具类（强推） https://github.com/Blankj/AndroidUtilCode； 等
### SreenShot
[link](https://github.com/yangxiaobinhaoshuai/Mango/blob/master/screenshot/Screenshot_2016-12-01.png)