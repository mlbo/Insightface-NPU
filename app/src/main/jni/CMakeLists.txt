project(facetengine)

cmake_minimum_required(VERSION 3.10)

set(distribution_DIR ${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI})
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/include)
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/include/image)
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/utility)

set(OpenCV_DIR ${CMAKE_SOURCE_DIR}/opencv-mobile-3.4.15-android/sdk/native/jni)
find_package(OpenCV REQUIRED core imgproc)

add_library(tengine SHARED IMPORTED)
set_target_properties(tengine PROPERTIES IMPORTED_LOCATION ${distribution_DIR}/libtengine-lite.so)

find_library(
        log-lib
        log)

unset(SCRFD_SRCS)
list(APPEND SCRFD_SRCS facetengine.cpp)
list(APPEND SCRFD_SRCS algorithm/scrfd.cc)
list(APPEND SCRFD_SRCS utility/letterbox.cc)
list(APPEND SCRFD_SRCS utility/permute.cc)
list(APPEND SCRFD_SRCS utility/nms.cc)
list(APPEND SCRFD_SRCS utility/iou.cc)
list(APPEND SCRFD_SRCS utility/timer.cc)

# Camera
list(APPEND SCRFD_SRCS ndkcamera.cpp)
list(APPEND SCRFD_SRCS include/image/YuvConverter.cpp)
list(APPEND SCRFD_SRCS include/image/ImageRotate.cpp)

add_library(
        facetengine
        SHARED
        ${SCRFD_SRCS}
)

# disable rtti and exceptions
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fno-rtti -fno-exceptions")

target_link_libraries(
        facetengine
        tengine
        ${OpenCV_LIBS}
        ${log-lib}
        -landroid
        camera2ndk
        mediandk
)
