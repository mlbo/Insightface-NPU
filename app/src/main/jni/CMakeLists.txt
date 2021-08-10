project(facetengine)

cmake_minimum_required(VERSION 3.10)

set(distribution_DIR ${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI})
include_directories(tengine/include/ utility ./)

set(OpenCV_DIR ${CMAKE_SOURCE_DIR}/opencv-mobile-3.4.15-android/sdk/native/jni)
find_package(OpenCV REQUIRED core imgproc )

set(ncnn_DIR ${CMAKE_SOURCE_DIR}/ncnn-20210525-android-vulkan/${ANDROID_ABI}/lib/cmake/ncnn)
find_package(ncnn REQUIRED)

add_library(tengine SHARED IMPORTED)
set_target_properties(tengine PROPERTIES IMPORTED_LOCATION ${distribution_DIR}/libtengine-lite.so)

find_library(
        log-lib
        log)

add_library(facetengine SHARED facetengine.cpp ndkcamera.cpp algorithm/scrfd.cc utility/letterbox.cc utility/permute.cc utility/nms.cc utility/iou.cc utility/timer.cc AndroidLog.h)


# disable rtti and exceptions
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fno-rtti -fno-exceptions")

target_link_libraries(facetengine tengine ncnn ${OpenCV_LIBS} ${log-lib} camera2ndk mediandk)
