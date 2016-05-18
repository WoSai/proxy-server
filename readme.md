# proxy-server 开发引导

------

##准备工作：

> * 安装jdk7及以上版本，下载地址：
http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html
> * 安装myeclipse2014及以上版本，下载地址：
http://jump.bdimg.com/safecheck/index?url=x+Z5mMbGPAs0fPjV59o0dKamYoQiOtL77MEs+BamiOZo7ifNswf1gaJoRfIB6lBfB2c6dkie3rXxBIpEvYJJRa2DCoDbW2/Zl1bBDb4HhIaKh4FxuvNd1gx486G6CPntk/vLZiPHhM2cgS4iHsNmeSQWuyasbAVzflX1pf1JnMTKnSdO6a98pr4j0xbXO4CJj+7apLpZRgoMLHTsqevsCSJhgtK3fJGyBT5RLVAOTZK1RgwimVAeNtfheUG/YfISNLPxrFpEDv8pEM+FczQ1B8dJQjDIbk49N9xo/+i/wRzjWna7BY+wWRbxIcWL9Ywidj2oeHoEzTI=
> * 安装tomcat7及以上版本，下载地址：
http://tomcat.apache.org/download-70.cgi
> * 安装maven3.0.5及以上版本，下载地址：
http://maven.apache.org/download.cgi

------
## 环境设置
> * jdk环境配置参考：
http://jingyan.baidu.com/article/e75aca85508d15142edac6b8.html
> * tomcat环境配置参考，示例中是tomcat6，请配置tomcat7.x：
http://jingyan.baidu.com/article/4853e1e53465271909f72690.html
> * maven环境配置参考：
http://jingyan.baidu.com/article/4f7d5712aa9c631a201927ea.html
> * 代码依赖环境变量配置：
tomcat配置完成后，需要在tomcat 7.x>JDK>optional java vm arguments 中添加以下内容：
```java
-Xms64m 
-Xmx128m 
-Dlogdir=D:\log
-Dlog.upay.dir=D:\log\upay\
-Dupay.flavor=default
```

## 导入工程

打开myclipse，依次点击菜单file>import>Maven4Myeclipse>exist maven projects，选择下载的proxy-server目录，一直下一步确定即可。


##配置

### proxy-core
打开proxy-server\proxy-core\src\main\resources\spring\flavor-default.properties，vendor参数必须更改配置：
```java
##api
#vendor.api.domain=http://112.124.63.101:9206/core-validation/
vendor.sn=1
vendor.app.id=2016050400000007
vendor.key=d131

# for hsitoric reason the terminal activation api is provided by the upay-api service (https://api.shouqianba.com)
vendor.api.terminal.activate.url=https://api.shouqianba.com/terminal/activate

# the api's below are provided by the vendor-api service (https//vendor-api.shouqianba.com)
vendor.api.store.create.url=http://121.41.41.54:9206/core-validation/v2/store/create
vendor.api.store.update.url=http://121.41.41.54:9206/core-validation/v2/store/update
vendor.api.store.get.url=http://121.41.41.54:9206/core-validation/v2/store/get
vendor.api.terminal.create.url=http://121.41.41.54:9206/core-validation/v2/terminal/create
vendor.api.terminal.update.url=http://121.41.41.54:9206/core-validation/v2/terminal/update
vendor.api.terminal.get.url=http://121.41.41.54:9206/core-validation/v2/terminal/get
vendor.api.terminal.move.url=http\://121.41.41.54\:9206/core-validation/v2/terminal/move
```
### proxy-upay
打开proxy-server\proxy-upay\src\main\resources\spring\flavor-default.properties，数据库参数必须更改配置：
```java
##database
jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/keystore?useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
jdbc.username=wosai
jdbc.password=wosai
jdbc.connection.eviction.interval=60000

##api
upay.api.domain=http://121.41.41.54:8088
upay.api.pay.url=/upay/v2/pay
upay.api.refund.url=/upay/v2/refund
upay.api.query.url=/upay/v2/query
upay.api.cancel.url=/upay/v2/cancel
upay.api.revoke.url=/upay/v2/revoke
upay.api.precreate.url=/upay/v2/precreate
upay.api.activate.url=/terminal/activate
upay.api.checkin.url=/terminal/checkin
upay.api.uploadLog.url=/terminal/uploadLog
```

### proxy-auto
打开proxy-server\proxy-auto\src\main\resources\spring\flavor-default.properties，数据库参数和rpc必须更改配置：
```java
## configure database
jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/mapstore?useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
jdbc.username=wosai
jdbc.password=wosai
jdbc.connection.eviction.interval=60000

## configure core and upay dependencies
proxy.core.rpc.url=http://localhost:8080/proxy-core/
proxy.upay.rpc.url=http://localhost:8080/proxy-upay/

## configure upload log timer
##timer.log.upload.hour=23
##timer.log.upload.minuter=30
##timer.log.upload.interval=86400

```
##发布和运行
参考地址：http://jingyan.baidu.com/article/afd8f4de6ee5c734e286e999.html




