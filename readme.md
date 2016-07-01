﻿# proxy-server 开发引导

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
> * 安装mysql数据库，下载地址：
http://www.mysql.com/downloads/

------
## 环境设置
### jdk环境配置
参考：http://jingyan.baidu.com/article/e75aca85508d15142edac6b8.html

### maven环境配置参考：
http://jingyan.baidu.com/article/4f7d5712aa9c631a201927ea.html
更改apache-maven-3.0.5\conf\setting.xml内容为：
```xml
<settings>
  <localRepository>E:\\repository</localRepository>
  <mirrors>
    <mirror>
      <!--This sends everything else to /public -->
      <id>nexus</id>
      <mirrorOf>*</mirrorOf>
      <url>http://maven.wosai-inc.com:8081/nexus/content/groups/public</url>
      <!--url>http://localhost:8081/nexus/content/groups/public</url-->
    </mirror>
  </mirrors>
  <profiles>
    <profile>
      <id>nexus</id>
      <!--Enable snapshots for the built in central repo to direct -->
      <!--all requests to nexus via the mirror -->
      <repositories>
        <repository>
          <id>central</id>
          <url>http://central</url>
          <releases><enabled>true</enabled></releases>
          <snapshots><enabled>true</enabled></snapshots>
        </repository>
      </repositories>
     <pluginRepositories>
        <pluginRepository>
          <id>central</id>
          <url>http://central</url>
          <releases><enabled>true</enabled></releases>
          <snapshots><enabled>true</enabled></snapshots>
        </pluginRepository>
      </pluginRepositories>
    </profile>
  </profiles>
  <activeProfiles>
    <!--make the profile active all the time -->
    <activeProfile>nexus</activeProfile>
  </activeProfiles>

</settings>

```
### tomcat环境配置
参考:http://jingyan.baidu.com/article/4853e1e53465271909f72690.html
示例中是tomcat6，请配置tomcat7.x
### 代码依赖环境变量配置：
tomcat配置完成后，需要在tomcat 7.x>JDK>optional java vm arguments 中添加以下内容：
```java
-Xms64m 
-Xmx128m 
-Dlogdir=D:\log
-Dlog.upay.dir=D:\log\upay\
-Dupay.flavor=default
```
### 数据库环境配置
在mysql中执行：
```mysql
-- MySQL Script generated by MySQL Workbench
-- Fri 18 Mar 2016 07:18:21 PM CST
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema keystore
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema keystore
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `keystore` DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_bin;
-- -----------------------------------------------------
-- Schema mapstore
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mapstore
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mapstore` DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_bin;
USE `keystore` ;

-- -----------------------------------------------------
-- Table `keystore`.`terminal_key`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `keystore`.`terminal_key` (
  `id` VARCHAR(37) NOT NULL,
  `secret` VARCHAR(64) NULL,
  `ctime` BIGINT(20) NULL DEFAULT NULL,
  `mtime` BIGINT(20) NULL DEFAULT NULL,
  `deleted` TINYINT(1) NOT NULL DEFAULT '0',
  `version` BIGINT(20) UNSIGNED NOT NULL)
ENGINE = InnoDB;

USE `mapstore` ;

-- -----------------------------------------------------
-- Table `mapstore`.`proxy_store`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mapstore`.`proxy_store` (
  `id` VARCHAR(37) NOT NULL COMMENT 'UUID',
  `client_merchant_sn` VARCHAR(64) NOT NULL,
  `client_store_sn` VARCHAR(64) NOT NULL,
  `store_sn` VARCHAR(64) NOT NULL,
  `ctime` BIGINT(20) NULL DEFAULT NULL,
  `mtime` BIGINT(20) NULL DEFAULT NULL,
  `deleted` TINYINT(1) NOT NULL DEFAULT '0',
  `version` BIGINT(20) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mapstore`.`proxy_terminal`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mapstore`.`proxy_terminal` (
  `id` VARCHAR(37) NOT NULL COMMENT 'UUID',
  `client_merchant_sn` VARCHAR(64) NOT NULL,
  `client_store_sn` VARCHAR(64) NOT NULL,
  `client_terminal_sn` VARCHAR(64) NOT NULL,
  `terminal_sn` VARCHAR(64) NOT NULL,
  `ctime` BIGINT(20) NULL DEFAULT NULL,
  `mtime` BIGINT(20) NULL DEFAULT NULL,
  `deleted` TINYINT(1) NOT NULL DEFAULT '0',
  `version` BIGINT(20) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mapstore`.`proxy_merchant`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mapstore`.`proxy_merchant` (
  `id` VARCHAR(37) NOT NULL COMMENT 'UUID',
  `client_merchant_sn` VARCHAR(64) NOT NULL,
  `merchant_sn` VARCHAR(64) NOT NULL,
  `ctime` BIGINT(20) NULL DEFAULT NULL,
  `mtime` BIGINT(20) NULL DEFAULT NULL,
  `deleted` TINYINT(1) NOT NULL DEFAULT '0',
  `version` BIGINT(20) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
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
参考地址：
http://jingyan.baidu.com/article/afd8f4de6ee5c734e286e999.html



