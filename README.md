# 跑步记录SDK开发记录

#### 一、如何发布新版本
> 1、更改config.gradle中runningVersion版本
> 2、Terminal中执行./gradlew bintrayUpload

#### 二、引入方式
> `implementation 'com.jvtd:running_sdk:latest.integration'`

#### 三、配置信息
> 1、项目的 “AndroidManifest.xml” 文件中，添加如下代码：
> `<meta-data 
>       android:name="com.amap.api.v2.apikey" 
>       android:value="请输入您的用户Key"/>`  
> 2、选择ndk框架，从`abiFilters "armeabi", "armeabi-v7a", "arm64-v8a", "x86","x86_64"`选择一个或者多个
