# sdkutil
## Download
gradle:
```
    compile 'com.transsion.api:coreutil:+'
```

## 使用方法

```
// 首先需要在Application的onCrate函数中调用如下初始化，如果SDK要使用此功能，请在SDK的初始化中调用此初始化
CoreUtil.init(context);
```

## 获取设备信息的示例代码

```

        String strImei = DeviceInfo.getIMEI();          //获得IMEI
        String gaId = DeviceInfo.getGAId();             //获得GAID
        String androidId = DeviceInfo.getAndroidID();   //获得AndroidId
        String Imsi = DeviceInfo.getIMSI();             //获得IMSI
        boolean isPad = DeviceInfo.isPad();             //判断是否是pad,是则返回true
        String btMac = DeviceInfo.getBTMAC();           //获得蓝牙的mac地址
      
      

```

## LogUtils说明
### 使用方法
同普通的Log输出日志的方法
```
    LogUtils.d("myTag", "My log \nlogloglog!");
```

### 提供了灵活的配置方法
配置方法：
```
		new LogUtils.Builder()
				.setLogSwitch(true)           // 日志开关，default:true
				.setGlobalTag("LogTest")      // 全局tag，输出log时如果不传入tag参数，则使用这个,default:null
				.setBorderSwitch(true)	      // 每条log是否增加边框效果, default:true
				.setConsoleFilter(LogUtils.V) // 设置能在终端输出的日志的等级，default:V
				.setLogHeadSwitch(true)       // 是否显示打印日志代码所在的函数和行号，default:true
				.setConsoleSwitch(true)       // 是否允许在终端输出日志 default:true
				.setLog2FileSwitch(true)      // 是否允许日志输出到文件 default:false
				.setFileFilter(LogUtils.V)    // 设置输出到文件的日志的等级，default:V
				.setDir(Environment.getExternalStorageDirectory().getPath() + System.getProperty("file.separator") + "test") // 制定输出文件的路径，缺省在应用的私有目录下
				;
```

## ObjectLogUtils说明
### 使用方法
此log输出工具带有自己的配置方法，适用于对象内自由控制log输出。
```
                ObjectLogUtils logUtils = new ObjectLogUtils.Builder().setGlobalTag("ObjectLogUtils").create();
                logUtils.i("----logUtils----");
```

### 提供了灵活的配置方法
配置方法示例，各种开关说明参考LogUtils
```
                ObjectLogUtils logUtils02 = new ObjectLogUtils.Builder()
                    .setGlobalTag("ObjectLogUtils")
                    .setBorderSwitch(false)
                    .setLogHeadSwitch(false)
                    .create();
                logUtils02.i("----logUtils02----\n----logUtils02----\n----logUtils02----\n----logUtils02----\n----logUtils02----\n");
```

> **一个sdk或者一个模块可以new一个自己的全局静态ObjectLogUtils对象，用于自己的log输出。**

## SharePreferencesUtils说明
### 使用方法

#### 工具的绑定：
```
     使用之前需在application的onCreate()方法中绑定Application的context。
     SharedPreferencesUtils.bindApplication(context.getApplicationContext());
     此方法已经在
     CoreUtil.init(getApplicationContext());
     方法中调用，故接入SDK时可不再调用。
```    
#### 工具的初始化：
```
     此工具的构造方法，需指定所操作的文件名。
     
     SharedPreferencesUtils.getInstance(操作文件名称,需String);需传入文件名。
     
     建议在此基础上封装一层自己的逻辑。
     
    private static final String spName = "XXXXXXXX";
    
    public static SharedPreferencesUtil getInstance() {
        return SharedPreferencesUtil.getInstance(spName);
    }
     
```
#### 存入数据方法：
```
     SharedPreferencesUtil.getInstance().putString(Tag, "字符串");
     SharedPreferencesUtil.getInstance().putInt(Tag1, 1);
     SharedPreferencesUtil.getInstance().putFloat(Tag2, 2);
     SharedPreferencesUtil.getInstance().putLong(Tag3, 3);
     SharedPreferencesUtil.getInstance().putBoolean(Tag4, true);
     SharedPreferencesUtil.getInstance().putStringSet(Tag5,new Set<String>());
```     
#### 取出数据方法：
```
     SharedPreferencesUtil.getInstance().getString(Tag);
     SharedPreferencesUtil.getInstance().getInt(Tag1);
     SharedPreferencesUtil.getInstance().getFloat(Tag2);
     SharedPreferencesUtil.getInstance().getLong(Tag3);
     SharedPreferencesUtil.getInstance().getBoolean(Tag4);
     SharedPreferencesUtil.getInstance().getStringSet(Tag5);
```     
#### 默认的取出数据方法是带有默认default的，如果想自己携带可按如下方法写：
```    
     SharedPreferencesUtil.getInstance().getString(Tag,"你需要的字符串");
     SharedPreferencesUtil.getInstance().getInt(Tag1,你需要的int型返回值);
     SharedPreferencesUtil.getInstance().getFloat(Tag2,你需要的float型返回值);
     SharedPreferencesUtil.getInstance().getLong(Tag3,你需要的long型返回值);
     SharedPreferencesUtil.getInstance().getBoolean(Tag4,你需要的Boolean型返回值);
     SharedPreferencesUtil.getInstance().getStringSet(Tag5,你需要的Set<String>型返回值);
```     
#### 查找是否存在某个Key值

