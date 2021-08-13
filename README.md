# Insightface Android NPU

## [app/src/main](https://github.com/mlbo/Insightface-NPU/tree/master/app/src/main)目录结构

```
├─assets                                存放模型
├─java                                  Java代码
├─jni                                   C++代码
│  ├─algorithm                          SCRFD代码
│  ├─include                            
│  │  └─image                           图像处理代码
│  ├─opencv-mobile-3.4.15-android       OpenCV mobile     
│  │  
│  └─utility                            SCRFD涉及处理代码
├─jniLibs
│  ├─arm64-v8a                          64位Tengine Android动态库
│  └─armeabi-v7a                        32位支持TIM-VX的Tengine Android动态库
```


## 准备
0. Khadas VIM3开发板一套
1. 用 Android Studio 建立一个空白Android项目
2. 把按照[交叉编译 Arm32/64 Android 版本](https://github.com/OAID/Tengine/blob/tengine-lite/doc/compile.md#3-%E4%BA%A4%E5%8F%89%E7%BC%96%E8%AF%91-arm3264-android-%E7%89%88%E6%9C%AC)和[编译 Android 32bit 平台](https://github.com/OAID/Tengine/blob/tengine-lite/doc/npu_tim-vx_user_manual_zh.md#29-%E7%BC%96%E8%AF%91-android-32bit-%E5%B9%B3%E5%8F%B0)
教程编译好的so分别复制到Android项目中jniLibs目录中arm64-v8a和armeabi-v7a文件夹内
3. 把[SCRFD模型](#)模型复制到Android项目中assets目录
4. 把[Tengine/demos/](https://github.com/OAID/Tengine/tree/tengine-lite/demos0)中的SCRFD涉及的代码复制到Android项目中的jni目录

## 编译环境搭建

参考[Insightface-NPU/app/build.gradle](https://github.com/mlbo/Insightface-NPU/blob/master/app/build.gradle)
### Android SDK
```
  minSdkVersion 24
  targetSdkVersion 30
```

### Android NDK

```
  ndk {
      moduleName "tengine-lite"
      abiFilters "armeabi-v7a",'arm64-v8a'
      ndkVersion "21.4.7075529"
  }
```


### Android OpenCV

下载[opencv-mobile-3.4.15-android.zip](https://github.com/nihui/opencv-mobile/releases/download/v13/opencv-mobile-3.4.15-android.zip)，然后解压放入jni目录



## 参考

1. [Tengine](https://github.com/OAID/Tengine)
2. [NCNN](https://github.com/OAID/Tengine)
3. [opencv-mobile](https://github.com/nihui/opencv-mobile)
4. [ncnn-android-nanodet](https://github.com/nihui/ncnn-android-nanodet)