```
     SharedPreferencesUtil.getInstance().contains(key);
```    
#### 移除某个key值已经对应的值
```   
     SharedPreferencesUtil.getInstance().remove(key);
```     
#### 返回所有的键值对
```     
     SharedPreferencesUtil.getInstance().getAll();
```     
#### 清除全部

```     
     SharedPreferencesUtil.getInstance().clear();
  
```

## tranjsonlibrary（Tson）说明
### 使用方法

######     gradle：
    
```

       远程依赖 compile 'com.transsion.api:tson:+'
       
       跟Gson使用类似，提供基本注解和格式化打印，示例代码：

       `MyBeanObject target = new MyBeanObject();
       String json = Tson.toJson(target); // serializes target bean to Json String
       //String json = Tson.toJson(target,true);// 格式化json String打印，默认false，只输出字符
       MyBeanObject  target = Tson.fromJson(json, MyBeanObject.class); // deserializes json string into target`
       
```
       
       
######     Bean注解：
    
```

       ` @TserializedName(name = "msg")
         public String msg;//指定json的“msg” key值，默认变量跟key同名`
         
       `@TserializedName(include = false)
        public String data;//排除该对象，默认为true`
``` 


## httplibrary说明
### 使用方法

######     gradle：
    
``` 

       远程依赖 compile 'com.transsion.api:http:+'
       
``` 
       
######     目前支持：
    
``` 
        *一般的get，post请求，其他请求暂未加入
        *支持文件（图片等）缓存请求
        *支持文件断点下载
        *支持自定义泛型Callback，自动根据泛型返回对象
        *支持http请求，目前未加入可信证书和自签名证书的https的访问
        *支持链式调用
``` 

** 普通请求**
   
   
      1.基本的网络请求


``` 
    
         HttpClient.get()                   //必须方法体
                    .log(true)               //是否打印log信息，非必须默认false
                    .url("服务器地址")        //必须方法体，服务器地址
                    .connectTimeout(10000)   //网络连接超时时间，单位毫秒，非必须默认10s
                    .readTimeout(10000)      //读取数据超时时间，单位毫秒，非必须默认10s
                    .addHeader("key","value")//添加请求头参数或一次性添加heads(map)，非必须
                    .build()                 //必须方法体
                    .execute(new StringCallback() {//必须方法体
                        @Override
                        public void onFailure(int statusCode, String responseString, Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(int statusCode, String responseString) {

                        }
                    });
                    
                    
```
                    
       2.普通Post请求

``` 
       
          HttpClient.post()                     //必须方法体
                    .log(false)
                    .connectTimeout(10000)
                    .readTimeout(10000)
                    .addHeader("key","value")
                    .addParam("key","value")   //表单上传k-v请求
                    .url("服务器地址")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onFailure(int statusCode, String responseString, Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(int statusCode, String responseString) {

                        }
                    });
                    
```
                    
       3.JsonPost请求

``` 
       
          HttpClient.postJson()                   //必须方法体
                    .log(false)
                    .connectTimeout(10000)
                    .readTimeout(10000)
                    .addHeader("key", "value")
                    .content("postString")       //标准json String，或者addParam("key","value")或者params(map)
                    .url("服务器地址")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onFailure(int statusCode, String responseString, Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(int statusCode, String responseString) {

                        }
                    });
                    
```
         
       4.请求Bitmap对象
       
``` 
       
       HttpClient.image(getContext())         //必须方法体，需要传入context
                    .log(false)
                    .cache(true)
                    .connectTimeout(10000)
                    .readTimeout(10000)
                    .addHeader("key","value")
                    .url("图片地址")           //必须方法体，传入图片url
                    .build()
                    .execute(new BitmapCallback() {
                        @Override
                        public void onFailure(int statusCode, Bitmap responseString, Throwable throwable) {
                        }

                        @Override
                        public void onSuccess(int statusCode, Bitmap responseFile) {
                        }
                    });
                    
``` 

       5.文件下载
       
``` 
        DownloadEngine downloadEngine = DownloadEngine.getEngine();  //单例
        String path = getContext().getFilesDir() + "/test.apk";
        RequestCall requestCall = HttpClient.download(getContext())
                                            .pathname(path)            //必须方法体，命名规则 路径 + 文件名
                                            .url("downurl")
                                            .log(true)
                                            .tag("downurl")            //传入Object作为tag，建议传入downurl来达到一一对应
                                            .build();
        downloadEngine.execute(requestCall, new DownloadCallback() {
            @Override
            public void onFailure(String url, String message) {
            }

            @Override
            public void onSuccess(String url, File downFile) {
            }

            @Override
            public void onLoading(String url, long current, long total) {
            }
        });
        
        
        downloadEngine.pauseLoad(tag);    //暂停下载
        downloadEngine.cancelLoad(tag);   //取消下载
        downloadEngine.continueLoad(tag); //继续下载，如果取消过该任务，需要重新execute才能有效
                    
``` 
                    
 ** 自定义CallBack使用**
    
          目前内部提供的包含StringCallBack ,BitmapCallback ,FileCallBack ,可传入Looper回调，可以根据自己的需求去自定义Callback。
          

## ToastUtil使用说明
### 使用方法

```   
        ToastUtil.showToast(R.string.app_name);    //支持id的短toast
        ToastUtil.showToast("your string");        //支持String的短toast
        ToastUtil.showLongToast(R.string.app_name);  //支持id的长toast
        ToastUtil.showLongToast("your string");    //支持String的长toast
     
``` 

## ScreenUtil使用说明
### 使用方法

```  
        int dpi = ScreenUtil.getDensityDpi();           //获得手机的dp
        float scale = ScreenUtil.getDensityScale();     //获得手机的sp
        int orienta = ScreenUtil.getScreenOrientation();  //获得手机的横竖屏状态
        int heigh = ScreenUtil.getWinHeight();          //获得手机的屏幕高(单位px)
        int width = ScreenUtil.getWinWidth();           //获得手机的屏幕宽（单位px）
        int px = ScreenUtil.dip2px(30);                 //dp转成px
        int px1 = ScreenUtil.sp2px(30);                 //sp转成px
        int dp = ScreenUtil.px2dip(30);                 //px转成dp
        int sp = ScreenUtil.px2sp(30);                  //px转成sp
        
``` 

## EncoderUtil使用说明
### 使用方法

```   
        public static final String ALGORITHM_MD5 = "MD5";           //MD5
        public static final String ALGORITHM_SHA_1 = "SHA-1";       //SHA-1
        public static final String ALGORITHM_SHA_256 = "SHA-256";   //SHA-256
        public static final String ALGORITHM__SHA_384 = "SHA-384";  //SHA-384
        public static final String ALGORITHM_SHA_512 = "SHA-512";   //SHA-512

        String result = EncoderUtil.EncoderByAlgorithm("your string");  //缺省的MD5加密编码方法。
        
        String result = EncoderUtil.EncoderByAlgorithm("your string",EncoderUtil.ALGORITHM_MD5);//需指定的编码方式编码方法，支持上述的5种编码方式
     
``` 

## AppUtil使用说明
### 使用方法

```   
        String appName = AppUtil.getAppName();         //获得App的名字
        String pkgName = AppUtil.getPkgName();         //获得App的包名
        int versionCode = AppUtil.getVersionCode();    //获得APP的version code
        String versionName = AppUtil.getVersionName(); //获得APP的version name
     
``` 

## ViewBitmapRecycleUtil说明
### 使用方法

```   
        ViewBitmapRecycleUtil.bitmapsRecycle(view);  //回收view或viewgroup中的全部子imageview的bitmap.非imageView不做任何处理。
   
``` 
## NetUtil使用说明
### 使用方法

```    
        public static final int NETWORK_CLASS_UNAVAILABLE  //无效的网络
        public static final int NETWORK_CLASS_UNKNOWN      //未知网络
        public static final int NETWORK_CLASS_WIFI         //wifi
        public static final int NETWORK_CLASS_2_G          //2G
        public static final int NETWORK_CLASS_3_G          //3G
        public static final int NETWORK_CLASS_4_G          //4G

        boolean connect = NetUtil.checkNetworkState();             //检查是否有网络。有则返回true
        String operator = NetUtil.getNetworkOperator();            //获得运营商编号(MCC+MNC)
        String operatorName = NetUtil.getNetworkOperatorName();    //获取运营商的名称
        String countryIso = NetUtil.getNetworkCountryIso();        //获得国家ISO代码
        String wlanMac = NetUtil.getWLANMAC();                     //获取Mac地址
        int netType = NetUtil.getNetworkType();                    //获取网络的类型，支持上述的几种状态
     
``` 

## PermissionUtil使用说明
### 使用方法

```   
       String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
                          Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                    
       boolean lack = PermissionUtil.lacksPermission(Manifest.permission.ACCESS_COARSE_LOCATION);  //判断是否缺少权限,缺少则返回true
       boolean lack = PermissionUtil.lacksPermissions(permissions);    //判断是否缺少权限数组中的权限，权限任一缺少则返回trues 
       
                          
     
``` 
          
